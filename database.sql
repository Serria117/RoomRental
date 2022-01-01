CREATE TABLE `Room` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`roomNumber` varchar(10) NOT NULL UNIQUE,
	`price` INT NOT NULL,
	`square` INT NOT NULL,
	`description` varchar(255) NOT NULL,
	`electricCounter` INT NOT NULL,
	`waterCounter` INT NOT NULL,
	`status` INT NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `Guest` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`fullName` varchar(50) NOT NULL,
	`citizenId` varchar(20) NOT NULL,
	`dateOfBirth` DATE NOT NULL,
	`phone` varchar(20) NOT NULL,
	`status` INT NOT NULL DEFAULT '1',
	`picture` varchar(255) NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `Contract` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`contractNumber` varchar(20) NOT NULL UNIQUE,
	`roomId` INT NOT NULL,
	`createdDate` DATE NOT NULL,
	`updatedDate` DATE NOT NULL,
	`fileLocation` varchar(255),
	`userId` INT NOT NULL,
	`status` INT NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `Bill` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`billNumber` varchar(20) NOT NULL,
	`contractId` INT NOT NULL,
	`description` TEXT,
	`period` varchar(7),
	`rentalPrice` INT,
	`quantity` INT NOT NULL DEFAULT '1',
	`createdDate` TIMESTAMP NOT NULL,
	`userId` INT NOT NULL,
	`status` INT NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `Service` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`serviceName` varchar(50) NOT NULL UNIQUE,
	`price` INT NOT NULL,
	`unit` varchar(10) NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `BillDetail` (
	`bill_Id` INT NOT NULL,
	`serviceId` INT NOT NULL,
	`servicePrice` INT NOT NULL,
	`firstCounter` INT,
	`lastCounter` INT,
	`quantity` INT NOT NULL,
	`subTotal` INT NOT NULL,
	PRIMARY KEY (`bill_Id`,`serviceId`)
);

CREATE TABLE `User` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`userName` varchar(20) NOT NULL UNIQUE,
	`password` varchar(100) NOT NULL,
	`phone` varchar(20) NOT NULL UNIQUE,
	`authority` INT NOT NULL,
	`status` INT NOT NULL UNIQUE DEFAULT '1',
	PRIMARY KEY (`id`)
);

CREATE TABLE `ContractDetail` (
	`guestId` INT NOT NULL,
	`contractId` INT NOT NULL,
	`role` INT NOT NULL,
	PRIMARY KEY (`guestId`,`contractId`)
);

ALTER TABLE `Contract` ADD CONSTRAINT `Contract_fk0` FOREIGN KEY (`roomId`) REFERENCES `Room`(`id`);

ALTER TABLE `Contract` ADD CONSTRAINT `Contract_fk1` FOREIGN KEY (`userId`) REFERENCES `User`(`id`);

ALTER TABLE `Bill` ADD CONSTRAINT `Bill_fk0` FOREIGN KEY (`contractId`) REFERENCES `Contract`(`id`);

ALTER TABLE `Bill` ADD CONSTRAINT `Bill_fk1` FOREIGN KEY (`userId`) REFERENCES `User`(`id`);

ALTER TABLE `BillDetail` ADD CONSTRAINT `BillDetail_fk0` FOREIGN KEY (`bill_Id`) REFERENCES `Bill`(`id`);

ALTER TABLE `BillDetail` ADD CONSTRAINT `BillDetail_fk1` FOREIGN KEY (`serviceId`) REFERENCES `Service`(`id`);

ALTER TABLE `ContractDetail` ADD CONSTRAINT `ContractDetail_fk0` FOREIGN KEY (`guestId`) REFERENCES `Guest`(`id`);

ALTER TABLE `ContractDetail` ADD CONSTRAINT `ContractDetail_fk1` FOREIGN KEY (`contractId`) REFERENCES `Contract`(`id`);









