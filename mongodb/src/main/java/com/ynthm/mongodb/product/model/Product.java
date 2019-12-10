package com.ynthm.mongodb.product.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Data
@Document(collection = "product")
public class Product implements Serializable {

    private static final long serialVersionUID = -5603805915550851551L;

    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    private String description;

    private int inventory;

    private BigDecimal price;

    private String categoryId;

    private List<String> tags;

    @Field("create_at")
    private Date createdAt;
}
