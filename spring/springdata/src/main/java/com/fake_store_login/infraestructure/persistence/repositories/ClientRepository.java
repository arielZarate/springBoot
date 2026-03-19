package com.fake_store_login.infraestructure.persistence.repositories;

import com.fake_store_login.infraestructure.persistence.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClientRepository  extends JpaRepository<ClientEntity,Long> {

    @Query("""
            SELECT DISTINCT c
            FROM ClientEntity c 
            LEFT JOIN FETCH c.address
            WHERE c.id = :id
            """)
    Optional<ClientEntity> findByIdWithAddresses(@Param("id") Long id);

    boolean existsByEmail(String email);
}
