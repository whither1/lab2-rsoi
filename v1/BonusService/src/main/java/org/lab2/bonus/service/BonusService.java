package org.lab2.bonus.service;

import org.lab2.bonus.entity.PrivilegeEntity;
import org.lab2.bonus.entity.PrivilegeHistoryEntity;
import org.lab2.bonus.repository.PrivilegeHistoryRepository;
import org.lab2.bonus.repository.PrivilegeRepository;
import org.lab2.dto.BalanceHistory;
import org.lab2.dto.PrivilegeInfoResponse;
import org.lab2.dto.PrivilegeOperationRequest;
import org.lab2.dto.PrivilegeShortInfo;
import org.lab2.enums.PrivilegeStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class BonusService {

    private static final int SILVER_THRESHOLD = 10000;
    private static final int GOLD_THRESHOLD = 50000;

    private final PrivilegeRepository privilegeRepository;
    private final PrivilegeHistoryRepository privilegeHistoryRepository;

    public BonusService(PrivilegeRepository privilegeRepository,
                        PrivilegeHistoryRepository privilegeHistoryRepository) {
        this.privilegeRepository = privilegeRepository;
        this.privilegeHistoryRepository = privilegeHistoryRepository;
    }

    public PrivilegeInfoResponse getPrivilege(String username) {
        PrivilegeEntity privilege = getOrCreate(username);
        List<BalanceHistory> history = privilegeHistoryRepository
                .findByPrivilege_IdOrderByIdAsc(privilege.getId()).stream()
                .map(this::toHistory)
                .toList();
        return new PrivilegeInfoResponse(privilege.getBalance(), privilege.getStatus(), history);
    }

    public PrivilegeShortInfo apply(String username, PrivilegeOperationRequest request) {
        PrivilegeEntity privilege = getOrCreate(username);

        int newBalance = privilege.getBalance() + request.balanceDiff();
        if (newBalance < 0) {
            newBalance = 0;
        }
        privilege.setBalance(newBalance);
        privilege.setStatus(resolveStatus(newBalance));

        PrivilegeHistoryEntity history = new PrivilegeHistoryEntity();
        history.setPrivilege(privilege);
        history.setTicketUid(request.ticketUid());
        history.setDatetime(LocalDateTime.now());
        history.setBalanceDiff(request.balanceDiff());
        history.setOperationType(request.operationType());
        privilegeHistoryRepository.save(history);

        return new PrivilegeShortInfo(privilege.getBalance(), privilege.getStatus());
    }

    private PrivilegeEntity getOrCreate(String username) {
        return privilegeRepository.findByUsername(username).orElseGet(() -> {
            PrivilegeEntity privilege = new PrivilegeEntity();
            privilege.setUsername(username);
            privilege.setStatus(PrivilegeStatus.BRONZE);
            privilege.setBalance(0);
            return privilegeRepository.save(privilege);
        });
    }

    private PrivilegeStatus resolveStatus(int balance) {
        if (balance >= GOLD_THRESHOLD) {
            return PrivilegeStatus.GOLD;
        }
        if (balance >= SILVER_THRESHOLD) {
            return PrivilegeStatus.SILVER;
        }
        return PrivilegeStatus.BRONZE;
    }

    private BalanceHistory toHistory(PrivilegeHistoryEntity entity) {
        return new BalanceHistory(
                entity.getDatetime().toString(),
                entity.getTicketUid(),
                entity.getBalanceDiff(),
                entity.getOperationType()
        );
    }
}
