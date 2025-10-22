package com.arielzarate.infraestructure.rest.mapper;


import com.arielzarate.domain.model.Product;
import com.arielzarate.infraestructure.rest.dto.ProductRequest;
import com.arielzarate.infraestructure.rest.dto.ProductResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponse mapToDTO(Product product);

    List<ProductResponse> mapToDTOList(List<Product> products);

    Product mapToDomain(ProductRequest productRequest);

}
