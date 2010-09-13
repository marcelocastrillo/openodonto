SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE  TABLE IF NOT EXISTS `openodonto`.`colaboradores_produtos` (
  `colaborador_id_pessoa` INT(10) NOT NULL ,
  `produto_id_produto` INT(10) NOT NULL ,
  `id` INT(10) NOT NULL ,
  INDEX `fk_colaboradores_has_colaboradores` (`colaborador_id_pessoa` ASC) ,
  INDEX `fk_colaboradores_has_produtos` (`produto_id_produto` ASC) ,
  PRIMARY KEY (`produto_id_produto`, `colaborador_id_pessoa`, `id`) ,
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
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;

DROP TABLE IF EXISTS `openodonto`.`colaboradores_produtos` ;

ALTER TABLE `openodonto`.`usuarios` DROP FOREIGN KEY `fk_usuarios_pessoas` ;

ALTER TABLE `openodonto`.`usuarios` 
  ADD CONSTRAINT `fk_usuarios_pessoas`
  FOREIGN KEY (`id_pessoa` )
  REFERENCES `openodonto`.`pessoas` (`id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `fk_usuarios_pessoas` (`id_pessoa` ASC) 
, DROP INDEX `fk_usuarios_pessoas` ;

ALTER TABLE `openodonto`.`telefones` DROP COLUMN `id_pessoa` , ADD COLUMN `id_pessoa` INT(10) NOT NULL  AFTER `tipo` 
, DROP INDEX `fk_telefones_pessoas1` 
, ADD INDEX `fk_telefones_pessoas1` (`id_pessoa` ASC) ;

ALTER TABLE `openodonto`.`colaboradores` DROP COLUMN `id_pessoa` , DROP COLUMN `operacao` , ADD COLUMN `categoria` INT(1) NOT NULL  AFTER `data_cadastro` , ADD COLUMN `id_pessoa` INT(10) NOT NULL  FIRST 
, DROP PRIMARY KEY 
, ADD PRIMARY KEY (`id_pessoa`) 
, DROP INDEX `fk_colaboradores_pessoas1` 
, ADD INDEX `fk_colaboradores_pessoas1` (`id_pessoa` ASC) ;

ALTER TABLE `openodonto`.`dentistas` CHANGE COLUMN `pessoas_id` `id_pessoa` INT(10) NOT NULL  
, DROP PRIMARY KEY 
, ADD PRIMARY KEY (`id_pessoa`) 
, DROP INDEX `fk_dentista_pessoas1` 
, ADD INDEX `fk_dentista_pessoas1` (`id_pessoa` ASC) ;

ALTER TABLE `openodonto`.`odontograma` DROP COLUMN `id_pessoa` , ADD COLUMN `id_pessoa` INT(10) NOT NULL  AFTER `id` 
, DROP INDEX `fk_odontograam_pacientes1` 
, ADD INDEX `fk_odontograam_pacientes1` (`id_pessoa` ASC) ;

ALTER TABLE `openodonto`.`produtos` DROP COLUMN `idprodutos` , ADD COLUMN `id_produto` INT(10) NOT NULL AUTO_INCREMENT  FIRST 
, DROP PRIMARY KEY 
, ADD PRIMARY KEY (`id_produto`) ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
