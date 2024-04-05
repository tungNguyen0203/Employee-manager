package com.company.model.dto.auth;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginInfoDTO {
	
	private int id;
	
	private String fullname;
	
	private String email;

	private String status;

	private String departmentName;

	private String role;

	private String token;
	
	private String refreshToken;
}