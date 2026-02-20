package com.arielzarate.infraestructure.adapter.mapper;

import com.arielzarate.domain.model.Address;
import com.arielzarate.domain.model.Client;
import com.arielzarate.infraestructure.persistence.model.AddressClientEntity;
import com.arielzarate.infraestructure.persistence.model.ClientEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClientMapper {


    public ClientEntity mapToEntity(Client client) {
        if (client == null) {
            return null;
        }
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setFirstName(client.getFirstName().trim());
        clientEntity.setLastName(client.getLastName().trim());
        clientEntity.setEmail(client.getEmail().trim());
        clientEntity.setPhone(client.getPhone().trim());
        clientEntity.setAddresses(mapAddressesToEntity(client.getAddresses()));
        return clientEntity;
    }

    public Client mapToDomainBasic(ClientEntity clientEntity) {
        if (clientEntity == null) return null;

        Client client = new Client();
        client.setClientId(clientEntity.getId());
        client.setFirstName(clientEntity.getFirstName());
        client.setLastName(clientEntity.getLastName());
        client.setEmail(clientEntity.getEmail());
        client.setPhone(clientEntity.getPhone());
        client.setAddresses(null);
        return client;
    }

    //for detail view, with addresses
    public Client mapToDomainWithAddresses(ClientEntity clientEntity) {
        if (clientEntity == null) return null;

        Client client = mapToDomainBasic(clientEntity);
        client.setAddresses(mapAddressesToDomain(clientEntity.getAddresses()));
        return client;
    }


    // ========= Address mappers =========

    public List<AddressClientEntity> mapAddressesToEntity(List<Address> addresses) {

        if (addresses == null) {
            return null;
        }
        return addresses.stream().map(address -> {
            AddressClientEntity addressClientEntity = new AddressClientEntity();
            addressClientEntity.setStreet(address.getStreet().trim());
            addressClientEntity.setCity(address.getCity().trim());
            addressClientEntity.setState(address.getState().trim());
            addressClientEntity.setZipCode(address.getZipCode().trim());
            return addressClientEntity;
        }).toList();
    }

    public List<Address> mapAddressesToDomain(List<AddressClientEntity> addressEntities) {

        if (addressEntities == null) {
            return null;
        }
        return addressEntities.stream().map(addressEntity -> {
            Address address = new Address();
            address.setStreet(addressEntity.getStreet().trim());
            address.setCity(addressEntity.getCity().trim());
            address.setState(addressEntity.getState().trim());
            address.setZipCode(addressEntity.getZipCode().trim());
            return address;
        }).toList();
    }

}
