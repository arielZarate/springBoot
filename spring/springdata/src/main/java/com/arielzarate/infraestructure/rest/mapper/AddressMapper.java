package com.arielzarate.infraestructure.rest.mapper;


import com.arielzarate.domain.model.Address;
import com.arielzarate.infraestructure.rest.dto.AddressRequest;
import com.arielzarate.infraestructure.rest.dto.AddressResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    Address mapToDomain(AddressRequest request);

    AddressResponse mapToResponse(Address address);
}

