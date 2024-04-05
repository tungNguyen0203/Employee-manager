package com.company.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.config.security.AccountBlockException;
import com.company.config.security.SecurityUtils;
import com.company.model.dto.auth.LoginInfoDTO;
import com.company.model.entity.Account;
import com.company.model.entity.Account.Status;
import com.company.model.form.auth.ChangePasswordForm;
import com.company.model.form.auth.CreatingAccountForm;
import com.company.model.form.auth.ResetPasswordForm;
import com.company.model.entity.Token;
import com.company.repository.AccountRepository;

@Service
@Transactional
public class AuthServiceImpl extends BaseService implements AuthService{

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private JWTTokenService jwtTokenService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private SecurityUtils securityUtils;
	
	@Override
	public LoginInfoDTO login(String username) throws AccountBlockException {
		
		// Get entity
		Account entity = accountService.getAccountByUsername(username);
		
		if (entity.getStatus() == Status.BLOCK) {
			throw new AccountBlockException("Your account is blocked!");
		}
		
		//Convert entity to dto
		LoginInfoDTO dto = convertObjectToObject(entity, LoginInfoDTO.class);
		
		// Add JWT token to dto
		dto.setToken(jwtTokenService.generateJWTToken(entity.getUsername()));
		
		// Add refresh token to dto
		Token token = jwtTokenService.generateRefreshToken(entity);
		dto.setRefreshToken(token.getKey());
		
		return dto;
	}

	@Override
	public void createAccount(CreatingAccountForm creatingAccountForm) {
		
		// Create account (status = block and role = employee)
		Account entity = convertObjectToObject(creatingAccountForm, Account.class);
		entity.setPassword(passwordEncoder.encode(creatingAccountForm.getPassword()));
		accountRepository.save(entity);
		
		// Create token and send to email
		sendAccountRegistrationTokenViaEmail(entity.getUsername());
	}
	
	@Override
	public void sendAccountRegistrationTokenViaEmail (String username) {
		// Get account
		Account account = accountRepository.findByUsername(username);
		// Create new token
		Token newRegistrationToken = tokenService.generateAccountRegistrationToken(account);
		// Send to email
		emailService.sendActiveAccountRegistrationEmail(account, newRegistrationToken.getKey());
	}

	@Override
	public void activeAccount(String registrationToken) {
		Token token = tokenService.getRegistrationTokenByKey(registrationToken);
		
		// Active account
		Account account = token.getAccount();
		account.setStatus(Status.ACTIVE);
		account.setUpdatedDateTime(new Date());
		accountRepository.save(account);
		
		// Delete registration token
		tokenService.deleteAccountRegistrationToken(account);
	}

	@Override
	public void sendAccountForgotPasswordTokenViaEmail(String usernameOrEmail) {
		
		Account account = accountRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
		
		Token newForgotPasswordToken = tokenService.generateForgotPasswordToken(account);
		
		emailService.sendForgotPasswordEmail(account, newForgotPasswordToken.getKey());
	}

	@Override
	public void resetPassword(ResetPasswordForm resetPasswordForm) {

		Token token = tokenService.getForgotPasswordTokenByKey(resetPasswordForm.getForgotPasswordToken());
		
		//reset password
		Account account = token.getAccount();
		account.setPassword(passwordEncoder.encode(resetPasswordForm.getNewPassword()));
		account.setUpdatedDateTime(new Date());
		account.setLastChangePasswordDateTime(new Date());
		
		accountRepository.save(account);
		
		//delete forgot token
		tokenService.deleteForgotPasswordToken(account);
		
		//delete refresh token
		jwtTokenService.deleteRefreshToken(account);
	}
	
	@Override
	public void changePassword(ChangePasswordForm changePasswordForm) {
		//change password
		Account account = securityUtils.getCurrentAcconutLogin();
		account.setPassword(passwordEncoder.encode(changePasswordForm.getNewPassword()));
		account.setUpdatedDateTime(new Date());
		account.setLastChangePasswordDateTime(new Date());
		
		accountRepository.save(account);
		
		//delete refresh token
		jwtTokenService.deleteRefreshToken(account);
		
		// send email
		emailService.sendChangePasswordEmail(account);
	}
}