package org.abondar.experimental.model;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Introspected
@Serdeable
public record DetectorResponse (
        String carModel
){}