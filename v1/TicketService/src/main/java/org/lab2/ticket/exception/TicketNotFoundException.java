package org.lab2.ticket.exception;

import java.util.UUID;

public class TicketNotFoundException extends RuntimeException {

    public TicketNotFoundException(UUID ticketUid) {
        super("Ticket not found: " + ticketUid);
    }
}
