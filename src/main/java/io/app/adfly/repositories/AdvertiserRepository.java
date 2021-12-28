package io.app.adfly.repositories;

import io.app.adfly.entities.Advertiser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertiserRepository extends JpaRepository<Advertiser, Long> {
}
