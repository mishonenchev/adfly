package io.app.adfly.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Products")
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
    @ManyToMany(mappedBy = "products")
    private Set<Category> categories;

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

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

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }

    private ProductStatus productStatus;
    private String externalReference;

    public String getExternalReference() {
        return externalReference;
    }

    public void setExternalReference(String externalReference) {
        this.externalReference = externalReference;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    @ManyToOne
    private Site site;
    public enum ProductStatus{
        Active,
        Retired
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    private String productUrl;

}
