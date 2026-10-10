package org.lab2.dto;

import org.lab2.enums.OperationType;

import java.util.UUID;

public record PrivilegeOperationRequest(
        UUID ticketUid,
        Integer balanceDiff,
        OperationType operationType
) {
}
