package com.arielzarate.infraestructure.adapter.mapper;


import com.arielzarate.domain.model.Product;
import com.arielzarate.infraestructure.persistence.model.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductEntityMapper {

    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    ProductEntity toEntity(Product product);

    @Mapping(
            target = "productId",
            source = "id"
    )
    Product toDomain(ProductEntity productEntity);

}
