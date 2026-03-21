package com.fake_store_login.domain.ports.in;

import com.fake_store_login.domain.model.Client;

import java.util.List;

public interface ClientService {

    public Client create(Client client);
    public Client getById(Long clientId);
    public void delete(Long clientId);
    public Client update(Client client, Long clientId);
    public List<Client> getAll();
    public void activate(Long clientId);
}
