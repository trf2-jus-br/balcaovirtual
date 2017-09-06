CREATE TABLE `balcao_virtual`.`sinal` (
  `SINA_ID` INT NOT NULL AUTO_INCREMENT COMMENT 'Id do sinal',
  `SINA_CD_USU` VARCHAR(30) NOT NULL COMMENT 'Username do usuário',
  `SINA_LG_INTERNO` TINYINT NOT NULL COMMENT 'Indica se usuário é interno ou externo',
  `SINA_CD_PROC` VARCHAR(22) NOT NULL COMMENT 'Código do processo, somente números',
  `SINA_LG_FAVORITO` TINYINT NOT NULL DEFAULT 0 COMMENT 'Indica se foi marcado como favorito',
  `SINA_DF_RECENTE` TIMESTAMP NULL COMMENT 'Indica se é processo recente e também qual foi a data do último acesso',
  PRIMARY KEY (`SINA_ID`))
COMMENT = 'Sinalização de processos favoritos e recentes';

ALTER TABLE `balcao_virtual`.`sinal` 
ADD UNIQUE INDEX `UK_SINA_USU_PROC` (`SINA_CD_USU` ASC, `SINA_LG_INTERNO` ASC, `SINA_CD_PROC` ASC);
