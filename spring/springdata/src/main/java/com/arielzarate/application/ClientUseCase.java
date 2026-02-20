package com.arielzarate.application;


import com.arielzarate.domain.model.Client;
import com.arielzarate.domain.ports.in.ClientService;
import com.arielzarate.domain.ports.out.ClientPort;
import com.arielzarate.error.model.ApplicationError;
import com.arielzarate.error.model.ClientError;
import com.arielzarate.error.model.exception.ApplicationException;
import com.arielzarate.error.model.exception.ClientException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClientUseCase implements ClientService {

    private final ClientPort clientPort;

    @Override
    public Client createClient(Client client) {
        return clientPort.saveClient(client);
    }

    @Override
    public Client getClientById(Long clientId) {
        return clientPort.getClientById(clientId)
                .orElseThrow(() -> new ClientException(ClientError.clientNotFound("Client not found with id: " + clientId)));
    }

    @Override
    public void deleteClient(Long clientId) {

    }

    @Override
    public Client updateClient(Client client) {
        return null;
    }

    @Override
    public List<Client> getAllClients() {
        return clientPort.getAllClients();
    }
}
