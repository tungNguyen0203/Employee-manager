package com.company.model.form.auth;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChangePasswordForm {
	
	@NotBlank
	@Length(min = 6, max = 20)
	private String oldPassword;
	
	@NotBlank
	@Length(min = 6, max = 20)
	private String newPassword;

}
