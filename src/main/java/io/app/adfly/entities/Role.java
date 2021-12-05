package io.app.adfly.entities;

import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor

public class Role implements GrantedAuthority {
  public Role(String authority){
      this.authority = authority;
  }

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
