package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Classe;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigEspecialidadeIdClassesGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigEspecialidadeIdClassesGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Especialidade;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IConfigEspecialidadeIdClassesGet;

public class ConfigEspecialidadeIdClassesGet implements IConfigEspecialidadeIdClassesGet {
	private static final Logger log = LoggerFactory.getLogger(ConfigEspecialidadeIdClassesGet.class);

	@Override
	public void run(ConfigEspecialidadeIdClassesGetRequest req, ConfigEspecialidadeIdClassesGetResponse resp)
			throws Exception {
		SessionsCreatePost.assertUsuarioAutorizado();

		resp.list = new ArrayList<>();
		if (req.id.equals("1")) {
			{
				Classe o = new Classe();
				o.id = "6010";
				o.nome = "AÇÃO CIVIL COLETIVA";
				resp.list.add(o);
			}
			{
				Classe o = new Classe();
				o.id = "5001";
				o.nome = "AÇÃO DE ALIMENTOS";
				resp.list.add(o);
			}
		} else {
			{
				Classe o = new Classe();
				o.id = "99";
				o.nome = "EXECUÇÃO FISCAL";
				resp.list.add(o);
			}
		}
	}

	@Override
	public String getContext() {
		return "obter lista de locais";
	}

}
