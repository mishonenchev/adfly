package io.app.adfly.entities;
import javax.persistence.*;

@Entity
@Table(name = "Sites")
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "advertiser_id", nullable = false)
    private User advertiser;
    private String linkUrl;

    public Site(){
    }
    public Site(User advertiser, String linkUrl) {
        this.advertiser = advertiser;
        this.linkUrl = linkUrl;
    }

    public Long getId() {
        return id;
    }

    public User getAdvertiser() {
        return advertiser;
    }

    public void setAdvertiser(User advertiser) {
        this.advertiser = advertiser;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
}
