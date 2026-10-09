package org.lab2.gateway.client;

import org.lab2.dto.PrivilegeInfoResponse;
import org.lab2.dto.PrivilegeOperationRequest;
import org.lab2.dto.PrivilegeShortInfo;
import org.lab2.gateway.config.AppProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class BonusClient {

    private final RestClient restClient;

    public BonusClient(RestClient.Builder builder, AppProperties properties) {
        this.restClient = builder.baseUrl(properties.getServices().getBonus()).build();
    }

    public PrivilegeInfoResponse getPrivilege(String username) {
        return restClient.get()
                .uri("/api/v1/privilege")
                .header("X-User-Name", username)
                .retrieve()
                .body(PrivilegeInfoResponse.class);
    }

    public PrivilegeShortInfo applyOperation(String username, PrivilegeOperationRequest request) {
        return restClient.post()
                .uri("/api/v1/privilege")
                .header("X-User-Name", username)
                .body(request)
                .retrieve()
                .body(PrivilegeShortInfo.class);
    }
}
