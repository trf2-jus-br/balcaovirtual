-- cria a nova tabela que guardar sinalizações em processos (favorito/recente)
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

-- altera a tabela de marca para incluir o username (cd_usu)
ALTER TABLE `balcao_virtual`.`marca` 
CHANGE COLUMN `MARC_LG_INTERNO` `MARC_LG_INTERNO` TINYINT(4) NOT NULL DEFAULT '0' COMMENT 'Indica se o usuário é interno ou externo' ,
ADD COLUMN `MARC_CD_USU` VARCHAR(30) NOT NULL COMMENT 'Username do usuário' AFTER `MARC_DF_ALTERACAO`;

-- altera a procedure gravar_marca
DROP PROCEDURE IF EXISTS `sp_gravar_marca`;
DELIMITER ;;
CREATE PROCEDURE `sp_gravar_marca`(
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
	in i_cdusu varchar(30),
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
        marc_cd_usu = i_cdusu,
        marc_nm_usu = i_nmusu,
        marc_ie_usu = i_idusuario, 
        marc_ie_unidade = i_idunidade,
        marc_df_alteracao = current_timestamp
        where marc_id = i_idmarca;
		select i_idmarca into o_idmarca;   
    else
		insert into marca(marc_id_peca, timi_id, proc_id, esti_id, marc_tx_conteudo, marc_nr_pag_inicial, marc_nr_pag_final, marc_lg_interno, marc_cd_usu, marc_nm_usu, marc_ie_usu, marc_ie_unidade, marc_df_alteracao) 
        values(i_idpeca, v_timi_id, v_proc_id, i_idestilo, o_texto, i_paginicial, i_pagfinal, i_interno, i_cdusu, i_nmusu, i_idusuario, i_idunidade, current_timestamp);
		select last_insert_id() into o_idmarca;   
	end if;
    
    end;
end;;
DELIMITER ;sp_gravar_marca

-- cria a procedure gravar_sinal
DELIMITER ;;
CREATE PROCEDURE `sp_gravar_sinal`(
	in i_numero varchar(22),
	in i_favorito tinyint,
	in i_recente tinyint,
    in i_interno tinyint,
	in i_cdusu varchar(30),
	out o_favorito tinyint, 
    out o_recente timestamp,
    out o_errormsg varchar(2000))
begin
 	declare v_count int;
    declare v_sina_id int;
    
    gravar: begin

  	-- verifica se já existe um registro para esse processo
  	select count(*) into v_count from sinal where sina_cd_proc = i_numero and sina_lg_interno = i_interno and sina_cd_usu = i_cdusu;
  	if (v_count = 0) then
		insert into sinal(sina_cd_proc, sina_lg_interno, sina_cd_usu) values (i_numero, i_interno, i_cdusu);
	end if;
    
    -- identifica o registro
	select sina_id into v_sina_id from sinal where sina_cd_proc = i_numero and sina_lg_interno = i_interno and sina_cd_usu = i_cdusu;
  
	if (i_favorito is not null)  then 
		update sinal set 
		sina_lg_favorito = i_favorito
        where sina_id = v_sina_id;
	end if;
    
	if (i_recente is not null)  then 
		if (i_recente) then
			update sinal set 
			sina_df_recente = current_timestamp
			where sina_id = v_sina_id;
        else 
			update sinal set 
			sina_df_recente = null
			where sina_id = v_sina_id;
        end if;
	end if;
    
	select sina_lg_favorito, sina_df_recente from sinal where sina_id = v_sina_id into o_favorito, o_recente;   
    end;
end;;
DELIMITER ;sp_gravar_sinal