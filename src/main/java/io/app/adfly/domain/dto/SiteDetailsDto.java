package io.app.adfly.domain.dto;

import lombok.Data;

@Data
public class SiteDetailsDto {
    private Long id;
    private String name;
    private String linkUrl;
    private String publicApiKey;
}
