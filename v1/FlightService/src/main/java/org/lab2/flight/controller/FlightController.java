package org.lab2.flight.controller;

import org.lab2.dto.FlightResponse;
import org.lab2.dto.PaginationResponse;
import org.lab2.flight.service.FlightService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/flights")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping
    public PaginationResponse<FlightResponse> list(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        return flightService.findFlights(page, size);
    }

    @GetMapping("/{flightNumber}")
    public FlightResponse getByNumber(@PathVariable String flightNumber) {
        return flightService.findByFlightNumber(flightNumber);
    }
}
