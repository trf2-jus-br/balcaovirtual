SET SQL_SAFE_UPDATES=0;

delete
from 
	processo
where
	processo.proc_id in (
		select 
			ids.pid 
		from 
			(
				select p.proc_id pid 
				from 
					processo p, 
					(select orga_id, proc_cd, count(*) FROM processo group by orga_id, proc_cd having count(*) > 1) d
				where 
					p.orga_id = d.orga_id 
					and p.proc_cd = d.proc_cd 
					and p.proc_id not in (select m.proc_id from marca m)
                    and p.proc_id not in (select n.proc_id from nota n)
			) ids
	);

SET SQL_SAFE_UPDATES=1;

ALTER TABLE `processo` 
ADD UNIQUE INDEX `UK_PROC_ORGA_ID_PROC_CD` (`ORGA_ID` ASC, `PROC_CD` ASC), 
COMMENT = 'Garante que não existam dos processos com o mesmo órgão e código' ;
