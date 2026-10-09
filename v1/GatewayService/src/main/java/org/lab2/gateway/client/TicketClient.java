package org.lab2.gateway.client;

import org.lab2.dto.TicketShortInfo;
import org.lab2.gateway.config.AppProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class TicketClient {

    private final RestClient restClient;

    public TicketClient(RestClient.Builder builder, AppProperties properties) {
        this.restClient = builder.baseUrl(properties.getServices().getTicket()).build();
    }

    public List<TicketShortInfo> getTickets(String username) {
        return restClient.get()
                .uri("/api/v1/tickets")
                .header("X-User-Name", username)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public TicketShortInfo getTicket(UUID ticketUid, String username) {
        return restClient.get()
                .uri("/api/v1/tickets/{ticketUid}", ticketUid)
                .header("X-User-Name", username)
                .retrieve()
                .body(TicketShortInfo.class);
    }

    public TicketShortInfo createTicket(String username, String flightNumber, Integer price) {
        return restClient.post()
                .uri("/api/v1/tickets")
                .header("X-User-Name", username)
                .body(Map.of("flightNumber", flightNumber, "price", price))
                .retrieve()
                .body(TicketShortInfo.class);
    }

    public void cancelTicket(UUID ticketUid, String username) {
        restClient.delete()
                .uri("/api/v1/tickets/{ticketUid}", ticketUid)
                .header("X-User-Name", username)
                .retrieve()
                .toBodilessEntity();
    }
}
