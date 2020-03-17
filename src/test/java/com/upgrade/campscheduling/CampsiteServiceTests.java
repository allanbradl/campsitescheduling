package com.upgrade.campscheduling;

import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.upgrade.campscheduling.exceptions.SchedulingException;
import com.upgrade.campscheduling.models.CampsiteReservation;
import com.upgrade.campscheduling.repositories.CampsiteReservationRepository;
import com.upgrade.campscheduling.services.CampsiteReservationService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CampsiteServiceTests {

	@Autowired
	private CampsiteReservationService campsiteReservationService;

	@MockBean
	private CampsiteReservationRepository campsiteReservationRepositoryMock;

	@Test
	public void saveCampsiteReservationSuccess() {
		LocalDate reservationStart = LocalDate.parse("2020-03-20");
		LocalDate reservationEnd = LocalDate.parse("2020-03-22");

		CampsiteReservation newReservation = new CampsiteReservation();
		newReservation.setStartDate(reservationStart);
		newReservation.setEndDate(reservationEnd);
		newReservation.setArrivalDate(LocalDate.parse("2020-03-19"));
		newReservation.setDepartureDate(LocalDate.parse("2020-04-10"));
		newReservation.setEmail("mytest@email.com");

		CampsiteReservation newReservationWithId = new CampsiteReservation();
		newReservationWithId.setStartDate(reservationStart);
		newReservationWithId.setEndDate(reservationEnd);
		newReservationWithId.setArrivalDate(LocalDate.parse("2020-03-19"));
		newReservationWithId.setDepartureDate(LocalDate.parse("2020-04-10"));
		newReservationWithId.setEmail("mytest@email.com");
		newReservationWithId.setId(new Long(10));

		when(campsiteReservationRepositoryMock.getCampsiteReservationsBetween(reservationStart, reservationEnd))
				.thenReturn(new TreeMap<>());
		when(campsiteReservationRepositoryMock.saveCampsiteReservation(newReservation))
				.thenReturn(newReservationWithId);

		try {
			newReservation = campsiteReservationService.save(newReservation);
			Assert.assertTrue(newReservation.getId() == newReservationWithId.getId());
		} catch (SchedulingException e) {
			e.printStackTrace();
		}
	}

	@Test(expected = SchedulingException.class)
	public void saveCampsiteReservationDatesAlreadyTaken() throws SchedulingException {
		LocalDate reservationStart = LocalDate.parse("2020-03-20");
		LocalDate reservationEnd = LocalDate.parse("2020-03-22");

		CampsiteReservation existingReservation = new CampsiteReservation();
		existingReservation.setStartDate(reservationStart);
		existingReservation.setEndDate(reservationEnd);
		existingReservation.setArrivalDate(LocalDate.parse("2020-03-19"));
		existingReservation.setDepartureDate(LocalDate.parse("2020-04-10"));
		existingReservation.setEmail("mytest@email.com");

		NavigableMap<LocalDate, Long> response = new TreeMap<LocalDate, Long>();
		response.put(reservationStart, (long) 1);

		when(campsiteReservationRepositoryMock.getCampsiteReservationsBetween(reservationStart, reservationEnd))
				.thenReturn(response);

		campsiteReservationService.save(existingReservation);
	}

	@Test(expected = SchedulingException.class)
	public void saveCampsiteReservationInvalidDuration() throws SchedulingException {
		CampsiteReservation s = new CampsiteReservation();
		s.setStartDate(LocalDate.parse("2020-03-20"));
		s.setEndDate(LocalDate.parse("2020-03-23"));
		s.setArrivalDate(LocalDate.parse("2020-03-19"));
		s.setDepartureDate(LocalDate.parse("2020-04-10"));
		s.setEmail("mytest@email.com");
		campsiteReservationService.save(s);
	}

	@Test(expected = SchedulingException.class)
	public void saveCampsiteReservationInvalidArrivalDateMin() throws SchedulingException {
		CampsiteReservation s = new CampsiteReservation();
		s.setStartDate(LocalDate.parse("2020-03-20"));
		s.setEndDate(LocalDate.parse("2020-03-21"));
		s.setArrivalDate(LocalDate.parse("2020-03-20"));
		s.setDepartureDate(LocalDate.parse("2020-04-10"));
		s.setEmail("mytest@email.com");
		campsiteReservationService.save(s);
	}

	@Test(expected = SchedulingException.class)
	public void saveCampsiteReservationInvalidArrivalDateMax() throws SchedulingException {
		CampsiteReservation s = new CampsiteReservation();
		s.setStartDate(LocalDate.parse("2020-03-20"));
		s.setEndDate(LocalDate.parse("2020-03-21"));
		s.setArrivalDate(LocalDate.parse("2020-02-18"));
		s.setDepartureDate(LocalDate.parse("2020-04-10"));
		s.setEmail("mytest@email.com");
		campsiteReservationService.save(s);
	}

}
