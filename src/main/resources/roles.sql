INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_PM');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');

        SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

DROP TABLE IF EXISTS `student`;
CREATE TABLE IF NOT EXISTS `student` (
  `student_id` int(11) NOT NULL AUTO_INCREMENT,
  `student_branch` varchar(255) DEFAULT NULL,
  `student_email` varchar(255) DEFAULT NULL,
  `student_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`student_id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=LATIN1;

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`email` VARCHAR(50) NULL DEFAULT NULL,
	`name` VARCHAR(50) NULL DEFAULT NULL,
	`password` VARCHAR(100) NULL DEFAULT NULL,
	`username` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`id`)
)
AUTO_INCREMENT=2
;

CREATE TABLE `roles` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(60) NULL DEFAULT NULL,
	PRIMARY KEY (`id`)
)
AUTO_INCREMENT=4
;