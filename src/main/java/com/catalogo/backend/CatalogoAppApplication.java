package com.catalogo.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CatalogoAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(CatalogoAppApplication.class, args);
	}
}
