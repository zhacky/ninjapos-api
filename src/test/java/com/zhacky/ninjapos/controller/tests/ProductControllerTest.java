package com.zhacky.ninjapos.controller.tests;

import com.zhacky.ninjapos.controller.ProductController;
import com.zhacky.ninjapos.model.Product;
import com.zhacky.ninjapos.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductControllerTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ProductRepository productRepository; // used by integration testing

    @Mock
    ProductRepository mockProductRepository; // used by unit testing

    private ProductController productController;

    @Before
    public void setUp() {

        boolean _INTEGRATION_ = false;

        if (_INTEGRATION_) {
            productController = new ProductController(productRepository);
        } else {
            productController = new ProductController(mockProductRepository);
            // list of products
            List<Product> productList = new ArrayList<>();
            // single product
            Product p = new Product();
            p.setId(1L);
            p.setCode("200");
            p.setName("Mock Product");
            p.setPrice(15d);
            Optional<Product> singleProduct = Optional.of(p);
            productList.add(p);

            Product savedProduct = new Product("My product");
            savedProduct.setId(2L);
            savedProduct.setPrice(25d);

            when(mockProductRepository.findAll()).thenReturn(productList);
            when(mockProductRepository.findById(anyLong())).thenReturn(singleProduct);
            when(mockProductRepository.save(any())).thenReturn(savedProduct);
            when(mockProductRepository.existsById(anyLong())).thenReturn(true);
            // for update
            p.setPrice(25d);
            when(mockProductRepository.saveAndFlush(any())).thenReturn(p);

        }
    }

    @Test
    public void testGetProducts_ShouldReturnProductList() {

        // given


        // when
        HttpEntity<?> responseEntity = productController.getProducts();
        List<Product> products = (List<Product>) responseEntity.getBody();
        // then
        Assertions.assertThat(products).isNotNull();
        Assertions.assertThat(products).isNotEmpty();
        Assertions.assertThat(products.size()).isGreaterThan(0);
        logger.info("\nProduct list: {}", products);

    }

    @Test
    public void testGetProductById_ShouldReturnAProduct() {
        // given
        Long givenId = 1L; // id of first item
        // when
        HttpEntity<?> responseEntity = productController.getProductById(givenId);
        try {
            Product product = ((Optional<Product>) responseEntity.getBody()).get();
            // then
            Assertions.assertThat(product).isNotNull();
            Assertions.assertThat(product.getId()).isEqualTo(givenId);
            logger.info("\nOptional product found: {}", product);
        } catch (Exception e) {
            logger.warn("An error has occurred: {}", e);
        }

    }

    @Test
    public void testCreateNewProduct_ShouldReturnCreatedProduct() {
        // given
        Product product = new Product("My product");

        // when
        HttpEntity<?> responseEntity = productController.createNewProduct(product);
        product = (Product) responseEntity.getBody();
        // then
        Assertions.assertThat(product.getName()).isEqualTo("My product");
        Assertions.assertThat(product.getId()).isNotNull().isPositive();
        logger.info("\nProduct created: {}", product);
    }

    @Test
    public void testUpdateProduct_ShouldReturnUpdatedProduct() {
        // given
        Long id = 1L;
        HttpEntity<?> responseEntity = productController.getProductById(id);
         Optional<Product> productForUpdate = (Optional<Product>) responseEntity.getBody();
         productForUpdate.get().setPrice(25d);

        // when
        responseEntity = productController.updateProduct(id, productForUpdate.get());
        Product product = (Product) responseEntity.getBody();
        // then
        Assertions.assertThat(product).isNotNull();
        Assertions.assertThat(product.getPrice()).isEqualTo(25d);
        logger.info("\nProduct updated: {}", product);

    }
}