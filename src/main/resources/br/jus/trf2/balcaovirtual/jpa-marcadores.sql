select distinct
	tmi
from 
	TipoMarca tm, in(tm.tipoMarcaItems) as tmi, in(tm.cnjClasses) as cl
where 
	cl.cnclId = :classe
order by
	tmi.timiNrOrdem