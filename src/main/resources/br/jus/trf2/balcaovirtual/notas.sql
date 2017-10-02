select 
	n.nota_id,
    n.nota_tx_conteudo,
    n.nota_lg_pessoal,
	n.nota_df_alteracao,
	n.nota_nm_usu
from 
	processo p, 
	orgao o, 
	nota n
where 
	n.proc_id = p.proc_id 
	and p.orga_id = o.orga_id 
	and p.proc_cd = ?
	and o.orga_sg = ?
	and (
			(n.nota_lg_pessoal and n.nota_ie_usu = ? and n.nota_ie_usu is not null) 
		or 
			(not n.nota_lg_pessoal and n.nota_ie_unidade = ? and n.nota_ie_unidade is not null)
	);