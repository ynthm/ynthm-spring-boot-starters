package com.ynthm.elasticsearch.entity;

import com.ynthm.elasticsearch.config.IndexNameConst;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.List;

/** @author ethan */
@Data
@Accessors(chain = true)
@Document(indexName = IndexNameConst.COMMODITY)
public class Commodity implements Serializable {

  @Id private String id;

  @Field(type = FieldType.Text, analyzer = "ik_max_word")
  private String name;

  @Field(type = FieldType.Keyword)
  private String category;

  @Field(type = FieldType.Double)
  private Double price;

  private String brand;

  @Field(type = FieldType.Keyword)
  private List<String> tags;

  private Integer stock;
}
