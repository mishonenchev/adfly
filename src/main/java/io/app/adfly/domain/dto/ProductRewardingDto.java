package io.app.adfly.domain.dto;

import io.app.adfly.entities.ProductRewarding;

public class ProductRewardingDto {
    public ProductRewarding.RewardingStrategy getRewardingStrategy() {
        return rewardingStrategy;
    }

    public void setRewardingStrategy(ProductRewarding.RewardingStrategy rewardingStrategy) {
        this.rewardingStrategy = rewardingStrategy;
    }

    public ProductRewarding.RewardingType getRewardingType() {
        return rewardingType;
    }

    public void setRewardingType(ProductRewarding.RewardingType rewardingType) {
        this.rewardingType = rewardingType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    private ProductRewarding.RewardingStrategy rewardingStrategy;
    private ProductRewarding.RewardingType rewardingType;
    private Double amount;

}
