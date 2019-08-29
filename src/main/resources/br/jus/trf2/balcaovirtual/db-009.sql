alter table processo change ORGA_ID SIST_ID int(11);
alter table orgao change ORGA_ID SIST_ID int(11);
alter table orgao change ORGA_SG SIST_SG varchar(20);
rename table orgao to sistema;

ALTER TABLE `nota` 
CHANGE COLUMN `NOTA_IE_USU` `NOTA_IE_USU` VARCHAR(30) NOT NULL COMMENT 'Identificador externo do usuário' ;
ALTER TABLE `nota` 
CHANGE COLUMN `NOTA_IE_UNIDADE` `NOTA_IE_UNIDADE` VARCHAR(30) NULL DEFAULT NULL COMMENT 'Identificador externo da unidade ou grupo' ;

ALTER TABLE `marca` 
CHANGE COLUMN `MARC_IE_USU` `MARC_IE_USU` VARCHAR(30) NOT NULL COMMENT 'Identificador externo do usuário' ;
ALTER TABLE `marca` 
CHANGE COLUMN `MARC_IE_UNIDADE` `MARC_IE_UNIDADE` VARCHAR(30) NULL DEFAULT NULL COMMENT 'Identificador externo da unidade ou grupo' ;

ALTER TABLE `marca` 
CHANGE COLUMN `MARC_ID_PECA` `MARC_ID_PECA` VARCHAR(30) NOT NULL COMMENT 'Identificador do código da peça, geralmente um número começando de 1, conforme informado pelo MNI' ;
