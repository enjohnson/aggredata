 /*
 * Copyright (c) 2012. The Energy Detective. All Rights Reserved
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
 
 /*
  * mySQL scrupt to create the support tables for AggreData
  * Script for Version: 1.0
  *
  */
 

CREATE SCHEMA IF NOT EXISTS aggredata;

DROP USER 'aggredata'@'localhost';  /*Uncomment this line if re-rerunning the script*/

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
  `weatherLocationId` INT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `weather_location_id` (`weatherLocationId` ASC) ,
  CONSTRAINT `weather_location_id`
    FOREIGN KEY (`weatherLocationId` )
    REFERENCES `aggredata`.`WeatherLocation` (`id` )
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
COMMENT = 'Groups of Gateways.\n\n';


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
CREATE  TABLE IF NOT EXISTS `aggredata`.`GatewayGroup` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `groupId` INT NOT NULL ,
  `gatewayId` INT NOT NULL ,
  PRIMARY KEY (`id`, `groupId`, `gatewayId`) ,
  INDEX `group_id` (`groupId` ASC) ,
  INDEX `gateway_id` (`gatewayId` ASC) ,
  CONSTRAINT `group_id_gl`
    FOREIGN KEY (`groupId` )
    REFERENCES `aggredata`.`Group` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `gateway_id_gl`
    FOREIGN KEY (`gatewayId` )
    REFERENCES `aggredata`.`Gateway` (`id` )
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
