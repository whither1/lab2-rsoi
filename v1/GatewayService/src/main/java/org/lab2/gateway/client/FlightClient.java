package org.lab2.gateway.client;

import org.lab2.dto.FlightResponse;
import org.lab2.dto.PaginationResponse;
import org.lab2.gateway.config.AppProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class FlightClient {

    private final RestClient restClient;

    public FlightClient(RestClient.Builder builder, AppProperties properties) {
        this.restClient = builder.baseUrl(properties.getServices().getFlight()).build();
    }

    public PaginationResponse<FlightResponse> getFlights(int page, int size) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/flights")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public FlightResponse getFlight(String flightNumber) {
        return restClient.get()
                .uri("/api/v1/flights/{flightNumber}", flightNumber)
                .retrieve()
                .body(FlightResponse.class);
    }
}
