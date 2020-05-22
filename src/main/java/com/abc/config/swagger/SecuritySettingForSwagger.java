package com.abc.config.swagger;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.abc.config.Constants;

@Configuration
//@Order(1)
public class SecuritySettingForSwagger extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().antMatchers(Constants.SWAGGER_PERMISSION).permitAll(); // TODO - check correctness
	}
}