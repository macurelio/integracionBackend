-- Database creation script for User API
-- This script represents the structure used in the H2 in-memory database.

-- Drop tables if they exist to allow re-runs
DROP TABLE IF EXISTS phones;
DROP TABLE IF EXISTS users;

-- Create users table
CREATE TABLE users (
    id UUID PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created TIMESTAMP NOT NULL,
    modified TIMESTAMP,
    last_login TIMESTAMP,
    token VARCHAR(500),
    is_active BOOLEAN DEFAULT TRUE
);

-- Create phones table
CREATE TABLE phones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    number VARCHAR(50),
    citycode VARCHAR(10),
    contrycode VARCHAR(10),
    user_id UUID,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Indices for performance
CREATE INDEX idx_user_email ON users(email);
