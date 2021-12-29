package io.app.adfly.repositories;

import io.app.adfly.entities.Category;
import io.app.adfly.entities.Product;
import io.app.adfly.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.Set;


public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "SELECT e from Product e where e.productStatus = 0 and e.user = ?1 ")
    Page<Product> findAllByUser(User user, Pageable pageable);

    boolean existsByExternalReferenceAndUser(String externalReference, User user);

    @Query(value = "SELECT p FROM Product p JOIN p.categories c WHERE c IN :userCategories")
    Page<Product> getAllByUserCategories(Set<Category> userCategories, Pageable pageable);
}
