package com.arielzarate.infraestructure.adapter.mapper;

import com.arielzarate.domain.model.Address;
import com.arielzarate.domain.model.Client;
import com.arielzarate.infraestructure.persistence.entity.AddressEntity;
import com.arielzarate.infraestructure.persistence.entity.ClientEntity;
import org.springframework.stereotype.Component;

@Component
public class ClientEntityMapper {


    /**
     * este mapper se encarga de mapear un objeto Client del dominio a un objeto ClientEntity de la capa de persistencia.
     *
     **/
    public ClientEntity mapToEntity(Client client) {
        if (client == null) {
            return null;
        }
        ClientEntity clientEntity = new ClientEntity();
        if (client.getClientId() != null) {
            clientEntity.setId(client.getClientId());
        }
        clientEntity.setFirstName(client.getFirstName() != null ? client.getFirstName().trim() : null);
        clientEntity.setLastName(client.getLastName() != null ? client.getLastName().trim() : null);
        clientEntity.setEmail(client.getEmail() != null ? client.getEmail().trim() : null);
        clientEntity.setPhone(client.getPhone() != null ? client.getPhone().trim() : null);
        // No se mapea active desde el domain - es automático (create=true, update=mantiene, delete=false, activate=true)
        clientEntity.setAddress(mapAddressesToEntity(client.getAddress()));
        return clientEntity;
    }


    /**
     * cuando se llama al metoddo findall() usamos el map mapToDomainBasic para no cargar las direcciones,
     * ya que en la vista de lista no se necesitan, y asi evitamos cargar datos innecesarios y mejorar el rendimiento
     *
     */
    public Client mapToDomainBasic(ClientEntity clientEntity) {
        if (clientEntity == null) return null;

        Client client = new Client();
        client.setClientId(clientEntity.getId());
        client.setFirstName(clientEntity.getFirstName());
        client.setLastName(clientEntity.getLastName());
        client.setEmail(clientEntity.getEmail());
        client.setPhone(clientEntity.getPhone());
        client.setActive(clientEntity.getIsActive());

        //no mapea las direcciones porque el getAllClients no las necesita, y asi evito hacer un join innecesario en la consulta a la BD.
        client.setAddress(null);
        return client;
    }


    /**
     * cuando se llama al metodo findById() usamos el map mapToDomainWithAddresses para cargar las direcciones,
     * ya que en la vista de detalle si se necesitan
     *
     */

    public Client mapToDomainWithAddresses(ClientEntity clientEntity) {
        if (clientEntity == null) return null;

        Client client = mapToDomainBasic(clientEntity);
        client.setAddress(mapAddressesToDomain(clientEntity.getAddress()));
        return client;
    }


    //=================ADDRESSS==============================

    /**
     * Este mapper se encarga de mapear un objeto Address del dominio a un objeto AddressEntity de la capa de persistencia.
     *
     */
    public AddressEntity mapAddressesToEntity(Address address) {
        if (address == null) {
            return null;
        }
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setStreet(address.getStreet() != null ? address.getStreet().trim() : null);
        addressEntity.setCity(address.getCity() != null ? address.getCity().trim() : null);
        addressEntity.setState(address.getState() != null ? address.getState().trim() : null);
        addressEntity.setPostalCode(address.getPostalCode() != null ? address.getPostalCode().trim() : null);
        addressEntity.setCountry(address.getCountry() != null ? address.getCountry().trim() : null);
        return addressEntity;
    }


    /**
     * Mapear un objeto AddressEntity de la capa de persistencia a un objeto Address del dominio.
     *
     */

    public Address mapAddressesToDomain(AddressEntity addressEntities) {
        if (addressEntities == null) {
            return null;
        }
        Address address = new Address();
        address.setAddressId(addressEntities.getId());
        address.setStreet(addressEntities.getStreet() != null ? addressEntities.getStreet().trim() : null);
        address.setCity(addressEntities.getCity() != null ? addressEntities.getCity().trim() : null);
        address.setState(addressEntities.getState() != null ? addressEntities.getState().trim() : null);
        address.setPostalCode(addressEntities.getPostalCode() != null ? addressEntities.getPostalCode().trim() : null);
        address.setCountry(addressEntities.getCountry() != null ? addressEntities.getCountry().trim() : null);
        return address;
    }



}
