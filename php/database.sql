/*
SQLyog Ultimate v13.1.1 (32 bit)
MySQL - 11.7.2-MariaDB : Database - spaceranger2
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`spaceranger2` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;

USE `spaceranger2`;

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `User` (
  `UserID` VARCHAR(15) NOT NULL,
  `UserName` VARCHAR(30) NOT NULL,
  `UserPassword` VARCHAR(64) NOT NULL,
  `UserLevel` VARCHAR(10) NOT NULL,
  `UserLastLogin` DATETIME DEFAULT NULL,
  `UserActive` TINYINT(1) UNSIGNED NOT NULL DEFAULT 1
) ENGINE=INNODB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

/*Data for the table `user` */

INSERT  INTO `user`(`UserID`,`UserName`,`UserPassword`,`UserLevel`,`UserLastLogin`,`UserActive`) VALUES 
('thamrin','thamrin','6ca13d52ca70c883e0f0bb101e425a89e8624de51db2d2392593af6a84118090','admin','2025-03-25 22:37:02',1),
('infosys','thamrin','6ca13d52ca70c883e0f0bb101e425a89e8624de51db2d2392593af6a84118090','admin',NULL,1);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
