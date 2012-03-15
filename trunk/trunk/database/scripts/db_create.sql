CREATE SCHEMA IF NOT EXISTS aggredata;

DROP USER 'aggredata'@'localhost';

CREATE USER 'aggredata'@'localhost' IDENTIFIED BY 'aggredata';
GRANT ALL PRIVILEGES ON aggredata.* TO 'aggredata'@'localhost' WITH GRANT OPTION;


SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';


-- -----------------------------------------------------
-- Table `aggredata`.`WeatherLocation`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `aggredata`.`WeatherLocation` (
  `id` INT NOT NULL AUTO_INCREMENT  ,
  `postal` VARCHAR(200) NULL ,
  `latitude` DECIMAL(10,6) NULL ,
  `longitude` DECIMAL(10,6) NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `aggredata`.`Location`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `aggredata`.`Location` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `description` VARCHAR(1000) NULL ,
  `address1` VARCHAR(200) NULL ,
  `address2` VARCHAR(200) NULL ,
  `city` VARCHAR(200) NULL ,
  `stateOrProvince` VARCHAR(200) NULL ,
  `postal` VARCHAR(200) NULL ,
  `country` VARCHAR(3) NULL ,
  `weatherLocationId` INT NULL ,
  `state` INT(1) NULL ,
  `userId` INT NULL COMMENT 'The owner of the location (allowed to delete or edit the location)	' ,
  PRIMARY KEY (`id`) ,
  INDEX `weather_location_id` (`weatherLocationId` ASC) ,
  CONSTRAINT `weather_location_id`
    FOREIGN KEY (`weatherLocationId` )
    REFERENCES `aggredata`.`WeatherLocation` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `aggredata`.`Gateway`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `aggredata`.`Gateway` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `locationId` INT NOT NULL ,
  `userAccountId` INT NOT NULL ,
  `gatewaySerialNumber` VARCHAR(6) NOT NULL ,
  `state` INT(1) NOT NULL ,
  `securityKey` VARCHAR(20) NULL ,
  `description` VARCHAR(1000) NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `location_id` (`locationId` ASC) ,
  CONSTRAINT `location_id`
    FOREIGN KEY (`locationId` )
    REFERENCES `aggredata`.`Location` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `aggredata`.`MTU`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `aggredata`.`MTU` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `gatewayId` INT NOT NULL ,
  `mtuSerialNumber` VARCHAR(6) NOT NULL ,
  `type` INT NULL ,
  `description` VARCHAR(1000) NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `gateway_id` (`gatewayId` ASC) ,
  CONSTRAINT `gateway_id`
    FOREIGN KEY (`gatewayId` )
    REFERENCES `aggredata`.`Gateway` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `aggredata`.`EnergyData`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `aggredata`.`EnergyData` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `mtuId` INT NOT NULL ,
  `timestamp` INT NOT NULL ,
  `rate` DECIMAL(8,5) NULL ,
  `energy` DECIMAL(25,4) NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `mtu_id` (`mtuId` ASC) ,
  INDEX `timestamp` (`timestamp` ASC, `mtuId` ASC) ,
  CONSTRAINT `mtu_id`
    FOREIGN KEY (`mtuId` )
    REFERENCES `aggredata`.`MTU` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = 'Table that stores all energy data posted from each gateway';


-- -----------------------------------------------------
-- Table `aggredata`.`User`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `aggredata`.`User` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `username` VARCHAR(200) NULL ,
  `password` VARCHAR(45) NULL ,
  `role` INT NULL COMMENT '0 = normal (can view their own data and see their own gateways)\n1 = admin (can see all users and admin screens)\n' ,
  `state` INT NULL ,
  `activationKey` VARCHAR(20) NULL ,
  `defaultGroupId` INT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `aggredata`.`Group`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `aggredata`.`Group` (
  `id` INT NOT NULL AUTO_INCREMENT  ,
  `description` VARCHAR(1000) NULL ,
  `ownerUserId` INT NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
COMMENT = 'By default, each user is placed in their own group. However, users may belong to more than one group.\n\n';


-- -----------------------------------------------------
-- Table `aggredata`.`WeatherHistory`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `aggredata`.`WeatherHistory` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `weatherLocationId` INT NOT NULL ,
  `timestamp` INT NOT NULL ,
  `temperature` INT NULL ,
  `windSpeed` INT NULL ,
  `direction` INT NULL ,
  `weatherConditions` VARCHAR(200) NULL ,
  `iconLink` VARCHAR(200) NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `weather_location_id` (`weatherLocationId` ASC) ,
  CONSTRAINT `weather_location_id_wh`
    FOREIGN KEY (`weatherLocationId` )
    REFERENCES `aggredata`.`WeatherLocation` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `aggredata`.`UserGroup`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `aggredata`.`UserGroup` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `userId` INT NOT NULL ,
  `groupId` INT NOT NULL ,
  `role` INT NULL COMMENT '0 = viewer\n1 = owner\n' ,
  PRIMARY KEY (`id`, `userId`, `groupId`) ,
  INDEX `user_id` (`userId` ASC) ,
  INDEX `group_id` (`groupId` ASC) ,
  CONSTRAINT `user_id`
    FOREIGN KEY (`userId` )
    REFERENCES `aggredata`.`User` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `group_id`
    FOREIGN KEY (`groupId` )
    REFERENCES `aggredata`.`Group` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `aggredata`.`GroupLocation`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `aggredata`.`GroupLocation` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `groupId` INT NOT NULL ,
  `locationId` INT NOT NULL ,
  PRIMARY KEY (`id`, `groupId`, `locationId`) ,
  INDEX `group_id` (`groupId` ASC) ,
  INDEX `location_id` (`locationId` ASC) ,
  CONSTRAINT `group_id_gl`
    FOREIGN KEY (`groupId` )
    REFERENCES `aggredata`.`Group` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `location_id_gl`
    FOREIGN KEY (`locationId` )
    REFERENCES `aggredata`.`Location` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `aggredata`.`ThirdPartyAccess`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `aggredata`.`ThirdPartyAccess` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `userId` INT NOT NULL ,
  `thirdPartyApplicationKey` VARCHAR(45) NOT NULL ,
  `thirdPartyAccessKey` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(200) NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `aggredata`.`ThirdPartyAccessGroup`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `aggredata`.`ThirdPartyAccessGroup` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `thirdPartyAccessId` INT NOT NULL ,
  `groupId` INT NOT NULL ,
  PRIMARY KEY (`id`, `thirdPartyAccessId`, `groupId`) ,
  INDEX `thirdPartyAccess_id` (`thirdPartyAccessId` ASC) ,
  INDEX `group_id` (`groupId` ASC) ,
  CONSTRAINT `thirdPartyAccess_id_tpag`
    FOREIGN KEY (`thirdPartyAccessId` )
    REFERENCES `aggredata`.`ThirdPartyAccess` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `group_id_tpag`
    FOREIGN KEY (`groupId` )
    REFERENCES `aggredata`.`Group` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
