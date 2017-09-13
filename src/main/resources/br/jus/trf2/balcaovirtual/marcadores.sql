select 
	tmi.timi_nm
from 
	tipo_marca_item tmi, 
    tx_tipo_marca_cnj_classe tx 
where 
	tmi.tima_id = tx.tima_id 
	and tx.cncl_id = ?
order by 
	timi_nr_ordem;