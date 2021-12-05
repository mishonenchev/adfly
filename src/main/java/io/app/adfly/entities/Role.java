package io.app.adfly.entities;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity

public class Role implements GrantedAuthority {
    public Long getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public static final String USER_ADVERTISER = "USER_ADVERTISER";
    public static final String USER_COMPANY = "USER_COMPANY";

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    private String authority;
    @Override
    public String getAuthority() {
        return authority;
    }

}
