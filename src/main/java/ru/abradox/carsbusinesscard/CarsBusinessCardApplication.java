package ru.abradox.carsbusinesscard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class CarsBusinessCardApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarsBusinessCardApplication.class, args);
    }

}
