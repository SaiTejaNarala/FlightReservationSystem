package com.airline.main;

import com.airline.model.Flight;
import com.airline.model.Reservation;
import com.airline.service.FlightService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static FlightService flightService = new FlightService();
    private static Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        System.out.println("✈️ Welcome to Airline Reservation System ✈️");
        System.out.println("=".repeat(50));

        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    searchFlights();
                    break;
                case 2:
                    bookFlight();
                    break;
                case 3:
                    viewReservations();
                    break;
                case 4:
                    viewAllFlights();
                    break;
                case 5:
                    cancelReservation();
                    break;
                case 6:
                    System.out.println("Thank you for using Airline Reservation System. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\nMAIN MENU");
        System.out.println("1. Search Flights");
        System.out.println("2. Book a Flight");
        System.out.println("3. View My Reservations");
        System.out.println("4. View All Available Flights");
        System.out.println("5. Cancel a Reservation");
        System.out.println("6. Exit");
        System.out.println("-".repeat(50));
    }

    private static void searchFlights() {
        System.out.println("\nSEARCH FLIGHTS");
        System.out.println("-".repeat(30));

        System.out.print("Enter destination: ");
        String destination = scanner.nextLine();

        LocalDate date = getDateInput("Enter travel date (yyyy-MM-dd): ");

        List<Flight> availableFlights = flightService.searchFlights(destination, date);

        if (availableFlights.isEmpty()) {
            System.out.println("No flights available for " + destination + " on " + date);
        } else {
            System.out.println("\nAvailable Flights:");
            System.out.println("-".repeat(80));
            for (int i = 0; i < availableFlights.size(); i++) {
                System.out.println((i + 1) + ". " + availableFlights.get(i));
            }
        }
    }

    private static void bookFlight() {
        System.out.println("\nBOOK A FLIGHT");
        System.out.println("-".repeat(30));

        System.out.print("Enter your name: ");
        String customerName = scanner.nextLine();

        viewAllFlights();

        List<Flight> allFlights = flightService.getAllFlights();
        if (allFlights.isEmpty()) {
            System.out.println("No flights available for booking.");
            return;
        }

        int flightIndex = getIntInput("Select flight number (1-" + allFlights.size() + "): ") - 1;

        if (flightIndex < 0 || flightIndex >= allFlights.size()) {
            System.out.println("Invalid flight selection.");
            return;
        }

        Flight selectedFlight = allFlights.get(flightIndex);

        System.out.println("Selected: " + selectedFlight);

        int seats = getIntInput("Enter number of seats to book: ");

        try {
            Reservation reservation = flightService.bookFlight(customerName, selectedFlight, seats);
            System.out.println("\n✅ Booking Successful!");
            System.out.println(reservation);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Booking failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ An error occurred: " + e.getMessage());
        }
    }

    private static void viewReservations() {
        System.out.println("\nVIEW RESERVATIONS");
        System.out.println("-".repeat(30));

        System.out.print("Enter your name: ");
        String customerName = scanner.nextLine();

        List<Reservation> reservations = flightService.getCustomerReservations(customerName);

        if (reservations.isEmpty()) {
            System.out.println("No reservations found for " + customerName);
        } else {
            System.out.println("\nYour Reservations:");
            System.out.println("=".repeat(80));
            for (Reservation reservation : reservations) {
                System.out.println(reservation);
                System.out.println("-".repeat(80));
            }
        }
    }

    private static void viewAllFlights() {
        System.out.println("\nALL AVAILABLE FLIGHTS");
        System.out.println("-".repeat(80));

        List<Flight> allFlights = flightService.getAllFlights();

        if (allFlights.isEmpty()) {
            System.out.println("No flights available.");
        } else {
            for (int i = 0; i < allFlights.size(); i++) {
                System.out.println((i + 1) + ". " + allFlights.get(i));
            }
        }
    }

    private static void cancelReservation() {
        System.out.println("\nCANCEL RESERVATION");
        System.out.println("-".repeat(30));

        System.out.print("Enter your name: ");
        String customerName = scanner.nextLine();

        List<Reservation> reservations = flightService.getCustomerReservations(customerName);

        if (reservations.isEmpty()) {
            System.out.println("No reservations found for " + customerName);
            return;
        }

        System.out.println("\nYour Reservations:");
        for (int i = 0; i < reservations.size(); i++) {
            System.out.println((i + 1) + ". " + reservations.get(i));
            System.out.println("-".repeat(60));
        }

        int reservationIndex = getIntInput("Select reservation to cancel (1-" + reservations.size() + "): ") - 1;

        if (reservationIndex < 0 || reservationIndex >= reservations.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        Reservation reservationToCancel = reservations.get(reservationIndex);

        System.out.print("Are you sure you want to cancel this reservation? (yes/no): ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("yes")) {
            boolean success = flightService.cancelReservation(reservationToCancel);
            if (success) {
                System.out.println("✅ Reservation cancelled successfully!");
            } else {
                System.out.println("❌ Failed to cancel reservation.");
            }
        } else {
            System.out.println("Cancellation aborted.");
        }
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static LocalDate getDateInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String dateStr = scanner.nextLine();
                return LocalDate.parse(dateStr, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd (e.g., 2024-01-15)");
            }
        }
    }
}