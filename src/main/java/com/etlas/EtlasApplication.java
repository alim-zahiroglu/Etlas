package com.etlas;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EtlasApplication {

	public static void main(String[] args) {
		SpringApplication.run(EtlasApplication.class, args);
	}

	@Bean
	public ModelMapper getMapper(){
		return new ModelMapper();
	}

}
