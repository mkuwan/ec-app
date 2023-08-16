package dev.mkuwan.adminstock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AdminStockApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminStockApplication.class, args);

        System.out.println("hello Admin Stock Application!");
    }

}
