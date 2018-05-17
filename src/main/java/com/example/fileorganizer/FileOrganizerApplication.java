package com.example.fileorganizer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.fileorganizer.config.FileOrganizerConfig;
import com.example.fileorganizer.domain.Results;
import com.example.fileorganizer.service.FileOrganizerService;

@SpringBootApplication
public class FileOrganizerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileOrganizerApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner process(FileOrganizerConfig config, FileOrganizerService service) {
		return (args) -> {
			System.out.println("begin processing with the following config...");
			System.out.println(config);
			
			Results results = service.organizeFiles(config.getSourcedir(), config.getDestdir(), config.getExtensions());
		};
	}
}
