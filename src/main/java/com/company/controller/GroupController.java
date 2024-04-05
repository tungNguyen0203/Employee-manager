package com.company.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.model.dto.group.GroupDTO;
import com.company.service.GroupService;

@RestController
@RequestMapping("api/v1/groups")
@Validated
public class GroupController {
	
	@Autowired
	private GroupService groupService;
	
	@GetMapping
	public Page<GroupDTO> getAllgroups (Pageable pageable) {
		return groupService.getAllGroupsPage(pageable);
	}
}
