-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jan 03, 2022 at 09:08 AM
-- Server version: 5.7.33
-- PHP Version: 8.1.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ql_chungcu`
--
CREATE DATABASE IF NOT EXISTS `ql_chungcu` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
USE `ql_chungcu`;

-- --------------------------------------------------------

--
-- Table structure for table `bill`
--

DROP TABLE IF EXISTS `bill`;
CREATE TABLE `bill` (
  `id` int(11) NOT NULL,
  `billNumber` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `contractId` int(11) NOT NULL,
  `description` text COLLATE utf8_unicode_ci,
  `period` varchar(7) COLLATE utf8_unicode_ci DEFAULT NULL,
  `rentalPrice` int(11) DEFAULT NULL,
  `quantity` int(11) NOT NULL DEFAULT '1',
  `createdDate` timestamp NOT NULL,
  `userId` int(11) NOT NULL,
  `status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `billdetail`
--

DROP TABLE IF EXISTS `billdetail`;
CREATE TABLE `billdetail` (
  `bill_Id` int(11) NOT NULL,
  `serviceId` int(11) NOT NULL,
  `servicePrice` int(11) NOT NULL,
  `firstCounter` int(11) DEFAULT NULL,
  `lastCounter` int(11) DEFAULT NULL,
  `quantity` int(11) NOT NULL,
  `subTotal` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `contract`
--

DROP TABLE IF EXISTS `contract`;
CREATE TABLE `contract` (
  `id` int(11) NOT NULL,
  `contractNumber` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `roomId` int(11) NOT NULL,
  `createdDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `price` int(11) NOT NULL,
  `updatedDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `fileLocation` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `userId` int(11) NOT NULL,
  `status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `contract`
--

INSERT INTO `contract` (`id`, `contractNumber`, `roomId`, `createdDate`, `price`, `updatedDate`, `fileLocation`, `userId`, `status`) VALUES
(1, 'P101_20211231', 13, '2022-01-01 17:00:00', 0, '2022-01-01 17:00:00', NULL, 1, 1),
(2, 'P102_20220101', 14, '2022-01-01 17:00:00', 0, '2022-01-01 17:00:00', NULL, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `contractdetail`
--

DROP TABLE IF EXISTS `contractdetail`;
CREATE TABLE `contractdetail` (
  `guestId` int(11) NOT NULL,
  `contractId` int(11) NOT NULL,
  `role` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `contractdetail`
--

INSERT INTO `contractdetail` (`guestId`, `contractId`, `role`) VALUES
(1, 1, 1),
(2, 2, 1),
(3, 1, 0);

-- --------------------------------------------------------

--
-- Table structure for table `guest`
--

DROP TABLE IF EXISTS `guest`;
CREATE TABLE `guest` (
  `id` int(11) NOT NULL,
  `fullName` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `citizenId` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `dateOfBirth` date NOT NULL,
  `phone` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `status` int(11) NOT NULL DEFAULT '1',
  `picture` varchar(255) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `guest`
--

INSERT INTO `guest` (`id`, `fullName`, `citizenId`, `dateOfBirth`, `phone`, `status`, `picture`) VALUES
(1, 'Tùng', '099988776', '2021-12-31', '098765433', 1, 'sfsfsfđfgdbdgdfgdf'),
(2, 'Quân', '908789768', '2002-01-02', '098776574', 1, 'none'),
(3, 'Thái', '8979878978', '2022-01-02', '098897896', 0, 'none');

-- --------------------------------------------------------

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
CREATE TABLE `room` (
  `id` int(11) NOT NULL,
  `roomNumber` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `price` int(11) NOT NULL,
  `square` int(11) NOT NULL,
  `description` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `electricCounter` int(11) NOT NULL,
  `waterCounter` int(11) NOT NULL,
  `status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `room`
--

INSERT INTO `room` (`id`, `roomNumber`, `price`, `square`, `description`, `electricCounter`, `waterCounter`, `status`) VALUES
(13, 'P101', 5000000, 50, '1 phòng ngủ, 1 phòng khách, \n1 giường 2m, tủ quần áo, điều hòa, bếp gas', 0, 0, 0),
(14, 'P102', 4500000, 40, '1 phòng ngủ, \nTủ quần áo, \nĐiều hòa, \nTủ lạnh, \nBếp gas', 102, 301, 0),
(15, 'P103', 4500000, 40, '1 phòng ngủ, 1 phòng khách \nFull nội thất', 0, 0, 1),
(16, 'P104', 4750000, 45, '1 phòng ngủ, 1 phòng khách, \nban công, \nfull nội thất', 0, 0, 1);

-- --------------------------------------------------------

--
-- Table structure for table `service`
--

DROP TABLE IF EXISTS `service`;
CREATE TABLE `service` (
  `id` int(11) NOT NULL,
  `serviceName` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `price` int(11) NOT NULL,
  `unit` varchar(10) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `userName` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `phone` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `authority` int(11) NOT NULL,
  `status` int(11) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `userName`, `password`, `phone`, `authority`, `status`) VALUES
(1, 'admin', '123456', '09876565', 1, 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bill`
--
ALTER TABLE `bill`
  ADD PRIMARY KEY (`id`),
  ADD KEY `Bill_fk0` (`contractId`),
  ADD KEY `Bill_fk1` (`userId`);

--
-- Indexes for table `billdetail`
--
ALTER TABLE `billdetail`
  ADD PRIMARY KEY (`bill_Id`,`serviceId`),
  ADD KEY `BillDetail_fk1` (`serviceId`);

--
-- Indexes for table `contract`
--
ALTER TABLE `contract`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `contractNumber` (`contractNumber`),
  ADD KEY `Contract_fk0` (`roomId`),
  ADD KEY `Contract_fk1` (`userId`);

--
-- Indexes for table `contractdetail`
--
ALTER TABLE `contractdetail`
  ADD PRIMARY KEY (`guestId`,`contractId`),
  ADD KEY `ContractDetail_fk1` (`contractId`);

--
-- Indexes for table `guest`
--
ALTER TABLE `guest`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `room`
--
ALTER TABLE `room`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `roomNumber` (`roomNumber`);

--
-- Indexes for table `service`
--
ALTER TABLE `service`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `serviceName` (`serviceName`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `userName` (`userName`),
  ADD UNIQUE KEY `phone` (`phone`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bill`
--
ALTER TABLE `bill`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `contract`
--
ALTER TABLE `contract`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `guest`
--
ALTER TABLE `guest`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `room`
--
ALTER TABLE `room`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `service`
--
ALTER TABLE `service`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `bill`
--
ALTER TABLE `bill`
  ADD CONSTRAINT `Bill_fk0` FOREIGN KEY (`contractId`) REFERENCES `contract` (`id`),
  ADD CONSTRAINT `Bill_fk1` FOREIGN KEY (`userId`) REFERENCES `user` (`id`);

--
-- Constraints for table `billdetail`
--
ALTER TABLE `billdetail`
  ADD CONSTRAINT `BillDetail_fk0` FOREIGN KEY (`bill_Id`) REFERENCES `bill` (`id`),
  ADD CONSTRAINT `BillDetail_fk1` FOREIGN KEY (`serviceId`) REFERENCES `service` (`id`);

--
-- Constraints for table `contract`
--
ALTER TABLE `contract`
  ADD CONSTRAINT `Contract_fk0` FOREIGN KEY (`roomId`) REFERENCES `room` (`id`),
  ADD CONSTRAINT `Contract_fk1` FOREIGN KEY (`userId`) REFERENCES `user` (`id`);

--
-- Constraints for table `contractdetail`
--
ALTER TABLE `contractdetail`
  ADD CONSTRAINT `ContractDetail_fk0` FOREIGN KEY (`guestId`) REFERENCES `guest` (`id`),
  ADD CONSTRAINT `ContractDetail_fk1` FOREIGN KEY (`contractId`) REFERENCES `contract` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
