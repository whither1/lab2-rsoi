package org.lab2.flight.service;

import org.lab2.dto.FlightResponse;
import org.lab2.dto.PaginationResponse;
import org.lab2.flight.entity.AirportEntity;
import org.lab2.flight.entity.FlightEntity;
import org.lab2.flight.exception.FlightNotFoundException;
import org.lab2.flight.repository.FlightRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class FlightService {

    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public PaginationResponse<FlightResponse> findFlights(int page, int size) {
        int pageIndex = Math.max(page - 1, 0);
        Page<FlightEntity> result = flightRepository.findAll(PageRequest.of(pageIndex, size));
        List<FlightResponse> items = result.getContent().stream()
                .map(this::toResponse)
                .toList();
        return new PaginationResponse<>(
                page,
                items.size(),
                result.getTotalElements(),
                items
        );
    }

    public FlightResponse findByFlightNumber(String flightNumber) {
        FlightEntity flight = flightRepository.findByFlightNumber(flightNumber)
                .orElseThrow(() -> new FlightNotFoundException(flightNumber));
        return toResponse(flight);
    }

    public FlightResponse toResponse(FlightEntity flight) {
        return new FlightResponse(
                flight.getFlightNumber(),
                airportName(flight.getFromAirport()),
                airportName(flight.getToAirport()),
                flight.getDatetime(),
                flight.getPrice()
        );
    }

    private String airportName(AirportEntity airport) {
        if (airport == null) {
            return null;
        }
        return airport.getCity() + " " + airport.getName();
    }
}
