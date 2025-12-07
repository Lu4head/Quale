package br.com.quale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // Habilita o suporte à auditoria JPA (datas de criação e modificação)
public class QualeApplication {

	public static void main(String[] args) {
		SpringApplication.run(QualeApplication.class, args);
	}

}
