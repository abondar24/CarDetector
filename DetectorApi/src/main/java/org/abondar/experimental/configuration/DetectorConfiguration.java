package org.abondar.experimental.configuration;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.Introspected;

@ConfigurationProperties("detector")
@Introspected
public record DetectorConfiguration (
        String model,
        String annotations,
        String bucket,

        String region
){}
