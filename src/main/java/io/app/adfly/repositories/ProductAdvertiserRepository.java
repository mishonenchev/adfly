package io.app.adfly.repositories;

import io.app.adfly.entities.Advertiser;
import io.app.adfly.entities.Category;
import io.app.adfly.entities.Product;
import io.app.adfly.entities.ProductAdvertiser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface ProductAdvertiserRepository extends JpaRepository<ProductAdvertiser, Long> {
    boolean existsByProductAndAdvertiser(Product product, Advertiser advertiser);
    Page<ProductAdvertiser> getAllByAdvertiser(Advertiser advertiser, Pageable pageable);

    Optional<ProductAdvertiser> getByRedirectCode(String redirectCode);
}
