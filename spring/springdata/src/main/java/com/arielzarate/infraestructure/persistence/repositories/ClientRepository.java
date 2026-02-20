package com.arielzarate.infraestructure.persistence.repositories;

import com.arielzarate.infraestructure.persistence.model.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClientRepository  extends JpaRepository<ClientEntity,Long> {

    @Query("""
            SELECT DISTINCT c
            FROM ClientEntity c 
            LEFT JOIN FETCH c.addresses
            """)
    List<ClientEntity> findAllWithAddresses();
}
