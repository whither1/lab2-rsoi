package org.lab2.bonus.controller;

import org.lab2.bonus.service.BonusService;
import org.lab2.dto.PrivilegeInfoResponse;
import org.lab2.dto.PrivilegeOperationRequest;
import org.lab2.dto.PrivilegeShortInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/privilege")
public class BonusController {

    private final BonusService bonusService;

    public BonusController(BonusService bonusService) {
        this.bonusService = bonusService;
    }

    @GetMapping
    public PrivilegeInfoResponse getPrivilege(@RequestHeader("X-User-Name") String username) {
        return bonusService.getPrivilege(username);
    }

    @PostMapping
    public PrivilegeShortInfo applyOperation(@RequestHeader("X-User-Name") String username,
                                             @RequestBody PrivilegeOperationRequest request) {
        return bonusService.apply(username, request);
    }
}
