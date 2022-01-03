package io.app.adfly.domain.dto;

import lombok.Data;

@Data
public class ProductAdvertiserDto {
    private Long id;
    private ProductDto product;
    private String url;
}
