package com.arielzarate.infraestructure.rest;


import com.arielzarate.domain.model.Product;
import com.arielzarate.domain.ports.in.ProductService;
import com.arielzarate.infraestructure.rest.dto.ProductRequest;
import com.arielzarate.infraestructure.rest.dto.ProductResponse;
import com.arielzarate.infraestructure.rest.mapper.ProductMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    private  ProductService productService;
    private  ProductMapper productMapper;


    @Operation(
            summary = "Get all products",
            description = "Retrieve a list of all products",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved list of products"
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> productList = productMapper.mapToDTOList(productService.getAllProducts());
        log.info("Response GET - /api/products {}", productList);
        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }


    @Operation(
            summary = "Create a new product",
            description = "Create a new product with the provided details",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Product created successfully"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
        log.info("Request POST api/products - Creating a new product with body: {}", productRequest);
        Product product = productService.createProduct(productMapper.mapToDomain(productRequest));
        ProductResponse productResponse = productMapper.mapToDTO(product);
        log.info("Response POST api/products - Created product successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }


    @Operation(
            summary = "Update an existing product",
            description = "Update the details of an existing product",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product updated successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found"
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        log.info("Request PUT api/products - Updating a product with id: {}", id);
        Product product = productService.updateProduct(id, productMapper.mapToDomain(productRequest));
        ProductResponse productResponse = productMapper.mapToDTO(product);
        log.info("Response PUT api/products - Updated product : {}", productResponse);
        return ResponseEntity.status(HttpStatus.OK).body(productResponse);
    }


    @Operation(
            summary = "Get product by ID",
            description = "Retrieve a product by its unique ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved the product"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        log.info("Request GET BY ID - /api/products/{} ", id);
        Product product = productService.getProductById(id);
        log.info("Response GET BY ID - /api/products/{}  -> \n{} ", id, product);
        return ResponseEntity.ok().body(productMapper.mapToDTO(product));

    }

    @Operation(
            summary = "Delete a product",
            description = "Delete a product by its unique ID",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "No Content - Product deleted successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found"
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("Request DELETE - /api/products");
        productService.deleteProduct(id);
        log.info("Response DELETE - /api/products/id - Product deleted with id {}", id);
        return ResponseEntity.noContent().build();
    }

}
