package com.company.model.dto.group;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDTO {
	
	private int id;
	
	private String name;

	private Integer memberSize;

	private String creatorFullname;

	private Date createdDateTime;
}
