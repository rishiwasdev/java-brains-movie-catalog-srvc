package com.abc.config.swagger;

import static springfox.documentation.builders.PathSelectors.regex;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicate;

import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Value("${api.basePackage}")
	private String basePackage;
	@Value("${api.title}")
	private String title;
	@Value("${api.description}")
	private String description;
	@Value("${api.version}")
	private String version;
	@Value("${api.contact.name}")
	private String contactName;
	@Value("${api.contact.email}")
	private String email;
	@Value("${api.contact.url}")
	private String contactUrl;
	@Value("${api.licenceUrl}")
	private String licenceUrl;
	@Value("${api.tos.url}")
	private String termOfService;

	@Bean
	Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select().apis(basePackage()).paths(paths()).build().securitySchemes(apiKeys()).securityContexts(securityContext());
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title(title).description(description).version(version).licenseUrl(licenceUrl).contact(new Contact(contactName, contactName, email)).termsOfServiceUrl(termOfService)
				.build();
	}

	private Predicate<RequestHandler> basePackage() {
		return RequestHandlerSelectors.basePackage(basePackage);
	}

	private Predicate<String> paths() {
		return regex("/.*");
	}

	private List<SecurityContext> securityContext() {
		SecurityContext context = SecurityContext.builder().securityReferences(defaultAuth()).forPaths(paths()).build();
		return Collections.singletonList(context);
	}

	private List<SecurityReference> defaultAuth() {
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[] { new AuthorizationScope("read", "read only"), new AuthorizationScope("write", "read and write") };
		return Collections.singletonList(new SecurityReference("Authorization", authorizationScopes));
	}

	private List<SecurityScheme> apiKeys() {
		return Collections.singletonList(new ApiKey("Authorization", "Authorization", "header"));
	}
}