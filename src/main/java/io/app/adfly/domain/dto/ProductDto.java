package io.app.adfly.domain.dto;

import io.app.adfly.entities.Category;

import java.util.Set;

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
    private Set<CategoryDto> categories;

    public Set<CategoryDto> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategoryDto> categories) {
        this.categories = categories;
    }

    public ProductRewardingDto getProductRewarding() {
        return productRewarding;
    }

    public void setProductRewarding(ProductRewardingDto productRewardingDto) {
        this.productRewarding = productRewardingDto;
    }

    private ProductRewardingDto productRewarding;


}
