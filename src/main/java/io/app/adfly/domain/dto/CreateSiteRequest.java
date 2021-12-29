package io.app.adfly.domain.dto;

import lombok.Data;

@Data
public class CreateSiteRequest {
    private String name;
    private String linkUrl;
}
