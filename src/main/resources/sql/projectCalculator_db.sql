-- Create database if none exists
CREATE DATABASE IF NOT EXISTS projectCalculator_db;

-- Set/Use database as default
USE projectCalculator_db;

-- Drop tables if they exist
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS project;
DROP TABLE IF EXISTS subProject;
DROP TABLE IF EXISTS tasks;
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
    project_title VARCHAR(50),
    project_description VARCHAR(999),
    project_deadline DATE,
    project_budget DOUBLE,
    user_id        INTEGER,
    PRIMARY KEY (project_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE subProject
(
    sub_id          INTEGER NOT NULL AUTO_INCREMENT,
    sub_title       VARCHAR(30),
    sub_deadline    DATE,
    project_id      INTEGER,
    PRIMARY KEY (sub_id),
    FOREIGN KEY (project_id) REFERENCES project (project_id)
);

CREATE TABLE tasks
(
    task_id         INTEGER NOT NULL AUTO_INCREMENT,
    task_title      VARCHAR(30),
    task_deadline   DATE,
    time_spent      DOUBLE,
    sub_id          INTEGER,
    PRIMARY KEY (task_id),
    FOREIGN KEY (sub_id) REFERENCES subProject (sub_id)
);