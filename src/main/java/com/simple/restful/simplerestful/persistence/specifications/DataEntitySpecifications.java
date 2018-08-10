package com.simple.restful.simplerestful.persistence.specifications;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.simple.restful.simplerestful.persistence.model.DataEntity;

public class DataEntitySpecifications {
	
	private DataEntitySpecifications() {
		
	}
	
	public static Specification<DataEntity> initialize() {
		return new Specification<DataEntity>() {
			private static final long serialVersionUID = 5371326775287328230L;
			@Override
			public Predicate toPredicate(Root<DataEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return null;
			}
		};
	}

	public static Specification<DataEntity> withAttribute(String attributeName, List<String> attributes) {
		return new Specification<DataEntity>() {
			private static final long serialVersionUID = 9108640606813152203L;

			@Override
			public Predicate toPredicate(Root<DataEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteria) {
				List<String> list = attributes.stream().filter(item -> item != null && !item.isEmpty())
						.collect(Collectors.toList());
				Predicate[] predicates = new Predicate[list.size()];
				int count = 0;
				for (String attribute : list) {
					predicates[count] = criteria.equal(root.get(attributeName), attribute);
					count++;
				}
				return criteria.or(predicates);
			}
		};
	}

	public static Specification<DataEntity> withStartDate(Date startDate) {
		return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("loginTime"), startDate);
	}

	public static Specification<DataEntity> withEndDate(Date endDate) {
		Calendar calStart = new GregorianCalendar();
		calStart.setTime(endDate);
		calStart.set(Calendar.HOUR_OF_DAY, 23);
		calStart.set(Calendar.MINUTE, 59);
		calStart.set(Calendar.SECOND, 59);
		calStart.set(Calendar.MILLISECOND, 999);
		Date endOfDay = calStart.getTime();
		return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("loginTime"), endOfDay);
	}

}
