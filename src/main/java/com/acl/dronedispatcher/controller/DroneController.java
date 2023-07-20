package com.acl.dronedispatcher.controller;

import com.acl.dronedispatcher.controller.model.DroneLoadRequest;
import com.acl.dronedispatcher.controller.model.DroneRequest;
import com.acl.dronedispatcher.controller.model.DroneResponse;
import com.acl.dronedispatcher.controller.model.MedicationResponse;
import com.acl.dronedispatcher.service.DroneService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Api(tags = "Drones", value = "This is the drone rest api")
@RestController
@RequestMapping("/drone-dispatcher/api/v1/drone")
@Slf4j
@RequiredArgsConstructor
@Validated
public class DroneController {

    private final DroneService droneService;

    /**
     * This method is used to create a drone.
     *
     * @param drone This is the drone to create.
     * @return created drone.
     */
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Creates a drone", responses = {
            @ApiResponse(responseCode = "201",description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = DroneResponse.class))),
            @ApiResponse(responseCode = "400",description = "Bad request"),
            @ApiResponse(responseCode = "500",description = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<DroneResponse> createDrone(@RequestBody @Valid DroneRequest drone) {
        return droneService.createDrone(drone);
    }

    /**
     * This method is used to load a drone with medications.
     *
     * @param droneLoad This is the information of the drone to load.
     * @return void.
     */
    @PutMapping(value = "/load", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Loads a drone with medications", responses = {
            @ApiResponse(responseCode = "204",description = "Successful operation"),
            @ApiResponse(responseCode = "400",description = "Bad request"),
            @ApiResponse(responseCode = "500",description = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> loadDrone(@RequestBody @Valid DroneLoadRequest droneLoad) {
        return droneService.loadDrone(droneLoad);
    }

    /**
     * This method is used to get drone medications.
     *
     * @param droneId This is the drone id.
     * @return a list of drone loaded medications.
     */
    @GetMapping(value = "/loaded-medications/{droneId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns a list of medications loaded in a drone", responses = {
            @ApiResponse(responseCode = "200",description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = MedicationResponse.class))),
            @ApiResponse(responseCode = "400",description = "Bad request"),
            @ApiResponse(responseCode = "500",description = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.OK)
    public Flux<MedicationResponse> getDroneLoadedMedications(@PathVariable Integer droneId) {
        return droneService.getDroneLoadedMedications(droneId);
    }

    /**
     * This method is used to get drones available for loading.
     *
     * @return a list of drones available.
     */
    @GetMapping(value = "/available-for-loading", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns a list of drones available for loading", responses = {
            @ApiResponse(responseCode = "200",description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = DroneResponse.class))),
            @ApiResponse(responseCode = "400",description = "Bad request"),
            @ApiResponse(responseCode = "500",description = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.OK)
    public Flux<DroneResponse> getDronesAvailableForLoading() {
        return droneService.getDronesAvailableForLoading();
    }

    /**
     * This method is used to get drones available for loading.
     *
     * @param droneId This is the drone id.
     * @return a list of drones available.
     */
    @GetMapping(value = "/battery-level/{droneId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns the drone battery level", responses = {
            @ApiResponse(responseCode = "200",description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "400",description = "Bad request"),
            @ApiResponse(responseCode = "500",description = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.OK)
    public Mono<Integer> getDroneBatteryLevel(@PathVariable Integer droneId) {
        return droneService.getDroneBatteryLevel(droneId);
    }
}
