package com.simple.restful.simplerestful.service;

import static org.mockito.Mockito.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
	
    @Captor
    private ArgumentCaptor<Specification<DataEntity>> captor;
    
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Test
	public void retrieveUniqueDates() throws Exception {
		when(dataRepository.findAll(any(Specification.class))).thenReturn(createDataEntitylist());
		List<LocalDate> dates = dataService.retrieveUniqueDates();
		verify(dataRepository, times(1)).findAll(captor.capture());
		assertTrue(Ordering.natural().isOrdered(dates));
		assertEquals(8, dates.size());
		assertEquals(LocalDate.of(2013,6,20), dates.get(2));
	}

	@Test
	public void retrieveUniqueUsersLoggedInOnGivenDate() throws ParseException {
		when(dataRepository.findAll(any(Specification.class))).thenReturn(createDataEntitylist());
		List<String> users = dataService.retrieveUniqueUsersLoggedInOnGivenDate(dateFormat.parse("2010-08-27"), dateFormat.parse("2018-12-31"));
		verify(dataRepository, times(1)).findAll(captor.capture());
		assertTrue(Ordering.natural().isOrdered(users));
		assertEquals(7, users.size());
		assertEquals("kdomini8", users.get(4));
	}
	
	@Test
	public void retrieveLoginFrequencyOnGivenAllCriteria() throws Exception {
		when(dataRepository.findAll(any(Specification.class))).thenReturn(createDataEntitylist().subList(8, 9));
		Map<String, List<String>> map = new HashMap<>();
		map.put("attributeOne", Arrays.asList("Granada"));
		map.put("attributeTwo", Arrays.asList("Peru"));
		map.put("attributeThree", Arrays.asList("Lazzy"));
		map.put("attributeFour", Arrays.asList("jcb"));
		Map<String, Integer> loginFrequency = dataService.retrieveLoginFrequencyOnGivenCriteria(dateFormat.parse("2010-08-27"), dateFormat.parse("2018-12-31"), map);
		verify(dataRepository, times(1)).findAll(captor.capture());
		verifyNoMoreInteractions(dataRepository);
		assertEquals(1, loginFrequency.size());
		assertTrue(loginFrequency.keySet().contains("dbrislandoo"));
		
		when(dataRepository.findAll(any(Specification.class)))
			.thenReturn(createDataEntitylist().stream()
					.filter(a -> a.getAttributeFour().equalsIgnoreCase("jcb")).collect(Collectors.toList()));
		map = new HashMap<>();
		map.put("attributeFour", Arrays.asList("jcb"));
		loginFrequency = dataService.retrieveLoginFrequencyOnGivenCriteria(dateFormat.parse("2010-08-27"), dateFormat.parse("2018-12-31"), map);
		verify(dataRepository, times(2)).findAll(captor.capture());
		verifyNoMoreInteractions(dataRepository);
		assertEquals(4, loginFrequency.size());
		assertEquals(new Integer(2), loginFrequency.get("kdomini8"));
	}
	
	@Test
	public void retrieveEmptyLoginFrequency() throws Exception {
		when(dataRepository.findAll(any(Specification.class))).thenReturn(new ArrayList<>());
		Map<String, List<String>> map = new HashMap<>();
		map.put("attributeOne", Arrays.asList("Manila"));
		map.put("attributeTwo", Arrays.asList("Philippines"));
		map.put("attributeThree", Arrays.asList("Vimbo"));
		map.put("attributeFour", Arrays.asList("diners-club-carte-blanche"));
		Map<String, Integer> loginFrequency = dataService.retrieveLoginFrequencyOnGivenCriteria(dateFormat.parse("2010-08-27"), dateFormat.parse("2018-12-31"), map);
		verify(dataRepository, times(1)).findAll(captor.capture());
		assertEquals(0, loginFrequency.size());
	}
	
	private List<DataEntity> createDataEntitylist() throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<DataEntity> entities = new ArrayList<>();
		entities.add(new DataEntity(1, "nswiffan7", formatter.parse("2010-08-29 00:23:52"), "Sanjiang", "China", "Photobean", "diners-club-carte-blanche"));
		entities.add(new DataEntity(2, "kdomini8", formatter.parse("2014-08-21 14:21:26"), "Poříčany", "Czech Republic", "Dazzlesphere", "jcb"));
		entities.add(new DataEntity(3, "ohammersleym", formatter.parse("2015-06-03 23:10:52"), "Huangwei", "China", "Yoveo", "americanexpress"));
		entities.add(new DataEntity(4, "iauchterlonie1d", formatter.parse("2010-08-29 09:37:29"), null, "Peru", "Mynte", "jcb"));
		entities.add(new DataEntity(5, "kdomini8", formatter.parse("2013-04-27 22:21:24"), "Madīnat ‘Īsá", "Bahrain", null, "jcb"));
		entities.add(new DataEntity(6, "ecanario5b", formatter.parse("2017-06-04 14:23:08"), "Cataguases", "Brazil", "Skilith", "diners-club-us-ca"));
		entities.add(new DataEntity(7, "iauchterlonie1d", formatter.parse("2018-05-08 00:56:43"), "Jiaowei", "China", "Devshare", "maestro"));
		entities.add(new DataEntity(8, "abindonfl", formatter.parse("2013-12-01 19:48:30"), "Canillo", "Andorra", "Yacero", "jcb"));
		entities.add(new DataEntity(9, "dbrislandoo", formatter.parse("2013-06-20 14:10:21"), "Granada", "Peru", "Lazzy", "jcb"));
		return entities;
	}

}
