package com.upgrade.campscheduling.repositories;

import java.time.LocalDate;
import java.util.NavigableMap;

import com.upgrade.campscheduling.models.CampsiteReservation;

public interface CampsiteReservationRepositoryCustom {
	public NavigableMap<LocalDate, Long> getCampsiteReservationsBetween(LocalDate start, LocalDate end);
	public CampsiteReservation saveCampsiteReservation(CampsiteReservation campsiteReservationToBeCreated);
	public void deleteCampsiteReservation(CampsiteReservation campsiteReservationToBeDeleted);
}