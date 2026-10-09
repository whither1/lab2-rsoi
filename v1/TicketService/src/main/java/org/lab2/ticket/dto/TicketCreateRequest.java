package org.lab2.ticket.dto;

public record TicketCreateRequest(
        String flightNumber,
        Integer price
) {
}
