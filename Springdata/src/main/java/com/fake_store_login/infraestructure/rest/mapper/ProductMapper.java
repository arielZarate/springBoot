package com.fake_store_login.infraestructure.rest.mapper;


import com.fake_store_login.domain.model.Product;
import com.fake_store_login.infraestructure.rest.dto.request.ProductRequest;
import com.fake_store_login.infraestructure.rest.dto.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "isActive", source = "isActive")
    ProductResponse mapToDTO(Product product);

    List<ProductResponse> mapToDTOList(List<Product> products);

    Product mapToDomain(ProductRequest productRequest);

}
