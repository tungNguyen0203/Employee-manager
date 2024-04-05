package com.company.service;

import com.company.model.entity.Account;
import com.company.model.entity.Token;

public interface TokenService {

	Token generateAccountRegistrationToken(Account account);

	void deleteAccountRegistrationToken(Account account);

	Token getRegistrationTokenByKey(String key);

	boolean isRegistrationTokenValid(String registrantionToken);
	
	Token generateForgotPasswordToken(Account account);
	
	void deleteForgotPasswordToken(Account account);

	Token getForgotPasswordTokenByKey(String forgotPasswordToken);
}
