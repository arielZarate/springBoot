package com.arielzarate.domain.services;


import com.arielzarate.domain.model.Address;
import com.arielzarate.domain.model.Client;
import com.arielzarate.domain.ports.out.ClientPort;
import com.arielzarate.error.model.ClientError;
import com.arielzarate.error.model.exception.ClientException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class ClientDomainService {

    private final ClientPort clientPort;


    public void validateEmail(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ClientException(ClientError.clientBadRequest("Invalid email format: " + email));
        }
    }

    public Client createClient(Client client) {
        validateEmail(client.getEmail());

        if (clientPort.existsByEmail(client.getEmail())) {
            throw new ClientException(ClientError.clientConflict("Email already exists: " + client.getEmail()));
        }

        Client validatedClient = validateForCreate(client);
        return clientPort.saveClient(validatedClient);
    }


    public Client getClientById(Long clientId) {
        return clientPort.getClientById(clientId)
                .orElseThrow(() -> new ClientException(ClientError.clientNotFound("Client not found with id: " + clientId)));
    }


    public Client updateClient(Client client, Long clientId) {
        Client existingClient = this.getClientById(clientId);

        // Validar email si viene
        if (client.getEmail() != null && !client.getEmail().trim().isEmpty()) {
            validateEmail(client.getEmail());

            // Si el email es diferente al actual, verificar que no exista
            if (!client.getEmail().equalsIgnoreCase(existingClient.getEmail())) {
                if (clientPort.existsByEmail(client.getEmail())) {
                    throw new ClientException(ClientError.clientConflict("Email already exists: " + client.getEmail()));
                }
            }
        }

        return clientPort.updateClient(validateForUpdate(existingClient, client));

    }

    public void deleteClient(Long clientId) {
        if (!clientPort.existsById(clientId)) {
            throw new ClientException(ClientError.clientNotFound("Client not found with id: " + clientId));
        }
        clientPort.deleteClient(clientId);
    }

    public void activateClient(Long clientId) {
        if (!clientPort.existsById(clientId)) {
            throw new ClientException(ClientError.clientNotFound("Client not found with id: " + clientId));
        }
        clientPort.activateClient(clientId);
    }


    public List<Client> getAllClients() {
        return clientPort.getAllClients();
    }


    public Client validateForCreate(Client client) {
        if (client.getFirstName() == null || client.getFirstName().trim().isEmpty()) {
            throw new ClientException(ClientError.clientBadRequest("First name is required"));
        }
        if (client.getLastName() == null || client.getLastName().trim().isEmpty()) {
            throw new ClientException(ClientError.clientBadRequest("Last name is required"));
        }
        if (client.getEmail() == null || client.getEmail().trim().isEmpty()) {
            throw new ClientException(ClientError.clientBadRequest("Email is required"));
        }
        if (client.getPhone() == null || client.getPhone().trim().isEmpty()) {
            throw new ClientException(ClientError.clientBadRequest("Phone is required"));
        }

        client.setFirstName(client.getFirstName().trim());
        client.setLastName(client.getLastName().trim());
        client.setEmail(client.getEmail().trim());
        client.setPhone(client.getPhone().trim());
        // active es automático: se maneja en el adapter/domain, no se acepta del request

        return client;
    }

    public Client validateForUpdate(Client existingClient, Client newClient) {
        // Merge obligatorios: usar los de BD si los nuevos vienen null o vacíos
        if (newClient.getFirstName() != null && !newClient.getFirstName().trim().isEmpty()) {
            existingClient.setFirstName(newClient.getFirstName().trim());
        }
        if (newClient.getLastName() != null && !newClient.getLastName().trim().isEmpty()) {
            existingClient.setLastName(newClient.getLastName().trim());
        }
        if (newClient.getEmail() != null && !newClient.getEmail().trim().isEmpty()) {
            existingClient.setEmail(newClient.getEmail().trim());
        }
        if (newClient.getPhone() != null && !newClient.getPhone().trim().isEmpty()) {
            existingClient.setPhone(newClient.getPhone().trim());
        }

        // Merge opcionales (address)
        if (newClient.getAddress() != null) {
            Address existingAddress = existingClient.getAddress();
            if (existingAddress == null) {
                existingAddress = new Address();
            }

            Address newAddress = newClient.getAddress();

            if (newAddress.getStreet() != null) {
                existingAddress.setStreet(newAddress.getStreet());
            }
            if (newAddress.getCity() != null) {
                existingAddress.setCity(newAddress.getCity());
            }
            if (newAddress.getState() != null) {
                existingAddress.setState(newAddress.getState());
            }
            if (newAddress.getPostalCode() != null) {
                existingAddress.setPostalCode(newAddress.getPostalCode());
            }
            if (newAddress.getCountry() != null) {
                existingAddress.setCountry(newAddress.getCountry());
            }

            existingClient.setAddress(existingAddress);
        }

        return existingClient;
    }


    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");


}
