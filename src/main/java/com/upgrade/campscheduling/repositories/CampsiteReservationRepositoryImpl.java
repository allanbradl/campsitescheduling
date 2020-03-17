package com.upgrade.campscheduling.repositories;

import java.time.LocalDate;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.upgrade.campscheduling.models.CampsiteReservation;

public class CampsiteReservationRepositoryImpl implements CampsiteReservationRepositoryCustom {

	@Autowired
	CampsiteReservationRepository campsiteReservationRepository;

	private NavigableMap<LocalDate, Long> campsiteReservations = new TreeMap<LocalDate, Long>();

	@Override
	public NavigableMap<LocalDate, Long> getCampsiteReservationsBetween(LocalDate start, LocalDate end) {
		NavigableMap<LocalDate, Long> reserverdDaysInRange = campsiteReservations.subMap(start, true, end, true);
		return reserverdDaysInRange;
	}

	@Override
	public CampsiteReservation saveCampsiteReservation(CampsiteReservation campsiteReservationToBeCreated) {
		campsiteReservationRepository.save(campsiteReservationToBeCreated);

		for (LocalDate date = campsiteReservationToBeCreated.getStartDate(); 
				date.isBefore(campsiteReservationToBeCreated.getEndDate()); date = date.plusDays(1)) {
			campsiteReservations.put(date, campsiteReservationToBeCreated.getId());
		}

		return campsiteReservationToBeCreated;
	}

	@Override
	public void deleteCampsiteReservation(CampsiteReservation campsiteReservationToBeDeleted) {
		
		campsiteReservationRepository.delete(campsiteReservationToBeDeleted);
		
		for (LocalDate date = campsiteReservationToBeDeleted.getStartDate(); 
				date.isBefore(campsiteReservationToBeDeleted.getEndDate()); date = date.plusDays(1)) {
			campsiteReservations.remove(date);
		}
	}
}
