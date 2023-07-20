package com.acl.dronedispatcher.domain;

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
public class Drone {

    private Integer id;

    private String serialNumber;

    private Integer batteryCapacity;

    private Integer weight;

    private String model;

    private String state;
}
