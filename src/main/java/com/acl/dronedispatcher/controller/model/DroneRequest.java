package com.acl.dronedispatcher.controller.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
public class DroneRequest {

    @NotBlank(message = "The serial number is a mandatory field")
    @Size(min =  1, max =  100, message = "Serial number size must be between one and one hundred characters")
    private String serialNumber;

    @NotNull(message = "The battery capacity is a mandatory field")
    @Min(value = 0, message = "Minimum battery capacity value is zero")
    @Max(value = 100, message = "Maximum battery capacity value is one hundred")
    private Integer batteryCapacity;

    @NotNull(message = "The drone weight is a mandatory field")
    @Min(value = 0, message = "Minimum weight value is zero")
    @Max(value = 500, message = "Maximum weight value is five hundred")
    private Integer weight;

    @NotBlank(message = "The drone model is a mandatory field")
    @Pattern(regexp="(?i)(Lightweight|Middleweight|Cruiserweight|Heavyweight)",
            message="Allowed model values are: Lightweight|Middleweight|Cruiserweight|Heavyweight")
    private String model;

    @NotBlank(message = "The drone state is a mandatory field")
    @Pattern(regexp="(?i)(IDLE|LOADING|LOADED|DELIVERING|DELIVERED|RETURNING).*",
            message="Allowed state values are: IDLE|LOADING|LOADED|DELIVERING|DELIVERED|RETURNING")
    private String state;
}
