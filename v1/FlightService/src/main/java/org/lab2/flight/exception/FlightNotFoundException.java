package org.lab2.flight.exception;

public class FlightNotFoundException extends RuntimeException {

    public FlightNotFoundException(String flightNumber) {
        super("Flight not found: " + flightNumber);
    }
}
