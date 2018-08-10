package com.simple.restful.simplerestful.controller;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.simple.restful.simplerestful.service.DataService;

@RestController
@RequestMapping(path = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class SearchController {

	private static Logger logger = LoggerFactory.getLogger(SearchController.class);
	
	@Autowired
	private DataService dataService;

	@GetMapping(path = "/dates")
	public @ResponseBody List<LocalDate> retrieveAllUniqueDates() {
		logger.info(String.format("Getting list of unique dates"));
		return dataService.retrieveUniqueDates();
	}

	@GetMapping(path = "/users")
	public @ResponseBody List<String> retrieveAllUniqueUsersLoginRecordForGivenTimePeriod(
			@RequestParam(value = "start", required = false) @DateTimeFormat(pattern = "yyyyMMdd") Date startDate,
			@RequestParam(value = "end", required = false) @DateTimeFormat(pattern = "yyyyMMdd") Date endDate) {
		logger.info(String.format("Getting list of unique users given the criteria: start=%tF, end=%tF",
				startDate, endDate));
		return dataService.retrieveUniqueUsersLoggedInOnGivenDate(startDate, endDate);
	}

	@GetMapping(path = "/logins")
	public @ResponseBody Map<String, Integer> retrieveUserLoginCountForGivenTimePeriodAndAttributes(
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
		logger.info(String.format("Getting login count of unique users given the criteria: %s, start=%tF, end=%tF",
				maps.toString(), startDate, endDate));
		return dataService.retrieveLoginFrequencyOnGivenCriteria(startDate, endDate, maps);
	}

}
