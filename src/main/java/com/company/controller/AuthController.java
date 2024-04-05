package com.company.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.config.security.AccountBlockException;
import com.company.model.dto.auth.LoginInfoDTO;
import com.company.model.dto.auth.TokenDTO;
import com.company.model.form.auth.ChangePasswordForm;
import com.company.model.form.auth.CreatingAccountForm;
import com.company.model.form.auth.LoginForm;
import com.company.model.form.auth.ResetPasswordForm;
import com.company.model.validation.account.AccountUsernameExists;
import com.company.model.validation.account.AccountUsernameOrEmailExists;
import com.company.model.validation.auth.RefreshTokenValid;
import com.company.model.validation.auth.RegistrationTokenValid;
import com.company.service.AuthService;
import com.company.service.JWTTokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "api/v1/auth")
@Validated
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JWTTokenService jwtTokenService;
	
	@PostMapping("/login")
	public LoginInfoDTO login (@RequestBody @Valid LoginForm loginForm) throws AccountBlockException {
		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginForm.getUsername(), 
						loginForm.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return authService.login(loginForm.getUsername());
	}
	
	@GetMapping("/refreshtoken")
	public TokenDTO refreshToken (@RefreshTokenValid String refreshToken) throws AccountBlockException{
		return jwtTokenService.getNewToken(refreshToken);
	}
	
	@PostMapping("/registration")
	public String createAccount (@Valid @RequestBody CreatingAccountForm creatingAccountForm) {
		authService.createAccount(creatingAccountForm);
		return "We have sent a email. Please check email to active account!";
	}
	// gui lai email khi link het han
	@GetMapping("/registration/active-mail")
	public String resendAccountRegistrationTokenViaEmail(@RequestParam @AccountUsernameExists String username) {
		authService.sendAccountRegistrationTokenViaEmail(username);
		return "We have sent a email. Please check email to active account!";
	}
	// click vao link de active
	@GetMapping("/registration/active")
	public String activeAccountViaEmail (@RequestParam @RegistrationTokenValid String registrationToken) {
		authService.activeAccount(registrationToken);
		return "Active success";
	}

	@GetMapping("/password/forgot-mail")
	public String forgotPasswordViaEmail (@RequestParam @AccountUsernameOrEmailExists String usernameOrEmail) {
		authService.sendAccountForgotPasswordTokenViaEmail(usernameOrEmail);
		return "Email sent to your email! Please check it!";
	}
	
	@PutMapping("/password/new-password")
	public String changeNewPassword (@Valid @RequestBody ResetPasswordForm resetPasswordForm) {
		authService.resetPassword(resetPasswordForm);
		return "Change password successfully!";
	}
	
	@PutMapping("/password/change")
	public String changePassword (@Valid @RequestBody ChangePasswordForm changePasswordForm) {
		authService.changePassword(changePasswordForm);
		return "Change password successfully!";
	}
}
