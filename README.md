
Concert Booking API
This is a backend system for handling concert ticket reservations with built-in concurrency control and dynamic pricing.


Setup Instructions
Prerequisites
Java 21
Maven
PostgreSQL

1. Database Setup
   Create a PostgreSQL database named *concert_booking_db*.

    Update application.properties with your database username and password.

2. Run the Application
   The project uses Flyway for migrations, so the tables and initial data will be created automatically when you start the app.

Run the following commands in your terminal:

3. API Documentation
   Swagger UI: http://localhost:8080/swagger-ui/index.html

Health Check: http://localhost:8080/actuator/health