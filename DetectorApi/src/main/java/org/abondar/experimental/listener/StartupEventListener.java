package org.abondar.experimental.listener;

import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;
import org.abondar.experimental.service.StorageService;
import org.abondar.experimental.store.ModelStore;
import org.nd4j.enums.Mode;

@Singleton
public class StartupEventListener {

    private final StorageService storageService;

    private final ModelStore modelStore;


    public StartupEventListener(StorageService storageService, ModelStore modelStore) {
        this.storageService = storageService;
        this.modelStore = modelStore;
    }


    @EventListener
    public void onStartup(StartupEvent event){
        var annotations = storageService.downloadAnnotations();
        var model = storageService.downloadModel();

        modelStore.setModel(model);
        modelStore.setAnnotations(annotations);
    }
}
