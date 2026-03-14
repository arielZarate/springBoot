package com.arielzarate.infraestructure.rest.mapper;


import com.arielzarate.domain.model.Address;
import com.arielzarate.infraestructure.rest.dto.AddressRequest;
import com.arielzarate.infraestructure.rest.dto.AddressResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    Address mapToDomain(AddressRequest request);

    @Mapping(target = "addressId", source = "addressId")
    @Mapping(target = "zipCode", source = "postalCode")
    AddressResponse mapToResponse(Address address);
}

