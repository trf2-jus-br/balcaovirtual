select 
	*
from 
	sinal 
where 
	sina_cd_proc = ?
	and sina_cd_usu = ? and sina_lg_interno = ?
	and (sina_lg_favorito <> 0 or sina_df_recente is not null);