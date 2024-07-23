package com.ghtk.ecommercewebsite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class EcommercewebsiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommercewebsiteApplication.class, args);
	}

}
