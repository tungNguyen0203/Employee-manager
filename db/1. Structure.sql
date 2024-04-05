-- =============================================
-- CREATE STRUCTURE
-- =============================================
DROP DATABASE IF EXISTS final_exam;
CREATE DATABASE final_exam;
USE final_exam;

-- create table 1: department
CREATE TABLE department (
	id 					SMALLINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	`name` 				NVARCHAR(100) UNIQUE KEY NOT NULL,
    member_size			INT UNSIGNED DEFAULT 0,
    manager_id			INT UNSIGNED,
    creator_id			INT UNSIGNED NOT NULL,
    created_date_time	DATETIME NOT NULL DEFAULT NOW(),
    modifier_id			INT UNSIGNED NOT NULL,
    updated_date_time	DATETIME NOT NULL DEFAULT NOW()
);

-- create table 2: account
CREATE TABLE `account` (
	id 								INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    firstname 						NVARCHAR(50) NOT NULL,
    lastname 						NVARCHAR(50) NOT NULL,
	email 							VARCHAR(50) UNIQUE KEY NOT NULL,
	username						VARCHAR(20) UNIQUE KEY NOT NULL,
    `password`						VARCHAR(800) NOT NULL,
	`status`						BIT NOT NULL DEFAULT 0, -- 0: Block, 1: Active
	department_id					SMALLINT UNSIGNED,
    `role`							ENUM('ADMIN', 'EMPLOYEE','MANAGER') DEFAULT 'EMPLOYEE',
    last_change_password_date_time	DATETIME NOT NULL DEFAULT NOW(),
	creator_id						INT UNSIGNED,
    created_date_time				DATETIME NOT NULL DEFAULT NOW(),
    modifier_id						INT UNSIGNED,
    updated_date_time				DATETIME NOT NULL DEFAULT NOW(),
    FOREIGN KEY (department_id) REFERENCES department(id) ON DELETE SET NULL,
    FOREIGN KEY (creator_id) REFERENCES `account`(id),
    FOREIGN KEY (modifier_id) REFERENCES `account`(id)
);

-- create table 3: group
CREATE TABLE `group` (
	id 					SMALLINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    `name`				NVARCHAR(100) UNIQUE KEY NOT NULL,
	member_size 		INT UNSIGNED DEFAULT 0,
    creator_id			INT UNSIGNED NOT NULL,
    created_date_time	DATETIME NOT NULL DEFAULT NOW(),
    modifier_id			INT UNSIGNED NOT NULL,
    updated_date_time	DATETIME NOT NULL DEFAULT NOW(),
    FOREIGN KEY (creator_id) REFERENCES `account`(id),
    FOREIGN KEY (modifier_id) REFERENCES `account`(id)
);
    
-- create table 4: group_account
CREATE TABLE group_account (
	group_id 		SMALLINT UNSIGNED NOT NULL,
	account_id 		INT UNSIGNED NOT NULL,
    join_date_time	DATETIME NOT NULL DEFAULT NOW(),
    PRIMARY KEY(group_id, account_id),
    FOREIGN KEY (group_id) REFERENCES `group`(id) ON DELETE CASCADE,
    FOREIGN KEY (account_id) REFERENCES `account`(id) ON DELETE CASCADE
);

-- create table 5: token
CREATE TABLE `token`(
	id						INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    account_id				INT UNSIGNED NOT NULL,
	`key` 					VARCHAR(100) NOT NULL UNIQUE,
    `type` 					ENUM('REFRESH_TOKEN','REGISTER','FORGOT_PASSWORD') NOT NULL,
    expired_date_time		DATETIME NOT NULL,
    FOREIGN KEY(account_id) REFERENCES `Account`(id) ON DELETE CASCADE
);