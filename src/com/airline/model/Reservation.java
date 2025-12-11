package com.airline.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Reservation {
    private String reservationId;
    private String customerName;
    private Flight flight;
    private int seatsBooked;
    private LocalDateTime reservationTime;
    private static int nextId = 1;

    public Reservation(String customerName, Flight flight, int seatsBooked) {
        this.reservationId = "RES" + String.format("%04d", nextId++);
        this.customerName = customerName;
        this.flight = flight;
        this.seatsBooked = seatsBooked;
        this.reservationTime = LocalDateTime.now();
    }

    // Getters
    public String getReservationId() {
        return reservationId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Flight getFlight() {
        return flight;
    }

    public int getSeatsBooked() {
        return seatsBooked;
    }

    public LocalDateTime getReservationTime() {
        return reservationTime;
    }

    @Override
    public String toString() {
        return String.format("Reservation ID: %s\nCustomer: %s\nFlight: %s\nSeats: %d\nBooked on: %s",
                reservationId, customerName, flight.getFlightNumber(), seatsBooked, reservationTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(reservationId, that.reservationId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(reservationId);
    }
}