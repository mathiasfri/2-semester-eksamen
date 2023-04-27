-- Create database if none exists
CREATE DATABASE IF NOT EXISTS projectCalculator_db;

-- Set/Use database as default
USE projectCalculator_db;

-- Drop tables if they exist
DROP TABLE IF EXISTS users;

-- Create new tables
CREATE TABLE users
(
    user_id       INTEGER NOT NULL AUTO_INCREMENT,
    user_email    VARCHAR(50) UNIQUE,
    user_password VARCHAR(50),
    PRIMARY KEY (user_id)
);