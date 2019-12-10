package com.ynthm.mongodb.product;

import com.ynthm.mongodb.product.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductDaoTest {

    @Autowired
    private ProductDao productDao;

    @Test
    void insert() {
        Product product = new Product();
        product.setName("PS4");
        productDao.insert(product);
    }

    @Test
    void testInsert() {
    }

    @Test
    void remove() {
    }

    @Test
    void updateFirst() {
    }

    @Test
    void find() {
    }
}