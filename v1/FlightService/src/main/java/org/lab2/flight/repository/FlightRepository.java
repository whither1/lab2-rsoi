package org.lab2.flight.repository;

import org.lab2.flight.entity.FlightEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<FlightEntity, Integer> {

    Optional<FlightEntity> findByFlightNumber(String flightNumber);
}
