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
-- Table `openodonto`.`odontogramas`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openodonto`.`odontogramas` ;

CREATE  TABLE IF NOT EXISTS `openodonto`.`odontogramas` (
  `id` INT(10) NOT NULL AUTO_INCREMENT ,
  `id_pessoa` INT(10) NOT NULL ,
  `nome` VARCHAR(150) NOT NULL ,
  `descricao` VARCHAR(500) NULL ,
  `data` DATETIME NOT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_odontograam_pacientes1`
    FOREIGN KEY (`id_pessoa` )
    REFERENCES `openodonto`.`pacientes` (`id_pessoa` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_odontograam_pacientes1` ON `openodonto`.`odontogramas` (`id_pessoa` ASC) ;


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
  `nome` VARCHAR(150) NOT NULL ,
  `descricao` VARCHAR(300) NULL ,
  `valor` FLOAT NOT NULL ,
  PRIMARY KEY (`id_procedimento`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `openodonto`.`odontograma_dentes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openodonto`.`odontograma_dentes` ;

CREATE  TABLE IF NOT EXISTS `openodonto`.`odontograma_dentes` (
  `dente` INT(2) NOT NULL ,
  `face` INT(2) NOT NULL ,
  `fk_odontograma` INT(10) NOT NULL ,
  `id_odontograma_dente` INT(10) NOT NULL AUTO_INCREMENT ,
  PRIMARY KEY (`id_odontograma_dente`) ,
  CONSTRAINT `fk_odontograma_dentes_odontograma1`
    FOREIGN KEY (`fk_odontograma` )
    REFERENCES `openodonto`.`odontogramas` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_odontograma_dentes_odontograma1` ON `openodonto`.`odontograma_dentes` (`fk_odontograma` ASC) ;

CREATE UNIQUE INDEX `ix_dente` ON `openodonto`.`odontograma_dentes` (`dente` ASC, `face` ASC, `fk_odontograma` ASC) ;


-- -----------------------------------------------------
-- Table `openodonto`.`procedimentos_dentes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openodonto`.`procedimentos_dentes` ;

CREATE  TABLE IF NOT EXISTS `openodonto`.`procedimentos_dentes` (
  `id` INT(10) NOT NULL AUTO_INCREMENT ,
  `fk_procedimento` INT(10) NOT NULL ,
  `fk_odontograma_dente` INT(10) NOT NULL ,
  `valor` FLOAT NOT NULL ,
  `data_procedimento` DATETIME NULL ,
  `status` INT(2) NULL ,
  `obs` VARCHAR(300) NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_procedimentos_has_procedimentos_odontograma_procedimentos1`
    FOREIGN KEY (`fk_procedimento` )
    REFERENCES `openodonto`.`procedimentos` (`id_procedimento` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_procedimentos_dentes_odontograma_dentes1`
    FOREIGN KEY (`fk_odontograma_dente` )
    REFERENCES `openodonto`.`odontograma_dentes` (`id_odontograma_dente` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_procedimentos_has_procedimentos_odontograma_procedimentos1` ON `openodonto`.`procedimentos_dentes` (`fk_procedimento` ASC) ;

CREATE INDEX `fk_procedimentos_dentes_odontograma_dentes1` ON `openodonto`.`procedimentos_dentes` (`fk_odontograma_dente` ASC) ;


-- -----------------------------------------------------
-- Table `openodonto`.`odontograma_aspectos`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openodonto`.`odontograma_aspectos` ;

CREATE  TABLE IF NOT EXISTS `openodonto`.`odontograma_aspectos` (
  `dente` INT(2) NOT NULL ,
  `fk_odontograma` INT(10) NOT NULL ,
  `aspecto` INT(2) NOT NULL DEFAULT 2 ,
  PRIMARY KEY (`dente`, `fk_odontograma`) ,
  CONSTRAINT `fk_odontograma_aspecto_odontogramas1`
    FOREIGN KEY (`fk_odontograma` )
    REFERENCES `openodonto`.`odontogramas` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_odontograma_aspecto_odontogramas1` ON `openodonto`.`odontograma_aspectos` (`fk_odontograma` ASC) ;


-- -----------------------------------------------------
-- Table `openodonto`.`questionario_anamnese`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openodonto`.`questionario_anamnese` ;

CREATE  TABLE IF NOT EXISTS `openodonto`.`questionario_anamnese` (
  `id_questionario_anamnese` INT(10) NOT NULL AUTO_INCREMENT ,
  `nome` VARCHAR(45) NULL ,
  `descricao` VARCHAR(100) NULL ,
  PRIMARY KEY (`id_questionario_anamnese`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `openodonto`.`paciente_anamnese`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openodonto`.`paciente_anamnese` ;

CREATE  TABLE IF NOT EXISTS `openodonto`.`paciente_anamnese` (
  `fk_pacientes` INT(10) NOT NULL ,
  `fk_questionario_anamnese` INT(10) NOT NULL ,
  PRIMARY KEY (`fk_pacientes`, `fk_questionario_anamnese`) ,
  CONSTRAINT `fk_paciente_anamnese_pacientes1`
    FOREIGN KEY (`fk_pacientes` )
    REFERENCES `openodonto`.`pacientes` (`id_pessoa` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_paciente_anamnese_questionario_anamnese1`
    FOREIGN KEY (`fk_questionario_anamnese` )
    REFERENCES `openodonto`.`questionario_anamnese` (`id_questionario_anamnese` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_paciente_anamnese_pacientes1` ON `openodonto`.`paciente_anamnese` (`fk_pacientes` ASC) ;

CREATE INDEX `fk_paciente_anamnese_questionario_anamnese1` ON `openodonto`.`paciente_anamnese` (`fk_questionario_anamnese` ASC) ;


-- -----------------------------------------------------
-- Table `openodonto`.`questao_anamnese`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openodonto`.`questao_anamnese` ;

CREATE  TABLE IF NOT EXISTS `openodonto`.`questao_anamnese` (
  `id_questao_anamnese` INT(10) NOT NULL AUTO_INCREMENT ,
  `pergunta` VARCHAR(300) NULL ,
  PRIMARY KEY (`id_questao_anamnese`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `openodonto`.`questao_questionario_anamnese`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openodonto`.`questao_questionario_anamnese` ;

CREATE  TABLE IF NOT EXISTS `openodonto`.`questao_questionario_anamnese` (
  `fk_questao_anamnese` INT(10) NOT NULL ,
  `fk_questionario_anamnese` INT(10) NOT NULL ,
  `obrigatoria` TINYINT(1) NULL ,
  `index` INT NULL ,
  PRIMARY KEY (`fk_questao_anamnese`, `fk_questionario_anamnese`) ,
  CONSTRAINT `fk_questao_questionario_anamnese_questao_anamnese1`
    FOREIGN KEY (`fk_questao_anamnese` )
    REFERENCES `openodonto`.`questao_anamnese` (`id_questao_anamnese` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_questao_questionario_anamnese_questionario_anamnese1`
    FOREIGN KEY (`fk_questionario_anamnese` )
    REFERENCES `openodonto`.`questionario_anamnese` (`id_questionario_anamnese` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_questao_questionario_anamnese_questao_anamnese1` ON `openodonto`.`questao_questionario_anamnese` (`fk_questao_anamnese` ASC) ;

CREATE INDEX `fk_questao_questionario_anamnese_questionario_anamnese1` ON `openodonto`.`questao_questionario_anamnese` (`fk_questionario_anamnese` ASC) ;


-- -----------------------------------------------------
-- Table `openodonto`.`paciente_anamnese_respostas`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openodonto`.`paciente_anamnese_respostas` ;

CREATE  TABLE IF NOT EXISTS `openodonto`.`paciente_anamnese_respostas` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `resposta` INT(2) NULL ,
  `observacao` VARCHAR(300) NULL ,
  `fk_questao_anamnese` INT(10) NOT NULL ,
  `fk_questionario_anamnese` INT(10) NOT NULL ,
  `fk_pacientes` INT(10) NOT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_paciente_anamnese_respostas_questao_questionario_anamnese`
    FOREIGN KEY (`fk_questao_anamnese` , `fk_questionario_anamnese` )
    REFERENCES `openodonto`.`questao_questionario_anamnese` (`fk_questao_anamnese` , `fk_questionario_anamnese` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_paciente_anamnese_respostas_paciente_anamnese`
    FOREIGN KEY (`fk_pacientes` , `fk_questionario_anamnese` )
    REFERENCES `openodonto`.`paciente_anamnese` (`fk_pacientes` , `fk_questionario_anamnese` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_paciente_anamnese_respostas_questao_questionario_anamnese` ON `openodonto`.`paciente_anamnese_respostas` (`fk_questao_anamnese` ASC, `fk_questionario_anamnese` ASC) ;

CREATE INDEX `fk_paciente_anamnese_respostas_paciente_anamnese` ON `openodonto`.`paciente_anamnese_respostas` (`fk_pacientes` ASC, `fk_questionario_anamnese` ASC) ;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
