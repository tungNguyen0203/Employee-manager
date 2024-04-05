package com.company.model.validation.account;

import org.springframework.beans.factory.annotation.Autowired;

import com.company.service.AccountService;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AccountEmailNotExistsValidator implements ConstraintValidator<AccountEmailNotExists, String> {

	@Autowired
	private AccountService service;

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {

		if (StringUtils.isEmpty(email)) {
			return true;
		}

		return !service.isAccountExistsByEmail(email);
	}
}
