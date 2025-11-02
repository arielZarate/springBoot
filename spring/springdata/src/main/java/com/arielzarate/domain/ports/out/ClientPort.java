package com.arielzarate.domain.ports.out;

import com.arielzarate.domain.model.Client;

import java.util.List;

public interface ClientPort {
    public Client saveClient(Client client);

    public Client getClientById(Long clientId);

    public void deleteClient(Long clientId);

    public Client updateClient(Client client);

    public boolean existsById(Long clientId);

    public List<Client> getAllClients();
}
