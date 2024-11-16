package com.wap.cano_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CanoBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CanoBeApplication.class, args);
	}

}
