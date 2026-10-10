package org.lab2.dto;

import org.lab2.enums.TicketStatus;

import java.util.UUID;

public record TicketShortInfo(
        UUID ticketUid,
        String flightNumber,
        Integer price,
        TicketStatus status
) {
}
