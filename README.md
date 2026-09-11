# SupplyConnect – Supplier & Purchase Order Management System

SupplyConnect is a full-stack web application designed to manage the purchase order lifecycle between buyers and suppliers.

The backend provides REST APIs for authentication, purchase order management, supplier confirmation, Advance Shipping Notices (ASN), delivery tracking, and user management.

## Key Features

- JWT-based authentication
- Role-based authorization
- Buyer, Supplier, and Admin roles
- Purchase order creation and tracking
- Supplier purchase order confirmation
- Advance Shipping Notice (ASN) creation
- Partial and complete delivery tracking
- Admin user management
- BCrypt password hashing
- Global exception handling
- Buyer/Supplier ownership validation

## User Roles

### Buyer
- Create purchase orders
- View purchase orders
- Receive deliveries
- Track partial and completed deliveries

### Supplier
- View confirmed purchase orders
- Confirm purchase order items
- Create Advance Shipping Notices (ASN)

### Admin
- Create users
- View users
- Delete users

## Purchase Order Workflow

Buyer creates Purchase Order  
↓  
Supplier confirms PO item  
↓  
Supplier creates ASN  
↓  
Buyer receives delivery  
↓  
Partial / Complete Delivery

## Tech Stack

### Backend
- Java
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA
- Hibernate
- MySQL
- Maven

### Security
- JWT authentication
- Role-based authorization
- BCrypt password hashing
- Ownership validation

## Backend Architecture

The backend follows a layered architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```
## Main Modules

```text
controller/
service/
repository/
entity/
dto/
security/
exception/
```
## API Overview

| Method | Endpoint                                             | Description                  | Role             |
| ------ | ---------------------------------------------------- | ---------------------------- | ---------------- |
| POST   | `/auth/login`                                        | Authenticate user            | Public           |
| GET    | `/purchase-orders`                                   | View purchase orders         | Buyer / Supplier |
| POST   | `/purchase-orders`                                   | Create purchase order        | Buyer            |
| GET    | `/purchase-orders/{poNumber}`                        | View purchase order          | Buyer / Supplier |
| PUT    | `/purchase-orders/{poNumber}/items/{itemId}/confirm` | Confirm PO item              | Supplier         |
| GET    | `/asn`                                               | View confirmed PO items      | Supplier         |
| POST   | `/asn`                                               | Create ASN                   | Supplier         |
| GET    | `/delivery`                                          | View eligible delivery items | Buyer            |
| POST   | `/delivery`                                          | Receive delivery             | Buyer            |
| GET    | `/users`                                             | View users                   | Admin            |
| POST   | `/users`                                             | Create user                  | Admin            |
| DELETE | `/users/{id}`                                        | Delete user                  | Admin            |

## Database

SupplyConnect uses MySQL as its relational database.

Key entities include:

- User
- Role
- Buyer
- Supplier
- Site
- PurchaseOrder
- POItem
- ASN

JPA and Hibernate are used for object-relational mapping and database operations.

## Configuration

Sensitive values are provided through environment variables instead of being stored directly in the source code.

```
spring.datasource.password=${DB_PASSWORD}
jwt.secret=${JWT_SECRET}
```
### Required environment variables

- DB_PASSWORD
- JWT_SECRET

## Running the Backend
### Prerequisites
- Java 22+
- Maven
- MySQL

### Steps

1. Clone the repository.
2. Configure the MySQL database.
3. Set the `DB_PASSWORD` and `JWT_SECRET` environment variables.
4. Start the Spring Boot application.

The backend runs by default on:
```
http://localhost:8080
```
## Frontend

The Angular frontend is maintained in a separate repository:

[SupplyConnect Frontend](https://github.com/Vidhya-08/supplyconnect-frontend)

## Project Status

The backend currently implements the core SupplyConnect workflow:

```text
Authentication
      ↓
Purchase Order
      ↓
PO Confirmation
      ↓
ASN
      ↓
Delivery
```
The project demonstrates REST API development, Spring Security, JWT authentication, role-based authorization, database relationships, and business workflow implementation.