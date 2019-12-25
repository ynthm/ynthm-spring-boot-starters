//package com.ynthm.elasticsearch.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.http.HttpHost;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestClientBuilder;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.Arrays;
//import java.util.Objects;
//
//@Slf4j
//@Configuration
//public class ElasticsearchConfig {
//    @Value("${elasticsearch.http.host}")
//    String[] httpHost;
//
//
//    @Bean
//    public RestClientBuilder restClientBuilder() {
//        HttpHost[] hosts = Arrays.stream(httpHost)
//                .map(this::makeHttpHost)
//                .filter(Objects::nonNull)
//                .toArray(HttpHost[]::new);
//        log.debug("hosts:{}", Arrays.toString(hosts));
//        return RestClient.builder(hosts);
//    }
//
//    @Bean(name = "highLevelClient")
//    public RestHighLevelClient highLevelClient(@Autowired RestClientBuilder restClientBuilder) {
//        return new RestHighLevelClient(restClientBuilder);
//    }
//
//    private HttpHost makeHttpHost(String s) {
//        assert StringUtils.isNotEmpty(s);
//        String[] address = s.split(":");
//        if (address.length == 2) {
//            String ip = address[0];
//            int port = Integer.parseInt(address[1]);
//            return new HttpHost(ip, port);
//        } else {
//            return null;
//        }
//    }
//}
