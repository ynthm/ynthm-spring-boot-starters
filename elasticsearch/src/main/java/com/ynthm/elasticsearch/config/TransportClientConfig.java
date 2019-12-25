//package com.ynthm.elasticsearch.config;
//
//import org.apache.http.HttpHost;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestClientBuilder;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.elasticsearch.rest.RestClientBuilderCustomizer;
//import org.springframework.boot.autoconfigure.elasticsearch.rest.RestClientProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.core.convert.support.DefaultConversionService;
//import org.springframework.data.elasticsearch.client.ClientConfiguration;
//import org.springframework.data.elasticsearch.client.RestClients;
//import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
//import org.springframework.data.elasticsearch.core.ElasticsearchEntityMapper;
//import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
//import org.springframework.data.elasticsearch.core.EntityMapper;
//
//import java.time.Duration;
//
//@Configuration
//public class TransportClientConfig extends AbstractElasticsearchConfiguration {
//
//    @Autowired
//    private  RestClientProperties properties;
//
//    @Override
//    public RestHighLevelClient elasticsearchClient() {
//        HttpHost[] hosts = properties.getUris().stream().map(HttpHost::create).toArray(HttpHost[]::new);
//
//        ClientConfiguration clientConfiguration = ClientConfiguration.builder().connectedTo("localhost:9200")
//                .withConnectTimeout(Duration.ofSeconds(15)).withSocketTimeout(Duration.ofSeconds(15))
//                //.withBasicAuth()
//                .build();
//        // ClientConfiguration.localhost()
//        ClientConfiguration.localhost().getEndpoints().forEach(e-> {
//            System.out.println(e.getHostName()+" : "+e.getPort());
//        });
//
//        return RestClients.create(ClientConfiguration.localhost()).rest();
//    }
//
////    @Bean
////    RestHighLevelClient client() {
////        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
////                // "localhost:9200", "localhost:9201"
////                .connectedTo("localhost:9200")
////                .build();
////
////        return RestClients.create(clientConfiguration).rest();
////    }
//
//    @Bean
//    @Primary
//    public ElasticsearchOperations elasticsearchTemplate() {
//        return elasticsearchOperations();
//    }
//
//    // no special bean creation needed
//    // 基类AbstractElasticsearchConfiguration已经提供了elasticsearchTemplate bean
//
//    // use the ElasticsearchEntityMapper 使用元模型对象映射 ElasticsearchMappe
//    @Bean
//    @Override
//    public EntityMapper entityMapper() {
//        ElasticsearchEntityMapper entityMapper = new ElasticsearchEntityMapper(elasticsearchMappingContext(),
//                new DefaultConversionService());
//        entityMapper.setConversions(elasticsearchCustomConversions());
//
//        return entityMapper;
//    }
//}