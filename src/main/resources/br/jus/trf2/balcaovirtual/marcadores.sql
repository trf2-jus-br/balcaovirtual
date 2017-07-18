select 
	tmi.*
from 
	tipo_marca_item tmi, 
    tx_tipo_marca_cnj_classe tx 
where 
	cncl_id = ?
order by 
	timi_ordem;