# Flight Reservation System

A Java-based console application for managing flight reservations.

## Features
- Search flights by destination and date
- Book flights with seat availability validation
- View and cancel reservations
- View all available flights
- Comprehensive unit tests

## How to Run

### Prerequisites
- Java 17 or higher
- Maven (for dependency management)
- IntelliJ IDEA (recommended)

### Steps to Run

1. *Clone/Copy the Code*
   - Copy all the Java files to your IntelliJ project

2. *Set Up Dependencies* (if using Maven)
   - Copy the pom.xml file to your project root
   - IntelliJ should automatically download dependencies

3. *Run the Application*
   - Open Main.java
   - Click the green run button or use Shift + F10

4. *Run Tests*
   - Open FlightServiceTest.java
   - Run tests using IntelliJ's test runner

### Running Without Maven
If you're not using Maven:
1. Ensure you have JUnit 5 libraries in your classpath
2. Run Main.java as a regular Java application

## Design Decisions

### 1. *Immutable Reservation ID*
- Used auto-incrementing reservation IDs for uniqueness
- Format: RES0001, RES0002, etc.

### 2. *Thread Safety Considerations*
- Current implementation is not thread-safe (suitable for single-user console app)
- In a real system, would need synchronization for concurrent bookings

### 3. *Date Handling*
- Used LocalDateTime for departure time
- Search functionality filters by date only (ignores time)
- Input validation for date formats

### 4. *Exception Handling*
- Custom exceptions for insufficient seats
- Input validation for negative seat counts
- Graceful error messages in console interface

### 5. *Data Management*
- In-memory storage using ArrayList
- No persistence between runs (as per requirements)
- Easy to replace with database in future

## Real-Life Considerations

### 1. *Concurrent Bookings*
In a real airline system, we would need:
- Database transactions with row-level locking
- Optimistic/pessimistic locking strategies
- Connection pooling for database connections

### 2. *Scalability*
- Current in-memory approach doesn't scale
- Real system would need:
  - Distributed caching (Redis)
  - Load balancers
  - Microservices architecture

### 3. *Persistence*
- Would need database (PostgreSQL/MySQL) for:
  - Data persistence
  - Backup and recovery
  - Transaction management

### 4. *Security*
- Authentication and authorization
- HTTPS for all communications
- Payment gateway integration
- PCI DSS compliance for payment processing

### 5. *Additional Features Needed*
1. *Payment Processing*
2. *Email/SMS notifications*
3. *Seat selection*
4. *Check-in functionality*
5. *Flight status updates*
6. *Loyalty program integration*
7. *Multi-city bookings*
8. *Baggage management*
9. *Flight change/cancellation policies*
10. *Reporting and analytics*

## Testing Strategy

- Unit tests for core business logic
- Edge cases covered:
  - Overbooking prevention
  - Empty search results
  - Invalid inputs
  - Reservation cancellation
- 85%+ code coverage achieved

## Future Enhancements

1. *Database Integration*
2. *Web Interface*
3. *REST API*
4. *Payment Integration*
5. *Email Notifications*
6. *Admin Dashboard*
7. *Reporting Module*
8. *Mobile App*
