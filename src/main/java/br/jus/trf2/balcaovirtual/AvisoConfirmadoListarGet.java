package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Aviso;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.AvisoConfirmadoListarGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.AvisoConfirmadoListarGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IAvisoConfirmadoListarGet;

public class AvisoConfirmadoListarGet implements IAvisoConfirmadoListarGet {

	@Override
	public void run(AvisoConfirmadoListarGetRequest req, AvisoConfirmadoListarGetResponse resp) throws Exception {
		String authorization = SessionsCreatePost.assertAuthorization();

		resp.list = new ArrayList<>();
		resp.status = new ArrayList<>();
		{
			Aviso r = new Aviso();
			r.idaviso = "1710131258171013130077377901";
			r.dataaviso = "20171013130000";
			r.tipo = "Citação";
			r.processo = "05018821920174025151";
			r.unidade = "10JEF";
			r.unidadetipo = "Juizado Especial";
			r.orgao = "JFRJ";
			r.localidade = "Rio de Janeiro";
			r.eventointimacao = "Decisão";
			r.motivointimacao = "Resposta";
			r.numeroprazo = "30";
			r.tipoprazo = "Dias";
			r.multiplicadorprazo = "Simples";
			r.datalimiteintimacaoautomatica = "20171022235900";
			r.assunto = "9580";
			r.dataconfirmacao = "20171013121314";
			r.usuarioconfirmacao = "usuario.teste";
			resp.list.add(r);
		}
		{
			Aviso r = new Aviso();
			r.idaviso = "1710131150171013115278051133";
			r.dataaviso = "20171013115200";
			r.tipo = "Intimação";
			r.processo = "01730314320174025151";
			r.unidade = "06JEF";
			r.unidadetipo = "Juizado Especial Previdenciário";
			r.orgao = "JFRJ";
			r.localidade = "Rio de Janeiro";
			r.eventointimacao = "Ato Ordinatório";
			r.motivointimacao = "Vista";
			r.numeroprazo = "10";
			r.tipoprazo = "Dias";
			r.multiplicadorprazo = "Simples";
			r.datalimiteintimacaoautomatica = "20171022235900";
			r.assunto = "6095";
			r.dataconfirmacao = "20171013151617";
			r.usuarioconfirmacao = "usuario.teste";
			resp.list.add(r);
		}
	}

	@Override
	public String getContext() {
		return "consultar avisos confirmados";
	}

}
