package com.simple.restful.simplerestful.persistence.specifications;

import java.util.Date;

import org.springframework.data.jpa.domain.Specification;

import com.simple.restful.simplerestful.persistence.model.DataEntity;

public class DataEntitySpecifications {

	public static Specification<DataEntity> withAttribute(String attributeName, String attribute) {
		if (attribute == null) {
			return null;
		} else {
			return (root, query, cb) -> cb.equal(root.get(attribute), attribute);
		}
	}

	public static Specification<DataEntity> withStartDate(Date startDate) {
		if (startDate == null) {
			return null;
		} else {
			return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("loginTime"), startDate);
		}
	}

	public static Specification<DataEntity> withEndDate(Date endDate) {
		if (endDate == null) {
			return null;
		} else {
			return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("loginTime"), endDate);
		}
	}

}
