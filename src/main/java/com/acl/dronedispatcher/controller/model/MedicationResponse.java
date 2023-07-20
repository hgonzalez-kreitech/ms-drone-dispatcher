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
public class MedicationResponse {

    private Integer id;

    private String name;

    private Integer weight;

    private String code;

    private byte[] image;

    private Integer droneId;
}
