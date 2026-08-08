package com.vidhya.supplyconnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
		exclude = {
				org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration.class
		}
)
public class SupplyconnectBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SupplyconnectBackendApplication.class, args);
	}

}
