package org.lab2.dto;

import org.lab2.enums.PrivilegeStatus;

import java.util.List;

public record PrivilegeInfoResponse(
        Integer balance,
        PrivilegeStatus status,
        List<BalanceHistory> history
) {
}
