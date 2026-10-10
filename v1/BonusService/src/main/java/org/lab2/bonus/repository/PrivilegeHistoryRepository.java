package org.lab2.bonus.repository;

import org.lab2.bonus.entity.PrivilegeHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PrivilegeHistoryRepository extends JpaRepository<PrivilegeHistoryEntity, Integer> {

    List<PrivilegeHistoryEntity> findByPrivilege_IdOrderByIdAsc(Integer privilegeId);

    Optional<PrivilegeHistoryEntity> findByTicketUid(UUID ticketUid);
}
