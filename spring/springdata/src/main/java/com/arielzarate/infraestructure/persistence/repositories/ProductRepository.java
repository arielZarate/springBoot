package com.arielzarate.infraestructure.persistence.repositories;

import com.arielzarate.infraestructure.persistence.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findByName(String name);
    Boolean existsByName(String name);

    Boolean existsByNameAndIdNot(String name, Long id);
}
