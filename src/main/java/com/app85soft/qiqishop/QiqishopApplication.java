package com.app85soft.qiqishop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class QiqishopApplication {

	public static void main(String[] args) {
		SpringApplication.run(QiqishopApplication.class, args);
	}
}
