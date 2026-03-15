package com.arielzarate.application;


import com.arielzarate.domain.model.Client;
import com.arielzarate.domain.ports.in.ClientService;
import com.arielzarate.domain.services.ClientDomainService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClientUseCase implements ClientService {

    private final ClientDomainService clientDomainService;

    @Override
    public Client create(Client client) {
        return clientDomainService.createClient(client);
    }

    @Override
    public Client getById(Long clientId) {
        return clientDomainService.getClientById(clientId);
    }

    @Override
    public void delete(Long clientId) {
        clientDomainService.deleteClient(clientId);
    }

    @Override
    public Client update(Client client, Long clientId) {
        return clientDomainService.updateClient(client, clientId);
    }

    @Override
    public List<Client> getAll() {
        return clientDomainService.getAllClients();
    }

    @Override
    public void activate(Long clientId) {
        clientDomainService.activateClient(clientId);
    }


}
