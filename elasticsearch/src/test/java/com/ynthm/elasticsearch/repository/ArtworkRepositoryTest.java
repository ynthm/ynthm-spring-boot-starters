package com.ynthm.elasticsearch.repository;

import com.ynthm.elasticsearch.entity.Artwork;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Streamable;

/**
 * @author Ethan Wang
 * @version 1.0
 * @date 2020/11/3 10:47 上午
 */
@SpringBootTest
class ArtworkRepositoryTest {

  @Autowired private ArtworkRepository artworkRepository;

  @Test
  void findByTitleContaining() {
    String title = "ABP-001";
    Streamable<Artwork> artworks =
        artworkRepository
            .findByTitleContaining(title)
            .and(artworkRepository.findByCodeContaining(title));
    artworks.forEach(
        i -> {
          System.out.println(i);
        });
  }

  @Test
  void findByCodeContaining() {}
}
