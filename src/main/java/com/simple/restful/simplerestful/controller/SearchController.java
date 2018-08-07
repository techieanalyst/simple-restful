package com.simple.restful.simplerestful.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.simple.restful.simplerestful.persistence.model.DataEntity;
import com.simple.restful.simplerestful.service.DataService;

@RestController
@RequestMapping(path = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class SearchController {

	@Autowired
	private DataService dataService;

	@GetMapping(path = "/dates")
	public void getAllUniqueDates() {
	}

	@GetMapping(path = "/users")
	public void getAllUniqueUsersLoginRecordForGivenTimePeriod(
			@RequestParam(value = "start", required = false) @DateTimeFormat(pattern = "yyyyMMdd") Date startDate,
			@RequestParam(value = "end", required = false) @DateTimeFormat(pattern = "yyyyMMdd") Date endDate) {

	}

	@GetMapping(path = "/logins")
	public List<DataEntity> retrieveUserLoginCountForGivenTimePeriodAndAttributes(
			@RequestParam(value = "start", required = false) @DateTimeFormat(pattern = "yyyyMMdd") Date startDate,
			@RequestParam(value = "end", required = false) @DateTimeFormat(pattern = "yyyyMMdd") Date endDate,
			@RequestParam(value = "attribute1", required = false) List<String> attribute1,
			@RequestParam(value = "attribute2", required = false) List<String> attribute2,
			@RequestParam(value = "attribute3", required = false) List<String> attribute3,
			@RequestParam(value = "attribute4", required = false) List<String> attribute4) {
		Map<String, List<String>> maps = new HashMap<>();
		maps.put("attributeOne", attribute1);
		maps.put("attributeTwo", attribute2);
		maps.put("attributeThree", attribute3);
		maps.put("attributeFour", attribute4);
		return dataService.retrieveDataEntitiesOnGivenCriteria(startDate, endDate, maps);
	}

}
