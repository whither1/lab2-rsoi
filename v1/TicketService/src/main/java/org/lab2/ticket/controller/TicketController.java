package org.lab2.ticket.controller;

import org.lab2.dto.TicketShortInfo;
import org.lab2.ticket.dto.TicketCreateRequest;
import org.lab2.ticket.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public List<TicketShortInfo> list(@RequestHeader("X-User-Name") String username) {
        return ticketService.findByUsername(username);
    }

    @GetMapping("/{ticketUid}")
    public TicketShortInfo get(@PathVariable UUID ticketUid,
                               @RequestHeader("X-User-Name") String username) {
        return ticketService.findByUuidAndUsername(ticketUid, username);
    }

    @PostMapping
    public ResponseEntity<TicketShortInfo> create(@RequestHeader("X-User-Name") String username,
                                                  @RequestBody TicketCreateRequest request) {
        TicketShortInfo created = ticketService.create(username, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{ticketUid}")
    public ResponseEntity<Void> cancel(@PathVariable UUID ticketUid,
                                       @RequestHeader("X-User-Name") String username) {
        ticketService.cancel(ticketUid, username);
        return ResponseEntity.noContent().build();
    }
}
