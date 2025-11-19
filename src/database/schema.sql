-- CampusCarpool Database Schema
-- SQLite Database
-- Group 2 Deliverable

-- ========================================
-- 1. USERS TABLE
-- ========================================
-- Stores all users (drivers, passengers, or both)
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    roll_number TEXT UNIQUE NOT NULL,
    email TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL,
    role TEXT NOT NULL CHECK(role IN ('DRIVER', 'PASSENGER', 'BOTH')),
    warnings INTEGER DEFAULT 0,
    blacklist_until TEXT, -- ISO 8601 format: YYYY-MM-DDTHH:MM:SS
    rating REAL DEFAULT 0.0,
    total_ratings INTEGER DEFAULT 0,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP
);

-- ========================================
-- 2. DRIVERS TABLE
-- ========================================
-- Stores driver-specific information
CREATE TABLE IF NOT EXISTS drivers (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER UNIQUE NOT NULL,
    license_number TEXT NOT NULL,
    vehicle_model TEXT NOT NULL,
    vehicle_number TEXT NOT NULL,
    seats_available INTEGER NOT NULL CHECK(seats_available > 0),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ========================================
-- 3. PASSENGERS TABLE
-- ========================================
-- Stores passenger-specific information
CREATE TABLE IF NOT EXISTS passengers (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER UNIQUE NOT NULL,
    preferred_destination TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ========================================
-- 4. DESTINATIONS TABLE
-- ========================================
-- Stores available ride destinations/locations
CREATE TABLE IF NOT EXISTS destinations (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT UNIQUE NOT NULL,
    description TEXT,
    latitude REAL,
    longitude REAL,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP
);

-- ========================================
-- 5. RIDES TABLE
-- ========================================
-- Stores rides posted by drivers
CREATE TABLE IF NOT EXISTS rides (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    driver_id INTEGER NOT NULL,
    driver_name TEXT NOT NULL,
    origin TEXT NOT NULL,
    destination TEXT NOT NULL,
    departure_date TEXT NOT NULL, -- ISO 8601: YYYY-MM-DD
    departure_time TEXT NOT NULL, -- ISO 8601: HH:MM:SS
    seats_available INTEGER NOT NULL CHECK(seats_available >= 0),
    seats_total INTEGER NOT NULL CHECK(seats_total > 0),
    price_per_seat REAL DEFAULT 0.0,
    is_women_only INTEGER DEFAULT 0 CHECK(is_women_only IN (0, 1)), -- 0 = false, 1 = true
    status TEXT DEFAULT 'ACTIVE' CHECK(status IN ('ACTIVE', 'COMPLETED', 'CANCELLED')),
    vehicle_info TEXT,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (driver_id) REFERENCES drivers(user_id) ON DELETE CASCADE
);

-- ========================================
-- 6. BOOKINGS TABLE
-- ========================================
-- Tracks ride bookings by passengers
CREATE TABLE IF NOT EXISTS bookings (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    ride_id INTEGER NOT NULL,
    passenger_id INTEGER NOT NULL,
    passenger_name TEXT NOT NULL,
    status TEXT DEFAULT 'REQUESTED' CHECK(status IN ('REQUESTED', 'CONFIRMED', 'CANCELLED')),
    seats_booked INTEGER DEFAULT 1,
    timestamp TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ride_id) REFERENCES rides(id) ON DELETE CASCADE,
    FOREIGN KEY (passenger_id) REFERENCES passengers(user_id) ON DELETE CASCADE,
    UNIQUE(ride_id, passenger_id) -- Prevent duplicate bookings
);

-- ========================================
-- 7. RATINGS TABLE
-- ========================================
-- Stores ratings given by users
CREATE TABLE IF NOT EXISTS ratings (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    from_user_id INTEGER NOT NULL,
    to_user_id INTEGER NOT NULL,
    ride_id INTEGER NOT NULL,
    score INTEGER NOT NULL CHECK(score >= 1 AND score <= 5),
    comment TEXT,
    timestamp TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (from_user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (to_user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (ride_id) REFERENCES rides(id) ON DELETE CASCADE,
    UNIQUE(from_user_id, to_user_id, ride_id) -- Prevent duplicate ratings for same ride
);

-- ========================================
-- 8. REPORTS TABLE
-- ========================================
-- Stores incident reports for safety/misconduct
CREATE TABLE IF NOT EXISTS reports (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    reported_by INTEGER NOT NULL,
    reported_user INTEGER NOT NULL,
    ride_id INTEGER,
    reason TEXT NOT NULL,
    status TEXT DEFAULT 'PENDING' CHECK(status IN ('PENDING', 'REVIEWED', 'RESOLVED')),
    timestamp TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (reported_by) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (reported_user) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (ride_id) REFERENCES rides(id) ON DELETE SET NULL
);

-- ========================================
-- 9. INDEXES FOR FASTER QUERIES
-- ========================================
-- Speed up common searches
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_roll_number ON users(roll_number);
CREATE INDEX IF NOT EXISTS idx_rides_driver ON rides(driver_id);
CREATE INDEX IF NOT EXISTS idx_rides_destination ON rides(destination);
CREATE INDEX IF NOT EXISTS idx_rides_date ON rides(departure_date);
CREATE INDEX IF NOT EXISTS idx_bookings_ride ON bookings(ride_id);
CREATE INDEX IF NOT EXISTS idx_bookings_passenger ON bookings(passenger_id);
CREATE INDEX IF NOT EXISTS idx_ratings_to_user ON ratings(to_user_id);
CREATE INDEX IF NOT EXISTS idx_reports_reported_user ON reports(reported_user);

-- ========================================
-- 10. SAMPLE DATA (Optional for Testing)
-- ========================================
-- Insert some sample destinations
INSERT OR IGNORE INTO destinations (name, description) VALUES
    ('BPDC Campus', 'Main campus location'),
    ('MG Road', 'Shopping and dining district'),
    ('Indiranagar', 'Residential and commercial area'),
    ('Koramangala', 'Tech hub and cafes'),
    ('Electronic City', 'IT park area'),
    ('Whitefield', 'IT corridor'),
    ('Airport', 'Kempegowda International Airport'),
    ('Railway Station', 'Bangalore City Railway Station');

-- Sample admin user for testing (password: admin123)
INSERT OR IGNORE INTO users (name, roll_number, email, password, role) VALUES
    ('Admin User', 'ADMIN001', 'admin@bpdc.edu', 'admin123', 'BOTH');

-- ========================================
-- END OF SCHEMA
-- ========================================

