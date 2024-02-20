package org.abondar.experimental.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.abondar.experimental.configuration.DetectorConfiguration;

import java.nio.ByteBuffer;
import java.util.List;

@Singleton
public class AwsBucketService implements StorageService {

    @Inject
    private DetectorConfiguration configuration;

    private AmazonS3 s3Client;

    public AwsBucketService(){
        this.s3Client = AmazonS3ClientBuilder.defaultClient();
    }



    @Override
    public List<String> downloadAnnotations() {
        return null;
    }

    @Override
    public ByteBuffer downloadModel() {
        return null;
    }
}
