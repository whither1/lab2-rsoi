package org.lab2.ticket.service;

import org.lab2.dto.TicketShortInfo;
import org.lab2.enums.TicketStatus;
import org.lab2.ticket.dto.TicketCreateRequest;
import org.lab2.ticket.entity.TicketEntity;
import org.lab2.ticket.exception.TicketNotFoundException;
import org.lab2.ticket.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Transactional(readOnly = true)
    public List<TicketShortInfo> findByUsername(String username) {
        return ticketRepository.findByUsernameOrderByIdAsc(username).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TicketShortInfo findByUuidAndUsername(UUID ticketUid, String username) {
        TicketEntity ticket = ticketRepository.findByTicketUidAndUsername(ticketUid, username)
                .orElseThrow(() -> new TicketNotFoundException(ticketUid));
        return toResponse(ticket);
    }

    public TicketShortInfo create(String username, TicketCreateRequest request) {
        TicketEntity ticket = new TicketEntity();
        ticket.setTicketUid(UUID.randomUUID());
        ticket.setUsername(username);
        ticket.setFlightNumber(request.flightNumber());
        ticket.setPrice(request.price());
        ticket.setStatus(TicketStatus.PAID);
        return toResponse(ticketRepository.save(ticket));
    }

    public void cancel(UUID ticketUid, String username) {
        TicketEntity ticket = ticketRepository.findByTicketUidAndUsername(ticketUid, username)
                .orElseThrow(() -> new TicketNotFoundException(ticketUid));
        ticket.setStatus(TicketStatus.CANCELED);
    }

    private TicketShortInfo toResponse(TicketEntity ticket) {
        return new TicketShortInfo(
                ticket.getTicketUid(),
                ticket.getFlightNumber(),
                ticket.getPrice(),
                ticket.getStatus()
        );
    }
}
