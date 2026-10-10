package org.lab2.dto;

import org.lab2.enums.TicketStatus;

import java.util.UUID;

public record TicketResponse(
        UUID ticketUid,
        String flightNumber,
        String fromAirport,
        String toAirport,
        String date,
        Integer price,
        TicketStatus status
) {
}
