SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `openodonto` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci ;
USE `openodonto`;

-- -----------------------------------------------------
-- Table `openodonto`.`pessoas`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openodonto`.`pessoas` ;

CREATE  TABLE IF NOT EXISTS `openodonto`.`pessoas` (
  `id` INT(10) NOT NULL AUTO_INCREMENT ,
  `email` VARCHAR(45) NULL ,
  `nome` VARCHAR(100) NOT NULL ,
  `endereco` VARCHAR(150) NULL ,
  `cidade` VARCHAR(45) NULL ,
  `estado` INT(2) NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `openodonto`.`pacientes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openodonto`.`pacientes` ;

CREATE  TABLE IF NOT EXISTS `openodonto`.`pacientes` (
  `id_pessoa` INT(10) NOT NULL ,
  `cpf` VARCHAR(11) NULL ,
  `data_inicio_tratamento` DATETIME NULL ,
  `data_termino_tratamento` DATETIME NULL ,
  `data_retorno` DATETIME NULL ,
  `data_nascimento` DATETIME NULL ,
  `responsavel` VARCHAR(150) NULL ,
  `referencia` VARCHAR(150) NULL ,
  `observacao` VARCHAR(500) NULL ,
  PRIMARY KEY (`id_pessoa`) )
ENGINE = InnoDB;

CREATE INDEX `fk_pacientes_pessoas` ON `openodonto`.`pacientes` (`id_pessoa` ASC) ;


-- -----------------------------------------------------
-- Table `openodonto`.`usuarios`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openodonto`.`usuarios` ;

CREATE  TABLE IF NOT EXISTS `openodonto`.`usuarios` (
  `id_pessoa` INT(10) NOT NULL ,
  `user` VARCHAR(45) NOT NULL ,
  `senha` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`id_pessoa`) )
ENGINE = InnoDB;

CREATE INDEX `fk_usuarios_pessoas1` ON `openodonto`.`usuarios` (`id_pessoa` ASC) ;


-- -----------------------------------------------------
-- Table `openodonto`.`telefones`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openodonto`.`telefones` ;

CREATE  TABLE IF NOT EXISTS `openodonto`.`telefones` (
  `id` INT(10) NOT NULL AUTO_INCREMENT ,
  `ddd` VARCHAR(3) NULL ,
  `numero` VARCHAR(15) NULL ,
  `tipo` INT(1) NULL ,
  `id_pessoa` INT(10) NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;

CREATE INDEX `fk_telefones_pessoas1` ON `openodonto`.`telefones` (`id_pessoa` ASC) ;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
