package io.app.adfly.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ProductEvents")
public class ProductEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Event event;
    private Double quantity;
    private Double totalPrice;
    private Date createdDateTime;
    @OneToOne
    private ProductAdvertiser productAdvertiser;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public ProductAdvertiser getProductAdvertiser() {
        return productAdvertiser;
    }

    public void setProductAdvertiser(ProductAdvertiser productAdvertiser) {
        this.productAdvertiser = productAdvertiser;
    }

    public enum Event{
        Click,
        Order
    }
}
