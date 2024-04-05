package com.company.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.model.dto.department.AccountDTO;
import com.company.model.dto.department.DepartmentDTO;
import com.company.model.form.department.AccountFilterForm;
import com.company.model.form.department.CreatingDepartmentForm;
import com.company.model.form.department.DepartmentFilterForm;
import com.company.model.validation.account.AccountIdExists;
import com.company.model.validation.department.DepartmentIdExists;
import com.company.service.DepartmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/departments")
@Validated
public class DepartmentController {
	
	@Autowired
	private DepartmentService departmentService;
	
	@GetMapping
	public Page<DepartmentDTO> getAllDepartments (Pageable pageable, @Valid DepartmentFilterForm departmentFilterForm) {
		return departmentService.getAllDepartmentsPage(pageable, departmentFilterForm);
	}
	
	@GetMapping("/{id}")
	public DepartmentDTO getDepartmentById (@PathVariable(name = "id")@DepartmentIdExists Integer id) {
		return departmentService.getDepartmentById(id);
	}
	
	@GetMapping("/{id}/accounts")
	public Page<AccountDTO> getAccountsByDepartmentId (
			@PathVariable(name = "id")@DepartmentIdExists Integer id, 
			Pageable pageable, 
			@Valid AccountFilterForm accountFilterForm) {
		return departmentService.getAccountsByDepartmentId(id, pageable, accountFilterForm);
	}
	
	@DeleteMapping("/accounts/{accountIds}")
	public String removeAccountsInDepartment (@PathVariable(name = "accountIds") List<@AccountIdExists Integer> accountIds) {
		departmentService.removeAccountsInDepartment(accountIds);
		return "Remove accounts successfully!";
	}
	
	@GetMapping("/name/exists")
	public boolean isDepartmentExistsByName(String name) {
		return departmentService.isDepartmentExistsByName(name);
	}
	
	@PostMapping
	public String createDepartment (@Valid @RequestBody CreatingDepartmentForm form) {
		System.out.println(form.getName().toString());
		departmentService.createDepartment(form);
		return "Create successfully!";
	}
}
