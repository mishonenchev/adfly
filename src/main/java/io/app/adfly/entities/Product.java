package io.app.adfly.entities;

import javax.persistence.*;

@Entity
public class Product {

    public Long getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    public ProductRewarding getProductRewarding() {
        return productRewarding;
    }

    public void setProductRewarding(ProductRewarding productRewarding) {
        this.productRewarding = productRewarding;
    }

    private String name;
    private String description;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    private User user;
    @OneToOne
    private ProductRewarding productRewarding;

}
