package com.simple.restful.simplerestful.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
	
	public List<String> sample() {
		Specification<DataEntity> specifications = DataEntitySpecifications.initialize();
		List<DataEntity> entities = dataRepository.findAll(specifications);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		List<Date> users = entities.stream().filter(distinctByKey(p -> dateFormat.format(p.getLoginTime()))).map(DataEntity::getUser)
				.collect(Collectors.toCollection(ArrayList::new));
	}
	
	public Map<String, Integer> retrieveLoginFrequencyOnGivenCriteria(Date startDate, Date endDate,
			Map<String, List<String>> map) {
		Specification<DataEntity> specifications = DataEntitySpecifications.initialize();
		for (String attributeName : map.keySet()) {
			List<String> attributes = map.get(attributeName);
			if (attributes != null && !attributes.isEmpty())
				specifications = specifications.and(DataEntitySpecifications.withAttribute(attributeName, attributes));
		}
		buildDateRangeSpecification(specifications, startDate, endDate);
		List<DataEntity> entities = dataRepository.findAll(specifications);
		Map<String, Integer> collect = entities.stream()
				.collect(Collectors.groupingBy(DataEntity::getUser, Collectors.summingInt(e -> 1)));

		return collect;
	}
	
	public List<String> retrieveUniqueUsersLoggedInOnGivenDate(Date startDate, Date endDate) {
		Specification<DataEntity> specifications = DataEntitySpecifications.initialize();
		buildDateRangeSpecification(specifications, startDate, endDate);
		List<DataEntity> entities = dataRepository.findAll(specifications);
		List<String> users = entities.stream().filter(distinctByKey(DataEntity::getUser)).map(DataEntity::getUser)
				.collect(Collectors.toCollection(ArrayList::new));
		return users;
	}

	private void buildDateRangeSpecification(Specification<DataEntity> specification, Date startDate, Date endDate) {
		if (startDate != null) {
			specification = specification.and(DataEntitySpecifications.withStartDate(startDate));
		}
		if (endDate != null) {
			specification = specification.and(DataEntitySpecifications.withEndDate(endDate));
		}		
	}

	private <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
	    Set<Object> seen = ConcurrentHashMap.newKeySet();
	    return t -> seen.add(keyExtractor.apply(t));
	}
	
}
