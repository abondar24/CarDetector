package org.abondar.experimental.service;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.abondar.experimental.configuration.DetectorConfiguration;
import org.abondar.experimental.model.DetectorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.List;

@Singleton
public class DetectorService {

    private static final Logger log = LoggerFactory.getLogger(DetectorService.class);

    @Inject
    StorageService storageService;

    private List<String> annotations;

    private ByteBuffer model;

    @PostConstruct
    public void downloadModel(){
        this.annotations = storageService.downloadAnnotations();
        this.model = storageService.downloadModel();
    }




    public DetectorResponse detectModel(File image){

        return new DetectorResponse("test",null);
    }

}
