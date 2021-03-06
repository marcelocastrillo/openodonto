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
  `dente` INT(2) NOT NULL ,
  `face` INT(2) NOT NULL ,
  `procedimento` INT(2) NOT NULL ,
  `descricao` VARCHAR(300) NULL ,
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
  `colaborador_id_pessoa` INT(10) NOT NULL ,
  `produto_id_produto` INT(10) NOT NULL ,
  `id` INT(10) NOT NULL AUTO_INCREMENT ,
  PRIMARY KEY (`id`, `produto_id_produto`, `colaborador_id_pessoa`) ,
  CONSTRAINT `fk_colaboradores_has_colaboradores`
    FOREIGN KEY (`colaborador_id_pessoa` )
    REFERENCES `openodonto`.`colaboradores` (`id_pessoa` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_colaboradores_has_produtos`
    FOREIGN KEY (`produto_id_produto` )
    REFERENCES `openodonto`.`produtos` (`id_produto` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_colaboradores_has_colaboradores` ON `openodonto`.`colaboradores_produtos` (`colaborador_id_pessoa` ASC) ;

CREATE INDEX `fk_colaboradores_has_produtos` ON `openodonto`.`colaboradores_produtos` (`produto_id_produto` ASC) ;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
