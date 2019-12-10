package com.ynthm.mongodb.product;

import com.ynthm.mongodb.product.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    Product findFirstByName(String name);

    //Supports native JSON query string
    @Query("{name:'?0'}")
    Product findCustomByName(String name);

    @Query("{name: { $regex: ?0 } })")
    List<Product> findCustomByRegExName(String name);

}
