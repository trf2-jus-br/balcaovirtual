select 
	tmi.timi_nm
from 
	tipo_marca_item tmi, 
    tx_tipo_marca_cnj_classe tx 
where 
	tmi.timi_id = tx.timi_id 
	and cncl_id = ?
order by 
	timi_nr_ordem;