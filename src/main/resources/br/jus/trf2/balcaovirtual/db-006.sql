CREATE TABLE `balcao_virtual`.`nota` (
  `NOTA_ID` INT NOT NULL AUTO_INCREMENT COMMENT 'Identificador da nota',
  `PROC_ID` INT NOT NULL COMMENT 'Identificador do processo',
  `NOTA_LG_PESSOAL` TINYINT NOT NULL DEFAULT 0 COMMENT 'Indica se a nota é pessoal ou da unidade',
  `NOTA_TX_CONTEUDO` VARCHAR(2000) NOT NULL COMMENT 'Texto da nota',
  `NOTA_DF_ALTERACAO` TIMESTAMP NOT NULL COMMENT 'Timestamp da criação/última alteração da nota',
  `NOTA_CD_USU` VARCHAR(30) NOT NULL COMMENT 'Username do usuário',
  `NOTA_LG_INTERNO` TINYINT NOT NULL DEFAULT 0 COMMENT 'Indica se o usuário é interno ou externo',
  `NOTA_NM_USU` VARCHAR(200) NOT NULL COMMENT 'Nome do usuário',
  `NOTA_IE_USU` INT NOT NULL COMMENT 'Identificador externo do usuário',
  `NOTA_IE_UNIDADE` INT NULL COMMENT 'Identificador externo da unidade ou grupo',
  PRIMARY KEY (`NOTA_ID`),
  INDEX `FK_NOTA_PROC_ID` (`PROC_ID` ASC),
  UNIQUE INDEX `UK_PROC_PESSOAL` (`PROC_ID` ASC, `NOTA_LG_PESSOAL` ASC, `NOTA_LG_INTERNO` ASC, `NOTA_IE_USU` ASC),
  CONSTRAINT `FK_NOTA_PROC_ID`
    FOREIGN KEY (`PROC_ID`)
    REFERENCES `balcao_virtual`.`processo` (`PROC_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
COMMENT = 'Armazena notas relativas ao processo, as notas podem ser pessoais ou da unidade/grupo';
