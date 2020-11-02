package com.ynthm.elasticsearch.repository;

import com.ynthm.elasticsearch.entity.Artwork;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author Ethan Wang
 * @version 1.0
 * @date 2020/11/2 3:30 下午
 */
public interface ArtworkRepository extends ElasticsearchRepository<Artwork, String> {}
