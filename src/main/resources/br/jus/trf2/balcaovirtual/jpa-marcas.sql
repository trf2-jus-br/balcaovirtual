select m, tmi.timiNm
from 
	Processo p
	join p.orgao o 
	join p.marcas m
	join m.estilo e
	left join m.tipoMarcaItem tmi
where 
	and p.procCd = :processo
	and o.orgaSg = :orgao
	and e.estiLgInterno = m.marcLgInterno
	and m.marc_lg_interno = :interno
	and (
			(m.marcIeUsu = :usuario and m.marcIeUsu is not null) 
		or 
			(not e.estiLgPessoal and m.marcIeUnidade = :unidade and m.marcIeUnidade is not null)
	)