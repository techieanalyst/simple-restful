package com.simple.restful.simplerestful.controller;

import static org.mockito.ArgumentMatchers.anyList;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.text.AbstractDocument.Content;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
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
	
	@Test
	public void retrieveUniqueDates() throws Exception {
		when(employeeService.retrieveUniqueDates()).thenReturn(createUniqueDateList());
		MvcResult result = mockMvc.perform(get("/search/dates").contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0]").value("2015-05-11"))
				.andExpect(jsonPath("$[2]").value("2016-09-30"))
				.andReturn();
		List<LocalDate> results = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<String>>(){});
		assertTrue(Ordering.natural().isOrdered(results));
	}

	@Test
	public void retrieveEmptyUniqueDates() throws Exception {
		when(employeeService.retrieveUniqueDates()).thenReturn(new ArrayList<>());
		mockMvc.perform(get("/search/dates").contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isEmpty());
	}
	
	@Test
	public void retrieveAllUniqueUsersLoginRecordWithStartDate() throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		when(employeeService.retrieveUniqueUsersLoggedInOnGivenDate(formatter.parse("2016-05-11"), null))
				.thenReturn(createUniqueUsersList());
		MvcResult result = mockMvc.perform(get("/search/users").param("start", "20160511").contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(status().isOk())
				.andExpect(jsonPath("$[1]").value("coconnell5h"))
				.andReturn();
		List<String> results = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<String>>(){});
		assertTrue(Ordering.natural().isOrdered(results));
	}
	
	@Test
	public void retrieveAllUniqueUsersLoginRecordWithStartDateAndEndDate() throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		when(employeeService.retrieveUniqueUsersLoggedInOnGivenDate(formatter.parse("2016-08-17"), formatter.parse("2017-12-31")))
				.thenReturn(createUniqueUsersList().subList(1, 2));
		MvcResult result = mockMvc.perform(get("/search/users").param("start", "20160817").param("end", "20171231")
				.contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk()).andExpect(jsonPath("$[0]").value("coconnell5h"))
				.andReturn();
		List<String> results = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<String>>(){});
		assertTrue(Ordering.natural().isOrdered(results));
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

//
//	@Test
//	public void sortEmployeesByInvalidAttribute() throws Exception {
//		when(employeeService.getAllEmployeesSortedByAttribute("birthday"))
//				.thenThrow(new EmployeeQueryException("invalid parameter for sorting"));
//		mockMvc.perform(get("/v1/employees/sorted/{attribute}", "birthday").contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isBadRequest()).andExpect(jsonPath("status").value("BAD_REQUEST"))
//				.andExpect(jsonPath("errors").value("invalid parameter for sorting"));
//	}
//
//	@Test
//	public void retrieveEmployeesByInvalidAge() throws Exception {
//		when(employeeService.getAllEmployeesYoungerThanAge(AdditionalMatchers.lt(1)))
//				.thenThrow(new EmployeeQueryException("invalid input parameter"));
//		mockMvc.perform(get("/v1/employees/younger/than/{age}", "asdasjhdsj").contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isBadRequest()).andExpect(jsonPath("status").value("BAD_REQUEST"))
//				.andExpect(jsonPath("errors").value("invalid input parameter"));
//
//		mockMvc.perform(get("/v1/employees/younger/than/{age}", "-50").contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isBadRequest()).andExpect(jsonPath("status").value("BAD_REQUEST"))
//				.andExpect(jsonPath("errors").value("invalid input parameter"));
//
//		mockMvc.perform(get("/v1/employees/younger/than/{age}", "0.5").contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isBadRequest()).andExpect(jsonPath("status").value("BAD_REQUEST"))
//				.andExpect(jsonPath("errors").value("invalid input parameter"));
//
//		mockMvc.perform(get("/v1/employees/younger/than/{age}", "0").contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isBadRequest()).andExpect(jsonPath("status").value("BAD_REQUEST"))
//				.andExpect(jsonPath("errors").value("invalid input parameter"));
//	}
//
//	@Test
//	public void uploadEmptyFile() throws Exception {
//		InputStream in = this.getClass().getClassLoader().getResourceAsStream("unit/employee-empty.json");
//		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "employee.json", "text/plain", in);
//		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/v1/employees/bulkupload")
//				.file(mockMultipartFile);
//		mockMvc.perform(builder).andExpect(status().isBadRequest())
//				.andExpect(jsonPath("errors").value("See logs for more details"));
//	}
//	
//	@Test
//	public void uploadIncompatibleFileContents() throws Exception {
//		InputStream in = this.getClass().getClassLoader().getResourceAsStream("unit/employee-incompatible.json");
//		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "employee.json", "text/plain", in);
//		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/v1/employees/bulkupload")
//				.file(mockMultipartFile);
//		mockMvc.perform(builder).andExpect(status().isBadRequest())
//				.andExpect(jsonPath("errors").value("See logs for more details"));
//		
//		in = this.getClass().getClassLoader().getResourceAsStream("unit/employee-not-array.json");
//		mockMultipartFile = new MockMultipartFile("file", "employee.json", "text/plain", in);
//		builder = MockMvcRequestBuilders.multipart("/v1/employees/bulkupload")
//				.file(mockMultipartFile);
//		mockMvc.perform(builder).andExpect(status().isBadRequest())
//				.andExpect(jsonPath("errors").value("See logs for more details"));
//	}
//
//	@Test
//	public void uploadIncompatibleFileContentsMissingAttribute() throws Exception {
//		InputStream in = this.getClass().getClassLoader().getResourceAsStream("unit/employee-missing-attribute.json");
//		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "employee.json", "text/plain", in);
//		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/v1/employees/bulkupload")
//				.file(mockMultipartFile);
//		mockMvc.perform(builder).andExpect(status().isBadRequest())
//				.andExpect(jsonPath("errors").value("list[0].age: must not be null"));
//		
//		in = this.getClass().getClassLoader().getResourceAsStream("unit/employee-missing-attribute-in-address.json");
//		mockMultipartFile = new MockMultipartFile("file", "employee.json", "text/plain", in);
//		builder = MockMvcRequestBuilders.multipart("/v1/employees/bulkupload")
//				.file(mockMultipartFile);
//		mockMvc.perform(builder).andExpect(status().isBadRequest())
//				.andExpect(jsonPath("errors").value("list[0].address.street: must not be null"));
//	}
//	
//	@Test
//	public void uploadContentsWithInvalidData() throws Exception {
//		InputStream in = this.getClass().getClassLoader().getResourceAsStream("unit/employee-multiple-incorrect-values.json");
//		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "employee.json", "text/plain", in);
//		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/v1/employees/bulkupload")
//				.file(mockMultipartFile);
//		mockMvc.perform(builder).andExpect(status().isBadRequest())
//				.andExpect(jsonPath("message").value("Errors on validating JSON input data"))
//				.andDo(MockMvcResultHandlers.print());
//	}
//	
//	@Test
//	public void errorOccurredWhenUploadingContents() throws Exception {
//		when(employeeService.persistEmployeeRequestEntries(anyList())).thenThrow(new InvalidDataAccessResourceUsageException("could not prepare statement; SQL ["));
//
//		InputStream in = this.getClass().getClassLoader().getResourceAsStream("integration/employee.json");
//		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "employee.json", "text/plain", in);
//		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/v1/employees/bulkupload")
//				.file(mockMultipartFile);
//		mockMvc.perform(builder).andExpect(status().isInternalServerError())
//				.andExpect(jsonPath("message").value("Error occurred due to invalid request"))
//				.andDo(MockMvcResultHandlers.print());
//	}
//	
//	@Test
//	public void uploadFileWithMoreThanFileSizeLimit() throws Exception {
//		byte[] bytes = new byte[1024 * 1024 * 6];
//		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "employee.json", "text/plain", bytes);
//		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/v1/employees/bulkupload")
//				.file(mockMultipartFile);
//		mockMvc.perform(builder).andExpect(status().isBadRequest())
//				.andDo(MockMvcResultHandlers.print());
//	}
}
