package com.simple.restful.simplerestful.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simple.restful.simplerestful.persistence.model.DataEntity;
import com.simple.restful.simplerestful.persistence.repositories.DataRepository;
import com.simple.restful.simplerestful.persistence.specifications.DataEntitySpecifications;

@Service
public class DataService {

	@Autowired
	private DataRepository dataRepository;

	public void sample() {
		dataRepository.findAll(DataEntitySpecifications.withAttribute("attribute1", "sample"));
	}
	
	public void sample1() {
		List<DataEntity> entities = dataRepository.findAll(DataEntitySpecifications.withStartDate(Date.valueOf(LocalDate.parse("2017-08-06"))));
		for (DataEntity data : entities) {
			System.out.println(data);
		}
	}

}
