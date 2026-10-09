package org.lab2.ticket.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lab2.dto.TicketShortInfo;
import org.lab2.enums.TicketStatus;
import org.lab2.ticket.dto.TicketCreateRequest;
import org.lab2.ticket.entity.TicketEntity;
import org.lab2.ticket.exception.TicketNotFoundException;
import org.lab2.ticket.repository.TicketRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    @Test
    void create_generatesUidAndPaidStatus() {
        when(ticketRepository.save(any(TicketEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TicketShortInfo created = ticketService.create("Test Max",
                new TicketCreateRequest("AFL031", 1500));

        assertThat(created.ticketUid()).isNotNull();
        assertThat(created.flightNumber()).isEqualTo("AFL031");
        assertThat(created.price()).isEqualTo(1500);
        assertThat(created.status()).isEqualTo(TicketStatus.PAID);
    }

    @Test
    void findByUsername_returnsTickets() {
        when(ticketRepository.findByUsernameOrderByIdAsc("Test Max"))
                .thenReturn(List.of(ticket(UUID.randomUUID(), "Test Max")));

        List<TicketShortInfo> result = ticketService.findByUsername("Test Max");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).flightNumber()).isEqualTo("AFL031");
    }

    @Test
    void cancel_setsCanceledStatus() {
        UUID uid = UUID.randomUUID();
        TicketEntity ticket = ticket(uid, "Test Max");
        when(ticketRepository.findByTicketUidAndUsername(uid, "Test Max")).thenReturn(Optional.of(ticket));

        ticketService.cancel(uid, "Test Max");

        assertThat(ticket.getStatus()).isEqualTo(TicketStatus.CANCELED);
    }

    @Test
    void cancel_throwsWhenTicketBelongsToAnotherUser() {
        UUID uid = UUID.randomUUID();
        when(ticketRepository.findByTicketUidAndUsername(uid, "Other")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.cancel(uid, "Other"))
                .isInstanceOf(TicketNotFoundException.class);
    }

    private TicketEntity ticket(UUID uid, String username) {
        return new TicketEntity(1, uid, username, "AFL031", 1500, TicketStatus.PAID);
    }
}
