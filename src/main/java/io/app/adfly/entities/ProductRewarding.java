package io.app.adfly.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ProductRewarding {
    public Long getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public RewardingStrategy getRewardingStrategy() {
        return rewardingStrategy;
    }

    public void setRewardingStrategy(RewardingStrategy rewardingStrategy) {
        this.rewardingStrategy = rewardingStrategy;
    }

    public RewardingType getRewardingType() {
        return rewardingType;
    }

    public void setRewardingType(RewardingType rewardingType) {
        this.rewardingType = rewardingType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    private RewardingStrategy rewardingStrategy;
    private RewardingType rewardingType;
    private Double amount;

    public enum RewardingStrategy{
        PerOrder,
        PerClick
    }
    public enum RewardingType{
        Percentage,
        FixedAmount
    }

}
