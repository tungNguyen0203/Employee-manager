package com.company.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.model.entity.Group;

public interface GroupRepository  extends JpaRepository<Group, Integer> {
	
}
