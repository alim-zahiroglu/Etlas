package com.etlas;


import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;


@SpringBootApplication
public class EtlasApplication {

	public static void main(String[] args) {
		SpringApplication.run(EtlasApplication.class, args);
	}

	@Bean
	public ModelMapper getMapper(){
		return new ModelMapper();
	}

	@Bean
	public MigrateResult migrateResult(DataSource dataSource){
		return Flyway.configure().baselineOnMigrate(true).dataSource(dataSource).load().migrate();
	}

}
