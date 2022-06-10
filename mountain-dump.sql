-- --------------------------------------------------------
-- Host:                         34.101.162.83
-- Server version:               8.0.26-google - (Google)
-- Server OS:                    Linux
-- HeidiSQL Version:             11.3.0.6394
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for mountain
CREATE DATABASE IF NOT EXISTS `mountain` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `mountain`;

-- Dumping structure for table mountain.feeds
CREATE TABLE IF NOT EXISTS `feeds` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `upload_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `caption` varchar(50) DEFAULT NULL,
  `photo` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`,`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table mountain.feeds: ~1 rows (approximately)
INSERT INTO `feeds` (`id`, `username`, `upload_date`, `caption`, `photo`) VALUES
	(1, 'user01', '2022-06-10 09:46:21', 'Ayo ke sini!', NULL);

-- Dumping structure for table mountain.mountain_detail
CREATE TABLE IF NOT EXISTS `mountain_detail` (
  `id` int NOT NULL AUTO_INCREMENT,
  `mountain_name` varchar(50) NOT NULL,
  `location` varchar(50) NOT NULL,
  `history` varchar(50) NOT NULL,
  `iconic_site` varchar(50) NOT NULL,
  PRIMARY KEY (`id`,`mountain_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table mountain.mountain_detail: ~1 rows (approximately)
INSERT INTO `mountain_detail` (`id`, `mountain_name`, `location`, `history`, `iconic_site`) VALUES
	(1, 'Tangkuban Perahu', 'Jawa Barat', 'Gunung Tangkuban Parahu terbentuk sekitar 90.000 t', 'Kawah Ratu');

-- Dumping structure for table mountain.mountain_review
CREATE TABLE IF NOT EXISTS `mountain_review` (
  `id` int NOT NULL AUTO_INCREMENT,
  `mountain_name` varchar(50) NOT NULL,
  `username` varchar(50) NOT NULL,
  `rating` int NOT NULL DEFAULT '0',
  `comment` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`,`mountain_name`,`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table mountain.mountain_review: ~2 rows (approximately)
INSERT INTO `mountain_review` (`id`, `mountain_name`, `username`, `rating`, `comment`) VALUES
	(1, 'Bromo', 'user02', 5, 'mantap jiwa'),
	(2, 'Tangkuban Perahu', 'user01', 5, 'Indah sekali.');

-- Dumping structure for table mountain.users
CREATE TABLE IF NOT EXISTS `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `email` varchar(50) DEFAULT NULL,
  `password` varchar(50) NOT NULL,
  PRIMARY KEY (`id`,`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table mountain.users: ~1 rows (approximately)
INSERT INTO `users` (`id`, `username`, `email`, `password`) VALUES
	(1, 'user01', 'bangkitdummy@gmail.com', '12345'),
	(2, 'user02', 'bangkit2gmail.com', '12345');

-- Dumping structure for table mountain.users_detail
CREATE TABLE IF NOT EXISTS `users_detail` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `name` varchar(50) DEFAULT 'USER',
  `photo` varchar(50) DEFAULT NULL,
  `favourite` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`,`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table mountain.users_detail: ~1 rows (approximately)
INSERT INTO `users_detail` (`id`, `username`, `name`, `photo`, `favourite`) VALUES
	(1, 'user01', 'Bambang', NULL, NULL);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
