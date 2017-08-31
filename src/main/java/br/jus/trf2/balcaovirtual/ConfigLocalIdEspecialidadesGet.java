package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigLocalIdEspecialidadesGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigLocalIdEspecialidadesGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Especialidade;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IConfigLocalIdEspecialidadesGet;

public class ConfigLocalIdEspecialidadesGet implements IConfigLocalIdEspecialidadesGet {
	private static final Logger log = LoggerFactory.getLogger(ConfigLocalIdEspecialidadesGet.class);

	@Override
	public void run(ConfigLocalIdEspecialidadesGetRequest req, ConfigLocalIdEspecialidadesGetResponse resp)
			throws Exception {
		SessionsCreatePost.assertUsuarioAutorizado();

		resp.list = new ArrayList<>();
		if (req.id.equals("11")) {
			{
				Especialidade o = new Especialidade();
				o.id = "1";
				o.nome = "CÍVEL";
				resp.list.add(o);
			}
			{
				Especialidade o = new Especialidade();
				o.id = "4";
				o.nome = "EXECUÇÃO FISCAL";
				resp.list.add(o);
			}
		} else {
			{
				Especialidade o = new Especialidade();
				o.id = "4";
				o.nome = "PREVIDENCIÁRIA";
				resp.list.add(o);
			}
		}
	}

	@Override
	public String getContext() {
		return "obter lista de locais";
	}

}
