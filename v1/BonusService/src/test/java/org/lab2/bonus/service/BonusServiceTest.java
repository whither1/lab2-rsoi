package org.lab2.bonus.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lab2.bonus.entity.PrivilegeEntity;
import org.lab2.bonus.repository.PrivilegeHistoryRepository;
import org.lab2.bonus.repository.PrivilegeRepository;
import org.lab2.dto.PrivilegeInfoResponse;
import org.lab2.dto.PrivilegeOperationRequest;
import org.lab2.dto.PrivilegeShortInfo;
import org.lab2.enums.OperationType;
import org.lab2.enums.PrivilegeStatus;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BonusServiceTest {

    @Mock
    private PrivilegeRepository privilegeRepository;

    @Mock
    private PrivilegeHistoryRepository privilegeHistoryRepository;

    @InjectMocks
    private BonusService bonusService;

    @Test
    void getPrivilege_createsDefaultWhenMissing() {
        when(privilegeRepository.findByUsername("Test Max")).thenReturn(Optional.empty());
        when(privilegeRepository.save(any(PrivilegeEntity.class)))
                .thenAnswer(invocation -> {
                    PrivilegeEntity entity = invocation.getArgument(0);
                    entity.setId(1);
                    return entity;
                });
        when(privilegeHistoryRepository.findByPrivilege_IdOrderByIdAsc(1)).thenReturn(List.of());

        PrivilegeInfoResponse response = bonusService.getPrivilege("Test Max");

        assertThat(response.balance()).isZero();
        assertThat(response.status()).isEqualTo(PrivilegeStatus.BRONZE);
        assertThat(response.history()).isEmpty();
    }

    @Test
    void apply_accruesBonusesForMoneyPurchase() {
        PrivilegeEntity privilege = new PrivilegeEntity(1, "Test Max", PrivilegeStatus.BRONZE, 0);
        when(privilegeRepository.findByUsername("Test Max")).thenReturn(Optional.of(privilege));
        when(privilegeHistoryRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        PrivilegeShortInfo result = bonusService.apply("Test Max",
                new PrivilegeOperationRequest(UUID.randomUUID(), 150, OperationType.FILLED_BY_MONEY));

        assertThat(result.balance()).isEqualTo(150);
        assertThat(privilege.getBalance()).isEqualTo(150);
    }

    @Test
    void apply_doesNotDropBalanceBelowZero() {
        PrivilegeEntity privilege = new PrivilegeEntity(1, "Test Max", PrivilegeStatus.BRONZE, 100);
        when(privilegeRepository.findByUsername("Test Max")).thenReturn(Optional.of(privilege));
        when(privilegeHistoryRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        PrivilegeShortInfo result = bonusService.apply("Test Max",
                new PrivilegeOperationRequest(UUID.randomUUID(), -500, OperationType.DEBIT_THE_ACCOUNT));

        assertThat(result.balance()).isZero();
    }

    @Test
    void apply_recomputesStatus() {
        PrivilegeEntity privilege = new PrivilegeEntity(1, "Test Max", PrivilegeStatus.BRONZE, 0);
        when(privilegeRepository.findByUsername("Test Max")).thenReturn(Optional.of(privilege));
        when(privilegeHistoryRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        PrivilegeShortInfo result = bonusService.apply("Test Max",
                new PrivilegeOperationRequest(UUID.randomUUID(), 60000, OperationType.FILL_IN_BALANCE));

        assertThat(result.status()).isEqualTo(PrivilegeStatus.GOLD);
    }
}
