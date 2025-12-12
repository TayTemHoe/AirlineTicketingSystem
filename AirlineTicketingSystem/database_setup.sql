-- Supabase PostgreSQL Database Setup Script
-- Run this script in your Supabase SQL Editor to create the required tables

-- Create flights table
CREATE TABLE IF NOT EXISTS flights (
    flight_id VARCHAR(50) PRIMARY KEY,
    departure_city VARCHAR(100) NOT NULL,
    arrival_city VARCHAR(100) NOT NULL,
    departure_time TIMESTAMP NOT NULL,
    arrival_time TIMESTAMP NOT NULL,
    plane_id VARCHAR(50),
    plane_model VARCHAR(100),
    plane_capacity INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create seats table
CREATE TABLE IF NOT EXISTS seats (
    seat_id VARCHAR(50) NOT NULL,
    flight_id VARCHAR(50) NOT NULL,
    seat_type VARCHAR(20) NOT NULL CHECK (seat_type IN ('ECONOMY', 'BUSINESS')),
    seat_status VARCHAR(20) NOT NULL CHECK (seat_status IN ('AVAILABLE', 'BOOKED', 'BLOCKED')),
    price DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (seat_id, flight_id),
    FOREIGN KEY (flight_id) REFERENCES flights(flight_id) ON DELETE CASCADE
);

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_flights_departure_city ON flights(departure_city);
CREATE INDEX IF NOT EXISTS idx_flights_arrival_city ON flights(arrival_city);
CREATE INDEX IF NOT EXISTS idx_flights_departure_time ON flights(departure_time);
CREATE INDEX IF NOT EXISTS idx_seats_flight_id ON seats(flight_id);
CREATE INDEX IF NOT EXISTS idx_seats_status ON seats(seat_status);

-- Optional: Create a function to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create trigger to automatically update updated_at
CREATE TRIGGER update_flights_updated_at BEFORE UPDATE ON flights
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Sample data (optional - for testing)
-- INSERT INTO flights (flight_id, departure_city, arrival_city, departure_time, arrival_time, plane_id, plane_model, plane_capacity)
-- VALUES 
-- ('F001', 'Malaysia', 'Japan', '2024-12-01 13:00:00', '2024-12-01 20:00:00', 'PL01', 'Boeing 737', 32),
-- ('F002', 'Malaysia', 'Japan', '2024-12-01 14:00:00', '2024-12-01 21:00:00', 'PL02', 'Airbus A320', 36);

