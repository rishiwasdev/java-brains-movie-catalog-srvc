package com.abc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.abc.config.AuditorAwareImpl;

@SpringBootApplication
@EnableJpaRepositories
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JavaBrainsMovieCatalogSrvcApplication {
	public static void main(String[] args) {
		SpringApplication.run(JavaBrainsMovieCatalogSrvcApplication.class, args);
	}

	@Bean
	public AuditorAware<String> auditorAware() {
		return new AuditorAwareImpl();
	}

	@Bean
	public RestTemplate restTemplate() { // (RestTemplateBuilder builder)
		return new RestTemplate();
	}

	@Bean
	public WebClient.Builder getWebClientBuilder() {
		return WebClient.builder(); // needs Reactive programming and Maven-dependency 'spring-webflux'
	}
//	public static void setApplicationContext(final ApplicationContext applicationContext) {
//		// always set the current context
//		SpringBeanFactory.applicationContext = applicationContext;
//	}
}
