package com.upgrade.campscheduling.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.upgrade.campscheduling.models.CampsiteReservation;

@Repository
public interface CampsiteReservationRepository extends JpaRepository<CampsiteReservation, Long>, CampsiteReservationRepositoryCustom {
}
