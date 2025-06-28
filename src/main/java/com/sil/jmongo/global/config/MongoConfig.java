package com.sil.jmongo.global.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.sil.jmongo")
@EnableMongoAuditing
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.uri}")
    private String MONGODB_URI;

    @Override
    protected String getDatabaseName() {
        return "jmongo";
    }

    @Override
    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(MONGODB_URI);
    }

    @Override
    protected boolean autoIndexCreation() {
        return true; // 필요에 따라 인덱스 자동 생성
    }

    @Bean
    public MongoTemplate mongoTemplate() {
//        System.out.println("MONGODB_URI = " + MONGODB_URI);
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }
}
