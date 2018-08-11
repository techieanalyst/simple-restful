package com.simple.restful.simplerestful.controller;

import static org.mockito.ArgumentMatchers.anyList;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.text.AbstractDocument.Content;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalMatchers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.restful.simplerestful.persistence.model.DataEntity;
import com.simple.restful.simplerestful.service.DataService;

import com.google.common.collect.Ordering;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(SearchController.class)
public class SearchControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private DataService employeeService;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Test
	public void retrieveUniqueDates() throws Exception {
		when(employeeService.retrieveUniqueDates()).thenReturn(createUniqueDateList());
		MvcResult result = mockMvc.perform(get("/search/dates").contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0]").value("2015-05-11"))
				.andExpect(jsonPath("$[2]").value("2016-09-30"))
				.andReturn();
		List<LocalDate> results = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<String>>(){});
		verify(employeeService, times(1)).retrieveUniqueDates();
		assertTrue(Ordering.natural().isOrdered(results));
	}

	@Test
	public void retrieveEmptyUniqueDates() throws Exception {
		when(employeeService.retrieveUniqueDates()).thenReturn(new ArrayList<>());
		mockMvc.perform(get("/search/dates").contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isEmpty());
		verify(employeeService, times(1)).retrieveUniqueDates();
	}
	
	@Test
	public void retrieveAllUniqueUsersLoginRecordWithStartDate() throws Exception {
		when(employeeService.retrieveUniqueUsersLoggedInOnGivenDate(dateFormat.parse("2016-05-11"), null))
				.thenReturn(createUniqueUsersList());
		MvcResult result = mockMvc.perform(get("/search/users").param("start", "20160511").contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(status().isOk())
				.andExpect(jsonPath("$[1]").value("coconnell5h"))
				.andReturn();
		verify(employeeService, times(1)).retrieveUniqueUsersLoggedInOnGivenDate(dateFormat.parse("2016-05-11"), null);
		List<String> results = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<String>>(){});
		assertTrue(Ordering.natural().isOrdered(results));
	}
	
	@Test
	public void retrieveAllUniqueUsersLoginRecordWithStartDateInvalidDateParam() throws Exception {
		when(employeeService.retrieveUniqueUsersLoggedInOnGivenDate(dateFormat.parse("2016-05-11"), null))
				.thenReturn(createUniqueUsersList());
		MvcResult result = mockMvc.perform(get("/search/users").param("start", "20163011").contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("status").value("BAD_REQUEST"))
				.andExpect(jsonPath("errors[0]").value("start should be of type java.util.Date"))
				.andReturn();
		verify(employeeService, times(0)).retrieveUniqueUsersLoggedInOnGivenDate(any(Date.class), any(Date.class));
	}
	
	@Test
	public void retrieveAllUniqueUsersLoginRecordWithStartDateAndEndDate() throws Exception {
		when(employeeService.retrieveUniqueUsersLoggedInOnGivenDate(dateFormat.parse("2016-08-17"), dateFormat.parse("2017-12-31")))
				.thenReturn(createUniqueUsersList().subList(1, 2));
		MvcResult result = mockMvc.perform(get("/search/users").param("start", "20160817").param("end", "20171231")
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk()).andExpect(jsonPath("$[0]").value("coconnell5h"))
				.andReturn();
		verify(employeeService, times(1)).retrieveUniqueUsersLoggedInOnGivenDate(any(Date.class), any(Date.class));
		List<String> results = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<String>>(){});
		assertTrue(Ordering.natural().isOrdered(results));
	}
	
	@Test
	public void retrieveUserLoginCountForGivenTimePeriodAndAttributes() throws Exception {
		when(employeeService.retrieveLoginFrequencyOnGivenCriteria(any(Date.class),
				any(Date.class), any())).thenReturn(createLoginFrequencyMap());
		MvcResult result = mockMvc
				.perform(get("/search/logins").param("start", "20160817").param("end", "20171231")
						.param("attribute1", "Manila", "Cebu").param("attribute2", "Philippines")
						.param("attribute3", "Zoonoodle").param("attribute4", "jcb")
						.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("gphilipp50").value("2"))
				.andExpect(jsonPath("bjeal5c").value("1"))
				.andReturn();
	}
	
	@Test
	public void retrieveUserLoginCountForGivenTimePeriodAndAttributesInvalidDateParam() throws Exception {
		when(employeeService.retrieveLoginFrequencyOnGivenCriteria(any(Date.class),
				any(Date.class), any())).thenReturn(createLoginFrequencyMap());
		MvcResult result = mockMvc
				.perform(get("/search/logins").param("start", "aksdjals").param("end", "20171231")
						.param("attribute1", "Manila", "Cebu").param("attribute2", "Philippines")
						.param("attribute3", "Zoonoodle").param("attribute4", "jcb")
						.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andDo(MockMvcResultHandlers.print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("status").value("BAD_REQUEST"))
				.andExpect(jsonPath("errors[0]").value("start should be of type java.util.Date"))
				.andReturn();
	}

	private List<LocalDate> createUniqueDateList() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		List<LocalDate> dates = new ArrayList<>();
		dates.add(LocalDate.parse("2015-05-11", formatter));
		dates.add(LocalDate.parse("2016-08-16", formatter));
		dates.add(LocalDate.parse("2016-09-30", formatter));
		dates.add(LocalDate.parse("2017-03-20", formatter));
		dates.add(LocalDate.parse("2018-05-05", formatter));
		dates.add(LocalDate.parse("2018-08-25", formatter));
		return dates;
	}
	
	private List<String> createUniqueUsersList() {
		List<String> users = new ArrayList<>();
		users.add("bjeal5c");
		users.add("coconnell5h");
		users.add("gphilipp50");
		users.add("zrickardsson5k");
		return users;		
	}
	
	private Map<String, Integer> createLoginFrequencyMap() {
		Map<String, Integer> map = new HashMap<>();
		map.put("gphilipp50", 2);
		map.put("bjeal5c", 1);
		return map;
	}

}
