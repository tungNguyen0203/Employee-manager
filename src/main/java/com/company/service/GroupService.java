package com.company.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.company.model.dto.group.GroupDTO;

public interface GroupService {

	public Page<GroupDTO> getAllGroupsPage(Pageable pageable);
}
