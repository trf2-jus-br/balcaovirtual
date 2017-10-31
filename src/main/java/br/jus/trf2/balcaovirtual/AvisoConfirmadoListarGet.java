package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Future;

import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Aviso;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.AvisoConfirmadoListarGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.AvisoConfirmadoListarGetResponse;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IAvisoConfirmadoListarGet;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioWebUsernameAvisoConfirmadoListarGetResponse;

public class AvisoConfirmadoListarGet implements IAvisoConfirmadoListarGet {

	@Override
	public void run(AvisoConfirmadoListarGetRequest req, AvisoConfirmadoListarGetResponse resp) throws Exception {
		String authorization = SessionsCreatePost.assertAuthorization();
		Map<String, Object> jwt = SessionsCreatePost.assertUsuarioAutorizado();

		Date dtFim = Utils.parsearData(req.datafinal);
		Date dtFimPlus1 = new Date(dtFim.getTime() + (1000 * 60 * 60 * 24));
		String datafinal = Utils.formatarData(dtFimPlus1);

		Future<SwaggerAsyncResponse<UsuarioWebUsernameAvisoConfirmadoListarGetResponse>> future = SwaggerCall.callAsync(
				"obter quantidade de avisos confirmado", "Bearer " + authorization, "GET",
				Utils.getWsProcessualUrl() + "/usuario-web/" + jwt.get("username")
						+ "/aviso-confirmado/listar?dataInicial=" + req.datainicial + "&dataFinal=" + datafinal
						+ "&confirmacao=" + req.confirmacao.toString() + "&omissao=" + req.omissao.toString()
						+ "&grupo=" + req.grupo.toString(),
				null, UsuarioWebUsernameAvisoConfirmadoListarGetResponse.class);
		SwaggerAsyncResponse<UsuarioWebUsernameAvisoConfirmadoListarGetResponse> sar = future.get();
		if (sar.getException() != null)
			throw sar.getException();
		UsuarioWebUsernameAvisoConfirmadoListarGetResponse r = (UsuarioWebUsernameAvisoConfirmadoListarGetResponse) sar
				.getResp();

		resp.list = new ArrayList<>();
		resp.status = new ArrayList<>();

		for (br.jus.trf2.sistemaprocessual.ISistemaProcessual.Aviso ra : r.list) {
			Aviso a = new Aviso();
			a.idaviso = ra.idAviso;
			a.dataaviso = Utils.parsearDataHoraMinutoSegundo(ra.dataAviso);
			a.tipo = ra.tipo;
			a.processo = ra.processo;
			a.unidade = ra.unidade;
			a.unidadetipo = ra.unidadeTipo;
			a.orgao = ra.orgao;
			a.localidade = ra.localidade;
			a.eventointimacao = ra.eventoIntimacao;
			a.motivointimacao = ra.motivoIntimacao;
			a.numeroprazo = ra.numeroPrazo;
			a.tipoprazo = ra.tipoPrazo;
			a.multiplicadorprazo = ra.multiplicadorPrazo;
			a.datalimiteintimacaoautomatica = Utils.parsearDataHoraMinutoSegundo(ra.dataLimiteIntimacaoAutomatica);
			a.assunto = ra.assunto;
			a.dataconfirmacao = Utils.parsearDataHoraMinutoSegundo(ra.dataConfirmacao);
			a.usuarioconfirmacao = ra.usuarioConfirmacao;
			a.teor = ra.teor;
			resp.list.add(a);
		}

		// {
		// Aviso a = new Aviso();
		// a.idaviso = "1710131150171013115278051133";
		// a.dataaviso = "20171013115200";
		// a.tipo = "Intimação";
		// a.processo = "01730314320174025151";
		// a.unidade = "06JEF";
		// a.unidadetipo = "Juizado Especial Previdenciário";
		// a.orgao = "JFRJ";
		// a.localidade = "Rio de Janeiro";
		// a.eventointimacao = "Ato Ordinatório";
		// a.motivointimacao = "Vista";
		// a.numeroprazo = "10";
		// a.tipoprazo = "Dias";
		// a.multiplicadorprazo = "Simples";
		// a.datalimiteintimacaoautomatica = "20171022235900";
		// a.assunto = "6095";
		// a.dataconfirmacao = "20171013151617";
		// a.usuarioconfirmacao = "usuario.teste";
		// resp.list.add(a);
		// }
	}

	@Override
	public String getContext() {
		return "consultar avisos confirmados";
	}

}
