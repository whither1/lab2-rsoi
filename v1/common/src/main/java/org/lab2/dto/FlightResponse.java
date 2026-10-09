package org.lab2.dto;

public record FlightResponse(
        String flightNumber,
        String fromAirport,
        String toAirport,
        String date,
        Integer price
) {
}
