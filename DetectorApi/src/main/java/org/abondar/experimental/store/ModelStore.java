package org.abondar.experimental.store;

import jakarta.inject.Singleton;

import java.nio.ByteBuffer;
import java.util.List;

@Singleton
public class ModelStore{

   private List<String> annotations;
   private byte[] model;

    public List<String> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<String> annotations) {
        this.annotations = annotations;
    }

    public byte[] getModel() {
        return model;
    }

    public void setModel(byte[] model) {
        this.model = model;
    }
}
