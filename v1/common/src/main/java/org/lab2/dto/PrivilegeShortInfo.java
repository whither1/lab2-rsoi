package org.lab2.dto;

import org.lab2.enums.PrivilegeStatus;

public record PrivilegeShortInfo(
        Integer balance,
        PrivilegeStatus status
) {
}
