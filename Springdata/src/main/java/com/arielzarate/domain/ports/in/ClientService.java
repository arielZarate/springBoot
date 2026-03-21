package com.arielzarate.domain.ports.in;

import com.arielzarate.domain.model.Client;

import java.util.List;

public interface ClientService {

    public Client create(Client client);
    public Client getById(Long clientId);
    public void delete(Long clientId);
    public Client update(Client client, Long clientId);
    public List<Client> getAll();
    public void activate(Long clientId);
}
