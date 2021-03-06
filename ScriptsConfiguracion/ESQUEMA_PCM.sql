/*CREACION DE ESQUEMAS.*/
DROP SCHEMA IF EXISTS `pcm`;
/*BAM.*/
CREATE SCHEMA `pcm` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
 USE `pcm`;

SET NAMES utf8 ;
SET character_set_client = utf8mb4 ;


CREATE TABLE `DEVICE_TYPE` (
  `ID` integer NOT NULL AUTO_INCREMENT,
  `TYPENAME` varchar(30),    
  `DESCRIPTION` varchar(30),   
  `CATEGORY` integer, 
  PRIMARY KEY (`ID`)  
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
INSERT INTO `DEVICE_TYPE` VALUES (1,'Ondeo.Transmitter-Tx4','Transmitter',1);
INSERT INTO `DEVICE_TYPE` VALUES (2,'Ondeo.Transmitter-Tx1','Transmitter',1);
INSERT INTO `DEVICE_TYPE` VALUES (3,'GISM','Emeter',2);
INSERT INTO `DEVICE_TYPE` VALUES (4,'GISR','Emeter',4);

CREATE TABLE `DEVICE` (
  `ID` integer NOT NULL AUTO_INCREMENT,
  `WOREFERPREFIX` varchar(20),
  `WOREFERNUMBER` varchar(20),
  `SERIAL` varchar(40),  
  `DEVICETYPE` integer,
  `PLACEID` integer,
  `STATUS` integer,
  `CREATIONDATE` timestamp DEFAULT CURRENT_TIMESTAMP,
  `LASTUPDATE` timestamp DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  KEY `FK_devDevtype_id` (`DEVICETYPE`),
  CONSTRAINT `FK_devDevtype_id` FOREIGN KEY (`DEVICETYPE`) REFERENCES `DEVICE_TYPE` (`ID`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
INSERT INTO `DEVICE` VALUES (1,1,'105991001','02456789QYF',1,1,1, STR_TO_DATE('10.31.2015',GET_FORMAT(DATE,'USA')), STR_TO_DATE('12-9-2019 10.35.14','%d-%m-%Y %H.%i.%s'));
INSERT INTO `DEVICE` VALUES (2,1,'805982010','55W14867i89',1,2,2, STR_TO_DATE('10.31.2015',GET_FORMAT(DATE,'USA')), STR_TO_DATE('2-1-2020 12.32.10','%d-%m-%Y %H.%i.%s'));
INSERT INTO `DEVICE` VALUES (3,3,'105982002','245YW678900',1,3,1, STR_TO_DATE('10.31.2015',GET_FORMAT(DATE,'USA')), STR_TO_DATE('12-9-2019 13.52.41','%d-%m-%Y %H.%i.%s'));
INSERT INTO `DEVICE` VALUES (4,1,'805982011','WAT-024867i89',1,4,1, STR_TO_DATE('10.31.2015',GET_FORMAT(DATE,'USA')), STR_TO_DATE('12-9-2019 09.01.01','%d-%m-%Y %H.%i.%s'));

CREATE TABLE `DEVICE_RELATION` (
  `ID` integer NOT NULL AUTO_INCREMENT,
  `SOURCE`	integer,
  `TARGET` integer,  
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
INSERT INTO `DEVICE_RELATION` VALUES (1,2,4);
-- INSERT INTO `DEVICE_RELATION` VALUES (2,4,2);

CREATE TABLE `DEVREL_DATA` (
  `ID` integer NOT NULL AUTO_INCREMENT,
  `ELEMENTVALUE`	integer,
  `ELEMENTNAME` varchar(30),  
  `DEVRELATIONSHIP` integer,
  PRIMARY KEY (`ID`),
  KEY `FK_devrelship_id` (`DEVRELATIONSHIP`),
  CONSTRAINT `FK_devrelship_id` FOREIGN KEY (`DEVRELATIONSHIP`) REFERENCES `DEVICE_RELATION` (`ID`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
INSERT INTO `DEVREL_DATA` VALUES (1,1,'portNumber',1);
COMMIT;