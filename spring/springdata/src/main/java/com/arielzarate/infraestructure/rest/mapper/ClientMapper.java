package com.arielzarate.infraestructure.rest.mapper;

import com.arielzarate.domain.model.Address;
import com.arielzarate.domain.model.Client;
import com.arielzarate.infraestructure.rest.dto.AddressRequest;
import com.arielzarate.infraestructure.rest.dto.ClientRequest;
import com.arielzarate.infraestructure.rest.dto.ClientResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
//aca le digo que clase usare para mapear las direcciones
public abstract class ClientMapper {

    //target apunta a la clase destino Client y el source apunta a la clase origen ClientRequest
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "orders", ignore = true)
    public abstract Client mapToDomainIgnoreAddressAndOrder(ClientRequest client);

    public Client mapToDomainWithAddresses(ClientRequest client) {
        Client mappedClient = mapToDomainIgnoreAddressAndOrder(client);
        AddressRequest addressRequest = client.getAddress();
        if (addressRequest != null) {
            // aca mapeo la direccion usando el AddressMapper
            Address mappedAddress = ((AddressMapper) this).mapToDomain(addressRequest);
            //mapeola direccion en una lista y se la asigno al cliente
            mappedClient.setAddresses(List.of(mappedAddress));
        }
        return mappedClient;
    }

    //  @Mapping(target = "addresses", ignore = true)
    // @Mapping(target = "orders" ,ignore = true)
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