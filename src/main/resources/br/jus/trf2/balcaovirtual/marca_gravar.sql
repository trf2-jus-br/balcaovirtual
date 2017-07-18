DROP PROCEDURE IF exists gravar_marca;

-- Trigger DDL Statements
DELIMITER $$

CREATE PROCEDURE gravar_marca (
	in i_numero varchar(22),
	in i_orgao varchar(20),
	in i_idclasse int,
	in i_idpeca int,
	in i_idmarca int,
	in i_texto varchar(200),
	in i_cor varchar(20),
	in i_paginicial int,
	in i_pagfinal int,
	out o_idmarca int, 
    out o_timi_nm varchar(200),
    out o_texto varchar(200),
    out o_errormsg varchar(2000))
begin
 	declare v_count int;
    declare v_esti_id int;
    declare v_orga_id int;
    declare v_proc_id int;
    declare v_timi_id int;

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
    
    -- identifica o estilo
	select esti_id into v_esti_id from estilo where ESTI_TP_COR = i_cor;

    -- identifica o processo
	select proc_id into v_proc_id from processo where proc_cd = i_numero and orga_id = v_orga_id;
  
	if (i_idmarca)  then 
		update marca set 
		marc_id_peca = i_idpeca,
        timi_id = v_timi_id,
        esti_id = v_esti_id,
        marc_tx = o_texto, 
        marc_pag_inicial = i_paginicial,
        marc_pag_final = i_pagfinal
        where marc_id = i_idmarca;
		select i_idmarca into o_idmarca;   
    else
		insert into marca(marc_id_peca, timi_id, proc_id, esti_id, marc_tx, marc_pag_inicial, marc_pag_final) 
        values(i_idpeca, v_timi_id, v_proc_id, v_esti_id, o_texto, i_paginicial, i_pagfinal);
		select last_insert_id() into o_idmarca;   
	end if;
    
end $$

DELIMITER ;