package com.upgrade.campscheduling.controllers;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.upgrade.campscheduling.models.CampsiteReservation;
import com.upgrade.campscheduling.services.CampsiteReservationService;

@RestController
public class CampsiteController {
	private final CampsiteReservationService service;

	CampsiteController(CampsiteReservationService service) {
		this.service = service;
	}

	@GetMapping("/campsite-availability/{start}/{end}")
	public List<LocalDate> checkCampsiteAvailabilityBetweenDates(@PathVariable("start") String start,
			@PathVariable("end") String end) {
		LocalDate startDate = LocalDate.parse(start);
		LocalDate endDate = LocalDate.parse(end);
		return service.getCampsiteAvailability(startDate, endDate);
	}

	@GetMapping("/campsite-availability")
	public List<LocalDate> checkCampsiteAvailability() {
		LocalDate startDate = LocalDate.now();
		LocalDate endDate = LocalDate.now().plusDays(30);
		return service.getCampsiteAvailability(startDate, endDate);
	}

	@PostMapping("/reservation")
	public CampsiteReservation createReservation(@Valid @RequestBody CampsiteReservation reservation) {
		try {
			return service.save(reservation);
		} catch (Exception exc) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, exc.getMessage());
		}
	}

	@PatchMapping("/reservation")
	public CampsiteReservation updateCampsiteReservation(@Valid @RequestBody CampsiteReservation reservation) {
		try {
			return service.update(reservation);
		} catch (Exception exc) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, exc.getMessage());
		}
	}

	@DeleteMapping("/reservation/{id}")
	public void deleteCampsiteReservation(@PathVariable("id") String reservationId) {
		try {
			Long reservation = Long.parseLong(reservationId);
			service.delete(reservation);
		} catch (Exception exc) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, exc.getMessage());
		}
	}
}
