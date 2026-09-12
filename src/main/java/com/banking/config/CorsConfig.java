package com.banking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer{

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
				.allowedOrigins("http://localhost:5173", "https://bank-front-end--himanshudubal63.replit.app","http://localhost:3000")
				.allowedMethods("GET","PUT,","POST","DELETE","OPTIONS","PATCH")
				.allowedHeaders("*")
				.exposedHeaders("Authorization")
				.allowCredentials(true);
				//.maxAge(3600);
			}
		};
	}

}
