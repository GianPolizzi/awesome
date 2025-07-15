CREATE DATABASE  IF NOT EXISTS `awesomedb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `awesomedb`;
-- MySQL dump 10.13  Distrib 8.0.40, for macos14 (arm64)
--
-- Host: localhost    Database: awesomedb
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `aw_order`
--

DROP TABLE IF EXISTS `aw_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `aw_order` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `currency` varchar(255) NOT NULL,
  `customer_address` varchar(255) NOT NULL,
  `customer_name` varchar(255) NOT NULL,
  `customer_phone` varchar(255) NOT NULL,
  `insert_order_date` datetime(6) NOT NULL,
  `order_status` enum('IN_PROCESS','PENDING','READY') NOT NULL,
  `total_price` double NOT NULL,
  `total_quantity` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aw_order`
--

LOCK TABLES `aw_order` WRITE;
/*!40000 ALTER TABLE `aw_order` DISABLE KEYS */;
INSERT INTO `aw_order` VALUES (1,'EUR','Via Fanti D\'Italia 29, Catania','Polizzi','3279876543','2025-07-15 17:52:35.018227','IN_PROCESS',16,2),(2,'EUR','Via Garibaldi 1, Catania','Scaravilli','3279876543','2025-07-15 17:55:03.595589','PENDING',33,4),(3,'EUR','Via Brera 1, Catania','Rossi','3279876543','2025-07-15 17:55:57.174385','PENDING',9.5,1);
/*!40000 ALTER TABLE `aw_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aw_order_detail`
--

DROP TABLE IF EXISTS `aw_order_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `aw_order_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `additional_info` text,
  `quantity` int NOT NULL,
  `order_id` bigint NOT NULL,
  `pizza_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkg2osmh2i8vwpvrt9lpvbc4dl` (`order_id`),
  KEY `FK2qb07ecvpe599t1lvjr1dd0pj` (`pizza_id`),
  CONSTRAINT `FK2qb07ecvpe599t1lvjr1dd0pj` FOREIGN KEY (`pizza_id`) REFERENCES `aw_pizza` (`id`),
  CONSTRAINT `FKkg2osmh2i8vwpvrt9lpvbc4dl` FOREIGN KEY (`order_id`) REFERENCES `aw_order` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aw_order_detail`
--

LOCK TABLES `aw_order_detail` WRITE;
/*!40000 ALTER TABLE `aw_order_detail` DISABLE KEYS */;
INSERT INTO `aw_order_detail` VALUES (1,'+ olives',1,1,3),(2,'lactose free',1,1,4),(3,NULL,1,2,1),(4,NULL,2,2,5),(5,'without onion',1,2,5),(6,NULL,1,3,6);
/*!40000 ALTER TABLE `aw_order_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `aw_pizza`
--

DROP TABLE IF EXISTS `aw_pizza`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `aw_pizza` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `ingredients` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `price` double NOT NULL,
  `currency` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aw_pizza`
--

LOCK TABLES `aw_pizza` WRITE;
/*!40000 ALTER TABLE `aw_pizza` DISABLE KEYS */;
INSERT INTO `aw_pizza` VALUES (1,'pomodoro, olio, origano, basilico','Pizzaiola',6,'EUR'),(2,'mozzarella, olio, origano, basilico','Biancaneve',6,'EUR'),(3,'pomodoro, mozzarella, olio, origano, basilico','Margherita',7,'EUR'),(4,'pomodoro, mozzarella, wustel, patatine, olio, origano, basilico','Patapizza',9,'EUR'),(5,'pomodoro, mozzarella, cipolla, salame piccante, olio, origano, basilico','Piccante',9,'EUR'),(6,'pomodoro datterino, mozzarella, bresaola, rucola, grana, olio, origano, basilico','San Daniele',9.5,'EUR');
/*!40000 ALTER TABLE `aw_pizza` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-15 18:06:56
