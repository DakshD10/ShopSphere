-- =============================================================================
-- Experiment 7: Java Database Connectivity (JDBC) with MySQL
-- Database Schema Definition & Seed Data
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 1. Primary Proposed Lab Schema: `java_lab` database with `students` table
--    Used by `Main.java` for full CRUD operations (Create, Read, Update, Delete)
-- -----------------------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS java_lab 
  CHARACTER SET utf8mb4 
  COLLATE utf8mb4_unicode_ci;

USE java_lab;

CREATE TABLE IF NOT EXISTS students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(160) NOT NULL UNIQUE,
    course VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Seed initial records for testing
INSERT INTO students (name, email, course) VALUES
('Rahul Sharma', 'rahul@example.com', 'Computer Science'),
('Amit Verma', 'amit@example.com', 'Information Technology'),
('Neha Patel', 'neha@example.com', 'Electronics & Communication')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- -----------------------------------------------------------------------------
-- 2. Professor PDF Companion Schema: `college` database with `student` table
--    Matches the step-by-step walkthrough in "JDBC ( Java Database Connectivity ).pdf"
--    Used by `JdbcDemo.java`
-- -----------------------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS college 
  CHARACTER SET utf8mb4 
  COLLATE utf8mb4_unicode_ci;

USE college;

CREATE TABLE IF NOT EXISTS student (
    id INT PRIMARY KEY,
    name VARCHAR(30) NOT NULL,
    branch VARCHAR(20) NOT NULL
);

INSERT INTO student (id, name, branch) VALUES
(101, 'Rahul', 'CSE'),
(102, 'Amit', 'IT'),
(103, 'Neha', 'ECE')
ON DUPLICATE KEY UPDATE name = VALUES(name);
