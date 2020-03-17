package com.upgrade.campscheduling.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upgrade.campscheduling.exceptions.SchedulingException;
import com.upgrade.campscheduling.models.CampsiteReservation;
import com.upgrade.campscheduling.repositories.CampsiteReservationRepository;

@Service
public class CampsiteReservationService {

	@Autowired
	private CampsiteReservationRepository campReservationRepository;
	
	public List<LocalDate> getCampsiteAvailability(LocalDate startDate, LocalDate endDate) {
		List<LocalDate> availableDates = new ArrayList<LocalDate>();
		NavigableMap<LocalDate, Long> reservedCampsiteDates = campReservationRepository
				.getCampsiteReservationsBetween(startDate, endDate);

		for (LocalDate date = startDate; date.isBefore(endDate) || date.equals(endDate)  ; date = date.plusDays(1)) {
			if (!reservedCampsiteDates.containsKey(date)) {
				availableDates.add(date);
			}
		}
		return availableDates;
	}

	public synchronized CampsiteReservation save(CampsiteReservation newCampsiteReservation) throws SchedulingException {
		reservationDateChecks(newCampsiteReservation);

		NavigableMap<LocalDate, Long> busyDaysInRange = campReservationRepository.getCampsiteReservationsBetween(
				newCampsiteReservation.getStartDate(), newCampsiteReservation.getEndDate());

		if (busyDaysInRange.isEmpty()) {
			return campReservationRepository.saveCampsiteReservation(newCampsiteReservation);
		}else{
			throw new SchedulingException("Selected dates are no longer available, please choose another date range.");
		}
	}

	public synchronized CampsiteReservation update(CampsiteReservation updatedCampsiteReservation) throws SchedulingException {
		reservationDateChecks(updatedCampsiteReservation);

		NavigableMap<LocalDate, Long> busyDaysInRange = campReservationRepository.getCampsiteReservationsBetween(
				updatedCampsiteReservation.getStartDate(), updatedCampsiteReservation.getEndDate());

		boolean errorsPresent = false;
		for (Long reservationId : busyDaysInRange.values()) {
			if (reservationId != updatedCampsiteReservation.getId()) {
				errorsPresent = true;
				break;
			}
		}

		if (!errorsPresent) {
			this.delete(updatedCampsiteReservation.getId());
			return this.save(updatedCampsiteReservation);
		} else {
			throw new SchedulingException(
					"Campsite reservation could not be updated because selected dates are already taken.");
		}
	}

	public synchronized void delete(Long reservation) throws SchedulingException {
		Optional<CampsiteReservation> toBeDeleted = campReservationRepository.findById(reservation);
		if (toBeDeleted.isPresent()){
			campReservationRepository.deleteCampsiteReservation(toBeDeleted.get());
		}else{
			throw new SchedulingException("Provided id could not be deleted because it was not found.");
		}
	}

	private void reservationDateChecks(CampsiteReservation campsiteReservation) throws SchedulingException {
		long reservationDurationInDays = ChronoUnit.DAYS.between(campsiteReservation.getStartDate(),
				campsiteReservation.getEndDate());

		if (0 > reservationDurationInDays || reservationDurationInDays > 2) {
			throw new SchedulingException("Reservation lenght does not meet the requirements, should be between 1 and 3 days.");
		}

		long reservationArrivalDifferenceInDays = ChronoUnit.DAYS.between(campsiteReservation.getArrivalDate(),
				campsiteReservation.getStartDate());

		if (reservationArrivalDifferenceInDays < 1 || reservationArrivalDifferenceInDays > 30) {
			throw new SchedulingException("Reservation dates are invalid. Please check arrival date.");
		}
	}
}
