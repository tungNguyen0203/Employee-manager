package com.company.service;

import com.company.config.security.AccountBlockException;
import com.company.model.dto.auth.LoginInfoDTO;
import com.company.model.form.auth.ChangePasswordForm;
import com.company.model.form.auth.CreatingAccountForm;
import com.company.model.form.auth.ResetPasswordForm;

public interface AuthService {

	LoginInfoDTO login(String username)throws AccountBlockException;
	
	void createAccount(CreatingAccountForm creatingAccountForm);

	void sendAccountRegistrationTokenViaEmail(String username);

	void activeAccount(String registrantionToken);
	
	void sendAccountForgotPasswordTokenViaEmail(String usernameOrEmail);
	
	void resetPassword(ResetPasswordForm resetPasswordForm);

	void changePassword(ChangePasswordForm changePasswordForm);
}