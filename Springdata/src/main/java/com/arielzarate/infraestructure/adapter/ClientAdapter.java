package com.arielzarate.infraestructure.adapter;

import com.arielzarate.domain.model.Client;
import com.arielzarate.domain.ports.out.ClientPort;
import com.arielzarate.infraestructure.adapter.mapper.ClientEntityMapper;
import com.arielzarate.infraestructure.persistence.entity.ClientEntity;
import com.arielzarate.infraestructure.persistence.repositories.ClientRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Component
@AllArgsConstructor
public class ClientAdapter implements ClientPort {

    private static final Logger log = LoggerFactory.getLogger(ClientAdapter.class);
    private final ClientRepository clientRepository;
    private final ClientEntityMapper clientEntityMapper;

    @Override
    public Client saveClient(Client client) {
        log.info("Request adapter {}", client.toString());
        ClientEntity cliententity = clientRepository.save(clientEntityMapper.mapToEntity(client));

        log.info("Client saved successfully: {}", cliententity);
        return clientEntityMapper.mapToDomainWithAddresses(cliententity);
    }

    @Override
    public Optional<Client> getClientById(Long clientId) {
        return clientRepository.findByIdWithAddresses(clientId).map(clientEntityMapper::mapToDomainWithAddresses);
    }

    @Override
    public Client updateClient(Client client) {
        log.info("Request fo udpate client: {}", client.toString());
        ClientEntity clientEntity = clientRepository.save(clientEntityMapper.mapToEntity(client));
        return clientEntityMapper.mapToDomainWithAddresses(clientEntity);
    }

    @Override
    public boolean existsById(Long clientId) {
        return clientRepository.existsById(clientId);
    }

    @Override
    public boolean existsByEmail(String email) {
        return clientRepository.existsByEmail(email);
    }


    /**
     * Soft delete manual (no usa @PreRemove que no funciona bien con MySQL).
     * Busca el cliente, setea isActive = false y deletedAt = now(), luego guarda.
     *
     */
    @Override
    public void deleteClient(Long clientId) {
        clientRepository.findById(clientId).ifPresent(entity -> {
            entity.setIsActive(false);
            entity.setDeletedAt(LocalDateTime.now());
            clientRepository.save(entity);

            log.info("Client with id {} soft deleted successfully", clientId);
        });
    }

    @Override
    public void activateClient(Long clientId) {
  clientRepository.findById(clientId).ifPresent(clientEntity -> {
            clientEntity.setIsActive(true);
            clientEntity.setDeletedAt(null);
            clientRepository.save(clientEntity);

            log.info("Client with id {} activated successfully", clientId);
      });
    }

    /**
     * mapeo a domain basic porque el getAllClients no necesita las direcciones, y asi evito hacer un join innecesario en la consulta a la BD.
     *
     */
    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll().stream().map(clientEntityMapper::mapToDomainBasic).toList();
    }
}
