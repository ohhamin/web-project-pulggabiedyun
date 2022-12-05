package com.ssafy.dokcho2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Dokcho2Application {

    public static void main(String[] args) {
        SpringApplication.run(Dokcho2Application.class, args);
    }

}
