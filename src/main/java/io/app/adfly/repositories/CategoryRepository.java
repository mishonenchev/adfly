package io.app.adfly.repositories;

import io.app.adfly.entities.Category;
import io.app.adfly.entities.Product;
import io.app.adfly.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "SELECT c FROM Category As c")
    Page<Category> getAll(Pageable pageable);

    boolean existsByName(String name);

    Page<Category> findByProducts(Product product, Pageable pageable);

    @Query(value = "SELECT u.advertiser.categories From User u WHERE u.id = :userId")
    Page<Category> findAllByAdvertiser(Long userId, Pageable pageable);
}
