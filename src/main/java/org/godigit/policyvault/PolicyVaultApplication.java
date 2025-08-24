package org.godigit.policyvault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class PolicyVaultApplication {

	public static void main(String[] args) {
		SpringApplication.run(PolicyVaultApplication.class, args);
	}

}
