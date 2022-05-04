package com.example.demorest;

import com.example.demorest.entities.Employee;
import com.example.demorest.repositories.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    private static final Logger LOG = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository repository) {
        return args -> {
            LOG.info("Preloading " + repository.save(new Employee("Bilbo Baggins", "burglar")));
            LOG.info("Preloading " + repository.save(new Employee("Frodo Baggins", "thief")));
        };
    }
}
