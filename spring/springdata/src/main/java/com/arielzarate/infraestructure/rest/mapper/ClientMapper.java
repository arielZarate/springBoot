package com.arielzarate.infraestructure.rest.mapper;

import com.arielzarate.domain.model.Address;
import com.arielzarate.domain.model.Client;
import com.arielzarate.infraestructure.rest.dto.ClientRequest;
import com.arielzarate.infraestructure.rest.dto.ClientResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public abstract class ClientMapper {

    @Autowired
    protected AddressMapper addressMapper;


    //target apunta a la clase destino Client y el source apunta a la clase origen ClientRequest
    @Mapping(target = "address",source = "address")
    @Mapping(target = "orders", ignore = true)
    public abstract Client mapToDomainIgnoreAddressAndOrder(ClientRequest client);

    public Client mapToDomainWithAddresses(ClientRequest client) {
        Client mappedClient = mapToDomainIgnoreAddressAndOrder(client);
        if (client.getAddress() != null) {
            Address mappedAddress = addressMapper.mapToDomain(client.getAddress());
            mappedClient.setAddress(mappedAddress);
        }
        return mappedClient;
    }


    @Mapping(target = "clientId", source = "clientId")
    @Mapping(target = "address", source = "address")
    public abstract ClientResponse mapToClientResponse(Client client);


    public List<ClientResponse> mapToClientResponseList(List<Client> clients) {
        return clients.stream()
                .map(this::mapToClientResponse)
                .toList();
    }


}


/**
 * Explicacion,tuve que usar abstract class en lugar de interface porque
 * MapStruct no soporta llamadas a otros mappers dentro de interfaces.
 * Al usar una clase abstracta, puedo definir métodos que llamen a otros mappers,
 * como AddressMapper, para mapear objetos anidados.
 *
 **/