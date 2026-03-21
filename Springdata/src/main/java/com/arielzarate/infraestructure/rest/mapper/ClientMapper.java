package com.arielzarate.infraestructure.rest.mapper;

import com.arielzarate.domain.model.Address;
import com.arielzarate.domain.model.Client;
import com.arielzarate.infraestructure.rest.dto.request.ClientRequest;
import com.arielzarate.infraestructure.rest.dto.response.ClientResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Maper manual sin usar librerías externas. Se encarga de convertir entre los objetos de dominio (Client) y los objetos de transferencia (ClientRequest y ClientResponse).

 * */

@Component
public class ClientMapper {


    // ==============REQUEST MAPPER================

    public Client mapToDomain(ClientRequest request) {
        Client client = new Client();
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setEmail(request.getEmail());
        client.setPhone(request.getPhone());

        // Si viene algún dato de dirección, la creo
        if (hasAddressData(request)) {
            Address address = new Address();
            address.setStreet(request.getStreet());
            address.setCity(request.getCity());
            address.setState(request.getState());
            address.setPostalCode(request.getPostalCode());
            address.setCountry(request.getCountry());
            client.setAddress(address);
        }

        return client;
    }

    private boolean hasAddressData(ClientRequest request) {
        return request.getStreet() != null || request.getCity() != null || request.getState() != null || request.getPostalCode() != null || request.getCountry() != null;
    }


    //==============RESPONSE MAPPER================

    // Con dirección (para getById, create, update)
    public ClientResponse mapToResponse(Client client) {
        ClientResponse response = new ClientResponse();
        response.setClientId(client.getClientId());
        response.setFirstName(client.getFirstName());
        response.setLastName(client.getLastName());
        response.setEmail(client.getEmail());
        response.setPhone(client.getPhone());
        response.setActive(client.getActive());

        if (client.getAddress() != null) {
            response.setStreet(client.getAddress().getStreet());
            response.setCity(client.getAddress().getCity());
            response.setState(client.getAddress().getState());
            response.setZipCode(client.getAddress().getPostalCode());
            response.setCountry(client.getAddress().getCountry());
        }
        return response;
    }

    // Sin dirección (para getAll - optimización)
    public ClientResponse mapToClientResponseWithoutAddress(Client client) {
        ClientResponse response = mapToResponse(client);
        response.setStreet(null);
        response.setCity(null);
        response.setState(null);
        response.setZipCode(null);
        response.setCountry(null);
        return response;
    }


    public List<ClientResponse> mapToResponseList(List<Client> clients) {
        return clients.stream().map(this::mapToClientResponseWithoutAddress).toList();
    }


}
