package com.company.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.company.model.entity.Account;
import com.company.repository.AccountRepository;

@Configuration
public class SecurityUtils {
	
	@Autowired
	private AccountRepository accountRepository;
	
	public Account getCurrentAcconutLogin() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication == null ? null : accountRepository.findByUsername(authentication.getName());
	}
}
