package io.app.adfly.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ChangePasswordRequest {
    @NotBlank
    private String password;
    @NotBlank
    private String rePassword;
    @NotBlank
    private String oldPassword;
}
