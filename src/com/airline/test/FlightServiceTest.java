package com.airline.test;

import com.airline.model.Flight;
import com.airline.model.Reservation;
import com.airline.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlightServiceTest {
    private FlightService flightService;

    @BeforeEach
    void setUp() {
        flightService = new FlightService();
    }

    @Test
    void testSearchFlights() {
        // Test searching for flights to New York on 2024-01-15
        List<Flight> flights = flightService.searchFlights("New York", LocalDate.of(2024, 1, 15));

        assertNotNull(flights);
        assertEquals(2, flights.size()); // Should find AA101 and AA102

        // Verify flight details
        assertEquals("AA101", flights.get(0).getFlightNumber());
        assertEquals("New York", flights.get(0).getDestination());
        assertEquals(150, flights.get(0).getAvailableSeats());
    }

    @Test
    void testSearchFlightsNoResults() {
        // Test searching for non-existent destination
        List<Flight> flights = flightService.searchFlights("Tokyo", LocalDate.of(2024, 1, 15));
        assertTrue(flights.isEmpty());
    }

    @Test
    void testSearchFlightsWrongDate() {
        // Test searching for correct destination but wrong date
        List<Flight> flights = flightService.searchFlights("New York", LocalDate.of(2024, 1, 20));
        assertTrue(flights.isEmpty());
    }

    @Test
    void testBookFlightSuccess() {
        // Get a flight to book
        List<Flight> flights = flightService.searchFlights("New York", LocalDate.of(2024, 1, 15));
        Flight flight = flights.get(0);
        int initialSeats = flight.getAvailableSeats();

        // Book 2 seats
        Reservation reservation = flightService.bookFlight("John Doe", flight, 2);

        assertNotNull(reservation);
        assertEquals("John Doe", reservation.getCustomerName());
        assertEquals(flight, reservation.getFlight());
        assertEquals(2, reservation.getSeatsBooked());

        // Verify seats were deducted
        assertEquals(initialSeats - 2, flight.getAvailableSeats());

        // Verify reservation is in the system
        List<Reservation> reservations = flightService.getCustomerReservations("John Doe");
        assertEquals(1, reservations.size());
        assertEquals(reservation, reservations.get(0));
    }

    @Test
    void testBookFlightInsufficientSeats() {
        List<Flight> flights = flightService.searchFlights("London", LocalDate.of(2024, 1, 16));
        Flight flight = flights.get(0);

        // Try to book more seats than available
        assertThrows(IllegalArgumentException.class, () -> {
            flightService.bookFlight("Jane Doe", flight, 300);
        });

        // Verify no seats were deducted
        assertEquals(200, flight.getAvailableSeats());
    }

    @Test
    void testBookFlightZeroSeats() {
        List<Flight> flights = flightService.searchFlights("Berlin", LocalDate.of(2024, 1, 17));
        Flight flight = flights.get(0);

        assertThrows(IllegalArgumentException.class, () -> {
            flightService.bookFlight("Bob Smith", flight, 0);
        });
    }

    @Test
    void testBookFlightNegativeSeats() {
        List<Flight> flights = flightService.searchFlights("Dubai", LocalDate.of(2024, 1, 15));
        Flight flight = flights.get(0);

        assertThrows(IllegalArgumentException.class, () -> {
            flightService.bookFlight("Alice Johnson", flight, -1);
        });
    }

    @Test
    void testGetCustomerReservations() {
        // Book multiple flights for the same customer
        List<Flight> flights = flightService.getAllFlights();

        flightService.bookFlight("John Doe", flights.get(0), 1);
        flightService.bookFlight("John Doe", flights.get(1), 2);
        flightService.bookFlight("Jane Doe", flights.get(2), 3);

        // Test getting John's reservations
        List<Reservation> johnsReservations = flightService.getCustomerReservations("John Doe");
        assertEquals(2, johnsReservations.size());

        // Test getting Jane's reservations
        List<Reservation> janesReservations = flightService.getCustomerReservations("Jane Doe");
        assertEquals(1, janesReservations.size());

        // Test non-existent customer
        List<Reservation> noReservations = flightService.getCustomerReservations("Nobody");
        assertTrue(noReservations.isEmpty());
    }

    @Test
    void testCancelReservation() {
        // Book a flight
        List<Flight> flights = flightService.searchFlights("Singapore", LocalDate.of(2024, 1, 18));
        Flight flight = flights.get(0);
        int initialSeats = flight.getAvailableSeats();

        Reservation reservation = flightService.bookFlight("John Doe", flight, 5);

        // Cancel the reservation
        boolean success = flightService.cancelReservation(reservation);
        assertTrue(success);

        // Verify seats were returned
        assertEquals(initialSeats, flight.getAvailableSeats());

        // Verify reservation is removed
        List<Reservation> reservations = flightService.getCustomerReservations("John Doe");
        assertTrue(reservations.isEmpty());
    }

    @Test
    void testCancelNonExistentReservation() {
        Reservation fakeReservation = new Reservation("Fake",
                new Flight("XYZ", "Nowhere", LocalDateTime.now(), 100), 10);

        boolean success = flightService.cancelReservation(fakeReservation);
        assertFalse(success);
    }

    @Test
    void testAddFlight() {
        int initialCount = flightService.getAllFlights().size();

        Flight newFlight = new Flight("TEST123", "Tokyo",
                LocalDateTime.of(2024, 2, 1, 12, 0), 150);

        flightService.addFlight(newFlight);

        List<Flight> allFlights = flightService.getAllFlights();
        assertEquals(initialCount + 1, allFlights.size());
        assertTrue(allFlights.contains(newFlight));
    }
}