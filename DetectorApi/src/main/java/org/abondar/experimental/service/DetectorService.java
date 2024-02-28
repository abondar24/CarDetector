package org.abondar.experimental.service;

import jakarta.inject.Singleton;
import org.abondar.experimental.exception.ModelNotReadyException;
import org.abondar.experimental.exception.ModelProcessingException;
import org.abondar.experimental.model.DetectorResponse;
import org.abondar.experimental.store.ModelStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensorflow.Graph;
import org.tensorflow.Tensor;
import org.tensorflow.TensorFlow;
import org.tensorflow.op.Ops;
import org.tensorflow.op.core.Constant;
import org.tensorflow.proto.SavedModel;
import org.tensorflow.types.TUint8;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.abondar.experimental.util.ImageUtil.IMAGE_HEIGHT;
import static org.abondar.experimental.util.ImageUtil.IMAGE_WIDTH;

@Singleton
public class DetectorService {

    private static final Logger log = LoggerFactory.getLogger(DetectorService.class);
    private final ModelStore modelStore;

    public DetectorService(ModelStore modelStore) {
        this.modelStore = modelStore;
    }

    public DetectorResponse detectCarModel(File image) {
        var model = modelStore.getModel();
        var annotations = modelStore.getAnnotations();

        if (model == null || annotations == null) {
            throw new ModelNotReadyException();
        }

        try {
            var input = preprocessImage(image);
            var output = processImage(model, input);

          //  var carModel = getClassFromOutput(output, annotations);

            return new DetectorResponse("HUI");
          //  return new DetectorResponse(carModel);
        } catch (IOException ex) {
            log.error(ex.getMessage());
            throw new ModelProcessingException();
        }
    }

    private Tensor preprocessImage(File image) throws IOException {
        var bufferedImage = ImageIO.read(image);
        var resizedImage = resizeImage(bufferedImage);

        try (var graph = new Graph()) {
            Ops tf = Ops.create(graph);
            Constant<TUint8> imageContent = tf.constant(resizedImage);

            return imageContent.asOutput().asTensor();
        }

    }

    private byte[] resizeImage(BufferedImage originalImage) {
        BufferedImage resizedImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(originalImage, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, null);
        graphics2D.dispose();

        ByteArrayOutputStream resizedOutput = new ByteArrayOutputStream();
        try {
            ImageIO.write(resizedImage, "jpg", resizedOutput);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ModelProcessingException();
        }
        return resizedOutput.toByteArray();
    }


    private byte[] processImage(byte[] model, Tensor input) {
        try {
            SavedModel saveModel = SavedModel.parseFrom(model);
           log.info(saveModel.toString());
           log.info(saveModel.getInitializationErrorString());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new ModelProcessingException();
        }
        return null;
    }

    private String getClassFromOutput(byte[] output, List<String> annotations) {
        var maxIndex = findMaxIndex(output);

        return annotations.get(maxIndex);
    }

    private int findMaxIndex(byte[] output) {
        int maxIdx = 0;
        float maxVal = -Float.MAX_VALUE;
        for (int i = 0; i < output.length; i++) {
            float val = (output[i] & 0xFF) / 255.0f; // Normalize to [0, 1]
            if (val > maxVal) {
                maxVal = val;
                maxIdx = i;
            }
        }
        return maxIdx;
    }
}
