-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jan 18, 2022 at 03:16 AM
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

-- --------------------------------------------------------

--
-- Table structure for table `bill`
--

DROP TABLE IF EXISTS `bill`;
CREATE TABLE `bill` (
  `id` int(11) NOT NULL,
  `billNumber` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `contractId` int(11) NOT NULL,
  `roomPrice` int(11) NOT NULL,
  `rentalQuantity` int(11) NOT NULL,
  `total` int(11) DEFAULT NULL,
  `userId` int(11) NOT NULL,
  `createdDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedDate` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '0 = chưa thanh toán; 1 = đã thanh toán'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `bill`
--

INSERT INTO `bill` (`id`, `billNumber`, `contractId`, `roomPrice`, `rentalQuantity`, `total`, `userId`, `createdDate`, `updatedDate`, `status`) VALUES
(5, 'P101_T12/2021', 1, 5000000, 1, 6071000, 1, '2022-01-10 03:49:54', '2022-01-15 17:31:01', 1),
(6, 'P103_T12/2021', 26, 4500000, 1, 6023000, 1, '2022-01-11 13:16:08', '2022-01-13 12:12:12', 1),
(7, 'P101_T1/2022', 31, 5000000, 1, 5575000, 1, '2022-01-12 12:49:54', '2022-01-13 02:29:52', 1),
(8, 'P101_T2/2021', 31, 5000000, 1, 5832000, 1, '2022-01-12 14:12:55', '2022-01-13 10:34:26', 1),
(9, 'P105_T12/2021', 28, 4900000, 1, 5904000, 1, '2022-01-13 10:35:08', '2022-01-13 12:38:00', 1),
(10, 'P103_T1/2022', 26, 4500000, 1, 4793000, 1, '2022-01-13 15:49:32', '2022-01-13 15:49:32', 0),
(11, 'P105_T1/2022', 28, 4900000, 1, 5414000, 1, '2022-01-13 15:51:33', '2022-01-13 15:51:33', 0),
(12, 'P102_T12/2021', 32, 4500000, 1, 5468000, 1, '2022-01-14 11:10:08', '2022-01-14 14:02:09', 1),
(13, 'P101_T3/2022', 31, 5000000, 1, 5550000, 2, '2022-01-15 14:21:08', '2022-01-15 14:21:08', 0),
(14, 'P106_T12/2021', 29, 4500000, 1, 5300000, 1, '2022-01-15 18:17:52', '2022-01-15 18:17:52', 0),
(15, 'P103_T5/2021', 26, 4500000, 1, 3334000, 2, '2022-01-16 04:38:51', '2022-01-16 04:38:52', 0);

-- --------------------------------------------------------

--
-- Table structure for table `billdetail`
--

DROP TABLE IF EXISTS `billdetail`;
CREATE TABLE `billdetail` (
  `billId` int(11) NOT NULL,
  `serviceId` int(11) NOT NULL,
  `price` int(11) NOT NULL,
  `quantity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `billdetail`
--

INSERT INTO `billdetail` (`billId`, `serviceId`, `price`, `quantity`) VALUES
(5, 1, 4000, 199),
(5, 2, 15000, 5),
(5, 3, 100000, 1),
(5, 4, 50000, 1),
(5, 5, 50000, 1),
(6, 1, 4000, 87),
(6, 2, 15000, 65),
(6, 3, 100000, 1),
(6, 4, 50000, 1),
(6, 5, 50000, 1),
(7, 1, 4000, 75),
(7, 2, 15000, 5),
(7, 3, 100000, 1),
(7, 4, 50000, 1),
(7, 5, 50000, 1),
(8, 1, 4000, 113),
(8, 2, 15000, 12),
(8, 3, 100000, 1),
(8, 4, 50000, 1),
(8, 5, 50000, 1),
(9, 1, 4000, 156),
(9, 2, 15000, 12),
(9, 3, 100000, 1),
(9, 4, 50000, 1),
(9, 5, 50000, 1),
(10, 1, 4000, 12),
(10, 2, 15000, 3),
(10, 3, 100000, 1),
(10, 4, 50000, 1),
(10, 5, 50000, 1),
(11, 1, 4000, 11),
(11, 2, 15000, 18),
(11, 3, 100000, 1),
(11, 4, 50000, 1),
(11, 5, 50000, 1),
(12, 1, 4000, 112),
(12, 2, 15000, 18),
(12, 3, 100000, 1),
(12, 4, 50000, 1),
(12, 5, 50000, 1),
(12, 6, 50000, 1),
(13, 1, 4000, 45),
(13, 2, 15000, 8),
(13, 3, 100000, 1),
(13, 4, 50000, 1),
(13, 5, 50000, 1),
(13, 6, 50000, 1),
(14, 1, 4000, 100),
(14, 2, 15000, 10),
(14, 3, 100000, 1),
(14, 4, 50000, 1),
(14, 5, 50000, 1),
(14, 6, 50000, 1),
(15, 1, 4000, -99),
(15, 2, 15000, -68),
(15, 3, 100000, 1),
(15, 4, 50000, 1),
(15, 5, 50000, 1),
(15, 6, 50000, 1);

--
-- Triggers `billdetail`
--
DROP TRIGGER IF EXISTS `updateTotal`;
DELIMITER $$
CREATE TRIGGER `updateTotal` AFTER INSERT ON `billdetail` FOR EACH ROW UPDATE bill SET total = ((SELECT SUM(price * quantity) FROM billdetail WHERE billId = bill.id) + (SELECT (bill.roomPrice * bill.rentalQuantity) WHERE bill.id = new.billId)) WHERE bill.id = new.billId
$$
DELIMITER ;

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
  `updatedDate` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `userId` int(11) NOT NULL,
  `status` int(11) NOT NULL DEFAULT '1',
  `fileLocation` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `contract`
--

INSERT INTO `contract` (`id`, `contractNumber`, `roomId`, `createdDate`, `price`, `updatedDate`, `userId`, `status`, `fileLocation`) VALUES
(1, 'P101_20211231', 13, '2022-01-01 17:00:00', 0, '2022-01-10 13:23:33', 1, 0, NULL),
(2, 'P102_20220101', 14, '2022-01-01 17:00:00', 0, '2022-01-11 10:59:05', 1, 0, NULL),
(26, 'P103_20220104', 15, '2022-01-04 07:24:11', 4500000, NULL, 1, 1, NULL),
(27, 'P104_20220104', 16, '2022-01-04 07:43:07', 4750000, '2022-01-11 10:59:59', 1, 0, NULL),
(28, 'P105_20220104', 17, '2022-01-04 07:47:30', 4900000, NULL, 1, 1, NULL),
(29, 'P106_20220107', 18, '2022-01-06 18:33:07', 4500000, NULL, 1, 1, NULL),
(30, 'P107_20220107', 19, '2022-01-06 18:35:53', 4500000, NULL, 1, 1, NULL),
(31, 'P101_20220111', 13, '2022-01-11 13:55:53', 5000000, NULL, 1, 1, NULL),
(32, 'P102_20220114', 14, '2022-01-14 11:09:05', 4500000, NULL, 1, 1, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `contractdetail`
--

DROP TABLE IF EXISTS `contractdetail`;
CREATE TABLE `contractdetail` (
  `guestId` int(11) NOT NULL,
  `contractId` int(11) NOT NULL,
  `role` int(11) NOT NULL,
  `status` int(11) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `contractdetail`
--

INSERT INTO `contractdetail` (`guestId`, `contractId`, `role`, `status`) VALUES
(1, 1, 1, 0),
(2, 2, 1, 0),
(3, 1, 0, 1),
(23, 26, 1, 1),
(24, 26, 0, 1),
(25, 27, 1, 0),
(26, 28, 1, 1),
(27, 29, 1, 1),
(28, 30, 1, 1),
(29, 31, 1, 1),
(30, 32, 1, 1);

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
  `picture` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `guest`
--

INSERT INTO `guest` (`id`, `fullName`, `citizenId`, `dateOfBirth`, `phone`, `status`, `picture`) VALUES
(1, 'Tùng', '099988776', '2021-12-31', '098765433', 0, NULL),
(2, 'Quân', '908789768', '2002-01-02', '098776574', 0, NULL),
(3, 'Thái', '8979878978', '2022-01-02', '098897896', 0, NULL),
(4, 'Hà', '1111111110', '1987-06-25', '0988144796', 1, NULL),
(23, 'Duc Hiep', '88765678', '2001-11-01', '3454565789', 1, NULL),
(24, 'Duy Khanh', '134594558', '2004-11-01', '3454565789', 1, NULL),
(25, 'Thanh Ha', '12345778', '1987-06-25', '45442414', 0, NULL),
(26, 'Minh Thái', '2132432345', '1999-09-09', '4578769768', 1, NULL),
(27, 'Đức Nghi', '123243455', '2002-03-05', '13242343', 1, NULL),
(28, 'Đạt', '1234567', '2000-01-12', '6543213', 1, NULL),
(29, 'Thanh Hà', '1234567890', '2022-01-06', '1234567890', 1, NULL),
(30, 'Đào Thanh Hà', '0988776589', '1990-01-01', '0987765541', 1, NULL);

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
  `description` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `electricCounter` int(11) NOT NULL,
  `waterCounter` int(11) NOT NULL,
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '1 = còn trống; 0 = đã cho thuê'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `room`
--

INSERT INTO `room` (`id`, `roomNumber`, `price`, `square`, `description`, `electricCounter`, `waterCounter`, `status`) VALUES
(13, 'P101', 5000000, 50, '1 phòng ngủ, 1 phòng khách, \n1 giường 2m, \ntủ quần áo, điều hòa, bếp gas', 478, 39, 0),
(14, 'P102', 4500000, 40, '1 phòng ngủ, \nTủ quần áo, \nĐiều hòa, \nTủ lạnh, \nBếp gas', 214, 319, 0),
(15, 'P103', 4500000, 45, '1 phòng ngủ, 1 phòng khách \nFull nội thất', 0, 0, 0),
(16, 'P104', 4750000, 45, '1 phòng ngủ, 1 phòng khách, \nban công, \nfull nội thất', 0, 0, 1),
(17, 'P105', 4900000, 50, 'Full nội thất', 167, 30, 0),
(18, 'P106', 4500000, 45, '1 phòng ngủ, full đồ', 100, 10, 0),
(19, 'P107', 4500000, 45, '1 phòng ngủ, \nFull đồ, không ban công', 0, 0, 0),
(20, 'P201', 5000000, 50, '1 phòng ngủ, 1 phòng khách\nFull nội thất, ban công', 0, 0, 1),
(21, 'P202', 4500000, 50, '1 phòng ngủ,\nđiều hòa, \ntủ lạnh, \ntủ bếp', 0, 0, 1),
(22, 'P203', 5000000, 45, '1 phòng ngủ có ban công, \nFull nội thất', 0, 0, 1);

-- --------------------------------------------------------

--
-- Table structure for table `service`
--

DROP TABLE IF EXISTS `service`;
CREATE TABLE `service` (
  `id` int(11) NOT NULL,
  `serviceName` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `price` int(11) NOT NULL,
  `unit` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `type` int(11) NOT NULL DEFAULT '1' COMMENT '1 = giá dịch vụ cố định; 0 = giá dịch vụ theo mức sử dụng'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `service`
--

INSERT INTO `service` (`id`, `serviceName`, `price`, `unit`, `type`) VALUES
(1, 'Điện', 4500, 'số', 0),
(2, 'Nước', 15000, 'số', 0),
(3, 'Internet', 100000, 'tháng', 1),
(4, 'Vệ sinh', 20000, 'tháng', 1),
(5, 'An ninh', 50000, 'tháng', 1),
(6, 'Thang máy', 50000, 'tháng', 1);

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
(1, 'admin', 'b9d11b3be25f5a1a7dc8ca04cd310b28', '1234567899', 1, 1),
(2, 'nhanvien1', 'cf565290fe7671a66aa6e7c32a49c20d', '0904833800', 0, 1),
(3, 'nhanvien2', 'd52a6a8468ee1cc886ed8792f3052d11', '0987659439', 0, 1),
(4, 'admin2', 'ee6e203686778eb7f2351059f7fa52d9', '0988144796', 1, 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bill`
--
ALTER TABLE `bill`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `billNumber` (`billNumber`),
  ADD KEY `Bill_fk0` (`contractId`),
  ADD KEY `Bill_fk1` (`userId`);

--
-- Indexes for table `billdetail`
--
ALTER TABLE `billdetail`
  ADD PRIMARY KEY (`billId`,`serviceId`),
  ADD KEY `fk_service` (`serviceId`);

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
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `citizenId` (`citizenId`);

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `contract`
--
ALTER TABLE `contract`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- AUTO_INCREMENT for table `guest`
--
ALTER TABLE `guest`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT for table `room`
--
ALTER TABLE `room`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT for table `service`
--
ALTER TABLE `service`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

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
  ADD CONSTRAINT `fk_bill` FOREIGN KEY (`billId`) REFERENCES `bill` (`id`),
  ADD CONSTRAINT `fk_service` FOREIGN KEY (`serviceId`) REFERENCES `service` (`id`);

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
