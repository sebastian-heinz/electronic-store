package org.example;

import org.example.persistence.DataStore;
import org.example.persistence.javaobject.JavaObjectDataStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class ElectronicStore {

    public static void main(String[] args) {
        SpringApplication.run(ElectronicStore.class, args);
    }

    @Bean
    public DataStore dataStore() {
        return new JavaObjectDataStore();
    }

}
