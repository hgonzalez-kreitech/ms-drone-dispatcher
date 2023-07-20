package com.acl.dronedispatcher.controller.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DroneResponse {

    private Integer id;

    private String serialNumber;

    private Integer batteryCapacity;

    private Integer weight;

    private String model;

    private String state;
}
