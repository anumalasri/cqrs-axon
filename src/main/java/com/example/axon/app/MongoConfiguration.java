package com.example.axon.app;

import java.util.Arrays;

import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.mongo.eventsourcing.eventstore.DefaultMongoTemplate;
import org.axonframework.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.mongo.eventsourcing.eventstore.MongoFactory;
import org.axonframework.mongo.eventsourcing.eventstore.documentperevent.DocumentPerEventStorageStrategy;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

@EnableMongoRepositories(mongoTemplateRef = "mongoTemplate", basePackages = "com.example.axon.repository")
@Configuration
public class MongoConfiguration {

	
	@Value("${mongodb.url}")
    private String mongoUrl;

    @Value("${mongodb.dbname}")
    private String mongoDbName;

    @Value("${mongodb.events.collection.name}")
    private String eventsCollectionName;

    @Value("${mongodb.events.snapshot.collection.name}")
    private String snapshotCollectionName;
    
    @Value("${mongodb.sagas.collection.name}")
    private String sagasCollectionName;

    @Bean(name="axonSerializer")
    public Serializer axonSerialzier() {
    	return new JacksonSerializer();
    }


    /** For Spring Data **/
	@Bean
	public MongoDbFactory mongoDbFactory() throws Exception {
		return new SimpleMongoDbFactory(new MongoClient(mongoUrl), mongoDbName);
	}

	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		return new MongoTemplate(mongoDbFactory());
	}

	
	/** For Axon Events Storage */

	@Bean(name = "axonMongoTemplate")
	public DefaultMongoTemplate axonMongoTemplate() {
		DefaultMongoTemplate template = new DefaultMongoTemplate(mongoClient(), mongoDbName, eventsCollectionName,
				snapshotCollectionName);
		return template;
	}

	@Bean
	public MongoClient mongoClient() {
		MongoFactory mongoFactory = new MongoFactory();
		mongoFactory.setMongoAddresses(Arrays.asList(new ServerAddress(mongoUrl)));
		return mongoFactory.createMongo();
	}

	@Bean
	public EventStorageEngine eventStorageEngine() {
		return new MongoEventStorageEngine(axonSerialzier(), null, axonMongoTemplate(), new DocumentPerEventStorageStrategy());
	}
	 
	
	 
}
