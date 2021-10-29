-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema BugTrackerDb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema BugTrackerDb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `BugTrackerDb` DEFAULT CHARACTER SET utf8 ;
-- -----------------------------------------------------
-- Schema new_schema1
-- -----------------------------------------------------
USE `BugTrackerDb` ;

-- -----------------------------------------------------
-- Table `BugTrackerDb`.`User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BugTrackerDb`.`User` (
  `idUser` INT NOT NULL AUTO_INCREMENT,
  `UserName` VARCHAR(45) NULL,
  `UserEmail` VARCHAR(45) NULL,
  `UserPhoneNo` VARCHAR(45) NULL,
  PRIMARY KEY (`idUser`),
  UNIQUE INDEX `idUser_UNIQUE` (`idUser` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BugTrackerDb`.`Role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BugTrackerDb`.`Role` (
  `idRole` INT NOT NULL AUTO_INCREMENT,
  `RoleName` VARCHAR(45) NULL,
  PRIMARY KEY (`idRole`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BugTrackerDb`.`Priority`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BugTrackerDb`.`Priority` (
  `idPriority` INT NOT NULL,
  `PriorityType` VARCHAR(45) NULL,
  PRIMARY KEY (`idPriority`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BugTrackerDb`.`Project`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BugTrackerDb`.`Project` (
  `idProject` INT NOT NULL,
  `ProjectName` VARCHAR(45) NULL,
  `ProjectDescription` VARCHAR(45) NULL,
  `ProjectOpen` VARCHAR(45) NULL,
  `ProjectDateStarted` DATETIME NULL,
  `Priority_Id` INT NULL,
  `ProjectFiles` VARCHAR(45) NULL,
  PRIMARY KEY (`idProject`),
  INDEX `Priority_Id_idx` (`Priority_Id` ASC) VISIBLE,
  CONSTRAINT `Project_Priority_Id`
    FOREIGN KEY (`Priority_Id`)
    REFERENCES `BugTrackerDb`.`Priority` (`idPriority`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BugTrackerDb`.`Ticket`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BugTrackerDb`.`Ticket` (
  `idTicket` INT NOT NULL,
  `Project_Id` INT NULL,
  `submit_User_Id` INT NULL,
  `TicketTitle` VARCHAR(45) NULL,
  `TicketDescription` VARCHAR(45) NULL,
  `TicketCreated` DATETIME NULL,
  `assigned_User_Id` INT NULL,
  `Priority_Id` INT NULL,
  `TicketDealine` DATETIME NULL,
  `TicketStatus` VARCHAR(45) NULL,
  PRIMARY KEY (`idTicket`),
  INDEX `Ticket_Project_Id_idx` (`Project_Id` ASC) VISIBLE,
  INDEX `Ticket_SubmitUser_User_Id_idx` (`submit_User_Id` ASC) VISIBLE,
  INDEX `Ticket_AssignedUser_User_Id_idx` (`assigned_User_Id` ASC) VISIBLE,
  INDEX `Ticket_Priority_Id_idx` (`Priority_Id` ASC) VISIBLE,
  CONSTRAINT `Ticket_Project_Id`
    FOREIGN KEY (`Project_Id`)
    REFERENCES `BugTrackerDb`.`Project` (`idProject`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `Ticket_SubmitUser_User_Id`
    FOREIGN KEY (`submit_User_Id`)
    REFERENCES `BugTrackerDb`.`User` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `Ticket_AssignedUser_User_Id`
    FOREIGN KEY (`assigned_User_Id`)
    REFERENCES `BugTrackerDb`.`User` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `Ticket_Priority_Id`
    FOREIGN KEY (`Priority_Id`)
    REFERENCES `BugTrackerDb`.`Priority` (`idPriority`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BugTrackerDb`.`Comment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BugTrackerDb`.`Comment` (
  `idComment` INT NOT NULL,
  `Ticket_Id` INT NULL,
  `User_Id` INT NULL,
  `CommentMessage` VARCHAR(45) NULL,
  PRIMARY KEY (`idComment`),
  INDEX `Ticket_id_idx` (`Ticket_Id` ASC) VISIBLE,
  INDEX `User_id_idx` (`User_Id` ASC) VISIBLE,
  CONSTRAINT `Comment_Ticket_Id`
    FOREIGN KEY (`Ticket_Id`)
    REFERENCES `BugTrackerDb`.`Ticket` (`idTicket`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `Comment_User_Id`
    FOREIGN KEY (`User_Id`)
    REFERENCES `BugTrackerDb`.`User` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BugTrackerDb`.`ProjectAssignment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BugTrackerDb`.`ProjectAssignment` (
  `Project_Id` INT NOT NULL,
  `User_Id` INT NOT NULL,
  `Role_Id` INT NULL,
  PRIMARY KEY (`Project_Id`, `User_Id`),
  INDEX `ProjectAssign_User_Id_idx` (`User_Id` ASC) VISIBLE,
  INDEX `ProjectAssign_Role_Id_idx` (`Role_Id` ASC) VISIBLE,
  CONSTRAINT `ProjectAssign_Project_Id`
    FOREIGN KEY (`Project_Id`)
    REFERENCES `BugTrackerDb`.`Project` (`idProject`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `ProjectAssign_User_Id`
    FOREIGN KEY (`User_Id`)
    REFERENCES `BugTrackerDb`.`User` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `ProjectAssign_Role_Id`
    FOREIGN KEY (`Role_Id`)
    REFERENCES `BugTrackerDb`.`Role` (`idRole`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BugTrackerDb`.`Tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BugTrackerDb`.`Tag` (
  `idTag` INT NOT NULL,
  `TagName` VARCHAR(45) NULL,
  PRIMARY KEY (`idTag`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BugTrackerDb`.`TagAssignment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BugTrackerDb`.`TagAssignment` (
  `Tag_Id` INT NOT NULL,
  `Ticket_Id` INT NOT NULL,
  PRIMARY KEY (`Tag_Id`, `Ticket_Id`),
  INDEX `TagAssign_Ticket_id_idx` (`Ticket_Id` ASC) VISIBLE,
  CONSTRAINT `TagAssign_Tag_Id`
    FOREIGN KEY (`Tag_Id`)
    REFERENCES `BugTrackerDb`.`Tag` (`idTag`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `TagAssign_Ticket_id`
    FOREIGN KEY (`Ticket_Id`)
    REFERENCES `BugTrackerDb`.`Ticket` (`idTicket`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BugTrackerDb`.`TicketHistory`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BugTrackerDb`.`TicketHistory` (
  `idTicketHistory` INT NOT NULL,
  `Ticket_Id` INT NULL,
  `TicketHistoryProperty` VARCHAR(45) NULL,
  `TicketHistoryOldValue` VARCHAR(45) NULL,
  `TicketHistorycolNewValue` VARCHAR(45) NULL,
  `TicketHistorycolDateChanged` DATETIME NULL,
  PRIMARY KEY (`idTicketHistory`),
  INDEX `TicketHistory_Ticket_Id_idx` (`Ticket_Id` ASC) VISIBLE,
  CONSTRAINT `TicketHistory_Ticket_Id`
    FOREIGN KEY (`Ticket_Id`)
    REFERENCES `BugTrackerDb`.`Ticket` (`idTicket`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BugTrackerDb`.`Attachment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BugTrackerDb`.`Attachment` (
  `idAttachment` INT NOT NULL,
  `Ticket_Id` INT NULL,
  `AttachmentDescription` VARCHAR(45) NULL,
  `AttachmentFilePath` VARCHAR(45) NULL,
  PRIMARY KEY (`idAttachment`),
  INDEX `Attachment_Ticket_Id_idx` (`Ticket_Id` ASC) VISIBLE,
  CONSTRAINT `Attachment_Ticket_Id`
    FOREIGN KEY (`Ticket_Id`)
    REFERENCES `BugTrackerDb`.`Ticket` (`idTicket`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
