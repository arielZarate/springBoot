package com.arielzarate.infraestructure.persistence.repositories;

import com.arielzarate.infraestructure.persistence.model.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClientRepository  extends JpaRepository<ClientEntity,Long> {

    @Query("""
            SELECT DISTINCT c
            FROM ClientEntity c 
            LEFT JOIN FETCH c.address
            WHERE c.id = :id
            """)
    Optional<ClientEntity> findByIdWithAddresses(@Param("id") Long id);
}
