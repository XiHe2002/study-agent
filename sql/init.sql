CREATE DATABASE IF NOT EXISTS study_agent
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE study_agent;


CREATE TABLE IF NOT EXISTS knowledge_document (
                                                  id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                                  original_filename VARCHAR(255) NOT NULL,
    stored_filename VARCHAR(500) NOT NULL,
    file_size BIGINT,
    content_type VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    file_hash VARCHAR(64) NOT NULL
    );


CREATE TABLE IF NOT EXISTS learning_record (
                                               id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                               topic VARCHAR(255) NOT NULL,
    description TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
    );


CREATE TABLE IF NOT EXISTS quiz_session (
                                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                            topic VARCHAR(255) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
    );


CREATE TABLE IF NOT EXISTS quiz_question (
                                             id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                             quiz_id BIGINT NOT NULL,
                                             question_text TEXT NOT NULL,
                                             reference_answer TEXT NOT NULL,
                                             question_order INT NOT NULL,
                                             created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS quiz_attempt (
                                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                            question_id BIGINT NOT NULL,
                                            user_answer TEXT NOT NULL,
                                            score INT,
                                            feedback TEXT,
                                            weak_points TEXT,
                                            weak_point_tags TEXT,
                                            created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);