package com.company.specification;

import java.util.Date;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.company.model.entity.Department;
import com.company.model.form.department.DepartmentFilterForm;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

public class DepartmentSpecification {

	@SuppressWarnings("deprecation")
	public static Specification<Department> buildWhere(DepartmentFilterForm departmentFilterForm) {

		Specification<Department> where = null;

		if (departmentFilterForm == null) {
			return where;
		}

		if (!StringUtils.isEmpty(departmentFilterForm.getSearch())) {
			String search = departmentFilterForm.getSearch();
			search = search.trim();
			CustomSpecification name = new CustomSpecification("name", search);
			CustomSpecification cretor = new CustomSpecification("creatorFullname", search);

			where = Specification.where(name).or(cretor);
		}

		if (departmentFilterForm.getMaxCreatedDate() != null) {
			CustomSpecification maxDate = new CustomSpecification("maxCreatedDate", departmentFilterForm.getMaxCreatedDate());
			if (where == null) {
				where = maxDate;
			} else {
				where = maxDate.and(where);
			}
		}
		if (departmentFilterForm.getMinCreatedDate() != null) {
			CustomSpecification minDate = new CustomSpecification("minCreatedDate", departmentFilterForm.getMinCreatedDate());
			if (where == null) {
				where = minDate;
			} else {
				where = minDate.and(where);
			}
		}
		if (departmentFilterForm.getMaxMemberSize() != null) {
			CustomSpecification maxMember = new CustomSpecification("maxMember", departmentFilterForm.getMaxMemberSize());
			if (where == null) {
				where = maxMember;
			} else {
				where = maxMember.and(where);
			}
		}
		if (departmentFilterForm.getMinMemberSize() != null) {
			CustomSpecification minMember = new CustomSpecification("minMember", departmentFilterForm.getMinMemberSize());
			if (where == null) {
				where = minMember;
			} else {
				where = minMember.and(where);
			}
		}
		return where;
	}

	@SuppressWarnings("serial")
	@RequiredArgsConstructor
	static class CustomSpecification implements Specification<Department> {

		@NonNull
		private String field;
		@NonNull
		private Object value;

		@Override
		public Predicate toPredicate(Root<Department> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

			// search
			if (field.equalsIgnoreCase("name")) {
				return criteriaBuilder.like(root.get("name"), "%" + value.toString() + "%");
			}
			if (field.equalsIgnoreCase("creatorFullname")) {
				return criteriaBuilder.like(root.get("creator").get("fullname"), "%" + value.toString() + "%");
			}

			// filter
			if (field.equalsIgnoreCase("maxCreatedDate")) {
				return criteriaBuilder.lessThanOrEqualTo(
						root.get("createdDateTime").as(java.sql.Date.class),
						(Date) value);
			}
			if (field.equalsIgnoreCase("minCreatedDate")) {
				return criteriaBuilder.greaterThanOrEqualTo(
						root.get("createdDateTime").as(java.sql.Date.class),
						(Date) value);
			}
			if (field.equalsIgnoreCase("minMember")) {
				return criteriaBuilder.greaterThanOrEqualTo(
						root.get("memberSize"), 
						(Integer) value);
			}
			if (field.equalsIgnoreCase("maxMember")) {
				return criteriaBuilder.lessThanOrEqualTo(
						root.get("memberSize"), 
						(Integer) value);
			}

			return null;
		}
	}
}
