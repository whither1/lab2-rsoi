package org.lab2.dto;

import java.util.List;

public record UserInfoResponse(
        List<TicketResponse> tickets,
        PrivilegeShortInfo privilege
) {
}
