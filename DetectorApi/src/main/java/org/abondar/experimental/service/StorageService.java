package org.abondar.experimental.service;

import org.abondar.experimental.exception.ModelNotReadyException;

import java.nio.ByteBuffer;
import java.util.List;

public interface StorageService {

    List<String> downloadAnnotations() throws ModelNotReadyException;

    byte[] downloadModel() throws ModelNotReadyException;
}
