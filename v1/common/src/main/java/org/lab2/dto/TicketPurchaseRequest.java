package org.lab2.dto;

public record TicketPurchaseRequest(
        String flightNumber,
        Integer price,
        Boolean paidFromBalance
) {
}
