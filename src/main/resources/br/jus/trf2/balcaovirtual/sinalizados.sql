select 
	*
from 
	sinal 
where 
	sina_cd_usu in (?) and sina_lg_interno = ?
	and (sina_lg_favorito <> 0 or sina_df_recente is not null)
order by 
	sina_df_recente desc;