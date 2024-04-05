package com.company.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.company.model.entity.Account;
import com.company.model.entity.Token;
import com.company.model.entity.Token.Type;

public interface TokenRepository  extends JpaRepository<Token, Integer> {
	
	@Modifying
	public void deleteByTypeAndAccount(Type type, Account account);
	
	public Token findBykeyAndType(String refreshToken, Type type);
}
