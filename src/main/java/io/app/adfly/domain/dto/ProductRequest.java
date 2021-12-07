package io.app.adfly.domain.dto;

public class ProductRequest {
    private String name;
    private String description;

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

    public ProductRewardingRequest getProductRewarding() {
        return productRewarding;
    }

    public void setProductRewarding(ProductRewardingRequest productRewarding) {
        this.productRewarding = productRewarding;
    }

    private ProductRewardingRequest productRewarding;
}
