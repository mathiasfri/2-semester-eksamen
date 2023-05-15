-- Create database if none exists
CREATE DATABASE IF NOT EXISTS projectCalculator_db;

-- Set/Use database as default
USE projectCalculator_db;

-- Disable foreign key checks
SET FOREIGN_KEY_CHECKS = 0;

-- Drop tables if they exist in reverse order of foreign key dependencies
DROP TABLE IF EXISTS tasks_user;
DROP TABLE IF EXISTS subproject_user;
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
    user_id    INTEGER,
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

CREATE TABLE project_user
(
    project_id INTEGER,
    user_id    INTEGER,
    PRIMARY KEY (project_id, user_id),
    FOREIGN KEY (project_id) REFERENCES project (project_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE subproject_user
(
    sub_id   INTEGER,
    user_id  INTEGER,
    PRIMARY KEY (sub_id, user_id),
    FOREIGN KEY (sub_id) REFERENCES subProject (sub_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE tasks_user
(
    task_id  INTEGER,
    user_id  INTEGER,
    PRIMARY KEY (task_id, user_id),
    FOREIGN KEY (task_id) REFERENCES tasks (task_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);
-- Set/Use database as default
USE projectCalculator_db;

-- Insert random test data into users table
INSERT INTO users (user_email, user_password)
VALUES ('user1@example.com', 'password1'),
       ('user2@example.com', 'password2'),
       ('user3@example.com', 'password3');

-- Insert random test data into project table
INSERT INTO project (project_title, project_description, project_deadline, project_budget, user_id)
VALUES ('Project 1', 'Description for Project 1', '2023-05-30', 1000.00, 1),
       ('Project 2', 'Description for Project 2', '2023-06-15', 2000.00, 2),
       ('Project 3', 'Description for Project 3', '2023-07-01', 1500.00, 3);

-- Insert random test data into subProject table
INSERT INTO subProject (sub_title, sub_deadline, project_id, user_id)
VALUES ('Subproject 1', '2023-05-31', 1, 1),
       ('Subproject 2', '2023-06-10', 1, 2),
       ('Subproject 3', '2023-06-20', 2, 2),
       ('Subproject 4', '2023-07-05', 3, 3);



CREATE TABLE subProject
(
    sub_id          INTEGER NOT NULL AUTO_INCREMENT,
    sub_title       VARCHAR(30),
    sub_deadline    DATE,
    project_id      INTEGER,
    user_id    INTEGER,
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

CREATE TABLE project_user
(
    project_id INTEGER,
    user_id    INTEGER,
    PRIMARY KEY (project_id, user_id),
    FOREIGN KEY (project_id) REFERENCES project (project_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE subproject_user
(
    sub_id   INTEGER,
    user_id  INTEGER,
    PRIMARY KEY (sub_id, user_id),
    FOREIGN KEY (sub_id) REFERENCES subProject (sub_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE task_user
(
    task_id  INTEGER,
    user_id  INTEGER,
    PRIMARY KEY (task_id, user_id),
    FOREIGN KEY (task_id) REFERENCES tasks (task_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);
-- Set/Use database as default
USE projectCalculator_db;

-- Insert random test data into users table
INSERT INTO users (user_email, user_password)
VALUES ('user1@example.com', 'password1'),
       ('user2@example.com', 'password2'),
       ('user3@example.com', 'password3');

-- Insert random test data into project table
INSERT INTO project (project_title, project_description, project_deadline, project_budget, user_id)
VALUES ('Project 1', 'Description for Project 1', '2023-05-30', 1000.00, 1),
       ('Project 2', 'Description for Project 2', '2023-06-15', 2000.00, 2),
       ('Project 3', 'Description for Project 3', '2023-07-01', 1500.00, 3);

-- Insert random test data into subProject table
INSERT INTO subProject (sub_title, sub_deadline, project_id, user_id)
VALUES ('Subproject 1', '2023-05-31', 1, 1),
       ('Subproject 2', '2023-06-10', 1, 2),
       ('Subproject 3', '2023-06-20', 2, 2),
       ('Subproject 4', '2023-07-05', 3, 3);

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