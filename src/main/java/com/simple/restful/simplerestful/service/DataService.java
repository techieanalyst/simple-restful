package com.simple.restful.simplerestful.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.simple.restful.simplerestful.persistence.model.DataEntity;
import com.simple.restful.simplerestful.persistence.repositories.DataRepository;
import com.simple.restful.simplerestful.persistence.specifications.DataEntitySpecifications;

@Service
public class DataService {

	@Autowired
	private DataRepository dataRepository;

	public List<DataEntity> retrieveDataEntitiesOnGivenCriteria(Date startDate, Date endDate, Map<String, List<String>> map) {
		Specification<DataEntity> specifications = new Specification<DataEntity>() {
			private static final long serialVersionUID = -1087003454127199932L;
			@Override
			public Predicate toPredicate(Root<DataEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return null;
			}
		};
		for (String attributeName : map.keySet()) {
			List<String> attributes = map.get(attributeName);
			if (attributes == null) continue;
			specifications = specifications.and(DataEntitySpecifications.withAttribute(attributeName, attributes));
		}
		if (startDate != null) {
			specifications = specifications.and(DataEntitySpecifications.withStartDate(startDate));
		}
		if (endDate != null) {
			specifications = specifications.and(DataEntitySpecifications.withEndDate(endDate));
		}
		
		return dataRepository.findAll(specifications);
	}

//	public void sample1() {
//		List<DataEntity> entities = dataRepository
//				.findAll(DataEntitySpecifications.withStartDate(Date.valueOf(LocalDate.parse("2017-08-06"))));
//		for (DataEntity data : entities) {
//			System.out.println(data);
//		}
//	}

}
