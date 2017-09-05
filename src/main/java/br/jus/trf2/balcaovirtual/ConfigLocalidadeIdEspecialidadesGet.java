package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigLocalidadeIdEspecialidadesGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigLocalidadeIdEspecialidadesGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Especialidade;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IConfigLocalidadeIdEspecialidadesGet;

public class ConfigLocalidadeIdEspecialidadesGet implements IConfigLocalidadeIdEspecialidadesGet {

	@Override
	public void run(ConfigLocalidadeIdEspecialidadesGetRequest req, ConfigLocalidadeIdEspecialidadesGetResponse resp)
			throws Exception {
		SessionsCreatePost.assertUsuarioAutorizado();

		resp.list = new ArrayList<>();
		if (req.id.equals("1")) {
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
