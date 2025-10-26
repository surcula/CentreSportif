-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Oct 26, 2025 at 07:38 PM
-- Server version: 9.1.0
-- PHP Version: 8.3.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `centresportif`
--

-- --------------------------------------------------------

--
-- Table structure for table `addresses`
--

DROP TABLE IF EXISTS `addresses`;
CREATE TABLE IF NOT EXISTS `addresses` (
                                           `id` int NOT NULL AUTO_INCREMENT,
                                           `street_name` varchar(255) NOT NULL,
                                           `street_number` int NOT NULL,
                                           `box_number` decimal(15,2) DEFAULT NULL,
                                           `city_id` int DEFAULT NULL,
                                           `is_active` tinyint(1) NOT NULL DEFAULT '1',
                                           PRIMARY KEY (`id`),
                                           KEY `city_id` (`city_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `addresses`
--

INSERT INTO `addresses` (`id`, `street_name`, `street_number`, `box_number`, `city_id`, `is_active`) VALUES
    (1, 'ffff', 14, 0.00, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `authorizations`
--

DROP TABLE IF EXISTS `authorizations`;
CREATE TABLE IF NOT EXISTS `authorizations` (
                                                `id` int NOT NULL AUTO_INCREMENT,
                                                `authorizations_name` varchar(255) NOT NULL,
                                                `is_active` tinyint(1) NOT NULL DEFAULT '1',
                                                PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `cities`
--

DROP TABLE IF EXISTS `cities`;
CREATE TABLE IF NOT EXISTS `cities` (
                                        `id` int NOT NULL AUTO_INCREMENT,
                                        `city_name` varchar(255) NOT NULL,
                                        `zip_code` int NOT NULL,
                                        `country_id` int DEFAULT NULL,
                                        `is_active` tinyint(1) NOT NULL DEFAULT '1',
                                        PRIMARY KEY (`id`),
                                        KEY `country_id` (`country_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `cities`
--

INSERT INTO `cities` (`id`, `city_name`, `zip_code`, `country_id`, `is_active`) VALUES
    (1, 'Charleroi', 6000, 7, 1);

-- --------------------------------------------------------

--
-- Table structure for table `closures`
--

DROP TABLE IF EXISTS `closures`;
CREATE TABLE IF NOT EXISTS `closures` (
                                          `id` int NOT NULL AUTO_INCREMENT,
                                          `start_date` date NOT NULL,
                                          `end_date` date NOT NULL,
                                          `sports_field_id` int DEFAULT NULL,
                                          `is_active` tinyint(1) NOT NULL DEFAULT '1',
                                          PRIMARY KEY (`id`),
                                          KEY `sports_field_id` (`sports_field_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `countries`
--

DROP TABLE IF EXISTS `countries`;
CREATE TABLE IF NOT EXISTS `countries` (
                                           `id` int NOT NULL AUTO_INCREMENT,
                                           `country_name` varchar(255) NOT NULL,
                                           `iso_alpha3` varchar(255) NOT NULL,
                                           `is_active` tinyint NOT NULL DEFAULT '1',
                                           PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `countries`
--

INSERT INTO `countries` (`id`, `country_name`, `iso_alpha3`, `is_active`) VALUES
    (7, 'Belgique', 'BEL', 1);

-- --------------------------------------------------------

--
-- Table structure for table `discounts`
--

DROP TABLE IF EXISTS `discounts`;
CREATE TABLE IF NOT EXISTS `discounts` (
                                           `id` int NOT NULL AUTO_INCREMENT,
                                           `discount_name` varchar(255) NOT NULL,
                                           `percent` decimal(15,2) NOT NULL,
                                           `is_active` tinyint(1) NOT NULL DEFAULT '1',
                                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `events`
--

DROP TABLE IF EXISTS `events`;
CREATE TABLE IF NOT EXISTS `events` (
                                        `id` int NOT NULL AUTO_INCREMENT,
                                        `event_name` varchar(255) NOT NULL,
                                        `begin_date_hour` date NOT NULL,
                                        `end_date_hour` date NOT NULL,
                                        `info` varchar(255) NOT NULL,
                                        `picture` varchar(255) NOT NULL,
                                        `is_active` tinyint(1) NOT NULL DEFAULT '1',
                                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `fields`
--

DROP TABLE IF EXISTS `fields`;
CREATE TABLE IF NOT EXISTS `fields` (
                                        `id` int NOT NULL AUTO_INCREMENT,
                                        `field_name` varchar(255) NOT NULL,
                                        `hall_id` int DEFAULT NULL,
                                        `is_active` tinyint(1) NOT NULL DEFAULT '1',
                                        PRIMARY KEY (`id`),
                                        KEY `hall_id` (`hall_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `fields`
--

INSERT INTO `fields` (`id`, `field_name`, `hall_id`, `is_active`) VALUES
                                                                      (13, 'Terrain de tennis 1', 11, 1),
                                                                      (14, 'Terrain de tennis 2', 11, 1),
                                                                      (15, 'Terrain de tennis 3', 11, 1),
                                                                      (16, 'Terrain de tennis 4', 11, 1),
                                                                      (17, 'terrain de squash 1', 12, 0),
                                                                      (18, 'terrain de volleybal 1', 13, 1);

-- --------------------------------------------------------

--
-- Table structure for table `halls`
--

DROP TABLE IF EXISTS `halls`;
CREATE TABLE IF NOT EXISTS `halls` (
                                       `id` int NOT NULL AUTO_INCREMENT,
                                       `hall_name` varchar(255) NOT NULL,
                                       `width` decimal(5,2) NOT NULL,
                                       `length` decimal(5,2) NOT NULL,
                                       `height` decimal(5,2) NOT NULL,
                                       `is_active` tinyint(1) NOT NULL DEFAULT '1',
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `halls`
--

INSERT INTO `halls` (`id`, `hall_name`, `width`, `length`, `height`, `is_active`) VALUES
                                                                                      (11, 'Hall 1', 500.00, 500.00, 12.00, 1),
                                                                                      (12, 'Hall 2', 600.00, 600.00, 16.00, 1),
                                                                                      (13, 'hall 3', 411.00, 555.00, 13.00, 1);

-- --------------------------------------------------------

--
-- Table structure for table `historicals_sports_prices`
--

DROP TABLE IF EXISTS `historicals_sports_prices`;
CREATE TABLE IF NOT EXISTS `historicals_sports_prices` (
                                                           `id` int NOT NULL AUTO_INCREMENT,
                                                           `begin_date` date NOT NULL,
                                                           `price` decimal(10,2) NOT NULL,
                                                           `sport_id` int DEFAULT NULL,
                                                           PRIMARY KEY (`id`),
                                                           KEY `sport_id` (`sport_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
CREATE TABLE IF NOT EXISTS `orders` (
                                        `id` int NOT NULL AUTO_INCREMENT,
                                        `date_order` datetime NOT NULL,
                                        `total_price` decimal(15,2) NOT NULL,
                                        `tva` decimal(15,2) NOT NULL,
                                        `user_id` int DEFAULT NULL,
                                        `is_active` tinyint(1) NOT NULL DEFAULT '1',
                                        `status` enum('on_hold','confirmed') NOT NULL,
                                        PRIMARY KEY (`id`),
                                        KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `orders_discounts`
--

DROP TABLE IF EXISTS `orders_discounts`;
CREATE TABLE IF NOT EXISTS `orders_discounts` (
                                                  `id` int NOT NULL AUTO_INCREMENT,
                                                  `created_at` datetime NOT NULL,
                                                  `updated_at` datetime NOT NULL,
                                                  `order_id` int NOT NULL,
                                                  `discount_id` int NOT NULL,
                                                  `is_active` tinyint(1) NOT NULL DEFAULT '1',
                                                  PRIMARY KEY (`id`),
                                                  KEY `discount_id` (`discount_id`),
                                                  KEY `orders_discounts_ibfk_1` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `orders_subscriptions`
--

DROP TABLE IF EXISTS `orders_subscriptions`;
CREATE TABLE IF NOT EXISTS `orders_subscriptions` (
                                                      `id` int NOT NULL AUTO_INCREMENT,
                                                      `created_at` datetime NOT NULL,
                                                      `updated_at` datetime NOT NULL,
                                                      `subscription_id` int NOT NULL,
                                                      `order_id` int NOT NULL,
                                                      `is_active` tinyint(1) NOT NULL DEFAULT '1',
                                                      PRIMARY KEY (`id`),
                                                      KEY `order_id` (`order_id`),
                                                      KEY `orders_subscriptions_ibfk_1` (`subscription_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `reservations`
--

DROP TABLE IF EXISTS `reservations`;
CREATE TABLE IF NOT EXISTS `reservations` (
                                              `id` int NOT NULL AUTO_INCREMENT,
                                              `reservation_name` varchar(255) NOT NULL,
                                              `start_date_reservation` datetime NOT NULL,
                                              `end_date_reservation` datetime NOT NULL,
                                              `price` decimal(15,2) NOT NULL,
                                              `tva` decimal(15,2) NOT NULL,
                                              `statut` enum('1','2','3') NOT NULL,
                                              `user_id` int DEFAULT NULL,
                                              `sports_field_id` int DEFAULT NULL,
                                              `is_active` tinyint(1) NOT NULL DEFAULT '1',
                                              PRIMARY KEY (`id`),
                                              KEY `user_id` (`user_id`),
                                              KEY `sports_field_id` (`sports_field_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
CREATE TABLE IF NOT EXISTS `roles` (
                                       `id` int NOT NULL AUTO_INCREMENT,
                                       `role_name` varchar(255) NOT NULL,
                                       `is_active` tinyint(1) NOT NULL DEFAULT '1',
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `roles`
--

INSERT INTO `roles` (`id`, `role_name`, `is_active`) VALUES
                                                         (1, 'ADMIN', 1),
                                                         (2, 'SECRETARY', 1),
                                                         (3, 'BARMAN', 1),
                                                         (4, 'USER', 1);

-- --------------------------------------------------------

--
-- Table structure for table `roles_authorizations`
--

DROP TABLE IF EXISTS `roles_authorizations`;
CREATE TABLE IF NOT EXISTS `roles_authorizations` (
                                                      `id` int NOT NULL AUTO_INCREMENT,
                                                      `created_at` datetime NOT NULL,
                                                      `updated_at` datetime NOT NULL,
                                                      `role_id` int NOT NULL,
                                                      `authorization_id` int NOT NULL,
                                                      `is_active` tinyint(1) NOT NULL DEFAULT '1',
                                                      PRIMARY KEY (`id`),
                                                      KEY `authorization_id` (`authorization_id`),
                                                      KEY `roles_authorizations_ibfk_1` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `sports`
--

DROP TABLE IF EXISTS `sports`;
CREATE TABLE IF NOT EXISTS `sports` (
                                        `id` int NOT NULL AUTO_INCREMENT,
                                        `sport_name` varchar(255) NOT NULL,
                                        `is_active` tinyint(1) NOT NULL DEFAULT '1',
                                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `sports`
--

INSERT INTO `sports` (`id`, `sport_name`, `is_active`) VALUES
                                                           (4, 'Tennis', 1),
                                                           (5, 'Judo', 0),
                                                           (6, 'Futsall', 1),
                                                           (7, 'Badmington', 1),
                                                           (8, 'Basket', 1),
                                                           (9, 'Volleyball', 1);

-- --------------------------------------------------------

--
-- Table structure for table `sports_fields`
--

DROP TABLE IF EXISTS `sports_fields`;
CREATE TABLE IF NOT EXISTS `sports_fields` (
                                               `id` int NOT NULL AUTO_INCREMENT,
                                               `sport_id` int DEFAULT NULL,
                                               `field_id` int DEFAULT NULL,
                                               `start_time` datetime NOT NULL,
                                               `end_time` datetime NOT NULL,
                                               `date_start` datetime NOT NULL,
                                               `day` int NOT NULL,
                                               `price` double(10,2) NOT NULL,
                                               `session_duration` int NOT NULL,
                                               `is_active` tinyint(1) NOT NULL DEFAULT '1',
                                               PRIMARY KEY (`id`),
                                               KEY `sport_id` (`sport_id`),
                                               KEY `field_id` (`field_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `sports_fields`
--

INSERT INTO `sports_fields` (`id`, `sport_id`, `field_id`, `start_time`, `end_time`, `date_start`, `day`, `price`, `session_duration`, `is_active`) VALUES
                                                                                                                                                        (1, 4, 13, '2025-10-26 17:21:49', '2025-10-26 17:21:49', '2025-10-26 17:21:49', 1, 37.00, 15, 1),
                                                                                                                                                        (2, 8, 15, '2025-10-26 18:16:14', '2025-10-26 18:16:14', '2025-10-26 18:16:14', 4, 7.00, 12, 0),
                                                                                                                                                        (3, 4, 13, '2025-10-26 18:53:08', '2025-10-26 18:53:08', '2025-10-26 18:53:08', 2, 37.00, 15, 1),
                                                                                                                                                        (4, 4, 13, '2025-10-26 18:53:45', '2025-10-26 18:53:45', '2025-10-26 18:53:45', 3, 37.00, 15, 1),
                                                                                                                                                        (5, 4, 13, '2025-10-26 18:54:02', '2025-10-26 18:54:02', '2025-10-26 18:54:02', 6, 35.00, 30, 1),
                                                                                                                                                        (6, 9, 17, '2025-10-26 19:13:58', '2025-10-26 19:13:58', '2025-10-26 19:13:58', 1, 26.00, 15, 1),
                                                                                                                                                        (7, 9, 18, '2025-10-26 19:14:33', '2025-10-26 19:14:33', '2025-10-26 19:14:33', 7, 50.00, 60, 1);

-- --------------------------------------------------------

--
-- Table structure for table `subscriptions`
--

DROP TABLE IF EXISTS `subscriptions`;
CREATE TABLE IF NOT EXISTS `subscriptions` (
                                               `id` int NOT NULL AUTO_INCREMENT,
                                               `subscription_name` varchar(255) NOT NULL,
                                               `token` int NOT NULL,
                                               `sport_id` int DEFAULT NULL,
                                               `is_active` tinyint(1) NOT NULL DEFAULT '1',
                                               PRIMARY KEY (`id`),
                                               KEY `sport_id` (`sport_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
                                       `id` int NOT NULL AUTO_INCREMENT,
                                       `first_name` varchar(90) NOT NULL,
                                       `last_name` varchar(90) NOT NULL,
                                       `email` varchar(255) NOT NULL,
                                       `birthdate` date NOT NULL,
                                       `password` varchar(64) NOT NULL,
                                       `phone` varchar(255) NOT NULL,
                                       `gender` enum('M','F','Autre') NOT NULL,
                                       `civilite` enum('M','Mme','Dr','Autre') NOT NULL,
                                       `role_id` int DEFAULT '1',
                                       `address_id` int DEFAULT NULL,
                                       `event_id` int DEFAULT NULL,
                                       `is_active` tinyint(1) NOT NULL DEFAULT '1',
                                       `is_blacklist` tinyint(1) NOT NULL DEFAULT '0',
                                       PRIMARY KEY (`id`),
                                       UNIQUE KEY `email` (`email`),
                                       KEY `role_id` (`role_id`),
                                       KEY `address_id` (`address_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `first_name`, `last_name`, `email`, `birthdate`, `password`, `phone`, `gender`, `civilite`, `role_id`, `address_id`, `event_id`, `is_active`, `is_blacklist`) VALUES
    (1, 'sarah', 'sarah', 'sarah@test.be', '1980-10-10', '07480fb9e85b9396af06f006cf1c95024af2531c65fb505cfbd0add1e2f31573', '0497848586', 'F', 'Mme', 1, 1, 0, 1, 0);

-- --------------------------------------------------------

--
-- Table structure for table `users_subscriptions`
--

DROP TABLE IF EXISTS `users_subscriptions`;
CREATE TABLE IF NOT EXISTS `users_subscriptions` (
                                                     `id` int NOT NULL AUTO_INCREMENT,
                                                     `user_id` int DEFAULT NULL,
                                                     `subscription_id` int DEFAULT NULL,
                                                     `start_date` date NOT NULL,
                                                     `end_date` date NOT NULL,
                                                     `quantity_max` int NOT NULL,
                                                     `is_active` tinyint(1) NOT NULL DEFAULT '1',
                                                     PRIMARY KEY (`id`),
                                                     KEY `user_id` (`user_id`),
                                                     KEY `subscription_id` (`subscription_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `addresses`
--
ALTER TABLE `addresses`
    ADD CONSTRAINT `addresses_ibfk_1` FOREIGN KEY (`city_id`) REFERENCES `cities` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `cities`
--
ALTER TABLE `cities`
    ADD CONSTRAINT `cities_ibfk_1` FOREIGN KEY (`country_id`) REFERENCES `countries` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `closures`
--
ALTER TABLE `closures`
    ADD CONSTRAINT `closures_ibfk_1` FOREIGN KEY (`sports_field_id`) REFERENCES `sports_fields` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `fields`
--
ALTER TABLE `fields`
    ADD CONSTRAINT `fields_ibfk_1` FOREIGN KEY (`hall_id`) REFERENCES `halls` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `historicals_sports_prices`
--
ALTER TABLE `historicals_sports_prices`
    ADD CONSTRAINT `historicals_sports_prices_ibfk_1` FOREIGN KEY (`sport_id`) REFERENCES `sports` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
    ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `orders_discounts`
--
ALTER TABLE `orders_discounts`
    ADD CONSTRAINT `orders_discounts_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    ADD CONSTRAINT `orders_discounts_ibfk_2` FOREIGN KEY (`discount_id`) REFERENCES `discounts` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `orders_subscriptions`
--
ALTER TABLE `orders_subscriptions`
    ADD CONSTRAINT `orders_subscriptions_ibfk_1` FOREIGN KEY (`subscription_id`) REFERENCES `subscriptions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    ADD CONSTRAINT `orders_subscriptions_ibfk_2` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `reservations`
--
ALTER TABLE `reservations`
    ADD CONSTRAINT `reservations_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
    ADD CONSTRAINT `reservations_ibfk_2` FOREIGN KEY (`sports_field_id`) REFERENCES `sports_fields` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `roles_authorizations`
--
ALTER TABLE `roles_authorizations`
    ADD CONSTRAINT `roles_authorizations_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    ADD CONSTRAINT `roles_authorizations_ibfk_2` FOREIGN KEY (`authorization_id`) REFERENCES `authorizations` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `sports_fields`
--
ALTER TABLE `sports_fields`
    ADD CONSTRAINT `sports_fields_ibfk_1` FOREIGN KEY (`sport_id`) REFERENCES `sports` (`id`),
    ADD CONSTRAINT `sports_fields_ibfk_2` FOREIGN KEY (`field_id`) REFERENCES `fields` (`id`);

--
-- Constraints for table `subscriptions`
--
ALTER TABLE `subscriptions`
    ADD CONSTRAINT `subscriptions_ibfk_1` FOREIGN KEY (`sport_id`) REFERENCES `sports` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `users`
--
ALTER TABLE `users`
    ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
    ADD CONSTRAINT `users_ibfk_2` FOREIGN KEY (`address_id`) REFERENCES `addresses` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `users_subscriptions`
--
ALTER TABLE `users_subscriptions`
    ADD CONSTRAINT `users_subscriptions_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    ADD CONSTRAINT `users_subscriptions_ibfk_2` FOREIGN KEY (`subscription_id`) REFERENCES `subscriptions` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
