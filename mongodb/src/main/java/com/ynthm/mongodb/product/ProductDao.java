package com.ynthm.mongodb.product;

import com.ynthm.mongodb.product.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public class ProductDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void insert(List<Product> products) {
        mongoTemplate.insert(products, Product.class);
    }

    public void insert(Product product) {
        mongoTemplate.insert(product);
    }

    public void remove() {
        Query query = Query.query(Criteria.where("name").is("Apple HomePod"));
        mongoTemplate.remove(query, Product.class);
    }

    public void updateFirst() {
        Query query = Query.query(Criteria.where("name").is("Apple HomePod"));
        Update update = Update.update("categories", Arrays.asList("music player")).set("count", 10);
        mongoTemplate.updateFirst(query, update, Product.class);
    }

    public void find() {
        Query query = Query.query(Criteria.where("name").is("Apple HomePod"));
        List<Product> products = mongoTemplate.find(query, Product.class);
        // product = mongoTemplate.findById(new ObjectId("57c6e1601e4735b2c306cdb7"), Product.class);

    }


}
