-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 13, 2025 at 05:31 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `logisticautomationg3_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `Id` int(11) NOT NULL,
  `Username` varchar(50) NOT NULL,
  `Password` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`Id`, `Username`, `Password`) VALUES
(1, 'admin', 'admin123'),
(2, 'shamla', '123');

-- --------------------------------------------------------

--
-- Table structure for table `customers`
--

CREATE TABLE `customers` (
  `Id` int(11) NOT NULL,
  `Name` varchar(100) NOT NULL,
  `Contact` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customers`
--

INSERT INTO `customers` (`Id`, `Name`, `Contact`) VALUES
(1, 'John Doe', 'john@email.com'),
(2, 'Jane Smith', 'jane@email.com'),
(3, 'Bob Wilson', 'bob@email.com');

-- --------------------------------------------------------

--
-- Table structure for table `deliveries`
--

CREATE TABLE `deliveries` (
  `Id` int(11) NOT NULL,
  `ShipmentId` int(11) NOT NULL,
  `CustomerId` int(11) NOT NULL,
  `DeliverySlot` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `deliveries`
--

INSERT INTO `deliveries` (`Id`, `ShipmentId`, `CustomerId`, `DeliverySlot`) VALUES
(1, 1, 2, '2025-06-15 11:03:26'),
(2, 2, 1, '2025-06-14 11:03:26'),
(3, 3, 3, '2025-06-16 11:03:26');

-- --------------------------------------------------------

--
-- Table structure for table `deliverypersonnel`
--

CREATE TABLE `deliverypersonnel` (
  `Id` int(11) NOT NULL,
  `Name` varchar(100) NOT NULL,
  `Contact` varchar(50) NOT NULL,
  `Schedule` varchar(100) DEFAULT NULL,
  `AssignedRoutes` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `deliverypersonnel`
--

INSERT INTO `deliverypersonnel` (`Id`, `Name`, `Contact`, `Schedule`, `AssignedRoutes`) VALUES
(1, 'Mike Johnson', 'mike@email.com', 'Morning Shift', 'Route 1'),
(2, 'Sarah Brown', 'sarah@email.com', 'Evening Shift', 'Route 2'),
(3, 'Tom Davis', 'tom@email.com', 'Night Shift', 'Route 3');

-- --------------------------------------------------------

--
-- Table structure for table `shipments`
--

CREATE TABLE `shipments` (
  `Id` int(11) NOT NULL,
  `Sender` varchar(100) NOT NULL,
  `Receiver` varchar(100) NOT NULL,
  `Contents` varchar(200) NOT NULL,
  `Status` varchar(50) NOT NULL,
  `Location` varchar(100) DEFAULT NULL,
  `EstimatedDelivery` datetime DEFAULT NULL,
  `AssignedDriverId` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `shipments`
--

INSERT INTO `shipments` (`Id`, `Sender`, `Receiver`, `Contents`, `Status`, `Location`, `EstimatedDelivery`, `AssignedDriverId`) VALUES
(1, 'John Doe', 'Jane Smith', 'Electronics Package', 'In Transit', 'Warehouse A', '2025-06-15 11:03:26', 1),
(2, 'Bob Wilson', 'John Doe', 'Documents', 'Pending', 'Sorting Center', '2025-06-14 11:03:26', 2),
(3, 'Jane Smith', 'Bob Wilson', 'Clothing Package', 'Delivered', 'Final Destination', '2025-06-16 11:03:00', 3);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`Id`),
  ADD UNIQUE KEY `Username` (`Username`);

--
-- Indexes for table `customers`
--
ALTER TABLE `customers`
  ADD PRIMARY KEY (`Id`);

--
-- Indexes for table `deliveries`
--
ALTER TABLE `deliveries`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `ShipmentId` (`ShipmentId`),
  ADD KEY `CustomerId` (`CustomerId`);

--
-- Indexes for table `deliverypersonnel`
--
ALTER TABLE `deliverypersonnel`
  ADD PRIMARY KEY (`Id`);

--
-- Indexes for table `shipments`
--
ALTER TABLE `shipments`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `AssignedDriverId` (`AssignedDriverId`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin`
--
ALTER TABLE `admin`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `customers`
--
ALTER TABLE `customers`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `deliveries`
--
ALTER TABLE `deliveries`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `deliverypersonnel`
--
ALTER TABLE `deliverypersonnel`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `shipments`
--
ALTER TABLE `shipments`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `deliveries`
--
ALTER TABLE `deliveries`
  ADD CONSTRAINT `deliveries_ibfk_1` FOREIGN KEY (`ShipmentId`) REFERENCES `shipments` (`Id`),
  ADD CONSTRAINT `deliveries_ibfk_2` FOREIGN KEY (`CustomerId`) REFERENCES `customers` (`Id`);

--
-- Constraints for table `shipments`
--
ALTER TABLE `shipments`
  ADD CONSTRAINT `shipments_ibfk_1` FOREIGN KEY (`AssignedDriverId`) REFERENCES `deliverypersonnel` (`Id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
