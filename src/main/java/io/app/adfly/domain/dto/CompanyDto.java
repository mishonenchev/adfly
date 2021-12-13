package io.app.adfly.domain.dto;

import lombok.Data;

@Data
public class CompanyDto {
    private String id;
    private String name;
    private String description;
    private String website;
    private String registrationNumber;
    private String registeredAddress;
}
