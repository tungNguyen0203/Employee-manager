package com.company.specification;

import java.util.Date;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.company.model.entity.Account;
import com.company.model.form.department.AccountFilterForm;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

public class AccountSpecification {

	@SuppressWarnings("deprecation")
	public static Specification<Account> buildWhere(Integer departmentId, AccountFilterForm accountFilterForm) {

		Specification<Account> where = Specification.where(new CustomSpecification("departmentId", departmentId));

		if (departmentId == null) {
			return where;
		}
		
		if (accountFilterForm == null) {
			return where;
		}

		if (accountFilterForm.getSearch() != null && !StringUtils.isEmpty(accountFilterForm.getSearch().trim())) {
			String search = accountFilterForm.getSearch().trim();
			CustomSpecification usernameSpecification = new CustomSpecification("username", search);
			CustomSpecification fullnameSpecification = new CustomSpecification("fullname", search);
			where = where.and(usernameSpecification.or(fullnameSpecification));
		}

		if (accountFilterForm.getMaxCreatedDate() != null) {
			CustomSpecification maxDate = new CustomSpecification("maxCreatedDate", accountFilterForm.getMaxCreatedDate());
			if (where == null) {
				where = maxDate;
			} else {
				where = maxDate.and(where);
			}
		}
		
		if (accountFilterForm.getMinCreatedDate() != null) {
			CustomSpecification minDate = new CustomSpecification("minCreatedDate", accountFilterForm.getMinCreatedDate());
			if (where == null) {
				where = minDate;
			} else {
				where = minDate.and(where);
			}
		}
		
		if (accountFilterForm.getRole() != null) {
			CustomSpecification roleSpecification = new CustomSpecification("role", accountFilterForm.getRole());
			if (where == null) {
				where = roleSpecification;
			} else {
				where = where.and(roleSpecification);
			}
		}
		
		if (accountFilterForm.getStatus() != null) {
			CustomSpecification statusSpecification = new CustomSpecification("status", accountFilterForm.getStatus());
			if (where == null) {
				where = statusSpecification;
			} else {
				where = where.and(statusSpecification);
			}
		}
		
		return where;
	}

	@SuppressWarnings("serial")
	@RequiredArgsConstructor
	static class CustomSpecification implements Specification<Account> {

		@NonNull
		private String field;
		@NonNull
		private Object value;

		@Override
		public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

			if (field.equalsIgnoreCase("departmentId")) {
				return criteriaBuilder.equal(root.get("department").get("id"), value);
			}
			
			// search
			if (field.equalsIgnoreCase("username")) {
				return criteriaBuilder.like(root.get("username"), "%" + value.toString() + "%");
			}
			if (field.equalsIgnoreCase("fullname")) {
				return criteriaBuilder.like(root.get("fullname"), "%" + value.toString() + "%");
			}

			// filter
			if (field.equalsIgnoreCase("minCreatedDate")) {
				return criteriaBuilder.greaterThanOrEqualTo(
						root.get("createdDateTime").as(java.sql.Date.class), 
						(Date) value);
			}

			if (field.equalsIgnoreCase("maxCreatedDate")) {
				return criteriaBuilder.lessThanOrEqualTo(
						root.get("createdDateTime").as(java.sql.Date.class), 
						(Date) value);
			}
			
			if (field.equalsIgnoreCase("status")) {
				return criteriaBuilder.equal(root.get("status"), value);
			}
			
			if (field.equalsIgnoreCase("role")) {
				return criteriaBuilder.equal(root.get("role"), value);
			}
			
			return null;
		}
	}
}
