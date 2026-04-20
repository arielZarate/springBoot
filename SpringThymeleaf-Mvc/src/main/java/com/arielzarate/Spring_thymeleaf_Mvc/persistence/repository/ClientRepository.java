package com.arielzarate.Spring_thymeleaf_Mvc.persistence.repository;

import com.arielzarate.Spring_thymeleaf_Mvc.persistence.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    
}