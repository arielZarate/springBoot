package com.arielzarate.domain.ports.out;

import com.arielzarate.domain.model.Product;

import java.util.List;
import java.util.Optional;


public interface ProductPersistencePort {


    Product saveProduct(Product product);

    Optional<Product> findProductById(Long id);

    void deleteProduct(Long id);

    List<Product> findAllProducts();

    Product updateProduct(Product product);

    Boolean existsProductByName(String name);

    Optional<Product> findProductByName(String name);

    Boolean existsProductByNameAndIdNot(String name, Long id);

}
