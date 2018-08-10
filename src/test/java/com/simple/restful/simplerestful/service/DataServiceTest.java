package com.simple.restful.simplerestful.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Ordering;
import com.simple.restful.simplerestful.persistence.model.DataEntity;
import com.simple.restful.simplerestful.persistence.repositories.DataRepository;

@RunWith(MockitoJUnitRunner.class)
public class DataServiceTest {
	
	@Mock
	private DataRepository dataRepository;
	
	@InjectMocks
	private DataService dataService;
	
	@Test
	public void retrieveUniqueDates() throws Exception {
		Mockito.when(dataRepository.findAll(any(Specification.class))).thenReturn(createDataEntitylist());
		List<LocalDate> dates = dataService.retrieveUniqueDates();
		assertTrue(Ordering.natural().isOrdered(dates));
		assertEquals(3, dates.size());
		assertEquals(LocalDate.of(2015,06,03), dates.get(2));
	}
	
	@Test
	public void retrieveUniqueUsersLoggedInOnGivenDate() throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//		Specification<DataEntity> specification = withStartDate(dateFormat.parse("2015-01-01")).and(withEndDate(dateFormat.parse("2016-01-01")));
		Mockito.when(dataRepository.findAll(any(Specification.class))).thenReturn(createDataEntitylist());
		List<String> users = dataService.retrieveUniqueUsersLoggedInOnGivenDate(dateFormat.parse("2010-08-27"), dateFormat.parse("2015-06-04"));
		assertTrue(Ordering.natural().isOrdered(users));
		assertEquals(4, users.size());
		assertEquals("kdomini8", users.get(1));
	}
	

	
	
	private List<DataEntity> createDataEntitylist() throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<DataEntity> entities = new ArrayList<>();
		entities.add(new DataEntity(1, "nswiffan7", formatter.parse("2010-08-29 00:23:52"), "Sanjiang", "China", "Photobean", "diners-club-carte-blanche"));
		entities.add(new DataEntity(2, "kdomini8", formatter.parse("2014-08-21 14:21:26"), "Poříčany", "Czech Republic", "Dazzlesphere", "jcb"));
		entities.add(new DataEntity(3, "ohammersleym", formatter.parse("2015-06-03 23:10:52"), "Huangwei", "China", "Yoveo", "americanexpress"));
		entities.add(new DataEntity(4, "iauchterlonie1d", formatter.parse("2010-08-29 09:37:29"), null, "Peru", "Mynte", "jcb"));
		entities.add(new DataEntity(5, "kdomini8", formatter.parse("2013-04-27 22:21:24"), "Madīnat ‘Īsá", "Bahrain", null, "jcb"));
		return entities;
	}
	
	private List<String> createUniqueUsersList() {
		List<String> users = new ArrayList<>();
		users.add("bjeal5c");
		users.add("coconnell5h");
		users.add("gphilipp50");
		users.add("zrickardsson5k");
		return users;		
	}
	
	private Specification<DataEntity> withStartDate(Date startDate) {
		return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("loginTime"), startDate);
	}

	private Specification<DataEntity> withEndDate(Date endDate) {
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
