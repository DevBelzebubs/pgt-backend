package com.portable.microservices.ms_tracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsTrackingApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsTrackingApplication.class, args);
	}

}
