package com.arielzarate.infraestructure.adapter;

import com.arielzarate.domain.model.Product;
import com.arielzarate.domain.ports.out.ProductPersistencePort;
import com.arielzarate.infraestructure.adapter.mapper.ProductEntityMapper;
import com.arielzarate.infraestructure.persistence.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class ProductPersistenceAdapter implements ProductPersistencePort {

    private  ProductRepository productRepository;
    private  ProductEntityMapper mapper;

    @Override
    public Product saveProduct(Product product) {
        var productEntity = mapper.toEntity(product);
        var savedEntity = productRepository.save(productEntity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Product> findProductById(Long id) {
        return productRepository.findById(id).map(mapper::toDomain);

    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());

    }

    @Override
    public Product updateProduct(Product product) {
        var entity = mapper.toEntity(product);
        var updatedEntity = productRepository.save(entity);
        return mapper.toDomain(updatedEntity);

    }

    @Override
    public  Boolean existsProductByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public Optional<Product> findProductByName(String name) {
        return productRepository.findByName(name).map(mapper::toDomain);
    }

    @Override
    public Boolean existsProductByNameAndIdNot(String name, Long id) {
        return productRepository.existsByNameAndIdNot(name, id);
    }


}
