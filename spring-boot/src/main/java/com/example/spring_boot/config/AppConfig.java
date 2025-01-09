package com.example.spring_boot.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;

import static java.util.Collections.singletonList;

@Configuration
public class AppConfig {

    @Value("${mongo.host}")
    private String mongoHost;

    @Value("${mongo.port}")
    private int mongoPort;

    @Value("${mongo.database}")
    private String mongoDbName;

    @Value("${mongo.username}")
    private String mongoUsername;

    @Value("${mongo.password}")
    private String mongoPassword;

    @Value("${mongo.authentication-database}")
    private String mongoAuthDatabase;



    public @Bean MongoClientFactoryBean mongo() {
        MongoClientFactoryBean mongo = new MongoClientFactoryBean();
        mongo.setHost(mongoHost);
        return mongo;
    }

    @Configuration
    public class MongoAppConfig extends AbstractMongoClientConfiguration {

         @Override
         public String getDatabaseName() {
            return mongoDbName;
        }

    /*    @Override
        protected boolean autoIndexCreation() {
            return true;
        }*/

        @Override
        protected void configureClientSettings(MongoClientSettings.Builder builder) {
            builder
                    .credential(MongoCredential.createCredential(mongoUsername, mongoAuthDatabase, mongoPassword.toCharArray()))
                    .applyToClusterSettings(settings -> {
                        settings.hosts(singletonList(new ServerAddress(mongoHost, mongoPort)));
                    });
        }
    }
}
