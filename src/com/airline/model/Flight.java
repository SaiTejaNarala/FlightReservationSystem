package com.airline.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Flight {
    private String flightNumber;
    private String destination;
    private LocalDateTime departureTime;
    private int availableSeats;

    public Flight(String flightNumber, String destination, LocalDateTime departureTime, int availableSeats) {
        this.flightNumber = flightNumber;
        this.destination = destination;
        this.departureTime = departureTime;
        this.availableSeats = availableSeats;
    }

    // Getters and Setters
    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public boolean bookSeats(int seats) {
        if (seats <= 0) {
            return false; // Cannot book 0 or negative seats
        }
        if (seats <= availableSeats) {
            availableSeats -= seats;
            return true;
        }
        return false;
    }

    public void releaseSeats(int seats) {
        availableSeats += seats;
    }

    @Override
    public String toString() {
        return String.format("Flight %s to %s | Departure: %s | Available Seats: %d",
                flightNumber, destination, departureTime, availableSeats);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return Objects.equals(flightNumber, flight.flightNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(flightNumber);
    }
}