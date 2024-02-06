package org.abondar.experimental.service;

import jakarta.inject.Singleton;
import org.abondar.experimental.model.DetectorResponse;

import java.io.File;

@Singleton
public class DetectorService {



    public DetectorResponse detectModel(File image){
        return new DetectorResponse("test",null);
    }

}
