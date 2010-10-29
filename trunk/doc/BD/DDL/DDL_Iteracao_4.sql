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
  PRIMARY KEY (`id_pessoa`) ,
  CONSTRAINT `fk_pacientes_pessoas`
    FOREIGN KEY (`id_pessoa` )
    REFERENCES `openodonto`.`pessoas` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_pacientes_pessoas` ON `openodonto`.`pacientes` (`id_pessoa` ASC) ;


-- -----------------------------------------------------
-- Table `openodonto`.`usuarios`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openodonto`.`usuarios` ;

CREATE  TABLE IF NOT EXISTS `openodonto`.`usuarios` (
  `id_pessoa` INT(10) NOT NULL ,
  `user` VARCHAR(45) NOT NULL ,
  `senha` VARCHAR(32) NOT NULL ,
  PRIMARY KEY (`id_pessoa`) ,
  CONSTRAINT `fk_usuarios_pessoas`
    FOREIGN KEY (`id_pessoa` )
    REFERENCES `openodonto`.`pessoas` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_usuarios_pessoas` ON `openodonto`.`usuarios` (`id_pessoa` ASC) ;


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
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_telefones_pessoas1`
    FOREIGN KEY (`id_pessoa` )
    REFERENCES `openodonto`.`pessoas` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_telefones_pessoas1` ON `openodonto`.`telefones` (`id_pessoa` ASC) ;


-- -----------------------------------------------------
-- Table `openodonto`.`colaboradores`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openodonto`.`colaboradores` ;

CREATE  TABLE IF NOT EXISTS `openodonto`.`colaboradores` (
  `id_pessoa` INT(10) NOT NULL ,
  `data_cadastro` DATETIME NULL ,
  `categoria` INT(1) NOT NULL ,
  `observacao` VARCHAR(500) NULL ,
  `cpf` VARCHAR(11) NULL ,
  `cnpj` VARCHAR(14) NULL ,
  PRIMARY KEY (`id_pessoa`) ,
  CONSTRAINT `fk_colaboradores_pessoas1`
    FOREIGN KEY (`id_pessoa` )
    REFERENCES `openodonto`.`pessoas` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_colaboradores_pessoas1` ON `openodonto`.`colaboradores` (`id_pessoa` ASC) ;


-- -----------------------------------------------------
-- Table `openodonto`.`dentistas`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openodonto`.`dentistas` ;

CREATE  TABLE IF NOT EXISTS `openodonto`.`dentistas` (
  `id_pessoa` INT(10) NOT NULL ,
  `cro` INT NULL ,
  `especialidade` VARCHAR(150) NULL ,
  `observacao` VARCHAR(500) NULL ,
  PRIMARY KEY (`id_pessoa`) ,
  CONSTRAINT `fk_dentista_pessoas1`
    FOREIGN KEY (`id_pessoa` )
    REFERENCES `openodonto`.`pessoas` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_dentista_pessoas1` ON `openodonto`.`dentistas` (`id_pessoa` ASC) ;


-- -----------------------------------------------------
-- Table `openodonto`.`odontograma`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openodonto`.`odontograma` ;

CREATE  TABLE IF NOT EXISTS `openodonto`.`odontograma` (
  `id` INT(10) NOT NULL AUTO_INCREMENT ,
  `id_pessoa` INT(10) NOT NULL ,
  `nome` VARCHAR(150) NOT NULL ,
  `descricao` VARCHAR(500) NULL ,
  `data` DATETIME NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_odontograam_pacientes1`
    FOREIGN KEY (`id_pessoa` )
    REFERENCES `openodonto`.`pacientes` (`id_pessoa` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_odontograam_pacientes1` ON `openodonto`.`odontograma` (`id_pessoa` ASC) ;


-- -----------------------------------------------------
-- Table `openodonto`.`produtos`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openodonto`.`produtos` ;

CREATE  TABLE IF NOT EXISTS `openodonto`.`produtos` (
  `id_produto` INT(10) NOT NULL AUTO_INCREMENT ,
  `categoria` INT(1) NOT NULL ,
  `nome` VARCHAR(150) NOT NULL ,
  `descricao` VARCHAR(300) NULL ,
  PRIMARY KEY (`id_produto`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `openodonto`.`colaboradores_produtos`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openodonto`.`colaboradores_produtos` ;

CREATE  TABLE IF NOT EXISTS `openodonto`.`colaboradores_produtos` (
  `fk_pessoa` INT(10) NOT NULL ,
  `fk_produto` INT NOT NULL ,
  `id` INT(10) NOT NULL AUTO_INCREMENT ,
  PRIMARY KEY (`id`, `fk_produto`, `fk_pessoa`) ,
  CONSTRAINT `fk_colaboradores_has_colaboradores`
    FOREIGN KEY (`fk_pessoa` )
    REFERENCES `openodonto`.`colaboradores` (`id_pessoa` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_colaboradores_has_produtos`
    FOREIGN KEY (`fk_produto` )
    REFERENCES `openodonto`.`produtos` (`id_produto` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_colaboradores_has_colaboradores` ON `openodonto`.`colaboradores_produtos` (`fk_pessoa` ASC) ;

CREATE INDEX `fk_colaboradores_has_produtos` ON `openodonto`.`colaboradores_produtos` (`fk_produto` ASC) ;


-- -----------------------------------------------------
-- Table `openodonto`.`procedimentos`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openodonto`.`procedimentos` ;

CREATE  TABLE IF NOT EXISTS `openodonto`.`procedimentos` (
  `id_procedimento` INT(10) NOT NULL AUTO_INCREMENT ,
  `nome` VARCHAR(150) NULL ,
  `descricao` VARCHAR(300) NULL ,
  `valor` DECIMAL(64) NULL ,
  PRIMARY KEY (`id_procedimento`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `openodonto`.`odontograma_dentes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openodonto`.`odontograma_dentes` ;

CREATE  TABLE IF NOT EXISTS `openodonto`.`odontograma_dentes` (
  `dente` INT(2) NOT NULL ,
  `face` INT(2) NOT NULL ,
  `id_odontograma` INT(10) NOT NULL ,
  PRIMARY KEY (`dente`, `face`, `id_odontograma`) ,
  CONSTRAINT `fk_odontograma_dentes_odontograma1`
    FOREIGN KEY (`id_odontograma` )
    REFERENCES `openodonto`.`odontograma` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_odontograma_dentes_odontograma1` ON `openodonto`.`odontograma_dentes` (`id_odontograma` ASC) ;


-- -----------------------------------------------------
-- Table `openodonto`.`procedimentos_dentes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openodonto`.`procedimentos_dentes` ;

CREATE  TABLE IF NOT EXISTS `openodonto`.`procedimentos_dentes` (
  `id` INT(10) NOT NULL AUTO_INCREMENT ,
  `fk_procedimento` INT(10) NOT NULL ,
  `fk_dente` INT(2) NOT NULL ,
  `fk_face` INT(2) NOT NULL ,
  `fk_odontograma` INT(10) NOT NULL ,
  `valor` DECIMAL(64) NULL ,
  PRIMARY KEY (`id`, `fk_procedimento`, `fk_dente`, `fk_face`, `fk_odontograma`) ,
  CONSTRAINT `fk_procedimentos_has_procedimentos_odontograma_procedimentos1`
    FOREIGN KEY (`fk_procedimento` )
    REFERENCES `openodonto`.`procedimentos` (`id_procedimento` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_procedimentos_dentes_odontograma_dentes1`
    FOREIGN KEY (`fk_dente` , `fk_face` , `fk_odontograma` )
    REFERENCES `openodonto`.`odontograma_dentes` (`dente` , `face` , `id_odontograma` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_procedimentos_has_procedimentos_odontograma_procedimentos1` ON `openodonto`.`procedimentos_dentes` (`fk_procedimento` ASC) ;

CREATE INDEX `fk_procedimentos_dentes_odontograma_dentes1` ON `openodonto`.`procedimentos_dentes` (`fk_dente` ASC, `fk_face` ASC, `fk_odontograma` ASC) ;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
