select 
	*
from 
	sinal 
where 
	sina_cd_usu = ? and sina_lg_interno = ?
order by 
	sina_df_recente desc;