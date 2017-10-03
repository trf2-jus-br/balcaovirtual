select m, tmi.timiNm
from 
	Processo p
	join p.orgao o 
	join p.marcas m
	join m.estilo e
	left join m.tipoMarcaItem tmi
where 
	p.procCd = :processo
	and o.orgaSg = :orgao
	and e.estiLgInterno = m.marcLgInterno
	and m.marcLgInterno = :interno
	and (
			(m.marcIeUsu = :usuario and m.marcIeUsu is not null) 
		or 
			(e.estiLgPessoal = 0 and m.marcIeUnidade = :unidade and m.marcIeUnidade is not null)
	)