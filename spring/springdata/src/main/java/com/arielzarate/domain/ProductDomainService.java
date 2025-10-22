package com.arielzarate.domain;


import com.arielzarate.domain.model.Product;
import com.arielzarate.domain.ports.out.ProductPersistencePort;
import com.arielzarate.error.model.ApplicationError;
import com.arielzarate.error.model.exception.ApplicationErrorException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ProductDomainService {

    private  ProductPersistencePort productPersistencePort;


    public List<Product> getAll() {
        return productPersistencePort.findAllProducts();
    }

    public Product getById(Long id) {
        return productPersistencePort.findProductById(id).orElseThrow(() -> new ApplicationErrorException(ApplicationError.notFound("id : " + id)));
    }


    public Product create(Product product) {
        if (productPersistencePort.findProductByName(product.getName()).isPresent()) {
            throw new ApplicationErrorException(ApplicationError.badRequest("Product with name '" + product.getName() + "' already exists."));
        }

        product.setName(product.getName().trim());
        return productPersistencePort.saveProduct(product);
    }

    public Product update(Long id, Product product) {
        Product prod = this.getById(id);

        if (productPersistencePort.existsProductByNameAndIdNot(product.getName(), id)) {
            throw new ApplicationErrorException(ApplicationError.badRequest("Product with name '" + product.getName() + "' already exists."));
        }
        prod.setName(product.getName().trim());
        prod.setDescription(product.getDescription());
        prod.setPrice(product.getPrice());
        prod.setStock(product.getStock());

        return productPersistencePort.updateProduct(prod);

    }


    public void remove(Long id) {
        Product prod = this.getById(id);
        productPersistencePort.deleteProduct(prod.getProductId());
    }


}
