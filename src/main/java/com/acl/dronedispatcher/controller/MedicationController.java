package com.acl.dronedispatcher.controller;

import com.acl.dronedispatcher.controller.model.MedicationRequest;
import com.acl.dronedispatcher.controller.model.MedicationResponse;
import com.acl.dronedispatcher.service.MedicationService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Api(tags = "Medications", value = "This is the medications rest api")
@RestController
@RequestMapping("/drone-dispatcher/api/v1/medication")
@Slf4j
@RequiredArgsConstructor
@Validated
public class MedicationController {

    private final MedicationService medicationService;

    /**
     * This method is used to create a medication.
     *
     * @param medication This is the medication to create.
     * @return created medication.
     */
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Creates a medication", responses = {
            @ApiResponse(responseCode = "201",description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = MedicationResponse.class))),
            @ApiResponse(responseCode = "400",description = "Bad request"),
            @ApiResponse(responseCode = "500",description = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MedicationResponse> createMedication(@RequestBody @Valid MedicationRequest medication) {
        return medicationService.createMedication(medication);
    }
}
