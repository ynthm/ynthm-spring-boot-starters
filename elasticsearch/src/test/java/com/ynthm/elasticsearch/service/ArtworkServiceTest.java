package com.ynthm.elasticsearch.service;

import com.ynthm.elasticsearch.entity.Artwork;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ethan Wang
 * @version 1.0
 * @date 2020/11/2 3:40 下午
 */
@SpringBootTest
class ArtworkServiceTest {

  @Autowired private ArtworkService artworkService;

  @Test
  void artworkList() {

    SearchHits<Artwork> artworks = artworkService.artworkList("我的");
    artworks.forEach(System.out::println);
  }

  @Test
  void count() {}

  @Test
  void save() {
    List<String> tags = new ArrayList<>();
    tags.add("科幻");
    tags.add("动作");
    Artwork artwork =
        new Artwork()
            .setCode("abc-123")
            .setTitle("abc-123 我的")
            .setTags(tags)
            // .setReleaseDate(LocalDate.now())
            .setPublishedTime(LocalDateTime.now());
    Artwork save = artworkService.save(artwork);
    System.out.println(save);
  }

  @Test
  void delete() {}

  @Test
  void getAll() {
    Iterable<Artwork> all = artworkService.getAll();
    all.forEach(
        i -> {
          System.out.println(i);
        });
  }

  @Test
  void getByName() {}

  @Test
  void pageQuery() {}
}
