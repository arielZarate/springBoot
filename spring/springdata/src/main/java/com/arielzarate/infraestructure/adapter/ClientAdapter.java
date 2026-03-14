package com.arielzarate.infraestructure.adapter;

import com.arielzarate.domain.model.Client;
import com.arielzarate.domain.ports.out.ClientPort;
import com.arielzarate.infraestructure.adapter.mapper.ClientMapper;
import com.arielzarate.infraestructure.persistence.model.ClientEntity;
import com.arielzarate.infraestructure.persistence.repositories.ClientRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
@AllArgsConstructor
public class ClientAdapter implements ClientPort {

    private static final Logger log = LoggerFactory.getLogger(ClientAdapter.class);
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    public Client saveClient(Client client) {
        log.info("Saving client: {}", client);

        ClientEntity cliententity = clientRepository.save(clientMapper.mapToEntity(client));

        log.info("Client saved successfully: {}", cliententity);
        return clientMapper.mapToDomainBasic(cliententity);
    }

    @Override
    public Optional<Client> getClientById(Long clientId) {
        return clientRepository.findByIdWithAddresses(clientId)
                .map(clientMapper::mapToDomainWithAddresses);

    }

    @Override
    public void deleteClient(Long clientId) {

    }

    @Override
    public Client updateClient(Client client) {
        log.info("Request adapter {}",client);
        ClientEntity clientEntity = clientRepository.save(clientMapper.mapToEntity(client));
        return  clientMapper.mapToDomainWithAddresses(clientEntity);
    }

    @Override
    public boolean existsById(Long clientId) {
        return clientRepository.existsById(clientId);
    }


    //only clients
    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll().stream().map(clientMapper::mapToDomainBasic).toList();
    }
}
