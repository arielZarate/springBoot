package com.arielzarate.Spring_thymeleaf_Mvc.service;

import com.arielzarate.Spring_thymeleaf_Mvc.dto.ClientDTO;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    List<ClientDTO> findAll();

    Optional<ClientDTO> findById(Long id);

    ClientDTO save(ClientDTO clientModel);

    void deleteById(Long id);

}