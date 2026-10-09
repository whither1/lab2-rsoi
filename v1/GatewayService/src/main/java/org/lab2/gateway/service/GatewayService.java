package org.lab2.gateway.service;

import org.lab2.dto.BalanceHistory;
import org.lab2.dto.FlightResponse;
import org.lab2.dto.PaginationResponse;
import org.lab2.dto.PrivilegeInfoResponse;
import org.lab2.dto.PrivilegeOperationRequest;
import org.lab2.dto.PrivilegeShortInfo;
import org.lab2.dto.TicketPurchaseRequest;
import org.lab2.dto.TicketPurchaseResponse;
import org.lab2.dto.TicketResponse;
import org.lab2.dto.TicketShortInfo;
import org.lab2.dto.UserInfoResponse;
import org.lab2.enums.OperationType;
import org.lab2.enums.TicketStatus;
import org.lab2.gateway.client.BonusClient;
import org.lab2.gateway.client.FlightClient;
import org.lab2.gateway.client.TicketClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class GatewayService {

    private static final int BONUS_PERCENT = 10;

    private final FlightClient flightClient;
    private final TicketClient ticketClient;
    private final BonusClient bonusClient;

    public GatewayService(FlightClient flightClient, TicketClient ticketClient, BonusClient bonusClient) {
        this.flightClient = flightClient;
        this.ticketClient = ticketClient;
        this.bonusClient = bonusClient;
    }

    public PaginationResponse<FlightResponse> getFlights(int page, int size) {
        return flightClient.getFlights(page, size);
    }

    public PrivilegeInfoResponse getPrivilege(String username) {
        return bonusClient.getPrivilege(username);
    }

    public List<TicketResponse> getTickets(String username) {
        Map<String, FlightResponse> flights = new HashMap<>();
        return ticketClient.getTickets(username).stream()
                .map(ticket -> enrich(ticket, flights))
                .toList();
    }

    public TicketResponse getTicket(UUID ticketUid, String username) {
        return enrich(ticketClient.getTicket(ticketUid, username), new HashMap<>());
    }

    public UserInfoResponse getMe(String username) {
        List<TicketResponse> tickets = getTickets(username);
        PrivilegeInfoResponse privilege = bonusClient.getPrivilege(username);
        return new UserInfoResponse(
                tickets,
                new PrivilegeShortInfo(privilege.balance(), privilege.status())
        );
    }

    public TicketPurchaseResponse purchase(String username, TicketPurchaseRequest request) {
        FlightResponse flight = flightClient.getFlight(request.flightNumber());
        PrivilegeInfoResponse privilege = bonusClient.getPrivilege(username);

        int price = flight.price();
        int paidByBonuses;
        int paidByMoney;
        int balanceDiff;
        OperationType operationType;

        if (Boolean.TRUE.equals(request.paidFromBalance()) && privilege.balance() > 0) {
            paidByBonuses = Math.min(privilege.balance(), price);
            paidByMoney = price - paidByBonuses;
            balanceDiff = -paidByBonuses;
            operationType = OperationType.DEBIT_THE_ACCOUNT;
        } else {
            paidByBonuses = 0;
            paidByMoney = price;
            balanceDiff = price * BONUS_PERCENT / 100;
            operationType = OperationType.FILLED_BY_MONEY;
        }

        TicketShortInfo ticket = ticketClient.createTicket(username, flight.flightNumber(), price);
        PrivilegeShortInfo newPrivilege = bonusClient.applyOperation(username,
                new PrivilegeOperationRequest(ticket.ticketUid(), balanceDiff, operationType));

        return new TicketPurchaseResponse(
                ticket.ticketUid(),
                flight.flightNumber(),
                flight.fromAirport(),
                flight.toAirport(),
                flight.date(),
                price,
                paidByMoney,
                paidByBonuses,
                TicketStatus.PAID,
                newPrivilege
        );
    }

    public void refund(UUID ticketUid, String username) {
        ticketClient.getTicket(ticketUid, username);
        ticketClient.cancelTicket(ticketUid, username);

        PrivilegeInfoResponse privilege = bonusClient.getPrivilege(username);
        privilege.history().stream()
                .filter(history -> ticketUid.equals(history.ticketUid()))
                .findFirst()
                .ifPresent(history -> reverseOperation(username, ticketUid, history));
    }

    private void reverseOperation(String username, UUID ticketUid, BalanceHistory history) {
        int amount = Math.abs(history.balanceDiff());
        OperationType reverseType;
        int diff;
        if (history.operationType() == OperationType.DEBIT_THE_ACCOUNT) {
            reverseType = OperationType.FILL_IN_BALANCE;
            diff = amount;
        } else {
            reverseType = OperationType.DEBIT_THE_ACCOUNT;
            diff = -amount;
        }
        bonusClient.applyOperation(username, new PrivilegeOperationRequest(ticketUid, diff, reverseType));
    }

    private TicketResponse enrich(TicketShortInfo ticket, Map<String, FlightResponse> cache) {
        FlightResponse flight = cache.computeIfAbsent(ticket.flightNumber(), flightClient::getFlight);
        return new TicketResponse(
                ticket.ticketUid(),
                ticket.flightNumber(),
                flight.fromAirport(),
                flight.toAirport(),
                flight.date(),
                ticket.price(),
                ticket.status()
        );
    }
}
