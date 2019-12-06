package com.ynthm.mongodb;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.Document;
import org.reactivestreams.Subscriber;

import java.util.ArrayList;
import java.util.List;

public class MongoUtil {

    private MongoClient mongoClient;

    private MongoUtil() {
        //服务器实例表
        List servers = new ArrayList();
        servers.add(new ServerAddress("localhost", 27017));

        //配置构建器
        MongoClientSettings.Builder settingsBuilder = MongoClientSettings.builder();

        //传入服务器实例
        settingsBuilder.applyToClusterSettings(
                builder -> builder.hosts(servers));

        //构建 Client 实例
        mongoClient = MongoClients.create(settingsBuilder.build());
    }

    private static class SingletonHolder {
        private static final MongoUtil UTIL = new MongoUtil();
    }

    public static MongoUtil instance() {
        return SingletonHolder.UTIL;
    }

    public MongoDatabase getDb(String databaseName) {
        return mongoClient.getDatabase(databaseName);
    }

    public MongoCollection<Document> getCollection(String databaseName, String collectionName) {
        MongoDatabase db = getDb(databaseName);
        return db.getCollection(collectionName);
    }

    public void listDatabaseNames(Subscriber subscriber) {
        mongoClient.listDatabaseNames().subscribe(subscriber);
    }

    /**
     * 程序关闭调用
     */
    public void close() {
        mongoClient.close();
    }

}
