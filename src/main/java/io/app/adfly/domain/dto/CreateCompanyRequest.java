package io.app.adfly.domain.dto;

import io.swagger.v3.oas.annotations.links.Link;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateCompanyRequest {
    @NotBlank
    private String name;
    private String description;
    @NotBlank
    private String website;
    @NotBlank
    private String registrationNumber;
    @NotBlank
    private String registeredAddress;
}
