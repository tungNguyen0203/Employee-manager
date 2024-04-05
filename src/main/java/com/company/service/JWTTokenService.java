package com.company.service;

import org.springframework.security.core.Authentication;

import com.company.model.dto.auth.TokenDTO;
import com.company.model.entity.Account;
import com.company.model.entity.Token;

import jakarta.servlet.http.HttpServletRequest;

public interface JWTTokenService {
	
	public String generateJWTToken(String username);
	
	public Authentication parseTokenToUserInformation(HttpServletRequest request);
	
	public Token generateRefreshToken(Account account);
	
	public boolean isRefreshTokenValid(String refreshToken);
	
	public TokenDTO getNewToken (String refreshToken);

	void deleteRefreshToken(Account account);
}
