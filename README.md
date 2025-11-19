# CampusCarpool – BPDC Carpool Management System

Desktop Java Application for managing carpooling among BPDC students, built using **Java Swing**, **SQLite**, and **JDBC**.

---

## Table of Contents

1. [Project Overview](#project-overview)
2. [Problem Statement](#problem-statement)
3. [Features](#features)
4. [System Requirements](#system-requirements)
5. [Architecture & Project Structure](#architecture--project-structure)
6. [Installation & Setup](#installation--setup)
7. [Usage](#usage)
8. [Technologies](#technologies)
9. [Contributing](#contributing)
10. [License](#license)

---

## Project Overview

CampusCarpool is a campus-specific platform that connects BPDC student drivers with passengers.
It provides safe, efficient ride posting, searching, booking, rating, reporting, and notifications while encouraging cost-sharing and sustainability.

---

## Problem Statement

BPDC students face challenges such as:

* Limited parking & high costs
* Unavailability of rides at certain times
* Inefficient communication between students

**Solution:** CampusCarpool allows verified BPDC students to post rides, search for rides, book seats, report incidents, and maintain a safe and reliable carpooling environment.

---

## Features

### User Roles

* **Driver** – Post rides, accept/decline requests
* **Passenger** – Search & request rides
* **Dual User** – Acts as Driver & Passenger
* **Admin** – Override warnings/blacklists, remove fake users

### Core Modules

* **Authentication:** Roll number validation, registration, login
* **Driver Module:** Post/edit rides, enable cost-sharing, women-only rides
* **Passenger Module:** Search rides, request/cancel booking, rate drivers
* **Booking Management:** View and manage “My Rides” / “My Bookings”
* **Notifications:** Pop-ups for ride requests, confirmations, warnings
* **Reporting & Safety:** Report users, temporary blacklists, warning system
* **Rating System:** 1–5 star rating affecting user trust score
* **Admin Dashboard:** View reports, warnings, ride history, remove fake users

---

## System Requirements

* **OS:** Windows / Linux / macOS
* **Java:** JDK 8+
* **Database:** SQLite (via JDBC)
* **IDE:** VS Code, IntelliJ, or Eclipse
* **Libraries:** Java Swing (GUI), JDBC (database connectivity)

**OOP Concepts Covered**

* Classes, objects, encapsulation, inheritance, polymorphism, interfaces, overloading & overriding, exception handling

**Java Topics Covered**

* GUI (Swing), file handling, multithreading, event handling, MVC design pattern, Factory & Observer patterns

---

## Architecture & Project Structure

### MVC + Services + Utils

```
src/
 ├─ models/          # Data classes: User, Driver, Passenger, Ride, Booking, Report, Rating, Destination
 ├─ controllers/     # MVC controllers: AuthController, RideController, BookingController, ReportController, ProfileController
 ├─ services/        # Business logic: AuthService, RideService, BookingService, ReportService, ProfileService
 ├─ views/           # Swing GUI windows: LoginView, RegisterView, DashboardView, PostRideView, SearchRideView, RideDetailsView, BookingView, ProfileView
 ├─ utils/           # Helpers: DBConnection, Validators, Constants, NotificationCenter
 ├─ database/        # schema.sql: SQLite tables
 └─ App.java          # Entry point
```

**Flow:** Views → Controllers → Services → Database
**Notifications:** Centralized via `NotificationCenter`

---

## Installation & Setup

1. **Clone the repository**

```bash
git clone https://github.com/anushri-anand/CampusCarpool.git
cd CampusCarpool
```

2. **Open in VS Code**

* Launch VS Code → `File > Open Folder` → select project folder
* Ensure Java extension is installed

3. **Set up SQLite database**

* Navigate to `/database`
* Run `schema.sql` using SQLite CLI or DB Browser for SQLite

4. **Compile & Run**

```bash
javac -d bin src/**/*.java
java -cp bin App
```

* App launches `LoginView` first

---

## Usage

1. Register using a valid BPDC roll number
2. Login as Driver/Passenger/Dual User
3. Post rides, search rides, request bookings
4. Rate rides, report incidents
5. Admin users can manage reports, warnings, and fake accounts

---

## Technologies

* Java 8+
* Java Swing GUI
* SQLite database via JDBC
* MVC design pattern
* Factory & Observer patterns
* Multithreading for background updates

---

## License

This project is for academic purposes and may not be used commercially without permission.

