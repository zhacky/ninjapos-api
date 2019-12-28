package com.zhacky.ninjapos.controller;

import com.zhacky.ninjapos.model.Product;
import com.zhacky.ninjapos.repository.ProductRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

/**
 * Handles REST endpoints for products
 * <pre>
 *      GET /products           - get all products
 *      GET /products/{id}      - get product with id
 *      POST /products          - create and add a product
 *      PUT /products/{id}      - update a product
 *      PATCH /products/{id}    - edit a product field
 *      DELETE /products/{id}   - destroy a product
 * </pre>
 */
@RestController
public class ProductController {


    @Value("Authorization")
    private String authHeader;

    @Autowired
    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/products")
    @ApiOperation(value = "Get list of all products", response = Product[].class, authorizations = {@Authorization(value = "JWT")}, notes = "User auth value format: \"Bearer [token]\"\nEx. 'Bearer eyAbCdEfG...'")
    public ResponseEntity<?> getProducts() {
        List<Product> products = productRepository.findAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("products/{id}")
    public ResponseEntity<Optional<Product>> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productRepository.findById(id));
    }

    @PostMapping("/products")
    public ResponseEntity<?> createNewProduct(@RequestBody Product product) {
        product = productRepository.save(product);
        return ResponseEntity.created(URI.create("/products/" + product.getId())).body(product);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        if (productRepository.existsById(id)) {
            if (product.getId() == null || product.getId() != id) {
                product.setId(id);
            }
            product = productRepository.saveAndFlush(product);
            return ResponseEntity.accepted().body(product);
        }
        return ResponseEntity.notFound().build();
    }
}
