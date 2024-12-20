CREATE TABLE `address` (
  `id` int NOT NULL AUTO_INCREMENT,
  `city` varchar(255) NOT NULL,
  `street` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKfe80plkgb7b5rvf00jfnik51q` (`city`,`street`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- cakeShop.personal_data definition

CREATE TABLE `personal_data` (
  `address` int DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `personal_name` varchar(255) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  `user_password` varchar(255) NOT NULL,
  `user_role` enum('DELIVER','MANAGER','RENTIER','SHOP','WORKER') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKggbcjir8fcit6rsftj01iuhuk` (`user_name`),
  KEY `FKsp92cl87hmncx83akulskj4dg` (`address`),
  CONSTRAINT `FKsp92cl87hmncx83akulskj4dg` FOREIGN KEY (`address`) REFERENCES `address` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- cakeShop.bank_account definition

CREATE TABLE `bank_account` (
  `amount` float NOT NULL,
  `beneficiary` int DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `is_rental` bit(1) NOT NULL,
  `iban` varchar(255) NOT NULL,
  `currency` enum('BG','EU') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK699j998jxie2f134gfnu86q96` (`iban`),
  KEY `FK35x54dhm6wif5dtdx3sff4fu1` (`beneficiary`),
  CONSTRAINT `FK35x54dhm6wif5dtdx3sff4fu1` FOREIGN KEY (`beneficiary`) REFERENCES `personal_data` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- cakeShop.legal_entity definition

CREATE TABLE `legal_entity` (
  `id` int NOT NULL AUTO_INCREMENT,
  `personal_data` int DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `uin` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK85tsx8lyd4g9b4d3ougobxfuq` (`uin`),
  UNIQUE KEY `UKqwkvi6b252e0w9l7nh59ohpp0` (`personal_data`),
  CONSTRAINT `FKpgoklx1d3ebbfohyv39o4stss` FOREIGN KEY (`personal_data`) REFERENCES `personal_data` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- cakeShop.comment definition

CREATE TABLE `comment` (
 `id` int NOT NULL AUTO_INCREMENT,
 `comment` varchar(255) NOT NULL,
 `date` DATETIME NOT NULL,
 `assesed` int DEFAULT NULL,
 PRIMARY KEY (`id`),
 CONSTRAINT FOREIGN KEY (`assesed`) REFERENCES `legal_entity` (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- cakeShop.contract definition

CREATE TABLE `contract` (
 `id` int NOT NULL AUTO_INCREMENT,
 `identifier` int NOT NULL,
 `ammount` float NOT NULL,
 `currency` ENUM('BG', 'EU') NOT NULL,
 `offeror` int DEFAULT NULL,
 `recipient` int DEFAULT NULL,
 `status` ENUM('SENT', 'PENDING', 'SIGNED') NOT NULL,
 PRIMARY KEY (`id`),
 UNIQUE KEY (`identifier`),
 CONSTRAINT FOREIGN KEY (`offeror`) REFERENCES `legal_entity` (`id`),
 CONSTRAINT FOREIGN KEY (`recipient`) REFERENCES `legal_entity` (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- cakeShop.transaction definition

CREATE TABLE `transaction` (
 `id` int NOT NULL AUTO_INCREMENT,
 `sender` int DEFAULT NULL,
 `recipient` int DEFAULT NULL,
 `date` DATETIME NOT NULL,
 `ammount` float NOT NULL,
 PRIMARY KEY (`id`),
 CONSTRAINT FOREIGN KEY (`recipient`) REFERENCES `bank_account` (`id`),
 CONSTRAINT FOREIGN KEY (`sender`) REFERENCES `bank_account` (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- cakeShop.item definition

CREATE TABLE `item` (
 `id` int NOT NULL AUTO_INCREMENT,
 `name` varchar(255) NOT NULL,
 `price` float NOT NULL,
 PRIMARY KEY (`id`),
 UNIQUE KEY (`name`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- cakeShop.storage definition

CREATE TABLE `storage` (
 `id` int NOT NULL AUTO_INCREMENT,
 `item` int DEFAULT NULL,
 `count` int NOT NULL,
 `owner` int DEFAULT NULL,
 PRIMARY KEY (`id`),
 UNIQUE KEY (`item`),
 CONSTRAINT FOREIGN KEY (`item`) REFERENCES `item` (`id`),
 CONSTRAINT FOREIGN KEY (`owner`) REFERENCES `legal_entity` (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- cakeShop.staff definition

CREATE TABLE `staff` (
 `id` int NOT NULL AUTO_INCREMENT,
 `employer` int DEFAULT NULL,
 `personalData` int DEFAULT NULL,
 PRIMARY KEY (`id`),
 CONSTRAINT FOREIGN KEY (`employer`) REFERENCES `legal_entity` (`id`),
 CONSTRAINT FOREIGN KEY (`personalData`) REFERENCES `personal_data` (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- cakeShop.turnover definition

CREATE TABLE `turnover` (
 `id` int NOT NULL AUTO_INCREMENT,
 `owner` int DEFAULT NULL,
 `ammount` float NOT NULL,
 `date` DATETIME NOT NULL,
 PRIMARY KEY (`id`),
 CONSTRAINT FOREIGN KEY (`owner`) REFERENCES `legal_entity` (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- cakeShop.offer definition

CREATE TABLE `offer` (
 `id` int NOT NULL AUTO_INCREMENT,
 `offerror` int DEFAULT NULL,
 `offerred` int DEFAULT NULL,
 `contract` int DEFAULT NULL,
 `limit` float NOT NULL,
 PRIMARY KEY (`id`),
 CONSTRAINT FOREIGN KEY (`offerror`) REFERENCES `legal_entity` (`id`),
 CONSTRAINT FOREIGN KEY (`offerred`) REFERENCES `legal_entity` (`id`),
 CONSTRAINT FOREIGN KEY (`contract`) REFERENCES `contract` (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- cakeShop.schedule_transaction definition

CREATE TABLE `schedule_transaction` (
 `id` int NOT NULL AUTO_INCREMENT,
 `sender` int DEFAULT NULL,
 `recipient` int DEFAULT NULL,
 `transactionTime` DATETIME NOT NULL,
 `percentageAmmount` int NOT NULL,
 PRIMARY KEY (`id`),
 CONSTRAINT FOREIGN KEY (`recipient`) REFERENCES `bank_account` (`id`),
 CONSTRAINT FOREIGN KEY (`sender`) REFERENCES `bank_account` (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;