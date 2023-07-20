package com.acl.dronedispatcher.controller.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
public class DroneLoadRequest {

    @NotNull(message = "The drone id is a mandatory field")
    private Integer droneId;

    @NotNull(message = "Medications list is mandatory")
    @Size(min = 1, message = "You have to provide medication information")
    private List<Integer> medications;
}
