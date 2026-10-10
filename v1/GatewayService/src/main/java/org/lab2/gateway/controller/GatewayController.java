package org.lab2.gateway.controller;

import org.lab2.dto.FlightResponse;
import org.lab2.dto.PaginationResponse;
import org.lab2.dto.PrivilegeInfoResponse;
import org.lab2.dto.TicketPurchaseRequest;
import org.lab2.dto.TicketPurchaseResponse;
import org.lab2.dto.TicketResponse;
import org.lab2.dto.UserInfoResponse;
import org.lab2.gateway.service.GatewayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class GatewayController {

    private final GatewayService gatewayService;

    public GatewayController(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @GetMapping("/flights")
    public PaginationResponse<FlightResponse> getFlights(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        return gatewayService.getFlights(page, size);
    }

    @GetMapping("/privilege")
    public PrivilegeInfoResponse getPrivilege(@RequestHeader("X-User-Name") String username) {
        return gatewayService.getPrivilege(username);
    }

    @GetMapping("/tickets")
    public List<TicketResponse> getTickets(@RequestHeader("X-User-Name") String username) {
        return gatewayService.getTickets(username);
    }

    @GetMapping("/tickets/{ticketUid}")
    public TicketResponse getTicket(@PathVariable UUID ticketUid,
                                    @RequestHeader("X-User-Name") String username) {
        return gatewayService.getTicket(ticketUid, username);
    }

    @GetMapping("/me")
    public UserInfoResponse getMe(@RequestHeader("X-User-Name") String username) {
        return gatewayService.getMe(username);
    }

    @PostMapping("/tickets")
    public TicketPurchaseResponse purchase(@RequestHeader("X-User-Name") String username,
                                           @RequestBody TicketPurchaseRequest request) {
        return gatewayService.purchase(username, request);
    }

    @DeleteMapping("/tickets/{ticketUid}")
    public ResponseEntity<Void> refund(@PathVariable UUID ticketUid,
                                       @RequestHeader("X-User-Name") String username) {
        gatewayService.refund(ticketUid, username);
        return ResponseEntity.noContent().build();
    }
}
