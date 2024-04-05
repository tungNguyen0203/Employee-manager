package com.company.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.company.model.entity.Account;



public interface AccountRepository  extends JpaRepository<Account, Integer>, JpaSpecificationExecutor<Account> {
	
	Account findByUsername(String username);

	boolean existsByUsername(String username);
	
	boolean existsByEmail(String email);
	
	Account findByUsernameOrEmail(String username, String email);

	@Query(value = "UPDATE `account` SET department_id = :departmentId WHERE id IN (:accountIds)", nativeQuery = true)
	@Modifying
	void updateDepartment(
			@Param("departmentId") Integer departmentId, 
			@Param("accountIds") List<Integer> accountIds);
	
}
