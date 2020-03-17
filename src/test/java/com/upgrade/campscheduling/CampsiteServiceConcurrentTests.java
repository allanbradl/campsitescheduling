package com.upgrade.campscheduling;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.upgrade.campscheduling.exceptions.SchedulingException;
import com.upgrade.campscheduling.models.CampsiteReservation;
import com.upgrade.campscheduling.services.CampsiteReservationService;

@SpringBootTest(classes = SchedulingApplication.class)
public class CampsiteServiceConcurrentTests extends AbstractTestNGSpringContextTests {

	@Autowired
	private CampsiteReservationService campsiteReservationService;

	@Test(threadPoolSize = 10, invocationCount = 10, timeOut = 1000, successPercentage = 90)
	public void saveCampsiteReservationConcurrent() {
		CampsiteReservation s = new CampsiteReservation();
		s.setStartDate(LocalDate.parse("2020-03-20"));
		s.setEndDate(LocalDate.parse("2020-03-22"));
		s.setArrivalDate(LocalDate.parse("2020-03-19"));
		s.setDepartureDate(LocalDate.parse("2020-04-10"));
		s.setEmail("mytest@email.com");
		
		Assertions.assertThrows(SchedulingException.class, () -> {
			campsiteReservationService.save(s);
	    });
	}

}
