-- phpMyAdmin SQL Dump
-- version 5.1.1deb5ubuntu1
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Mar 01, 2024 at 10:39 AM
-- Server version: 10.6.16-MariaDB-0ubuntu0.22.04.1
-- PHP Version: 8.1.2-1ubuntu2.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `BikeRentalDB`
--
CREATE DATABASE IF NOT EXISTS `BikeRentalDB` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `BikeRentalDB`;

-- --------------------------------------------------------

--
-- Table structure for table `bikes`
--

CREATE TABLE `bikes` (
  `bike_id` int(11) NOT NULL,
  `station_id` int(11) NOT NULL,
  `bike_model` varchar(255) NOT NULL,
  `bike_type` enum('CITY','SPORT','MOUNTAIN','CARGO','E_BIKE') NOT NULL,
  `bike_price` double NOT NULL,
  `bike_status` enum('AVAILABLE','RENTED','MAINTENANCE','CHARGING') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bikes`
--

INSERT INTO `bikes` (`bike_id`, `station_id`, `bike_model`, `bike_type`, `bike_price`, `bike_status`) VALUES
(1, 2, 'Scott Scale', 'MOUNTAIN', 25, 'RENTED'),
(2, 4, 'Gazelle Ultimate C8+', 'E_BIKE', 29, 'AVAILABLE'),
(3, 5, 'Electra Townie Go!', 'E_BIKE', 25, 'AVAILABLE'),
(4, 2, 'Surly Big Dummy', 'CARGO', 27, 'AVAILABLE'),
(5, 2, 'Schwinn Wayfarer', 'E_BIKE', 24, 'AVAILABLE'),
(6, 4, 'Riese & Müller Load', 'E_BIKE', 26, 'RENTED'),
(7, 1, 'Specialized Sirrus', 'SPORT', 26, 'MAINTENANCE'),
(8, 3, 'Trek FX 3', 'CITY', 23, 'RENTED'),
(10, 3, 'Cannondale Trail Neo', 'E_BIKE', 24, 'CHARGING'),
(11, 5, 'Canyon Endurace', 'SPORT', 26, 'RENTED'),
(12, 4, 'Tondar Tr5', 'SPORT', 32, 'AVAILABLE'),
(13, 5, 'Ultimate C8+', 'CITY', 27, 'AVAILABLE'),
(14, 4, 'Tern GSD', 'MOUNTAIN', 34, 'AVAILABLE'),
(16, 4, 'BMC Alpenchallenge', 'CITY', 31, 'AVAILABLE'),
(17, 3, 'Tonder X8', 'E_BIKE', 32, 'AVAILABLE'),
(19, 5, 'Gido Bido', 'CARGO', 29, 'MAINTENANCE'),
(20, 1, 'Trek Domane SLR 9', 'CITY', 32, 'AVAILABLE'),
(21, 2, 'Cannondale Quick', 'CITY', 28, 'AVAILABLE'),
(22, 2, 'BMC Timemachine', 'SPORT', 31, 'AVAILABLE');

-- --------------------------------------------------------

--
-- Table structure for table `bike_rentals`
--

CREATE TABLE `bike_rentals` (
  `rental_id` int(11) NOT NULL,
  `bike_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `start_station_id` int(11) NOT NULL,
  `start_date` date NOT NULL,
  `return_date` date DEFAULT NULL,
  `rent_price` decimal(10,2) DEFAULT NULL,
  `is_returned` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bike_rentals`
--

INSERT INTO `bike_rentals` (`rental_id`, `bike_id`, `user_id`, `start_station_id`, `start_date`, `return_date`, `rent_price`, `is_returned`) VALUES
(13, 1, 2, 2, '2024-02-28', '2024-02-29', '50.00', 1),
(14, 8, 2, 3, '2024-02-29', '2024-02-29', '23.00', 1),
(15, 14, 1, 3, '2024-02-28', '2024-02-29', '68.00', 1),
(16, 11, 1, 5, '2024-02-29', '2024-03-02', '78.00', 0),
(18, 6, 1, 4, '2024-03-01', '2024-03-02', '52.00', 0);

-- --------------------------------------------------------

--
-- Table structure for table `stations`
--

CREATE TABLE `stations` (
  `station_id` int(11) NOT NULL,
  `station_address` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `stations`
--

INSERT INTO `stations` (`station_id`, `station_address`) VALUES
(1, 'Ginnheimer Landstr.14, 60430 Frankfurt'),
(2, 'Schwarzwaldstr. 4A, 65779 Kelkheim'),
(3, 'Debusweg 36, Königstein im Taunus'),
(4, 'Praunheimer 49, 60488 Frankfurt'),
(5, 'Rebstockbad, 60486 Frankfurt');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  `user_phone` varchar(255) NOT NULL,
  `user_email` varchar(255) NOT NULL,
  `role` enum('user','admin') NOT NULL DEFAULT 'user',
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `user_name`, `user_phone`, `user_email`, `role`, `password`) VALUES
(1, 'Missy Cooper', '01793421125', 'Missy-C@gmail.com', 'user', '635241'),
(2, 'Lilium', '015247859696', 'amir95@gmail.com', 'user', '654321'),
(4, 'Rosa', '01758963256', 'RosaKeyka@gmail.com', 'user', '7458963'),
(5, 'lawana', '01789621453', 'lawana-detkova@gmail.com', 'admin', '968574'),
(8, 'Miley Twigg', '017985412853', 'M-twigg32@gmail.com', 'user', '741852'),
(10, 'Luka Pour', '01789589641', 'Luka-Pr@gmail.de', 'user', '124578'),
(11, 'Monique', '01589658741', 'MQ20-albay@gmail.de', 'user', '1563248'),
(13, 'Rasta Vand', '017896325698', 'R-vand@gmail.com', 'user', '147852'),
(14, 'Amir Houshang', '01785698080', 'houshangAmir@gmail.com', 'user', '123456'),
(15, 'admin', '01785214569', 'admin@gmail.com', 'admin', '123456'),
(16, 'user', '01785474757', 'user@gmail.com', 'user', '654321'),
(17, 'Tara-hzd', '12378925633', 'T2024@gmail.de', 'user', '123987'),
(18, 'Narges', '01789654178', 'Nari90@gmail.com', 'user', '123987'),
(20, 'Marina ', '01579685743', 'Mrn23@gmail.de', 'user', '963852'),
(21, 'Silvana', '01794562020', 'S-Stoilkova@gmai.com', 'user', '147258'),
(22, 'Donya', '09125892145', 'D-Agashte@gmail.com', 'user', '159874');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bikes`
--
ALTER TABLE `bikes`
  ADD PRIMARY KEY (`bike_id`),
  ADD KEY `fk_station_id` (`station_id`);

--
-- Indexes for table `bike_rentals`
--
ALTER TABLE `bike_rentals`
  ADD PRIMARY KEY (`rental_id`) USING BTREE,
  ADD KEY `fk_user_id` (`user_id`),
  ADD KEY `fk_start_location` (`start_station_id`),
  ADD KEY `fk_bike_id` (`bike_id`);

--
-- Indexes for table `stations`
--
ALTER TABLE `stations`
  ADD PRIMARY KEY (`station_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `user_email` (`user_email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bikes`
--
ALTER TABLE `bikes`
  MODIFY `bike_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT for table `bike_rentals`
--
ALTER TABLE `bike_rentals`
  MODIFY `rental_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT for table `stations`
--
ALTER TABLE `stations`
  MODIFY `station_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `bikes`
--
ALTER TABLE `bikes`
  ADD CONSTRAINT `fk_station_id` FOREIGN KEY (`station_id`) REFERENCES `stations` (`station_id`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Constraints for table `bike_rentals`
--
ALTER TABLE `bike_rentals`
  ADD CONSTRAINT `fk_bike_id` FOREIGN KEY (`bike_id`) REFERENCES `bikes` (`bike_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_start_location` FOREIGN KEY (`start_station_id`) REFERENCES `stations` (`station_id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE NO ACTION ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
