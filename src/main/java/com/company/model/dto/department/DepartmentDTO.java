package com.company.model.dto.department;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDTO {
	
	private int id;
	
	private String name;

	private Integer memberSize;

	private String managerFullname;

	private String managerEmail;

	private String creatorFullname;

	private String creatorEmail;

	private Date createdDateTime;
}
