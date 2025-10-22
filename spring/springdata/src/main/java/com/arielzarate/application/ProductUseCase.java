package com.arielzarate.application;


import com.arielzarate.domain.ProductDomainService;
import com.arielzarate.domain.model.Product;
import com.arielzarate.domain.ports.in.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductUseCase implements ProductService {
    private final ProductDomainService productDomainService;

    @Override
    public List<Product> getAllProducts() {
        return productDomainService.getAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productDomainService.getById(id);
    }

    @Override
    public Product createProduct(Product product) {
        return productDomainService.create(product);
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        return productDomainService.update(id, product);
    }

    @Override
    public void deleteProduct(Long id) {
        productDomainService.remove(id);
    }
}


/**
 * Siempre validar que los métodos de la capa de aplicación (use case) no tengan lógica de negocio.
 * la logica de negocio debe estar en la capa de dominio (servicios de dominio o entidades).
 * si deseo agregar validaciones, debo crear un servicio de dominio que se encargue de esa lógica.
 */