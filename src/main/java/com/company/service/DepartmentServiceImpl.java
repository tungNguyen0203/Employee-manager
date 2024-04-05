package com.company.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.config.security.SecurityUtils;
import com.company.model.dto.department.AccountDTO;
import com.company.model.dto.department.DepartmentDTO;
import com.company.model.entity.Account;
import com.company.model.entity.Account.Role;
import com.company.model.entity.Department;
import com.company.model.form.department.AccountFilterForm;
import com.company.model.form.department.CreatingDepartmentForm;
import com.company.model.form.department.DepartmentFilterForm;
import com.company.repository.AccountRepository;
import com.company.repository.DepartmentRepository;
import com.company.specification.AccountSpecification;
import com.company.specification.DepartmentSpecification;

@Service
public class DepartmentServiceImpl extends BaseService implements DepartmentService{

	@Autowired
	private DepartmentRepository departmentRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private SecurityUtils securityUtils;
	
	@Override
	public Page<DepartmentDTO> getAllDepartmentsPage(Pageable pageable, DepartmentFilterForm departmentFilterForm) {
		
		Specification<Department> where = DepartmentSpecification.buildWhere(departmentFilterForm);
		
		// get entity page
		Page<Department> entityPage = departmentRepository.findAll(where, pageable);
		
		// convert entity to dto page
		Page<DepartmentDTO> dtoPage = convertObjectPageToObjectPage(entityPage, pageable, DepartmentDTO.class);
		
		return dtoPage;
	}
	
	@Override
	public DepartmentDTO getDepartmentById(Integer id) {
		return convertObjectToObject(departmentRepository.findById(id).get(), DepartmentDTO.class);
	}
	
	@Override
	public boolean isDepartmentExistsById(Integer id) {
		return departmentRepository.existsById(id);
	}
	
	@Override
	public Page<AccountDTO> getAccountsByDepartmentId(Integer departmentId, Pageable pageable, AccountFilterForm accountFilterForm) {
		
		Specification<Account> where = AccountSpecification.buildWhere(departmentId, accountFilterForm);
		
		Page<Account> entityPage = accountRepository.findAll(where, pageable);
		
		return convertObjectPageToObjectPage(entityPage, pageable, AccountDTO.class);
	}

	@Transactional
	@Override
	public void removeAccountsInDepartment(List<Integer> accountIds) {
		for (Integer id : accountIds) {
			// get account by id
			Account account = accountRepository.findById(id).get();
			
			// update member size and manager id in department
			Department department = account.getDepartment();
			department.setMemberSize(department.getMemberSize() - 1);
			if (account.getRole().equals(Role.MANAGER)) {
				department.setManager(null);
			}
			departmentRepository.save(department);
			
			// update field department id in account
			account.setDepartment(null);
			if (account.getRole().equals(Role.MANAGER)) {
				account.setRole(Role.EMPLOYEE);
			}
			
			account.setModifier(securityUtils.getCurrentAcconutLogin());
			account.setUpdatedDateTime(new Date());
			accountRepository.save(account);
		}
	}

	@Override
	public boolean isDepartmentExistsByName(String name) {
		return departmentRepository.findByName(name);
	}

	@Override
	public void createDepartment(CreatingDepartmentForm form) {
		Department department = Department.builder()
				.name(form.getName())
//				.manager(Account.builder().id(form.getManagerId()) .build())
				.creator(securityUtils.getCurrentAcconutLogin())
				.createdDateTime(new Date())
				.modifier(securityUtils.getCurrentAcconutLogin())
				.updatedDateTime(new Date())
				.build();
		Department entity = departmentRepository.save(department);
		
		// add accounts to new department
		if(form.getEmployeeIds() != null && form.getEmployeeIds().size() > 0) {
			accountRepository.updateDepartment(entity.getId(), form.getEmployeeIds());
		}
		
		// add manager
		Account manager = accountRepository.findById(form.getManagerId()).get();
		manager.setRole(Role.MANAGER);
		manager.setDepartment(entity);
		accountRepository.save(manager);
		
		// update member_size
		department.setMemberSize(1 + (form.getEmployeeIds() == null ? 0 : form.getEmployeeIds().size()));
	}
}