package io.app.adfly.domain.dto;

import lombok.Data;

@Data
public class UserDto {

    private String id;

    private String username;
    private String fullName;
    private CompanyDto company;

    //TODO: View for company and sites
}