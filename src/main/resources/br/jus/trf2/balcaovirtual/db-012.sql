CREATE TABLE `notificacao` (
  `NOTI_ID` INT NOT NULL AUTO_INCREMENT COMMENT 'Identificador da notificacao',
  `NOTI_CD_USU` VARCHAR(30) NOT NULL COMMENT 'Username do usuario',
  `NOTI_CD` VARCHAR(200) NOT NULL DEFAULT 0 COMMENT 'Token para notificacao',
  PRIMARY KEY (`NOTI_ID`),
  INDEX `FK_NOTI_USU_CD` (`NOTI_CD_USU` ASC),
  UNIQUE INDEX `UK_NOTI_CD` (`NOTI_CD` ASC)
  ) 
  COMMENT = 'Armazena os tokens de notificação relativos aos usuários';