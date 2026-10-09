package org.lab2.ticket.repository;

import org.lab2.ticket.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, Integer> {

    List<TicketEntity> findByUsernameOrderByIdAsc(String username);

    Optional<TicketEntity> findByTicketUid(UUID ticketUid);

    Optional<TicketEntity> findByTicketUidAndUsername(UUID ticketUid, String username);
}
