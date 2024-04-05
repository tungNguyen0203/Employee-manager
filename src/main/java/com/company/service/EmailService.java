package com.company.service;

import com.company.model.entity.Account;

public interface EmailService {

	void sendActiveAccountRegistrationEmail(Account account, String newRegistrationToken);

	void sendForgotPasswordEmail(Account account, String forgotPasswordToken);

	void sendChangePasswordEmail(Account account);

}
