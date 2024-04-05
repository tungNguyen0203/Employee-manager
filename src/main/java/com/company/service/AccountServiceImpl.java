package com.company.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.company.model.dto.account.AccountNoDepartmentDTO;
import com.company.model.dto.department.AccountDTO;
import com.company.model.entity.Account;
import com.company.repository.AccountRepository;
import com.company.specification.AccountNoDepartmentSpecification;

@Service
public class AccountServiceImpl extends BaseService implements AccountService{

	@Autowired
	private AccountRepository accountRepository;
	
	@Override
	public Page<AccountDTO> getAllAccountsPage(Pageable pageable) {
		
		// get entity page
		Page<Account> entityPage = accountRepository.findAll(pageable);
		
		// convert entity to dto page
		Page<AccountDTO> dtoPage = convertObjectPageToObjectPage(entityPage, pageable, AccountDTO.class);
		
		return dtoPage;
	}

//	login
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Account account = accountRepository.findByUsername(username);

		if (account == null) {
			throw new UsernameNotFoundException(username);
		}

		return new User(
				account.getUsername(), 
				account.getPassword(), 
				AuthorityUtils.createAuthorityList(account.getRole().toString()));
	}
	
	@Override
	public Account getAccountByUsername(String username) {
		return accountRepository.findByUsername(username);
	}

	@Override
	public boolean isAccountExistsByUsername(String username) {
		return accountRepository.existsByUsername(username);
	}

	@Override
	public boolean isAccountExistsByEmail(String email) {
		return accountRepository.existsByEmail(email);
	}

	@Override
	public boolean isAccountExistsById(Integer id) {
		return accountRepository.existsById(id);
	}

	@SuppressWarnings("deprecation")
	@Override
	public Account getAccountById(Integer accountId) {
		return accountRepository.getById(accountId);
	}

	@Override
	public List<AccountNoDepartmentDTO> getAllAccountsNoDepartment(Sort sort, String q) {
		Specification<Account> where = AccountNoDepartmentSpecification.buildWhere(q);
		
		// get entity list
		List<Account> entities = accountRepository.findAll(where, sort);

		// convert entity to dto list
		List<AccountNoDepartmentDTO> dtos = convertListObjectToListObject(entities, AccountNoDepartmentDTO.class);

		return dtos;
	}
	
	
}