package io.app.adfly.repositories;

import io.app.adfly.entities.Company;
import io.app.adfly.entities.Product;
import io.app.adfly.entities.Site;
import io.app.adfly.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteRepository extends JpaRepository<Site, Long> {

    Page<Site> findAllByCompany(Company company, Pageable pageable);
}
