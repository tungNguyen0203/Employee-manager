package com.company.model.validation.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.company.service.AccountService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AccountUsernameExistsValidator implements ConstraintValidator<AccountUsernameExists, String>{

	@Autowired
	private AccountService accountService;
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean isValid(String username, ConstraintValidatorContext context) {
		
		if (StringUtils.isEmpty(username)) {
			return true;
		}

		return accountService.isAccountExistsByUsername(username);
	}

}
