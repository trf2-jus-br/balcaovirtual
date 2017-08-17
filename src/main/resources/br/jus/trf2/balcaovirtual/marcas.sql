select 
	m.marc_id,
    m.marc_id_peca,
    m.marc_tx_conteudo,
    m.marc_nr_pag_inicial,
    m.marc_nr_pag_final,
	e.esti_id, 
	tmi.timi_nm,
	m.marc_df_alteracao,
	m.marc_nm_usu
from 
	processo p, 
	orgao o, 
	estilo e, 
	marca m left join tipo_marca_item tmi on (tmi.timi_id = m.timi_id)
where 
	m.proc_id = p.proc_id 
	and p.orga_id = o.orga_id 
	and m.esti_id = e.esti_id 
	and p.proc_cd = ?
	and o.orga_sg = ?
	and e.esti_lg_interno = m.marc_lg_interno
	and m.marc_lg_interno = ?
	and (m.marc_ie_usu = ? or (not e.esti_lg_pessoal and m.marc_ie_unidade = ?));