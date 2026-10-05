# Library Management System

A full-stack Library Management System built with **Angular** for the frontend and **Spring Boot** for the backend. The application provides secure authentication, book management, and library reservation functionality with role-based access control.

## 🚀 Features

### Authentication & Security
- User registration and login
- JWT-based authentication
- Password encryption using BCrypt
- Role-based authorization
- Separate ADMIN and USER access
- Protected API endpoints
- CORS configuration

### Book Management
- View available books
- View individual book details
- Admin can add books
- Admin can update books
- Admin can delete books
- Manage available copies
- ISBN duplicate validation
- Book availability tracking
- Soft delete support

### Reservation Management
- Users can reserve books
- Track active and returned reservations
- Automatically update available book copies
- Return reserved books
- Users can view their own reservations
- Admin can view all reservations
- Prevent users from accessing other users' reservations

### Validation & Error Handling
- Request validation using Jakarta Validation
- Global exception handling
- Meaningful HTTP status codes
- Custom exceptions for business rules
- Duplicate resource handling
- Resource-not-found handling
- Access-denied handling

### Testing
- Unit tests using JUnit and Mockito
- Service-layer testing
- **28/28 service tests passing**

## 🛠️ Technologies Used

### Frontend
- Angular
- TypeScript
- HTML
- CSS
- Angular Services
- Angular Routing

### Backend
- Java 17
- Spring Boot 4.0.2
- Spring Web MVC
- Spring Data JPA
- Spring Security
- Spring Validation
- JWT
- Maven

### Database
- MySQL

### API Documentation
- OpenAPI / Swagger UI

### Testing
- JUnit
- Mockito

### Tools
- Visual Studio Code
- Eclipse
- Git
- GitHub




API Overview
Authentication
Method
Endpoint
Access
POST
/api/auth/register
Public
POST
/api/auth/login
Public
Books
Method
Endpoint
Access
GET
/api/books
USER / ADMIN
GET
/api/books/{id}
USER / ADMIN
POST
/api/books/add
ADMIN
PUT
/api/books/{id}
ADMIN
DELETE
/api/books/delete/{id}
ADMIN
Reservations
Method
Endpoint
Access
POST
/api/reservations
USER / ADMIN
GET
/api/users/{id}/reservations
USER / ADMIN
GET
/api/admin/reservations
ADMIN
PUT
/api/reservations/{reservationId}/return
USER / ADMIN


Running the Project
Backend
Navigate to the backend:
cd librarysystembackend

Run the Spring Boot application using Maven:
mvn spring-boot:run
The backend runs on:
http://localhost:8080

Frontend
Navigate to the frontend:
cd library-frontend
Install dependencies:
npm install
Start the Angular development server:
ng serve
The frontend runs on:
http://localhost:4200

🗄️ Database Configuration
The backend uses MySQL.
Create a MySQL database and configure the local database connection in:
librarysystembackend/src/main/resources/application.properties


API Documentation
Once the backend is running, Swagger UI is available at:
http://localhost:8080/swagger-ui/index.html
OpenAPI documentation is available at:
http://localhost:8080/v3/api-docs


Testing
The backend includes unit tests for the service layer using JUnit and Mockito.
Current service test coverage includes:
BookService
UserService
ReservationService
AuthService
Current result: 28/28 tests passing.
Run the tests with:
mvn test


Security
The application uses:
Spring Security
JWT authentication
BCrypt password hashing
Role-based authorization
Stateless session management
Protected REST endpoints
CORS configuration
Global exception handling
Sensitive configuration files are excluded from Git using .gitignore.


Project Purpose
This project was developed to demonstrate a complete full-stack application using modern Java backend development and Angular frontend development.
It demonstrates:
REST API development
Spring Boot architecture
Database integration
Authentication and authorization
JWT security
Role-based access control
Exception handling
Validation
Unit testing
Angular frontend development
Git and GitHub workflow