CREATE TABLE `patients` (
  `id` integer PRIMARY KEY,
  `firstName` varchar(255) NOT NULL,
  `lastName` varchar(255) NOT NULL,
  `dateOfBirth` date NOT NULL,
  `gender` varchar(10),
  `address` varchar(255),
  `phoneNumber` varchar(255)
);

CREATE INDEX `patients_index_0` ON `patients` (`lastName`);
