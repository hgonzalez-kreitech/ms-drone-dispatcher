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
public class Medication {

    private Integer id;

    private String name;

    private Integer weight;

    private String code;

    private byte[] image;

    private Integer droneId;
}
