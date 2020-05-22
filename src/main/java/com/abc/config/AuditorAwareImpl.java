package com.abc.config;

import java.util.Optional;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class AuditorAwareImpl implements AuditorAware<String> {
	@Override
	public Optional<String> getCurrentAuditor() {
		String username = "System";
		// ((UserPrinciple)
		// SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
		return Optional.of(username);
	}
}