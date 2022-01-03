package io.app.adfly.entities;

import javax.persistence.*;

@Entity
public class ProductAdvertiser {
    public Long getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Advertiser advertiser;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Advertiser getAdvertiser() {
        return advertiser;
    }

    public void setAdvertiser(Advertiser advertiser) {
        this.advertiser = advertiser;
    }

    public String getRedirectCode() {
        return redirectCode;
    }

    public void setRedirectCode(String redirectCode) {
        this.redirectCode = redirectCode;
    }

    public String getExternalCode() {
        return externalCode;
    }

    public void setExternalCode(String externalCode) {
        this.externalCode = externalCode;
    }

    private String redirectCode;
    private String externalCode;
}
