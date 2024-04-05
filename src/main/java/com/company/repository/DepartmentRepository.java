package com.company.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.company.model.entity.Department;


public interface DepartmentRepository  extends JpaRepository<Department, Integer>, JpaSpecificationExecutor<Department> {
	Boolean findByName(String name);
}
