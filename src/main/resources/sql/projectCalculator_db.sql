-- Create database if none exists.
CREATE DATABASE IF NOT EXISTS projectCalculator_db;

-- Set/Use database as default
USE projectCalculator_db;

-- Disable foreign key checks
SET FOREIGN_KEY_CHECKS = 0;

-- Drop tables if they exist in reverse order of foreign key dependencies
DROP TABLE IF EXISTS tasks_user;
DROP TABLE IF EXISTS subproject_user;
DROP TABLE IF EXISTS subproject_tasks;
DROP TABLE IF EXISTS tasks;
DROP TABLE IF EXISTS subProject;
DROP TABLE IF EXISTS project_user;
DROP TABLE IF EXISTS project;
DROP TABLE IF EXISTS users;

-- Enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- Create new tables
CREATE TABLE users
(
    user_id       INTEGER NOT NULL AUTO_INCREMENT,
    email    VARCHAR(50) UNIQUE,
    password VARCHAR(50),
    PRIMARY KEY (user_id)
);

CREATE TABLE project
(
    id    INTEGER NOT NULL AUTO_INCREMENT,
    title VARCHAR(50),
    description VARCHAR(999),
    deadline DATE,
    budget DOUBLE,
    time_spent DOUBLE,
    user_id INTEGER,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE subProject
(
    id          INTEGER NOT NULL AUTO_INCREMENT,
    title       VARCHAR(30),
    deadline    DATE,
    time_spent  DOUBLE,
    project_id  INTEGER,
    PRIMARY KEY (id),
    FOREIGN KEY (project_id) REFERENCES project (id) ON DELETE CASCADE
);

CREATE TABLE tasks
(
    id           INTEGER NOT NULL AUTO_INCREMENT,
    title        VARCHAR(30),
    deadline     DATE,
    time_spent   DOUBLE,
    sub_id       INTEGER,
    PRIMARY KEY (id),
    FOREIGN KEY (sub_id) REFERENCES subProject (id) ON DELETE CASCADE
);

CREATE TABLE project_user
(
    project_id INTEGER,
    user_id    INTEGER,
    PRIMARY KEY (project_id, user_id),
    FOREIGN KEY (project_id) REFERENCES project (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE subproject_user
(
    sub_id   INTEGER,
    user_id  INTEGER,
    PRIMARY KEY (sub_id, user_id),
    FOREIGN KEY (sub_id) REFERENCES subProject (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE tasks_user
(
    task_id  INTEGER,
    user_id  INTEGER,
    PRIMARY KEY (task_id, user_id),
    FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE subproject_tasks
(
    sub_id  INTEGER,
    task_id INTEGER,
    PRIMARY KEY (sub_id, task_id),
    FOREIGN KEY (sub_id) REFERENCES subProject (id) ON DELETE CASCADE,
    FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE
);

-- Insert random test data into users table
INSERT INTO users (email, password)
VALUES ('user1@example.com', 'password1'),
       ('user2@example.com', 'password2'),
       ('user3@example.com', 'password3');

-- Insert random test data into project table
INSERT INTO project (title, description, deadline, budget, time_spent, user_id)
VALUES ('Project 1', 'Description for Project 1', '2023-05-30', 1000.00, 3, 1),
       ('Project 2', 'Description for Project 2', '2023-06-15', 2000.00, 3, 2),
       ('Project 3', 'Description for Project 3', '2023-07-01', 1500.00,3, 3);

-- Insert random test data into subProject table
INSERT INTO subProject (title, deadline,time_spent, project_id, user_id)
VALUES ('Subproject 1', '2023-05-31', 4, 1, 1),
       ('Subproject 2', '2023-06-10', 5, 1, 2),
       ('Subproject 3', '2023-06-20', 4, 2, 2),
       ('Subproject 4', '2023-07-05', 5, 3, 3);

-- Insert random test data into tasks table
INSERT INTO tasks (title, deadline, time_spent, sub_id)
VALUES ('Task 1', '2023-05-31', 3, 1),
       ('Task 2', '2023-06-05', 1, 1),
       ('Task 3', '2023-06-10', 3, 2),
       ('Task 4', '2023-06-15', 5, 2);

-- Insert random test data into project_user table
INSERT INTO project_user (project_id, user_id)
VALUES (1, 1),
       (1, 2),
       (2, 2),
       (3, 3);

-- Insert random test data into subproject_user table
INSERT INTO subproject_user (sub_id, user_id)
VALUES (1, 1),
       (1, 2),
       (2, 1),
       (3, 2),
       (3, 3),
       (4, 3);

-- Insert random test data into tasks_user table
INSERT INTO tasks_user (task_id, user_id)
VALUES (1, 1),
       (1, 2),
       (2, 1),
       (3, 2),
       (4, 3);