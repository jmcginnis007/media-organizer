package com.example.mediaorganizer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.mediaorganizer.config.Config;
import com.example.mediaorganizer.service.ServiceManager;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner process(Config config, ServiceManager serviceMgr) {
		return (args) -> {
			System.out.println("[BEGIN] Processing has begun with the following configuration -->");
			System.out.println(config);

			serviceMgr.process();
		};
	}
}
