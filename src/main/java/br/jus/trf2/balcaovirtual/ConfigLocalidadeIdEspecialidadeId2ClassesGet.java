package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Classe;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigLocalidadeIdEspecialidadeId2ClassesGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.ConfigLocalidadeIdEspecialidadeId2ClassesGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IConfigLocalidadeIdEspecialidadeId2ClassesGet;

public class ConfigLocalidadeIdEspecialidadeId2ClassesGet implements IConfigLocalidadeIdEspecialidadeId2ClassesGet {

	@Override
	public void run(ConfigLocalidadeIdEspecialidadeId2ClassesGetRequest req,
			ConfigLocalidadeIdEspecialidadeId2ClassesGetResponse resp) throws Exception {
		SessionsCreatePost.assertUsuarioAutorizado();

		resp.list = new ArrayList<>();
		if (req.id2.equals("1")) {
			{
				Classe o = new Classe();
				o.id = "240";
				o.nome = "AÇÃO CIVIL COLETIVA";
				resp.list.add(o);
			}
			{
				Classe o = new Classe();
				o.id = "69";
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
