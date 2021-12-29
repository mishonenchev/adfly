package io.app.adfly.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Advertiser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String summary;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    @ManyToMany
    private Set<Category> categories;
    protected Advertiser(){}

    public Advertiser(String summary, Set<Category> categories){
        this.summary = summary;
        this.categories = categories;
    }
}
