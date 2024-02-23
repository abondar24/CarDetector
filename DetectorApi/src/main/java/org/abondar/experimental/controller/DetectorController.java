package org.abondar.experimental.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.multipart.StreamingFileUpload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.inject.Inject;
import org.abondar.experimental.model.DetectorResponse;
import org.abondar.experimental.model.Messages;
import org.abondar.experimental.service.DetectorService;

import java.io.File;
import java.io.IOException;

@Controller("/detector")
public class DetectorController {

    private final DetectorService detectorService;

    public DetectorController(DetectorService detectorService){
        this.detectorService = detectorService;
    }

    @Post(produces = "text/plain")
    @Operation(summary = "Detect a car", description = "Detect a car model by image")
    @ApiResponse(
            content = @Content(mediaType = "application/json", schema = @Schema(type = "DetectorResponse"))
    )
    @ApiResponse(responseCode = "200",description = "Car Detected")
    @ApiResponse(responseCode = "400",description = "Error reading image file")
    @ApiResponse(responseCode = "404",description = "Car Not Detected")
    @ApiResponse(responseCode = "501",description = "Error processing model")
    @ApiResponse(responseCode = "502",description = "Error accessing model")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<DetectorResponse> detectCar(@Part StreamingFileUpload file) throws IOException {
        var tempFile = File.createTempFile("uploaded-image", ".jpg");
        file.transferTo(tempFile);

        var resp = detectorService.detectCarModel(tempFile);
        return HttpResponse.ok(resp);
    }
}