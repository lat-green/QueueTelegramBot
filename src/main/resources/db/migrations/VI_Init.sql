SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

CREATE TABLE `Client` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `isAdmin` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Position` (
  `id` int(11) NOT NULL,
  `number` int(11) NOT NULL,
  `client` int(11) DEFAULT NULL,
  `queue` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Queue` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

ALTER TABLE `Client`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `Position`
  ADD PRIMARY KEY (`id`),
  ADD KEY `client` (`client`),
  ADD KEY `queue` (`queue`);

ALTER TABLE `Queue`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `Client`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `Position`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `Queue`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `Position`
  ADD CONSTRAINT `Position_ibfk_1` FOREIGN KEY (`client`) REFERENCES `Client` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `Position_ibfk_2` FOREIGN KEY (`queue`) REFERENCES `Queue` (`id`) ON DELETE CASCADE;
COMMIT;
