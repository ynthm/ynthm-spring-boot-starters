package com.ynthm.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.WriteResult;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.TextSearchOptions;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import com.mongodb.reactivestreams.client.Success;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static com.mongodb.client.model.Filters.text;
import static java.util.Arrays.asList;
import static com.ynthm.mongodb.SubscriberHelper.*;

class MongoUtilTest {

    @Test
    void test1() throws Throwable {

        // get a handle to the "test" collection
        MongoCollection<Document> collection = MongoUtil.instance().getCollection("mydb", "test");
        ObservableSubscriber subscriber = new ObservableSubscriber<Success>();
        collection.drop().subscribe(subscriber);
        subscriber.await();

        // getting a list of databases
        MongoUtil.instance().listDatabaseNames(new PrintSubscriber<String>("Database Names: %s"));

        // drop a database
        subscriber = new ObservableSubscriber<Success>();
        MongoUtil.instance().getDb("databaseToBeDropped").drop().subscribe(subscriber);
        subscriber.await();


        MongoDatabase mydb = MongoUtil.instance().getDb("mydb");
        // create a collection
        mydb.createCollection("cappedCollection", new CreateCollectionOptions().capped(true).sizeInBytes(0x100000))
                .subscribe(new PrintSubscriber<Success>("Creation Created!"));

        mydb.listCollectionNames().subscribe(new PrintSubscriber<>("Collection Names: %s"));

        // drop a collection:
        subscriber = new ObservableSubscriber<Success>();
        collection.drop().subscribe(subscriber);
        subscriber.await();

        // create an ascending index on the "i" field
        collection.createIndex(new Document("i", 1)).subscribe(new PrintSubscriber<String>("Created an index named: %s"));

        // list the indexes on the collection
        collection.listIndexes().subscribe(new PrintDocumentSubscriber());


        // create a text index on the "content" field
        subscriber = new PrintSubscriber<String>("Created an index named: %s");
        collection.createIndex(new Document("content", "text")).subscribe(subscriber);
        subscriber.await();

        subscriber = new OperationSubscriber();
        collection.insertMany(asList(new Document("_id", 0).append("content", "textual content"),
                new Document("_id", 1).append("content", "additional content"),
                new Document("_id", 2).append("content", "irrelevant content"))).subscribe(subscriber);
        subscriber.await();

        // Find using the text index
        subscriber = new PrintSubscriber("Text search matches: %s");
        collection.countDocuments(text("textual content -irrelevant")).subscribe(subscriber);
        subscriber.await();

        // Find using the $language operator
        subscriber = new PrintSubscriber("Text search matches (english): %s");
        Bson textSearch = text("textual content -irrelevant", new TextSearchOptions().language("english"));
        collection.countDocuments(textSearch).subscribe(subscriber);
        subscriber.await();

        // Find the highest scoring match
        System.out.print("Highest scoring document: ");
        Document projection = new Document("score", new Document("$meta", "textScore"));
        collection.find(textSearch).projection(projection).first().subscribe(new PrintDocumentSubscriber());


        // Run a command
//        database.runCommand(new Document("buildInfo", 1)).subscribe(new PrintDocumentSubscriber());
//
//        // release resources
//        subscriber = new OperationSubscriber();
//        database.drop().subscribe(subscriber);
//        subscriber.await();
//        mongoClient.close();
    }
}