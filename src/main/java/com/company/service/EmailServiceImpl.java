package com.company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.company.model.entity.Account;

@Service
public class EmailServiceImpl extends BaseService implements EmailService{

	@Value("${server.protocol}")
	private String SERVER_PROTOCOL;

	@Value("${server.hostname}")
	private String SERVER_HOSTNAME;

	@Value("${server.port}")
	private long SERVER_PORT;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Override
	public void sendActiveAccountRegistrationEmail(Account account, String newRegistrationToken) {
		String activeAccountUrl = String.format("%s://%s:%d/api/v1/auth/registration/active?registrationToken=%s", 
				SERVER_PROTOCOL, 
				SERVER_HOSTNAME, 
				SERVER_PORT,
				newRegistrationToken);
		String subject = "[FinalExam] Active Account";
		String content = "You have successfully registered an account\n"
				+ "Click on the link below to activate your account\n" + activeAccountUrl;
		
		sendEmail(account.getEmail(), subject, content);
	}

	private void sendEmail(String recipientEmail, String subject, String content) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(recipientEmail);
		message.setSubject(subject);
		message.setText(content);
		
		mailSender.send(message);
	}
	
	@Override
	public void sendForgotPasswordEmail(Account account, String forgotPasswordToken) {
		String forgotPasswordUrl = String.format("%s://%s:%d/api/v1/auth/password/new-password?forgotPasswordToken=%s", 
				SERVER_PROTOCOL, 
				SERVER_HOSTNAME, 
				SERVER_PORT,
				forgotPasswordToken);
		String subject = "[FinalExam] Forgot Password";
		String content = "You have just sent a forgot password request\n"
				+ "Click on the link below to get new password\n" + forgotPasswordUrl;
		
		sendEmail(account.getEmail(), subject, content);
	}
	
	@Override
	public void sendChangePasswordEmail(Account account) {
		String subject = "[FinalExam] Change Password";
		String content = "You have just changed your password\n";
		
		sendEmail(account.getEmail(), subject, content);
	}
}
