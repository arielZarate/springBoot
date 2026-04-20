package com.arielzarate.Spring_thymeleaf_Mvc.service.mapper;

import com.arielzarate.Spring_thymeleaf_Mvc.dto.ClientDTO;
import com.arielzarate.Spring_thymeleaf_Mvc.persistence.entity.Client;

public class ClientMapper {


    // Mappers
    public static ClientDTO toModel(Client entity) {
        if (entity == null) {
            return null;
        }
        return new ClientDTO(
                entity.getId(),
                entity.getName(),
                entity.getLast_name(),
                entity.getPhone(),
                entity.getAddress(),
                entity.getEmail(),
                entity.getCuit()
        );
    }

    public static Client toEntity(ClientDTO model) {
        if (model == null) {
            return null;
        }
        Client entity = new Client();
        if (model.getId() != null) {
            entity.setId(model.getId());
        }
        entity.setName(model.getName());
        entity.setLast_name(model.getLastName());
        entity.setPhone(model.getPhone());
        entity.setAddress(model.getAddress());
        entity.setEmail(model.getEmail());
        entity.setCuit(model.getCuit());
        return entity;
    }

}
