-- MySQL dump 10.13  Distrib 5.7.18, for Win64 (x86_64)
--
-- Host: localhost    Database: balcao_virtual
-- ------------------------------------------------------
-- Server version	5.7.18-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cnj_classe`
--

DROP TABLE IF EXISTS `cnj_classe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cnj_classe` (
  `CNCL_ID` int(11) NOT NULL COMMENT 'Identificador da classe processual de acordo com tabela única do CNJ',
  `CNCL_NM` varchar(1000) DEFAULT NULL COMMENT 'Nome da classe processual de acordo com tabela única do CNJ',
  PRIMARY KEY (`CNCL_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `estilo`
--

DROP TABLE IF EXISTS `estilo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `estilo` (
  `ESTI_ID` int(11) NOT NULL COMMENT 'Identificador do estilo',
  `ESTI_NM` varchar(45) DEFAULT NULL COMMENT 'Nome do estilo',
  `ESTI_TP_COR` varchar(20) NOT NULL COMMENT 'Cor característica do estilo',
  `ESTI_LG_INTERNO` tinyint(4) DEFAULT '0' COMMENT 'Estilo para usuário interno ou externo',
  `ESTI_LG_PESSOAL` tinyint(4) DEFAULT '0' COMMENT 'Estilo pessoal ou do grupo/unidade',
  `ESTI_NR_ORDEM` int(11) DEFAULT NULL COMMENT 'Número utilizado para ordenar os estilos por prioridade',
  PRIMARY KEY (`ESTI_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `marca`
--

DROP TABLE IF EXISTS `marca`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `marca` (
  `MARC_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Identificador da marca',
  `PROC_ID` int(11) NOT NULL COMMENT 'Identificador do processo',
  `TIMI_ID` int(11) DEFAULT NULL COMMENT 'Identificador do tipo de uma marca padronizada, apenas quando for utilizado um padrão',
  `ESTI_ID` int(11) NOT NULL COMMENT 'Identificador do estilo (cor) da marca',
  `MARC_ID_PECA` int(11) NOT NULL COMMENT 'Identificador do código da peça, geralmente um número começando de 1, conforme informado pelo MNI',
  `MARC_TX_CONTEUDO` varchar(2000) DEFAULT NULL COMMENT 'Texto da marca. Quando houver TIMI_ID esse texto será um complemento opcional; quando não houver, será o texto completo da marca',
  `MARC_NR_PAG_INICIAL` decimal(4,0) DEFAULT NULL COMMENT 'Número da página inicial, em relação ao processo como um todo e não à peça em questão. Quando se desejar marcar a peça inteira, esse campo de ser nulo.',
  `MARC_NR_PAG_FINAL` decimal(4,0) DEFAULT NULL COMMENT 'Número da página final, em relação ao processo como um todo e não à peça em questão. Quando se desejar marcar a peça inteira, esse campo de ser nulo.',
  `MARC_DF_ALTERACAO` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Timestamp da criação/última alteração da marca',
  `MARC_LG_INTERNO` tinyint(4) DEFAULT '0' COMMENT 'Indica se o usuário é interno ou externo',
  `MARC_NM_USU` varchar(200) NOT NULL COMMENT 'Nome do usuário',
  `MARC_IE_USU` int(11) NOT NULL COMMENT 'Identificador externo do usuário',
  `MARC_IE_UNIDADE` int(11) DEFAULT NULL COMMENT 'Identificador externo da unidade ou grupo',
  PRIMARY KEY (`MARC_ID`),
  UNIQUE KEY `UK_MARC_ID` (`MARC_ID`),
  KEY `FK_MARC_PROC_ID` (`PROC_ID`),
  KEY `FK_MARC_ESTI_ID` (`ESTI_ID`),
  KEY `FK_MARC_TIMI_ID` (`TIMI_ID`),
  CONSTRAINT `FK_MARC_ESTI_ID` FOREIGN KEY (`ESTI_ID`) REFERENCES `estilo` (`ESTI_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_MARC_PROC_ID` FOREIGN KEY (`PROC_ID`) REFERENCES `processo` (`PROC_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_MARC_TIMI_ID` FOREIGN KEY (`TIMI_ID`) REFERENCES `tipo_marca_item` (`TIMI_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `orgao`
--

DROP TABLE IF EXISTS `orgao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orgao` (
  `ORGA_ID` int(11) NOT NULL COMMENT 'Identificador do órgão (TRF2/JFRJ/JFES)',
  `ORGA_SG` varchar(20) NOT NULL COMMENT 'Sigla do órgão',
  PRIMARY KEY (`ORGA_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `processo`
--

DROP TABLE IF EXISTS `processo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `processo` (
  `PROC_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Identificador do processo (auto incremental)',
  `ORGA_ID` int(11) NOT NULL COMMENT 'Identificador do órgão',
  `PROC_CD` varchar(22) NOT NULL COMMENT 'Número do processo segundo padrão do CNJ',
  PRIMARY KEY (`PROC_ID`),
  KEY `FK_PROC_ORGA_ID` (`ORGA_ID`),
  CONSTRAINT `FK_PROC_ORGA_ID` FOREIGN KEY (`ORGA_ID`) REFERENCES `orgao` (`ORGA_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tipo_marca`
--

DROP TABLE IF EXISTS `tipo_marca`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_marca` (
  `TIMA_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Identificador do tipo da marca',
  `TIMA_NM` varchar(45) DEFAULT NULL COMMENT 'Nome do tipo da marca',
  PRIMARY KEY (`TIMA_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='Representa uma tabela de tipos de marca referêntes a processos de determinadas classes, por exemplo, um processo criminal terá por padrão determinadas marcas para identificar ocorrências comuns em processos dessa classe.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tipo_marca_item`
--

DROP TABLE IF EXISTS `tipo_marca_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_marca_item` (
  `TIMI_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Identificador do item de tipo de marca',
  `TIMA_ID` int(11) DEFAULT NULL COMMENT 'Identificador do tipo de marca',
  `TIMI_NM` varchar(200) DEFAULT NULL COMMENT 'Nome do item',
  `TIMI_NR_ORDEM` int(11) DEFAULT NULL COMMENT 'Número utilizado para ordenar os itens de determinado tipo de marca',
  PRIMARY KEY (`TIMI_ID`),
  KEY `FK_TIMI_TIMA_ID` (`TIMA_ID`),
  CONSTRAINT `FK_TIMA_ID` FOREIGN KEY (`TIMA_ID`) REFERENCES `tipo_marca` (`TIMA_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tx_tipo_marca_cnj_classe`
--

DROP TABLE IF EXISTS `tx_tipo_marca_cnj_classe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tx_tipo_marca_cnj_classe` (
  `TIMA_ID` int(11) NOT NULL COMMENT 'Identificador do tipo de marca',
  `CNCL_ID` int(11) NOT NULL COMMENT 'Identificador da classe processual',
  PRIMARY KEY (`TIMA_ID`,`CNCL_ID`),
  KEY `FK_TMCL_TIMA_ID` (`TIMA_ID`),
  KEY `FK_TMCL_CNCL_ID` (`CNCL_ID`),
  CONSTRAINT `FK_TMCL_CNCL_ID` FOREIGN KEY (`CNCL_ID`) REFERENCES `cnj_classe` (`CNCL_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_TMCL_TIMA_ID` FOREIGN KEY (`TIMA_ID`) REFERENCES `tipo_marca` (`TIMA_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Tabela que relaciona tipos de marca com classes do CNJ.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'balcao_virtual'
--
/*!50003 DROP PROCEDURE IF EXISTS `gravar_marca` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `gravar_marca`(
	in i_numero varchar(22),
	in i_orgao varchar(20),
	in i_idclasse int,
	in i_idpeca int,
	in i_idmarca int,
	in i_texto varchar(200),
	in i_idestilo int,
	in i_paginicial int,
	in i_pagfinal int,
    in i_interno tinyint,
	in i_nmusu varchar(200),
    in i_idusuario int,
    in i_idunidade int,
	out o_idmarca int, 
    out o_timi_nm varchar(200),
    out o_texto varchar(200),
    out o_errormsg varchar(2000))
begin
 	declare v_count int;
    declare v_orga_id int;
    declare v_proc_id int;
    declare v_timi_id int;
    
    gravar: begin

  	-- verifica se o estilo está compatível com o usuário (interno/externo)
  	select count(*) into v_count from estilo where esti_id = i_idestilo and esti_lg_interno = i_interno;
  	if (v_count = 0) then
		select 'Estilo inválido' into o_errormsg;
        leave gravar;
	end if;

  	-- identifica o órgao
  	select orga_id into v_orga_id from orgao where orga_sg = i_orgao;
  	
  	-- verifica se o processo já está cadastrado
  	select count(*) into v_count from processo where proc_cd = i_numero and orga_id = v_orga_id;
  	if (v_count = 0) then
		insert into processo(orga_id, proc_cd) values (v_orga_id, i_numero);
	end if;
    
  	-- verifica se o texto representa um tipo_marca_item
    select i_texto into o_texto;
  	select count(*) into v_count from tipo_marca_item tmi, tx_tipo_marca_cnj_classe tx where tmi.tima_id = tx.tima_id and tx.cncl_id = i_idclasse and i_texto like CONCAT(timi_nm, '%');
  	if (v_count = 1) then
		select tmi.timi_id, tmi.timi_nm into v_timi_id, o_timi_nm from tipo_marca_item tmi, tx_tipo_marca_cnj_classe tx where tmi.tima_id = tx.tima_id and tx.cncl_id = i_idclasse and i_texto like CONCAT(timi_nm, '%');
        if (char_length(o_timi_nm) = char_length(o_texto)) then
			select null into o_texto;
		else
			select trim(mid(o_texto, char_length(o_timi_nm) + 1)) into o_texto;
            if (o_texto like '-%') then
				select trim(mid(o_texto, 2)) into o_texto;
			end if;
		end if;
	end if;
    
    -- identifica o processo
	select proc_id into v_proc_id from processo where proc_cd = i_numero and orga_id = v_orga_id;
  
	if (i_idmarca)  then 
		update marca set 
		marc_id_peca = i_idpeca,
        timi_id = v_timi_id,
        esti_id = i_idestilo,
        marc_tx_conteudo = o_texto, 
        marc_nr_pag_inicial = i_paginicial,
        marc_nr_pag_final = i_pagfinal,
        marc_lg_interno = i_interno,
        marc_nm_usu = i_nmusu,
        marc_ie_usu = i_idusuario, 
        marc_ie_unidade = i_idunidade,
        marc_df_alteracao = current_timestamp
        where marc_id = i_idmarca;
		select i_idmarca into o_idmarca;   
    else
		insert into marca(marc_id_peca, timi_id, proc_id, esti_id, marc_tx_conteudo, marc_nr_pag_inicial, marc_nr_pag_final, marc_lg_interno, marc_nm_usu, marc_ie_usu, marc_ie_unidade, marc_df_alteracao) 
        values(i_idpeca, v_timi_id, v_proc_id, i_idestilo, o_texto, i_paginicial, i_pagfinal, i_interno, i_nmusu, i_idusuario, i_idunidade, current_timestamp);
		select last_insert_id() into o_idmarca;   
	end if;
    
    end;
end ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-08-17 13:19:31
