package com.company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.company.model.dto.group.GroupDTO;
import com.company.model.entity.Group;
import com.company.repository.GroupRepository;

@Service
public class GroupServiceImpl extends BaseService implements GroupService{

	@Autowired
	private GroupRepository groupRepository;
	
	@Override
	public Page<GroupDTO> getAllGroupsPage(Pageable pageable) {
		
		// get entity page
		Page<Group> entityPage = groupRepository.findAll(pageable);
		
		// convert entity to dto page
		Page<GroupDTO> dtoPage = convertObjectPageToObjectPage(entityPage, pageable, GroupDTO.class);
		
		return dtoPage;
	}
}