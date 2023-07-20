package com.acl.dronedispatcher.controller.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
public class MedicationRequest {

    @NotBlank(message = "The medication name is a mandatory field")
    @Pattern(regexp="^[a-zA-Z0-9_\\-\\u00C0-\\u017F\\s]+$",
            message="Name must contain only letters, numbers, ‘-‘, ‘_’")
    private String name;

    @NotNull(message = "The medication weight is a mandatory field")
    private Integer weight;

    @NotBlank(message = "The medication code is a mandatory field")
    @Pattern(regexp="^[A-Z0-9_]+$",
            message="Code must contain only upper case letters, underscore and numbers")
    private String code;

    private byte[] image;
}
