package com.arielzarate.Spring_thymeleaf_Mvc.service;

import com.arielzarate.Spring_thymeleaf_Mvc.dto.ClientDTO;
import com.arielzarate.Spring_thymeleaf_Mvc.persistence.entity.Client;
import com.arielzarate.Spring_thymeleaf_Mvc.persistence.repository.ClientRepository;
import com.arielzarate.Spring_thymeleaf_Mvc.service.mapper.ClientMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.arielzarate.Spring_thymeleaf_Mvc.service.mapper.ClientMapper.toEntity;
import static com.arielzarate.Spring_thymeleaf_Mvc.service.mapper.ClientMapper.toModel;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;


    @Override
    @Transactional(readOnly = true)
    public List<ClientDTO> findAll() {
        log.info("Find all clients");
        return clientRepository.findAll()
                .stream()
                .map(ClientMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClientDTO> findById(Long id) {
        log.info("Find client by id: {}", id);
        return clientRepository.findById(id)
                .map(ClientMapper::toModel);
    }

    @Override
    @Transactional
    public ClientDTO save(ClientDTO clientModel) {
        log.info("Save client: {}", clientModel.getName());
        Client entity = toEntity(clientModel);
        entity = clientRepository.save(entity);
        return toModel(entity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("Delete client by id: {}", id);
        clientRepository.deleteById(id);
    }




    private void validation(ClientDTO clientModel) {
        if (clientModel.getName() == null || clientModel.getName().isEmpty()) {
            throw new IllegalArgumentException("Client name is required");
        }
        if (clientModel.getLastName() == null || clientModel.getLastName().isEmpty()) {
            throw new IllegalArgumentException("Client last name is required");
        }
        if (clientModel.getEmail() == null || clientModel.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Client email is required");
        }
        if (clientModel.getCuit() == null || clientModel.getCuit().isEmpty()) {
            throw new IllegalArgumentException("Client cuit is required");

    }
    }

}