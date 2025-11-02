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
        return clientMapper.mapToDomain(cliententity);
    }

    @Override
    public Client getClientById(Long clientId) {
        return null;
    }

    @Override
    public void deleteClient(Long clientId) {

    }

    @Override
    public Client updateClient(Client client) {
        return null;
    }

    @Override
    public boolean existsById(Long clientId) {
        return false;
    }

    @Override
    public List<Client> getAllClients() {
        return List.of();
    }
}
