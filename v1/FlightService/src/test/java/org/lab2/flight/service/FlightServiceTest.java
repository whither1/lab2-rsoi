package org.lab2.flight.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lab2.dto.FlightResponse;
import org.lab2.dto.PaginationResponse;
import org.lab2.flight.entity.AirportEntity;
import org.lab2.flight.entity.FlightEntity;
import org.lab2.flight.exception.FlightNotFoundException;
import org.lab2.flight.repository.FlightRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightService flightService;

    @Test
    void findByFlightNumber_buildsDisplayNameFromCityAndName() {
        FlightEntity flight = afl031();
        when(flightRepository.findByFlightNumber("AFL031")).thenReturn(Optional.of(flight));

        FlightResponse response = flightService.findByFlightNumber("AFL031");

        assertThat(response.flightNumber()).isEqualTo("AFL031");
        assertThat(response.fromAirport()).isEqualTo("Санкт-Петербург Пулково");
        assertThat(response.toAirport()).isEqualTo("Москва Шереметьево");
        assertThat(response.date()).isEqualTo("2021-10-08 20:00");
        assertThat(response.price()).isEqualTo(1500);
    }

    @Test
    void findByFlightNumber_throwsWhenMissing() {
        when(flightRepository.findByFlightNumber("NONE")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> flightService.findByFlightNumber("NONE"))
                .isInstanceOf(FlightNotFoundException.class)
                .hasMessageContaining("NONE");
    }

    @Test
    void findFlights_returnsPaginationResponse() {
        Page<FlightEntity> page = new PageImpl<>(List.of(afl031()), PageRequest.of(0, 10), 1);
        when(flightRepository.findAll(any(PageRequest.class))).thenReturn(page);

        PaginationResponse<FlightResponse> response = flightService.findFlights(1, 10);

        assertThat(response.page()).isEqualTo(1);
        assertThat(response.pageSize()).isEqualTo(1);
        assertThat(response.totalElements()).isEqualTo(1);
        assertThat(response.items()).hasSize(1);
        assertThat(response.items().get(0).flightNumber()).isEqualTo("AFL031");
    }

    private FlightEntity afl031() {
        AirportEntity pulkovo = new AirportEntity(2, "Пулково", "Санкт-Петербург", "Россия");
        AirportEntity sheremetyevo = new AirportEntity(1, "Шереметьево", "Москва", "Россия");
        return new FlightEntity(1, "AFL031", "2021-10-08 20:00", pulkovo, sheremetyevo, 1500);
    }
}
