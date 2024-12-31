package com.example.spring_boot.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;

import static java.util.Collections.singletonList;

@Configuration
public class AppConfig {

    public @Bean MongoClientFactoryBean mongo() {
        MongoClientFactoryBean mongo = new MongoClientFactoryBean();
        mongo.setHost("localhost");
        return mongo;
    }

    @Configuration
    public class MongoAppConfig extends AbstractMongoClientConfiguration {

        @Override
        public String getDatabaseName() {
            return "gis";
        }

        @Override
        protected boolean autoIndexCreation() {
            return true;
        }

        @Override
        protected void configureClientSettings(MongoClientSettings.Builder builder) {
            builder
                    .credential(MongoCredential.createCredential("root", "admin", "example".toCharArray()))
                    .applyToClusterSettings(settings -> {
                        settings.hosts(singletonList(new ServerAddress("127.0.0.1", 27017)));
                    });
        }
    }
}
