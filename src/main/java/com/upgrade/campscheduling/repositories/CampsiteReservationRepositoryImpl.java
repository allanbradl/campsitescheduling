package com.upgrade.campscheduling.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.upgrade.campscheduling.models.CampsiteReservation;

public class CampsiteReservationRepositoryImpl implements CampsiteReservationRepositoryCustom {

	@Autowired
	CampsiteReservationRepository campsiteReservationRepository;

	private NavigableMap<LocalDate, Long> campsiteReservations = new TreeMap<LocalDate, Long>();

	@PostConstruct
    private void init() {
		List<CampsiteReservation> existingReservations = campsiteReservationRepository.findAllByStartDateAfter(LocalDate.now());
		for (CampsiteReservation campsiteReservation : existingReservations) {
			saveCampsiteReservationInMemory(campsiteReservation);
		}
    }
	
	@Override
	public NavigableMap<LocalDate, Long> getCampsiteReservationsBetween(LocalDate start, LocalDate end) {
		NavigableMap<LocalDate, Long> reserverdDaysInRange = campsiteReservations.subMap(start, true, end, true);
		return reserverdDaysInRange;
	}

	@Override
	public CampsiteReservation saveCampsiteReservation(CampsiteReservation campsiteReservationToBeCreated) {
		// DB save.
		campsiteReservationRepository.save(campsiteReservationToBeCreated);
		// Memory save.
		this.saveCampsiteReservationInMemory(campsiteReservationToBeCreated);
		return campsiteReservationToBeCreated;
	}

	@Override
	public void deleteCampsiteReservation(CampsiteReservation campsiteReservationToBeDeleted) {
		// DB delete.
		campsiteReservationRepository.delete(campsiteReservationToBeDeleted);
		// Memory delete.
		this.deleteCampsiteReservationInMemory(campsiteReservationToBeDeleted);
	}
	
	private void saveCampsiteReservationInMemory (CampsiteReservation campsiteReservationToBeCreated){
		for (LocalDate date = campsiteReservationToBeCreated.getStartDate(); 
				date.isBefore(campsiteReservationToBeCreated.getEndDate()); date = date.plusDays(1)) {
			campsiteReservations.put(date, campsiteReservationToBeCreated.getId());
		}
	}
	
	private void deleteCampsiteReservationInMemory (CampsiteReservation campsiteReservationToBeDeleted){
		for (LocalDate date = campsiteReservationToBeDeleted.getStartDate(); 
				date.isBefore(campsiteReservationToBeDeleted.getEndDate()); date = date.plusDays(1)) {
			campsiteReservations.remove(date);
		}
	}
}
