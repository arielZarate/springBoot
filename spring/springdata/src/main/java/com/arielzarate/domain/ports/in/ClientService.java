package com.arielzarate.domain.ports.in;

import com.arielzarate.domain.model.Client;

import java.util.List;

public interface ClientService {

    public Client createClient(Client client);
    public Client getClientById(Long clientId);
    public void deleteClient(Long clientId);
    public Client updateClient(Client client, Long clientId);
    public List<Client>getAllClients();
}
