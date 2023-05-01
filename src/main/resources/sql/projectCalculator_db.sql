-- Create database if none exists
CREATE DATABASE IF NOT EXISTS projectCalculator_db;

-- Set/Use database as default
USE projectCalculator_db;

-- Drop tables if they exist
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS project;
-- Create new tables
CREATE TABLE users
(
    user_id       INTEGER NOT NULL AUTO_INCREMENT,
    user_email    VARCHAR(50) UNIQUE,
    user_password VARCHAR(50),
    PRIMARY KEY (user_id)
);

CREATE TABLE project
(
    project_id    INTEGER NOT NULL AUTO_INCREMENT,
    project_titel VARCHAR(50),
    project_deadline DATE,
    project_budget INTEGER,
    user_id        INTEGER,
    PRIMARY KEY (project_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);