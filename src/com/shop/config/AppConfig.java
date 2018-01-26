package com.shop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

@Configuration 
@ComponentScan("com.shop") 
@EnableWebMvc
@EnableSpringDataWebSupport
@PropertySource(value= "classpath:/mongo.properties")
@EnableMongoRepositories(basePackages = "com.shop.dao")
public class AppConfig extends AbstractMongoConfiguration {

	@Autowired
	Environment env;
	
	@Override
	public MongoClient mongoClient() {
		MongoClientOptions.Builder builder =  new MongoClientOptions.Builder();
		builder.connectionsPerHost(50);
		builder.writeConcern(WriteConcern.JOURNALED);
		builder.readPreference(ReadPreference.secondaryPreferred());
		MongoClientOptions options = builder.build();
		MongoClient mongoClient = new MongoClient(new ServerAddress(env.getProperty("mongo.server"), Integer.parseInt( env.getProperty("mongo.port"))), options);
		return mongoClient;
		//return new MongoClient("localhost", 27017);
	}

	/*
	@Override
	public MongoDbFactory mongoDbFactory() {
		MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongoClient(),
		env.getProperty("mongo.databaseName"));

		return mongoDbFactory;
	}
	
	@Override
	public MongoTemplate mongoTemplate() {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
        return mongoTemplate;
    }

	*/

	@Override
	protected String getDatabaseName() {
		return env.getProperty("mongo.databaseName");
	}  
} 