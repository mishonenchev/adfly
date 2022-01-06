package io.app.adfly.repositories;

import io.app.adfly.entities.ProductEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductEventRepository extends JpaRepository<ProductEvent, Long> {
}
