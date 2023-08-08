package dev.mkuwan.customer.catalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CatalogForCustomerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatalogForCustomerApplication.class, args);

		System.out.println("Hello Catalog Service");
	}

}
