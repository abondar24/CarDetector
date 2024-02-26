package org.abondar.experimental.store;

import jakarta.inject.Singleton;

import java.nio.ByteBuffer;
import java.util.List;

@Singleton
public class ModelStore{

   private List<String> annotations;
   private   ByteBuffer model;

    public List<String> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<String> annotations) {
        this.annotations = annotations;
    }

    public ByteBuffer getModel() {
        return model;
    }

    public void setModel(ByteBuffer model) {
        this.model = model;
    }
}
