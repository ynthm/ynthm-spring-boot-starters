package com.ynthm.elasticsearch.entity;

import com.ynthm.elasticsearch.config.IndexNameConst;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Ethan Wang
 * @version 1.0
 * @date 2020/11/2 3:21 下午
 */
@Data
@Accessors(chain = true)
@Document(indexName = IndexNameConst.ARTWORK)
public class Artwork {

  @Id private String id;

  /** 作品番号 */
  @NotEmpty private String code;
  /** 作品标题 */
  private String title;

  /** 发行日 */
  @Field(type = FieldType.Date, format = DateFormat.year_month_day)
  private LocalDate releaseDate;
  /** 时长 */
  private String playDuration;
  /** 导演 */
  private String director;
  /** 片商 */
  private String maker;
  /** 发行 */
  private String publisher;
  /** 系列 */
  private String series;
  /** 评分，及多少人评分 */
  private String mark;
  /** 类别 标签 */
  @Field(type = FieldType.Keyword)
  private List<String> tags;
  /** 演员列表 */
  @Field(type = FieldType.Keyword)
  private List<String> actorNames;

  @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
  private LocalDateTime publishedTime;
}
