package com.portable.microservices.ms_administration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsAdministrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsAdministrationApplication.class, args);
	}

}
