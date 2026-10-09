package org.lab2.gateway.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lab2.dto.FlightResponse;
import org.lab2.dto.PrivilegeInfoResponse;
import org.lab2.dto.PrivilegeOperationRequest;
import org.lab2.dto.PrivilegeShortInfo;
import org.lab2.dto.TicketPurchaseRequest;
import org.lab2.dto.TicketPurchaseResponse;
import org.lab2.dto.TicketResponse;
import org.lab2.dto.TicketShortInfo;
import org.lab2.enums.OperationType;
import org.lab2.enums.PrivilegeStatus;
import org.lab2.enums.TicketStatus;
import org.lab2.gateway.client.BonusClient;
import org.lab2.gateway.client.FlightClient;
import org.lab2.gateway.client.TicketClient;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GatewayServiceTest {

    @Mock
    private FlightClient flightClient;

    @Mock
    private TicketClient ticketClient;

    @Mock
    private BonusClient bonusClient;

    @InjectMocks
    private GatewayService gatewayService;

    @Test
    void purchase_withMoneyAccruesTenPercentBonuses() {
        UUID ticketUid = UUID.randomUUID();
        when(flightClient.getFlight("AFL031")).thenReturn(flight());
        when(bonusClient.getPrivilege("Test Max"))
                .thenReturn(new PrivilegeInfoResponse(0, PrivilegeStatus.BRONZE, List.of()));
        when(ticketClient.createTicket("Test Max", "AFL031", 1500))
                .thenReturn(new TicketShortInfo(ticketUid, "AFL031", 1500, TicketStatus.PAID));
        when(bonusClient.applyOperation(eq("Test Max"), any())).thenAnswer(invocation -> {
            PrivilegeOperationRequest request = invocation.getArgument(1);
            return new PrivilegeShortInfo(request.balanceDiff(), PrivilegeStatus.BRONZE);
        });

        TicketPurchaseResponse response = gatewayService.purchase("Test Max",
                new TicketPurchaseRequest("AFL031", 1500, false));

        assertThat(response.paidByMoney()).isEqualTo(1500);
        assertThat(response.paidByBonuses()).isZero();
        assertThat(response.status()).isEqualTo(TicketStatus.PAID);
        assertThat(response.privilege().balance()).isEqualTo(150);

        ArgumentCaptor<PrivilegeOperationRequest> captor = ArgumentCaptor.forClass(PrivilegeOperationRequest.class);
        verify(bonusClient).applyOperation(eq("Test Max"), captor.capture());
        assertThat(captor.getValue().operationType()).isEqualTo(OperationType.FILLED_BY_MONEY);
        assertThat(captor.getValue().balanceDiff()).isEqualTo(150);
    }

    @Test
    void purchase_fromBalanceDebitsBonuses() {
        UUID ticketUid = UUID.randomUUID();
        when(flightClient.getFlight("AFL031")).thenReturn(flight());
        when(bonusClient.getPrivilege("Test Max"))
                .thenReturn(new PrivilegeInfoResponse(500, PrivilegeStatus.BRONZE, List.of()));
        when(ticketClient.createTicket("Test Max", "AFL031", 1500))
                .thenReturn(new TicketShortInfo(ticketUid, "AFL031", 1500, TicketStatus.PAID));
        when(bonusClient.applyOperation(eq("Test Max"), any())).thenAnswer(invocation -> {
            PrivilegeOperationRequest request = invocation.getArgument(1);
            return new PrivilegeShortInfo(0, PrivilegeStatus.BRONZE);
        });

        TicketPurchaseResponse response = gatewayService.purchase("Test Max",
                new TicketPurchaseRequest("AFL031", 1500, true));

        assertThat(response.paidByBonuses()).isEqualTo(500);
        assertThat(response.paidByMoney()).isEqualTo(1000);
    }

    @Test
    void getTickets_enrichesWithFlightInfo() {
        when(ticketClient.getTickets("Test Max"))
                .thenReturn(List.of(new TicketShortInfo(UUID.randomUUID(), "AFL031", 1500, TicketStatus.PAID)));
        when(flightClient.getFlight("AFL031")).thenReturn(flight());

        List<TicketResponse> tickets = gatewayService.getTickets("Test Max");

        assertThat(tickets).hasSize(1);
        assertThat(tickets.get(0).fromAirport()).isEqualTo("Санкт-Петербург Пулково");
        assertThat(tickets.get(0).toAirport()).isEqualTo("Москва Шереметьево");
        assertThat(tickets.get(0).date()).isEqualTo("2021-10-08 20:00");
    }

    private FlightResponse flight() {
        return new FlightResponse("AFL031", "Санкт-Петербург Пулково", "Москва Шереметьево",
                "2021-10-08 20:00", 1500);
    }
}
