package com.ynthm.mongodb.product;

import com.ynthm.mongodb.product.model.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    @BeforeAll
    public void init() {

        repository.deleteAll();

        Product product = new Product();
        product.setName("iPhone");

        repository.save(product);
    }

    @Test
    public void countAllCountries() {

        List<Product> products = repository.findAll();
        assertEquals(1, products.size());
    }

    @Test
    public void countOneCountry() {
        Product product = new Product();
        product.setName("iPhone");
        Example<Product> example = Example.of(product);
        assertEquals(1L, repository.count(example));
    }


    @Test
    public void setsIdOnSave() {
        Product product = new Product();
        product.setName("XBox");
        Product product1 = repository.save(product);
        assertNotNull(product1.getId());
    }

    @Test
    public void findOneCountry() {
        Product product = new Product();
        product.setName("XBox");
        Example<Product> example = Example.of(product);

        Optional<Product> country = repository.findOne(example);
        assertEquals("XBox", country.get().getName());
    }

}