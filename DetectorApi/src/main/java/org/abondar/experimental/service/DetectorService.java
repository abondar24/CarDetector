package org.abondar.experimental.service;

import jakarta.inject.Singleton;
import org.abondar.experimental.exception.ModelNotReadyException;
import org.abondar.experimental.exception.ModelProcessingException;
import org.abondar.experimental.model.DetectorResponse;
import org.abondar.experimental.store.ModelStore;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import static org.abondar.experimental.util.ImageUtil.*;

@Singleton
public class DetectorService {

    private final ModelStore modelStore;

    public DetectorService(ModelStore modelStore) {
        this.modelStore = modelStore;
    }

    private static final Logger log = LoggerFactory.getLogger(DetectorService.class);

    public DetectorResponse detectCarModel(File image){
        var model = modelStore.getModel();
        var annotations = modelStore.getAnnotations();

        if (model==null ||annotations==null){
            throw new ModelNotReadyException();
        }

        try {
            var input = preprocessImage(image);
            var networkModel = convertModel(model);
            var output = networkModel.output(input);

            var carModel = getClassFromOutput(output,annotations);

            return new DetectorResponse(carModel);
        } catch (IOException ex){
            log.error(ex.getMessage());
            throw new ModelProcessingException();
        }
    }

    private MultiLayerNetwork convertModel(ByteBuffer model) {
        var modelBytes = new byte[model.remaining()];
        model.get(modelBytes);

        try (ByteArrayInputStream bis = new ByteArrayInputStream(modelBytes);  var dataInputStream = new DataInputStream(bis)){
           return ModelSerializer.restoreMultiLayerNetwork(dataInputStream);
        } catch (IOException ex){
            log.error(ex.getMessage());
            throw new ModelProcessingException();
        }
    }

    private INDArray preprocessImage(File image) throws IOException{
        var bufferedImage = ImageIO.read(image);
        var resizedImage = resizeImage(bufferedImage);
        var inputArray = Nd4j.create(IMAGE_CHANNELS,IMAGE_HEIGHT,IMAGE_WIDTH);

        // Normalize the pixel values to the range [0, 1] and set them in the input array
        for (int i = 0; i < IMAGE_HEIGHT; i++) {
            for (int j = 0; j < IMAGE_WIDTH; j++) {
                int rgb = resizedImage.getRGB(j, i);
                inputArray.putScalar(new int[]{0, i, j}, ((rgb >> 16) & 0xFF) / 255.0); // Red channel
                inputArray.putScalar(new int[]{1, i, j}, ((rgb >> 8) & 0xFF) / 255.0);  // Green channel
                inputArray.putScalar(new int[]{2, i, j}, (rgb & 0xFF) / 255.0);         // Blue channel
            }
        }

        return inputArray;
    }

    private BufferedImage resizeImage(BufferedImage originalImage) {
        BufferedImage resizedImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(originalImage, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, null);
        graphics2D.dispose();
        return resizedImage;
    }

    private String getClassFromOutput(INDArray output, List<String> annotations) {
        // Find the index of the maximum value in the output array
        int maxIdx = Nd4j.argMax(output).getInt(0);

        // Get the class label corresponding to the maximum index from the annotations list
        return annotations.get(maxIdx);
    }

}
