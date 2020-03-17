package com.upgrade.campscheduling;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.upgrade.campscheduling.models.CampsiteReservation;
import com.upgrade.campscheduling.services.CampsiteReservationService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CampsiteServiceTests {

	@Autowired
	private CampsiteReservationService campsiteReservationService;

	@Test
	public void saveCampsiteReservationSuccess() {
		CampsiteReservation s = new CampsiteReservation();
		s.setStartDate(LocalDate.parse("2020-03-20"));
		s.setEndDate(LocalDate.parse("2020-03-22"));
		s.setArrivalDate(LocalDate.parse("2020-03-19"));
		s.setDepartureDate(LocalDate.parse("2020-04-10"));
		s.setEmail("mytest@email.com");
		try {
			campsiteReservationService.save(s);
			Assert.assertTrue(s.getId() != null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test (expected = Exception.class)
	public void saveCampsiteReservationInvalidLongRange() throws Exception {
		CampsiteReservation s = new CampsiteReservation();
		s.setStartDate(LocalDate.parse("2020-03-20"));
		s.setEndDate(LocalDate.parse("2020-03-23"));
		s.setArrivalDate(LocalDate.parse("2020-03-19"));
		s.setDepartureDate(LocalDate.parse("2020-04-10"));
		s.setEmail("mytest@email.com");
		campsiteReservationService.save(s);
	}
	
	@Test (expected = Exception.class)
	public void saveCampsiteReservationInvalidArrivalDateShort() throws Exception {
		CampsiteReservation s = new CampsiteReservation();
		s.setStartDate(LocalDate.parse("2020-03-20"));
		s.setEndDate(LocalDate.parse("2020-03-21"));
		s.setArrivalDate(LocalDate.parse("2020-03-20"));
		s.setDepartureDate(LocalDate.parse("2020-04-10"));
		s.setEmail("mytest@email.com");
		campsiteReservationService.save(s);
	}
	
	@Test (expected = Exception.class)
	public void saveCampsiteReservationInvalidArrivalDateLong() throws Exception {
		CampsiteReservation s = new CampsiteReservation();
		s.setStartDate(LocalDate.parse("2020-03-20"));
		s.setEndDate(LocalDate.parse("2020-03-21"));
		s.setArrivalDate(LocalDate.parse("2020-02-18"));
		s.setDepartureDate(LocalDate.parse("2020-04-10"));
		s.setEmail("mytest@email.com");
		campsiteReservationService.save(s);
	}
	
}
