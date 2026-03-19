package com.fake_store_login.infraestructure.persistence.repositories;



import com.fake_store_login.infraestructure.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findByName(String name);
    Boolean existsByName(String name);

    Boolean existsByNameAndIdNot(String name, Long id);
}
