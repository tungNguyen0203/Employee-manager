package com.company.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.company.model.dto.account.AccountNoDepartmentDTO;
import com.company.model.dto.department.AccountDTO;
import com.company.model.entity.Account;

public interface AccountService extends UserDetailsService{

	Page<AccountDTO> getAllAccountsPage(Pageable pageable);
	
	Account getAccountByUsername(String username);
	
	boolean isAccountExistsByUsername (String username);
	
	boolean isAccountExistsByEmail (String email);

	boolean isAccountExistsById(Integer id);

	Account getAccountById(Integer accountId);

	List<AccountNoDepartmentDTO> getAllAccountsNoDepartment(Sort sort, String q);

}