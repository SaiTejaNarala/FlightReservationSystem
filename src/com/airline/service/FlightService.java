package com.airline.service;

import com.airline.model.Flight;
import com.airline.model.Reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FlightService {
    private List<Flight> flights;
    private List<Reservation> reservations;

    public FlightService() {
        this.flights = new ArrayList<>();
        this.reservations = new ArrayList<>();
        initializeSampleFlights();
    }

    private void initializeSampleFlights() {
        flights.add(new Flight("AA101", "New York", LocalDateTime.of(2024, 1, 15, 10, 30), 150));
        flights.add(new Flight("AA102", "New York", LocalDateTime.of(2024, 1, 15, 18, 45), 120));
        flights.add(new Flight("BA201", "London", LocalDateTime.of(2024, 1, 16, 14, 20), 200));
        flights.add(new Flight("LH301", "Berlin", LocalDateTime.of(2024, 1, 17, 9, 15), 80));
        flights.add(new Flight("EK401", "Dubai", LocalDateTime.of(2024, 1, 15, 22, 10), 100));
        flights.add(new Flight("SQ501", "Singapore", LocalDateTime.of(2024, 1, 18, 16, 45), 90));
    }

    public List<Flight> searchFlights(String destination, LocalDate date) {
        return flights.stream()
                .filter(flight -> flight.getDestination().equalsIgnoreCase(destination))
                .filter(flight -> flight.getDepartureTime().toLocalDate().equals(date))
                .filter(flight -> flight.getAvailableSeats() > 0)
                .collect(Collectors.toList());
    }

    public Reservation bookFlight(String customerName, Flight flight, int seats) {
        // Validate seat count first
        if (seats <= 0) {
            throw new IllegalArgumentException("Number of seats must be at least 1. Requested: " + seats);
        }

        // Check if seats are available
        if (flight.getAvailableSeats() < seats) {
            throw new IllegalArgumentException("Not enough seats available. Available: " + flight.getAvailableSeats() + ", Requested: " + seats);
        }

        // Book the seats
        boolean success = flight.bookSeats(seats);
        if (!success) {
            throw new IllegalStateException("Failed to book seats. Please try again.");
        }

        // Create reservation
        Reservation reservation = new Reservation(customerName, flight, seats);
        reservations.add(reservation);

        return reservation;
    }

    public List<Reservation> getCustomerReservations(String customerName) {
        return reservations.stream()
                .filter(reservation -> reservation.getCustomerName().equalsIgnoreCase(customerName))
                .collect(Collectors.toList());
    }

    public boolean cancelReservation(Reservation reservation) {
        if (reservations.remove(reservation)) {
            // Release the booked seats
            reservation.getFlight().releaseSeats(reservation.getSeatsBooked());
            return true;
        }
        return false;
    }

    public void addFlight(Flight flight) {
        flights.add(flight);
    }

    public List<Flight> getAllFlights() {
        return new ArrayList<>(flights);
    }

    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations);
    }
}