//package com.ynthm.elasticsearch.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.http.HttpHost;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestClientBuilder;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.springframework.beans.factory.DisposableBean;
//import org.springframework.beans.factory.FactoryBean;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.util.Arrays;
//import java.util.Objects;
//
//@Slf4j
//@Component("esClient")
//public class ElasticsearchBeanFactory implements FactoryBean<RestHighLevelClient>, InitializingBean, DisposableBean {
//
//    @Value("${elasticsearch.http.host}")
//    String[] httpHost;
//
//    private RestHighLevelClient restHighLevelClient;
//
//    @Override
//    public void destroy() throws Exception {
//        try {
//            log.info("Closing ElasticSearch client");
//            if (restHighLevelClient != null) {
//                restHighLevelClient.close();
//            }
//        } catch (final Exception e) {
//            log.error("Error closing ElasticSearch client: ", e);
//        }
//    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        buildClient();
//    }
//
//    @Override
//    public RestHighLevelClient getObject() throws Exception {
//        return restHighLevelClient;
//    }
//
//    @Override
//    public Class<?> getObjectType() {
//        return RestHighLevelClient.class;
//    }
//
//    @Override
//    public boolean isSingleton() {
//        return false;
//    }
//
//    protected void buildClient() {
//
////        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
////        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
////        RestClientBuilder builder = RestClient.builder(new HttpHost(host, port))
////                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
//
//
//
//        restHighLevelClient = new RestHighLevelClient(restClientBuilder());
//    }
//
//    public RestClientBuilder restClientBuilder() {
//        HttpHost[] hosts = Arrays.stream(httpHost)
//                .map(this::makeHttpHost)
//                .filter(Objects::nonNull)
//                .toArray(HttpHost[]::new);
//        log.debug("hosts:{}", Arrays.toString(hosts));
//        return RestClient.builder(hosts);
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
