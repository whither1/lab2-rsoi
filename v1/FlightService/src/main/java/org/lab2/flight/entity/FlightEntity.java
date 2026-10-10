package org.lab2.flight.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "flight")
public class FlightEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "flight_number", nullable = false, length = 20)
    private String flightNumber;

    @Column(name = "datetime", nullable = false)
    private String datetime;

    @ManyToOne
    @JoinColumn(name = "from_airport_id")
    private AirportEntity fromAirport;

    @ManyToOne
    @JoinColumn(name = "to_airport_id")
    private AirportEntity toAirport;

    @Column(name = "price", nullable = false)
    private Integer price;

    public FlightEntity() {
    }

    public FlightEntity(Integer id, String flightNumber, String datetime,
                        AirportEntity fromAirport, AirportEntity toAirport, Integer price) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.datetime = datetime;
        this.fromAirport = fromAirport;
        this.toAirport = toAirport;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public AirportEntity getFromAirport() {
        return fromAirport;
    }

    public void setFromAirport(AirportEntity fromAirport) {
        this.fromAirport = fromAirport;
    }

    public AirportEntity getToAirport() {
        return toAirport;
    }

    public void setToAirport(AirportEntity toAirport) {
        this.toAirport = toAirport;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
