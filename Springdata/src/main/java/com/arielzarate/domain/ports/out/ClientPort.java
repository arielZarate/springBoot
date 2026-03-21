package com.arielzarate.domain.ports.out;

import com.arielzarate.domain.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientPort {
    public Client saveClient(Client client);

    public Optional<Client> getClientById(Long clientId);

    public void deleteClient(Long clientId);

    public Client updateClient(Client client);

    public boolean existsById(Long clientId);

    public List<Client> getAllClients();

    public boolean existsByEmail(String email);

    public void activateClient(Long clientId);
}
