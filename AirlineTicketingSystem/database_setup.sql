-- Supabase PostgreSQL Database Setup Script
-- Run this script in your Supabase SQL Editor to create the required tables

- create staff table
CREATE TABLE IF NOT EXISTS staff (
    staff_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    gender VARCHAR(10),
    password VARCHAR(255) NOT NULL, --need hash
    role VARCHAR(20) DEFAULT 'STAFF', -- 'ADMIN' or 'STAFF'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- create customer table
CREATE TABLE IF NOT EXISTS customer (
    ic_no VARCHAR(50) PRIMARY KEY, 
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20),
    gender VARCHAR(10),
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS plane (
    plane_id VARCHAR(50) PRIMARY KEY,
    model VARCHAR(50),
    capacity INT
);

CREATE TABLE IF NOT EXISTS passenger (
    passport_number VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100)
);

-- 2. Create Tables with Dependencies

CREATE TABLE IF NOT EXISTS flight (
    flight_id VARCHAR(50) PRIMARY KEY,
    depart_country VARCHAR(50),
    arrive_country VARCHAR(50),
    depart_time TIMESTAMP,
    arrive_time TIMESTAMP,
    price_economy DECIMAL(10, 2),
    price_business DECIMAL(10, 2),
    plane_id VARCHAR(50) REFERENCES plane(plane_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS seat (
    seat_id VARCHAR(50) PRIMARY KEY,
    seat_number VARCHAR(10),
    type VARCHAR(20),
    status VARCHAR(20),
    flight_id VARCHAR(50) REFERENCES flight(flight_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS booking (
    booking_id VARCHAR(50) PRIMARY KEY,
    flight_id VARCHAR(50) REFERENCES flight(flight_id),
    -- Based on your requirement: "ic_no in booking is the passport_number"
    ic_no VARCHAR(20) REFERENCES passenger(passport_number), 
    seat_id VARCHAR(50) REFERENCES seat(seat_id),
    status VARCHAR(20),
    booking_date TIMESTAMP,
    total_price DECIMAL(10, 2)
);

CREATE TABLE IF NOT EXISTS payments (
    payment_id SERIAL PRIMARY KEY,
    booking_id VARCHAR(50) REFERENCES booking(booking_id),
    total_amount DECIMAL(10, 2),
    card_last_four VARCHAR(10),
    payment_method VARCHAR(50),
    status VARCHAR(20),
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ticket (
    ticket_id VARCHAR(50) PRIMARY KEY,
    flight_id VARCHAR(50) REFERENCES flight(flight_id),
    seat_id VARCHAR(50) REFERENCES seat(seat_id),
    seat VARCHAR(10), -- Stores seat number directly for easier querying
    passenger_passport_number VARCHAR(20) REFERENCES passenger(passport_number),
    customer_ic_number VARCHAR(20) REFERENCES customer(ic_no)
);

-- ==========================================
-- 1. INSERT MORE PLANES
-- ==========================================
INSERT INTO plane (plane_id, model, capacity) VALUES
('PL001', 'Boeing 737-800', 60),  -- Small plane for testing
('PL002', 'Airbus A320', 180),    -- Standard size
('PL003', 'Boeing 777X', 300),     -- Large international
('PL004', 'Boeing 787 Dreamliner', 240),
('PL005', 'Airbus A350', 280),
('PL006', 'ATR 72', 72)
ON CONFLICT (plane_id) DO NOTHING;

-- ==========================================
-- 2. INSERT MANY FLIGHTS (Various Dates/Routes)
-- ==========================================


INSERT INTO flight (flight_id, depart_country, arrive_country, depart_time, arrive_time, price_economy, price_business, plane_id) 
VALUES 
('MH360', 'Malaysia', 'Japan', NOW() + INTERVAL '1 day', NOW() + INTERVAL '1 day 7 hours', 1500.00, 4500.00, 'PL001'),
('SQ308', 'Singapore', 'London', NOW() + INTERVAL '7 days', NOW() + INTERVAL '7 days 13 hours', 3200.00, 8000.00, 'PL002'),
('AK123', 'Malaysia', 'Thailand', NOW() + INTERVAL '1 day 2 hours', NOW() + INTERVAL '1 day 4 hours', 300.00, 900.00, 'PL001'),
('JL724', 'Malaysia', 'Japan', NOW() + INTERVAL '2 days', NOW() + INTERVAL '2 days 7 hours', 1800.00, 5000.00, 'PL003'),
('BA033', 'London', 'Malaysia', NOW() + INTERVAL '5 days', NOW() + INTERVAL '5 days 13 hours', 3500.00, 8200.00, 'PL005'),
('QR845', 'Malaysia', 'Qatar', NOW() + INTERVAL '10 days', NOW() + INTERVAL '10 days 8 hours', 2200.00, 6000.00, 'PL004'),
('EK346', 'Dubai', 'Malaysia', NOW() + INTERVAL '12 days', NOW() + INTERVAL '12 days 7 hours', 2100.00, 5800.00, 'PL004'),

-- Regional / ASEAN Flights
('TR453', 'Singapore', 'Thailand', NOW() + INTERVAL '3 days', NOW() + INTERVAL '3 days 2 hours', 250.00, 600.00, 'PL002'),
('VN674', 'Vietnam', 'Singapore', NOW() + INTERVAL '4 days', NOW() + INTERVAL '4 days 2 hours', 280.00, 650.00, 'PL002'),
('CGK01', 'Indonesia', 'Malaysia', NOW() + INTERVAL '6 days', NOW() + INTERVAL '6 days 2 hours', 180.00, 450.00, 'PL001'),
('MNL99', 'Philippines', 'Malaysia', NOW() + INTERVAL '8 days', NOW() + INTERVAL '8 days 4 hours', 320.00, 750.00, 'PL002'),

-- Domestic Flights (Testing Short Duration)
('MY101', 'Kuala Lumpur', 'Penang', NOW() + INTERVAL '1 day 10 hours', NOW() + INTERVAL '1 day 11 hours', 89.00, 250.00, 'PL006'),
('MY102', 'Penang', 'Langkawi', NOW() + INTERVAL '2 days 10 hours', NOW() + INTERVAL '2 days 10 hours 40 minutes', 59.00, 150.00, 'PL006'),
('MY103', 'Kuala Lumpur', 'Kuching', NOW() + INTERVAL '3 days 8 hours', NOW() + INTERVAL '3 days 10 hours', 199.00, 499.00, 'PL001')
ON CONFLICT (flight_id) DO NOTHING;

-- ==========================================
-- 3. AUTO-GENERATE SEATS (Advanced SQL)
-- ==========================================
-- This block automatically creates seats (1A to 20D) for the flights listed above.
-- It avoids writing thousands of manual INSERT lines.
-- 1. DROP OLD SEAT TABLE (Because we are changing ID from int to string)
DROP TABLE IF EXISTS seat CASCADE;

-- 2. CREATE NEW SEAT TABLE
CREATE TABLE seat (
    seat_id VARCHAR(50) PRIMARY KEY,  -- NEW: String ID (e.g. "MH370-1A")
    seat_number VARCHAR(10) NOT NULL, -- e.g. "1A"
    type VARCHAR(20) NOT NULL CHECK (type IN ('ECONOMY', 'BUSINESS')),
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    flight_id VARCHAR(50) REFERENCES flight(flight_id) ON DELETE CASCADE
);

-- 3. AUTO-GENERATE SEATS FOR ALL EXISTING FLIGHTS
-- This block loops through every flight, finds its plane capacity, and inserts seats.
DO $$
DECLARE
    f_record RECORD;
    p_capacity INT;
    r INT;
    c INT;
    seat_char CHAR;
    seat_label VARCHAR;
    full_id VARCHAR;
    s_type VARCHAR;
BEGIN
    -- Loop through every flight currently in the database
    FOR f_record IN SELECT f.flight_id, p.capacity 
                    FROM flight f 
                    JOIN plane p ON f.plane_id = p.plane_id 
    LOOP
        -- Determine rows (Capacity / 4)
        FOR r IN 1..(f_record.capacity / 4) LOOP
            FOR c IN 0..3 LOOP
                -- Determine Column Letter (A, B, C, D)
                seat_char := CHR(65 + c);
                seat_label := r || seat_char; -- e.g. "1A"
                
                -- NEW ID FORMAT: FlightID-SeatNum (e.g. "MH370-1A")
                full_id := f_record.flight_id || '-' || seat_label;

                -- Determine Type (Row 1 is Business)
                IF r = 1 THEN s_type := 'BUSINESS'; ELSE s_type := 'ECONOMY'; END IF;

                -- Insert
                INSERT INTO seat (seat_id, seat_number, type, status, flight_id)
                VALUES (full_id, seat_label, s_type, 'AVAILABLE', f_record.flight_id)
                ON CONFLICT (seat_id) DO NOTHING;
            END LOOP;
        END LOOP;
    END LOOP;
END $$;


-- ==========================================
-- 4. UPDATE SOME SEATS TO "BOOKED"
-- ==========================================
-- To test your "Seat Selection" UI (Red boxes)
UPDATE seat SET status = 'BOOKED' WHERE flight_id = 'JL724' AND seat_number IN ('1A', '2C', '5B', '10D');
UPDATE seat SET status = 'BOOKED' WHERE flight_id = 'MY101' AND seat_number IN ('1A', '1B', '1C', '1D'); -- Full First Row