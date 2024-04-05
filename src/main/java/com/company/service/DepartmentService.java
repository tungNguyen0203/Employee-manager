package com.company.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.company.model.dto.department.AccountDTO;
import com.company.model.dto.department.DepartmentDTO;
import com.company.model.form.department.AccountFilterForm;
import com.company.model.form.department.CreatingDepartmentForm;
import com.company.model.form.department.DepartmentFilterForm;

import jakarta.validation.Valid;

public interface DepartmentService {

	Page<DepartmentDTO> getAllDepartmentsPage(Pageable pageable, DepartmentFilterForm departmentFilterForm);

	DepartmentDTO getDepartmentById(Integer id);

	boolean isDepartmentExistsById(Integer id);

	Page<AccountDTO> getAccountsByDepartmentId(Integer id, Pageable pageable, AccountFilterForm accountFilterForm);

	void removeAccountsInDepartment(List<Integer> accountIds);

	boolean isDepartmentExistsByName(String name);

	void createDepartment(CreatingDepartmentForm form);
}
