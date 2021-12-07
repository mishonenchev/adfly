package io.app.adfly.domain.dto;

public class ProductDto {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String description;
    private Long id;

    public ProductRewardingDto getProductRewarding() {
        return productRewarding;
    }

    public void setProductRewarding(ProductRewardingDto productRewardingDto) {
        this.productRewarding = productRewardingDto;
    }

    private ProductRewardingDto productRewarding;


}
