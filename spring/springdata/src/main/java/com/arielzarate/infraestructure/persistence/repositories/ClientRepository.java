package com.arielzarate.infraestructure.persistence.repositories;

import com.arielzarate.infraestructure.persistence.model.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository  extends JpaRepository<ClientEntity,Long> {
}
