package com.company.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.model.dto.account.AccountNoDepartmentDTO;
import com.company.model.dto.department.AccountDTO;
import com.company.service.AccountService;

@RestController
@RequestMapping("api/v1/accounts")
@Validated
public class AccountController {
	
	@Autowired
	private AccountService accountService;
	
	@GetMapping
	public Page<AccountDTO> getAllAccounts (Pageable pageable) {
		return accountService.getAllAccountsPage(pageable);
	}
	
	@GetMapping("/noDepartment")
	public List<AccountNoDepartmentDTO> getAllAccountsNoDepartment(Sort sort,
			@RequestParam(value = "q", required = false) String q) {
		return accountService.getAllAccountsNoDepartment(sort, q);
	}
	
}
