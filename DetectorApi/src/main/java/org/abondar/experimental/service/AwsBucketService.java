package org.abondar.experimental.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import jakarta.inject.Singleton;
import org.abondar.experimental.configuration.DetectorConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class AwsBucketService implements StorageService {

    private static final Logger log = LoggerFactory.getLogger(AwsBucketService.class);

    private final DetectorConfiguration configuration;

    private final AmazonS3 s3Client;

    public AwsBucketService(DetectorConfiguration configuration) {
        this.configuration = configuration;
        this.s3Client = AmazonS3ClientBuilder
                .standard()
                .withRegion(configuration.region())
                .build();
    }


    @Override
    public List<String> downloadAnnotations() {
        try {
            log.info("Downloading annotations");
            var annotationObject = s3Client.getObject(new GetObjectRequest(configuration.bucket(), configuration.annotations()));
            var annotationStream = annotationObject.getObjectContent();
            List<String> annotations = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(annotationStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    annotations.add(line);
                }
            }
            annotationStream.close();
            return annotations;
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }

        return null;
    }

    @Override
    public byte[] downloadModel() {
        try {
            log.info("Downloading data model");
            var modelObject = s3Client.getObject(new GetObjectRequest(configuration.bucket(), configuration.annotations()));
            var modelStream = modelObject.getObjectContent();

            var outStream = new ByteArrayOutputStream();
            byte[] model = new byte[1024];
            int bytesRead;
            while ((bytesRead = modelStream.read(model)) != -1) {
                outStream.write(model,0,bytesRead);
            }

            return model;
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }

        return null;
    }
}
