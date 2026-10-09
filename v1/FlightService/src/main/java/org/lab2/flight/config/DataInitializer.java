package org.lab2.flight.config;

import org.lab2.flight.entity.AirportEntity;
import org.lab2.flight.entity.FlightEntity;
import org.lab2.flight.repository.AirportRepository;
import org.lab2.flight.repository.FlightRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AirportRepository airportRepository;
    private final FlightRepository flightRepository;

    public DataInitializer(AirportRepository airportRepository, FlightRepository flightRepository) {
        this.airportRepository = airportRepository;
        this.flightRepository = flightRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (airportRepository.count() > 0 || flightRepository.count() > 0) {
            return;
        }

        AirportEntity sheremetyevo = airportRepository.save(
                new AirportEntity(1, "Шереметьево", "Москва", "Россия"));
        AirportEntity pulkovo = airportRepository.save(
                new AirportEntity(2, "Пулково", "Санкт-Петербург", "Россия"));

        flightRepository.save(new FlightEntity(
                1,
                "AFL031",
                "2021-10-08 20:00",
                pulkovo,
                sheremetyevo,
                1500
        ));
    }
}
