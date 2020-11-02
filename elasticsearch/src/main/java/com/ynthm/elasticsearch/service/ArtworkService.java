package com.ynthm.elasticsearch.service;

import com.ynthm.elasticsearch.entity.Artwork;
import com.ynthm.elasticsearch.repository.ArtworkRepository;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ethan Wang
 * @version 1.0
 * @date 2020/11/2 3:31 下午
 */
@Service
public class ArtworkService {
  private final ArtworkRepository artworkRepository;

  private ElasticsearchRestTemplate elasticsearchTemplate;

  @Autowired
  public void setElasticsearchTemplate(ElasticsearchRestTemplate elasticsearchTemplate) {
    this.elasticsearchTemplate = elasticsearchTemplate;
  }

  public ArtworkService(ArtworkRepository artworkRepository) {
    this.artworkRepository = artworkRepository;
  }

  public SearchHits<Artwork> artworkList(String artworkTitle) {
    NativeSearchQuery searchQuery =
        new NativeSearchQueryBuilder()
            .withQuery(QueryBuilders.matchQuery("title", artworkTitle))
            .build();
    return elasticsearchTemplate.search(searchQuery, Artwork.class);
  }

  public long count() {
    return artworkRepository.count();
  }

  public Artwork save(Artwork artwork) {
    return artworkRepository.save(artwork);
  }

  public void delete(Artwork artwork) {
    artworkRepository.delete(artwork);
    //   commodityRepository.deleteById(commodity.getSkuId());
  }

  public Iterable<Artwork> getAll() {
    return artworkRepository.findAll();
  }

  public List<Artwork> getByName(String name) {
    List<Artwork> list = new ArrayList<>();
    MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("name", name);
    Iterable<Artwork> iterable = artworkRepository.search(matchQueryBuilder);
    iterable.forEach(e -> list.add(e));
    return list;
  }

  public Page<Artwork> pageQuery(Integer pageNo, Integer pageSize, String kw) {
    NativeSearchQuery searchQuery =
        new NativeSearchQueryBuilder()
            .withQuery(QueryBuilders.matchPhraseQuery("name", kw))
            .withPageable(PageRequest.of(pageNo, pageSize))
            .build();

    return artworkRepository.search(searchQuery);
  }
}
