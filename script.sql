CREATE DATABASE IF NOT EXISTS buses_ca;
USE buses_ca;

-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: localhost    Database: buses_ca
-- ------------------------------------------------------
-- Server version 8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `pais`
--

DROP TABLE IF EXISTS `pais`;
CREATE TABLE `pais` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `codigo_iso` varchar(10) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `codigoIso` (`codigo_iso`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `pais` WRITE;
INSERT INTO `pais` VALUES (1,'Costa Rica','CR'),(2,'Nicaragua','NI'),(3,'Guatemala','GUA'),(4,'Honduras','HN'),(5,'El Salvador','ES'),(6,'Panamá','PN');
UNLOCK TABLES;

--
-- Table structure for table `ciudad`
--

DROP TABLE IF EXISTS `ciudad`;
CREATE TABLE `ciudad` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `pais_id` int(11) NOT NULL,
  `codigo` varchar(10) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_ciudad_pais_codigo` (`pais_id`,`codigo`),
  CONSTRAINT `ciudad_ibfk_1` FOREIGN KEY (`pais_id`) REFERENCES `pais` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `ciudad` WRITE;
INSERT INTO `ciudad` VALUES (1,'San José',1,'SJO'),(2,'Managua',2,'MGA'),(3,'Ciudad de Guatemala',3,'GUA'),(4,'Tegucigalpa',4,'TGU'),(5,'San Salvador',5,'SAL'),(6,'Ciudad de Panamá',6,'PTY');
UNLOCK TABLES;

--
-- Table structure for table `compania`
--

DROP TABLE IF EXISTS `compania`;
CREATE TABLE `compania` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre_comercial` varchar(255) NOT NULL,
  `activa` tinyint(1) DEFAULT 1,
  `ruc_nit` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `compania` WRITE;
INSERT INTO `compania` VALUES (1,'Tica Bus',1,'RUC-001'),(2,'Pullmantur Centroamericano',1,'RUC-002'),(3,'Expreso Centroamerica',1,'RUC-003');
UNLOCK TABLES;

--
-- Table structure for table `bus`
--

DROP TABLE IF EXISTS `bus`;
CREATE TABLE `bus` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `placa` varchar(20) NOT NULL,
  `capacidad_total` int(11) NOT NULL,
  `compania_id` int(11) NOT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `modelo` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `placa` (`placa`),
  KEY `compania_id` (`compania_id`),
  CONSTRAINT `bus_ibfk_1` FOREIGN KEY (`compania_id`) REFERENCES `compania` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `bus` WRITE;
INSERT INTO `bus` VALUES (1,'BUS-101',40,1,1,'Volvo 9800'),(2,'BUS-202',50,2,1,'Mercedes Tourismo'),(3,'BUS-303',45,3,1,'Scania Marcopolo');
UNLOCK TABLES;

--
-- Table structure for table `asiento`
--

DROP TABLE IF EXISTS `asiento`;
CREATE TABLE `asiento` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bus_id` int(11) NOT NULL,
  `numero` varchar(10) NOT NULL,
  `categoria` varchar(30) NOT NULL,
  `piso` int(11) DEFAULT 1,
  `activo` tinyint(1) DEFAULT 1,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_asiento_bus_numero_piso` (`bus_id`,`numero`,`piso`),
  CONSTRAINT `asiento_ibfk_1` FOREIGN KEY (`bus_id`) REFERENCES `bus` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `asiento` WRITE;
INSERT INTO `asiento` VALUES (1,1,'A1','VIP',1,1),(2,1,'A2','VIP',1,1),(3,1,'B1','NORMAL',1,1),(4,1,'B2','NORMAL',1,1),(5,2,'A1','VIP',1,1),(6,2,'A2','VIP',1,1);
UNLOCK TABLES;

--
-- Table structure for table `cliente`
--

DROP TABLE IF EXISTS `cliente`;
CREATE TABLE `cliente` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombres` varchar(255) NOT NULL,
  `apellidos` varchar(100) NOT NULL,
  `documento_identidad` varchar(255) NOT NULL,
  `nacionalidad` varchar(50) NOT NULL,
  `email` varchar(255) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_nacimiento` date DEFAULT NULL,
  `tipo_cliente` enum('ESTANDAR','ESTUDIANTE') DEFAULT 'ESTANDAR',
  `puntos_acumulados` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `pasaporte` (`documento_identidad`),
  UNIQUE KEY `correoElectronico` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `cliente` WRITE;
INSERT INTO `cliente` VALUES (1,'Victor','Ramirez','CR123456','Costarricense','vicramirez@gmail.com','6055-3318',1,'2000-03-11','ESTANDAR',0),(2,'Laura','Mendez','CR654321','Costarricense','laura@gmail.com','8888-1111',1,'1999-07-21','ESTUDIANTE',120),(3,'Carlos','Soto','CR999888','Costarricense','carlos@gmail.com','7777-2222',1,'1995-11-10','ESTANDAR',50);
UNLOCK TABLES;

--
-- Table structure for table `tarjeta`
--

DROP TABLE IF EXISTS `tarjeta`;
CREATE TABLE `tarjeta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cliente_id` int(11) NOT NULL,
  `enmascarada` varchar(255) NOT NULL,
  `titular` varchar(100) NOT NULL,
  `marca` enum('VISA','MASTERCARD','AMEX') NOT NULL,
  `token_referencia` varchar(255) DEFAULT NULL,
  `ultimo4` varchar(4) NOT NULL,
  `mes_expiracion` int(11) NOT NULL,
  `anio_expiracion` int(11) NOT NULL,
  `activa` tinyint(1) DEFAULT 1,
  `creado_en` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `cliente_id` (`cliente_id`),
  CONSTRAINT `tarjeta_ibfk_1` FOREIGN KEY (`cliente_id`) REFERENCES `cliente` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `tarjeta` WRITE;
INSERT INTO `tarjeta` VALUES (1,1,'**** **** **** 9012','Diego Viquez','AMEX','TK-001','1234',5,2028,1,'2026-05-09 19:41:47'),(2,1,'**** **** **** 3456','Oscar Benavides','VISA','TK-002','5678',9,2027,1,'2026-05-09 19:41:47'),(3,1,'**** **** **** 7890','Marta Salas','MASTERCARD','TK-003','9012',11,2029,1,'2026-05-09 19:41:47');
UNLOCK TABLES;

--
-- Table structure for table `compra`
--

DROP TABLE IF EXISTS `compra`;
CREATE TABLE `compra` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cliente_id` int(11) NOT NULL,
  `fecha_compra` datetime NOT NULL,
  `total` decimal(10,2) NOT NULL,
  `estado` enum('PENDIENTE','PAGADA','CANCELADA') DEFAULT 'PENDIENTE',
  `metodo_pago_referencia` varchar(100) DEFAULT NULL,
  `moneda` varchar(10) DEFAULT 'USD',
  `tarjeta_id` int(11) DEFAULT NULL,
  `canal` varchar(255) DEFAULT NULL,
  `codigo_operacion_externa` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `cliente_id` (`cliente_id`),
  KEY `fk_compra_tarjeta` (`tarjeta_id`),
  CONSTRAINT `compra_ibfk_1` FOREIGN KEY (`cliente_id`) REFERENCES `cliente` (`id`),
  CONSTRAINT `fk_compra_tarjeta` FOREIGN KEY (`tarjeta_id`) REFERENCES `tarjeta` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `compra` WRITE;
INSERT INTO `compra` VALUES (1,1,'2026-05-08 12:36:39',160.00,'PAGADA','PAY-001','USD',1,'WEB','EXT-001'),(2,1,'2026-05-08 12:36:39',80.00,'PAGADA','PAY-002','USD',2,'WEB','EXT-002'),(3,1,'2026-05-08 12:36:39',140.00,'PENDIENTE','PAY-003','USD',3,'APP','EXT-003'),(4,1,'2026-05-08 12:36:39',220.00,'PAGADA','PAY-004','USD',1,'WEB','EXT-004'),(5,1,'2026-05-08 12:36:39',120.00,'CANCELADA','PAY-005','USD',2,'APP','EXT-005');
UNLOCK TABLES;

--
-- Table structure for table `ruta`
--

DROP TABLE IF EXISTS `ruta`;
CREATE TABLE `ruta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ciudad_origen_id` int(11) NOT NULL,
  `ciudad_destino_id` int(11) NOT NULL,
  `distancia_km` double NOT NULL,
  `duracion_minutos` int(11) NOT NULL,
  `activa` tinyint(1) DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `ruta_ibfk_1` (`ciudad_origen_id`),
  KEY `ruta_ibfk_2` (`ciudad_destino_id`),
  CONSTRAINT `ruta_ibfk_1` FOREIGN KEY (`ciudad_origen_id`) REFERENCES `ciudad` (`id`),
  CONSTRAINT `ruta_ibfk_2` FOREIGN KEY (`ciudad_destino_id`) REFERENCES `ciudad` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `ruta` WRITE;
INSERT INTO `ruta` VALUES (1,1,2,420,480,1),(2,1,5,800,720,1),(3,1,3,900,780,1),(4,1,4,700,660,1),(5,1,6,850,600,1);
UNLOCK TABLES;

--
-- Table structure for table `servicio`
--

DROP TABLE IF EXISTS `servicio`;
CREATE TABLE `servicio` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ruta_id` int(11) NOT NULL,
  `bus_id` int(11) NOT NULL,
  `salida_programada` datetime NOT NULL,
  `llegada_programada` datetime NOT NULL,
  `precio_base` decimal(12,2) NOT NULL,
  `estado` enum('PROGRAMADO','CANCELADO','FINALIZADO') DEFAULT 'PROGRAMADO',
  `capacidad_disponible` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `ruta_id` (`ruta_id`),
  KEY `bus_id` (`bus_id`),
  CONSTRAINT `servicio_ibfk_1` FOREIGN KEY (`ruta_id`) REFERENCES `ruta` (`id`),
  CONSTRAINT `servicio_ibfk_2` FOREIGN KEY (`bus_id`) REFERENCES `bus` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `servicio` WRITE;
INSERT INTO `servicio` VALUES (1,1,1,'2026-05-06 03:00:00','2026-05-06 11:00:00',80.00,'PROGRAMADO',40),(2,2,2,'2026-05-06 06:00:00','2026-05-06 18:00:00',120.00,'PROGRAMADO',50),(3,3,3,'2026-05-07 06:00:00','2026-05-07 19:00:00',140.00,'PROGRAMADO',45);
UNLOCK TABLES;

--
-- Table structure for table `ticket`
--

DROP TABLE IF EXISTS `ticket`;
CREATE TABLE `ticket` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `compra_id` int(11) NOT NULL,
  `cliente_id` int(11) NOT NULL,
  `servicio_id` int(11) NOT NULL,
  `codigo_ticket` varchar(255) NOT NULL,
  `precio_final` decimal(10,2) NOT NULL,
  `estado` enum('ACTIVO','USADO','CANCELADO') DEFAULT 'ACTIVO',
  `asiento_id` int(11) DEFAULT NULL,
  `fecha_emision` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `codigoUnico` (`codigo_ticket`),
  KEY `compra_id` (`compra_id`),
  KEY `cliente_id` (`cliente_id`),
  KEY `servicio_id` (`servicio_id`),
  KEY `fk_ticket_asiento` (`asiento_id`),
  CONSTRAINT `fk_ticket_asiento` FOREIGN KEY (`asiento_id`) REFERENCES `asiento` (`id`),
  CONSTRAINT `ticket_ibfk_1` FOREIGN KEY (`compra_id`) REFERENCES `compra` (`id`),
  CONSTRAINT `ticket_ibfk_2` FOREIGN KEY (`cliente_id`) REFERENCES `cliente` (`id`),
  CONSTRAINT `ticket_ibfk_3` FOREIGN KEY (`servicio_id`) REFERENCES `servicio` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `ticket` WRITE;
INSERT INTO `ticket` VALUES (1,1,1,1,'TKT-0001',80.00,'ACTIVO',1,'2026-05-09 20:08:25'),(2,1,1,1,'TKT-0002',80.00,'ACTIVO',2,'2026-05-09 20:08:25'),(3,2,1,2,'TKT-0003',80.00,'USADO',3,'2026-05-09 20:08:25'),(4,3,1,3,'TKT-0004',140.00,'ACTIVO',1,'2026-05-09 20:08:25'),(5,4,1,2,'TKT-0005',120.00,'CANCELADO',2,'2026-05-09 20:08:25');
UNLOCK TABLES;

--
-- Table structure for table `reservaasiento`
--

DROP TABLE IF EXISTS `reservaasiento`;
CREATE TABLE `reservaasiento` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `servicio_id` int(11) NOT NULL,
  `asiento_id` int(11) NOT NULL,
  `ticket_id` int(11) DEFAULT NULL,
  `estado` enum('RESERVADO','CONFIRMADO','LIBERADO') DEFAULT 'RESERVADO',
  `creado_en` datetime NOT NULL,
  `expira_en` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `servicio_id` (`servicio_id`),
  KEY `asiento_id` (`asiento_id`),
  KEY `ticket_id` (`ticket_id`),
  CONSTRAINT `reservaasiento_ibfk_1` FOREIGN KEY (`servicio_id`) REFERENCES `servicio` (`id`),
  CONSTRAINT `reservaasiento_ibfk_2` FOREIGN KEY (`asiento_id`) REFERENCES `asiento` (`id`),
  CONSTRAINT `reservaasiento_ibfk_3` FOREIGN KEY (`ticket_id`) REFERENCES `ticket` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `reservaasiento` WRITE;
INSERT INTO `reservaasiento` VALUES (1,1,1,1,'CONFIRMADO','2026-05-08 12:43:32','2026-05-08 12:58:32'),(2,1,2,2,'CONFIRMADO','2026-05-08 12:43:32','2026-05-08 12:58:32'),(3,2,5,3,'RESERVADO','2026-05-08 12:43:32','2026-05-08 12:58:32');
UNLOCK TABLES;

--
-- Table structure for table `comprobante`
--

DROP TABLE IF EXISTS `comprobante`;
CREATE TABLE `comprobante` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `compra_id` int(11) NOT NULL,
  `fecha_emision` datetime NOT NULL,
  `tipo` varchar(255) NOT NULL,
  `serie` varchar(255) NOT NULL,
  `numero` varchar(255) NOT NULL,
  `monto_total` decimal(10,2) NOT NULL,
  `moneda` varchar(255) NOT NULL,
  `estado` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `compra_id` (`compra_id`),
  CONSTRAINT `comprobante_ibfk_1` FOREIGN KEY (`compra_id`) REFERENCES `compra` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `comprobante` WRITE;
INSERT INTO `comprobante` VALUES (1,1,'2026-05-08 12:50:12','FACTURA','FAC','0001',160.00,'USD','EMITIDO'),(2,2,'2026-05-08 12:50:12','FACTURA','FAC','0002',80.00,'USD','EMITIDO'),(3,4,'2026-05-08 12:50:12','FACTURA','FAC','0003',220.00,'USD','EMITIDO');
UNLOCK TABLES;

--
-- Table structure for table `movimientopuntos`
--

DROP TABLE IF EXISTS `movimientopuntos`;
CREATE TABLE `movimientopuntos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cliente_id` int(11) NOT NULL,
  `compra_id` int(11) DEFAULT NULL,
  `puntos` int(11) NOT NULL,
  `tipo_movimiento` varchar(255) NOT NULL,
  `fecha_movimiento` datetime NOT NULL,
  `saldo_posterior` int(11) NOT NULL,
  `motivo` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `cliente_id` (`cliente_id`),
  KEY `compra_id` (`compra_id`),
  CONSTRAINT `movimientopuntos_ibfk_1` FOREIGN KEY (`cliente_id`) REFERENCES `cliente` (`id`),
  CONSTRAINT `movimientopuntos_ibfk_2` FOREIGN KEY (`compra_id`) REFERENCES `compra` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `movimientopuntos` WRITE;
INSERT INTO `movimientopuntos` VALUES (1,1,1,80,'GANADOS','2026-05-08 12:52:07',80,'Compra de ticket'),(2,1,2,40,'GANADOS','2026-05-08 12:52:07',120,'Compra de ticket'),(3,1,3,20,'CANJEADOS','2026-05-08 12:52:07',100,'Canje de puntos'),(4,1,NULL,50,'AJUSTE','2026-05-08 12:52:07',150,'Ajuste manual');
UNLOCK TABLES;

--
-- Table structure for table `user_security`
--

DROP TABLE IF EXISTS `user_security`;
CREATE TABLE `user_security` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cliente_id` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  `locked` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_security_username` (`username`),
  UNIQUE KEY `uk_user_security_cliente` (`cliente_id`),
  CONSTRAINT `fk_user_security_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `cliente` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOCK TABLES `user_security` WRITE;
INSERT INTO `user_security` VALUES (1,1,'victor','HASH123',1,0),(2,2,'laura99','HASH456',1,0),(3,3,'carlos_soto','HASH789',1,0);
UNLOCK TABLES;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;