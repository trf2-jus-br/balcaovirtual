package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Future;

import com.crivano.swaggerservlet.SwaggerAsyncResponse;
import com.crivano.swaggerservlet.SwaggerCall;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Documento;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IMesaIdGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.MesaIdGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.MesaIdGetResponse;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.MesaDocumento;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.MesaExpediente;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.MesaMovimento;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioUsernameLocalIdMesaId2DocumentosGetResponse;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioUsernameLocalIdMesaId2ExpedientesGetResponse;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioUsernameLocalIdMesaId2MovimentosGetResponse;

public class MesaIdGet implements IMesaIdGet {

	@Override
	public void run(MesaIdGetRequest req, MesaIdGetResponse resp) throws Exception {
		Usuario u = SessionsCreatePost.assertUsuario();
		if (!u.isInterno())
			throw new Exception("Mesas só podem ser acessadas por usuários internos");

		String split[] = req.id.split("_");
		String orgao = split[0];
		String idlocal = split[1];
		String idmesa = split[2];
		resp.list = new ArrayList<>();
		{
			Future<SwaggerAsyncResponse<UsuarioUsernameLocalIdMesaId2DocumentosGetResponse>> future = SwaggerCall
					.callAsync("obter documentos em mesa virtual", null, "GET",
							Utils.getWsProcessualUrl() + "/usuario/" + u.usuario + "/local/" + idlocal + "/mesa/"
									+ idmesa + "/documentos?orgao=" + orgao,
							null, UsuarioUsernameLocalIdMesaId2DocumentosGetResponse.class);
			SwaggerAsyncResponse<UsuarioUsernameLocalIdMesaId2DocumentosGetResponse> sar = future.get();
			if (sar.getException() != null)
				throw sar.getException();
			UsuarioUsernameLocalIdMesaId2DocumentosGetResponse r = (UsuarioUsernameLocalIdMesaId2DocumentosGetResponse) sar
					.getResp();

			for (MesaDocumento rd : r.list) {
				Documento d = new Documento();
				d.docorigin = "Apolo";
				d.documento = rd.numerododocumento;
				d.processo = Utils.removePontuacao(rd.numerodoprocesso);
				d.motivo = rd.motivo;
				d.situacao = rd.situacao;
				d.responsavel = rd.usuarioinclusao;
				d.dataentrada = Utils.parsearDataHoraFormatoJapones(rd.datadeentrada);
				resp.list.add(d);
			}
		}

		// Incluir movimentos
		{
			Future<SwaggerAsyncResponse<UsuarioUsernameLocalIdMesaId2MovimentosGetResponse>> future = SwaggerCall
					.callAsync("obter movimentos em mesa virtual", null, "GET",
							Utils.getWsProcessualUrl() + "/usuario/" + u.usuario + "/local/" + idlocal + "/mesa/"
									+ idmesa + "/movimentos?orgao=" + orgao,
							null, UsuarioUsernameLocalIdMesaId2MovimentosGetResponse.class);
			SwaggerAsyncResponse<UsuarioUsernameLocalIdMesaId2MovimentosGetResponse> sar = future.get();
			if (sar.getException() != null)
				throw sar.getException();
			UsuarioUsernameLocalIdMesaId2MovimentosGetResponse r = (UsuarioUsernameLocalIdMesaId2MovimentosGetResponse) sar
					.getResp();

			for (MesaMovimento rm : r.list) {
				String processo = Utils.removePontuacao(rm.numerodoprocesso);
				for (Documento d : resp.list) {
					if (d.processo.equals(processo) && d.documento == null) {
						if (d.docsystem != null) {
							Documento nd = new Documento();
							nd.docorigin = d.docorigin;
							nd.documento = d.documento;
							nd.processo = d.processo;
							nd.motivo = d.motivo;
							nd.situacao = d.situacao;
							nd.responsavel = d.responsavel;
							nd.dataentrada = d.dataentrada;
							resp.list.add(nd);
							d = nd;
						}
						d.docsystem = Utils.getAssijusSystemMovimentos();
						d.docid = u.cpf + rm.codsecao + "_" + rm.coddoc + "_"
								+ Utils.parsearDataHoraFormatoJapones(rm.datahoramovimento).getTime();
						d.docsecret = rm.segredo;
						d.doccode = Utils.formatarNumeroProcesso(processo);
						d.docdescr = rm.motivo;
						d.dockind = rm.ato;
						break;
					}
				}
			}
		}

		// Incluir expedientes
		{
			Future<SwaggerAsyncResponse<UsuarioUsernameLocalIdMesaId2ExpedientesGetResponse>> future = SwaggerCall
					.callAsync("obter expedientes em mesa virtual", null, "GET",
							Utils.getWsProcessualUrl() + "/usuario/" + u.usuario + "/local/" + idlocal + "/mesa/"
									+ idmesa + "/expedientes?orgao=" + orgao,
							null, UsuarioUsernameLocalIdMesaId2ExpedientesGetResponse.class);
			SwaggerAsyncResponse<UsuarioUsernameLocalIdMesaId2ExpedientesGetResponse> sar = future.get();
			if (sar.getException() != null)
				throw sar.getException();
			UsuarioUsernameLocalIdMesaId2ExpedientesGetResponse r = (UsuarioUsernameLocalIdMesaId2ExpedientesGetResponse) sar
					.getResp();

			for (MesaExpediente rm : r.list) {
				String processo = Utils.removePontuacao(rm.numerodoprocesso);
				String documento = Utils.removePontuacao(rm.numerododocumento);

				for (Documento d : resp.list) {
					if (d.processo.equals(processo) && d.documento != null && d.documento.equals(documento)) {
						d.docsystem = Utils.getAssijusSystemExpedientes();
						d.docid = u.cpf + rm.codsecao + "_" + rm.coddoc;
						d.docsecret = rm.segredo;
						d.doccode = rm.numerododocumento;
						d.docdescr = rm.descr;
						d.dockind = "Expediente";
						break;
					}
				}
			}
		}

		{
			Documento d = new Documento();
			d.docsystem = Utils.getAssijusSystemMovimentos();
			d.docid = "00489623760_51_4950827_1283805660000";
			d.docsecret = "9F60BC8503C57109DBB2EEBCF2A9165A962FDD7D";
			d.doccode = Utils.formatarNumeroProcesso("0010021-86.2005.4.02.5167");
			d.docdescr = "CERTIDÃO - Citação/intimação";
			d.dockind = "Citação/intimação";
			d.docorigin = "Apolo";
			d.documento = "CERTIDÃO - Citação/intimação";
			d.processo = Utils.removePontuacao("0010021-86.2005.4.02.5167");
			d.motivo = "Pronto para ser assinado";
			d.situacao = "Já verifiquei";
			d.responsavel = "JRJTAH";
			d.dataentrada = new Date();
			resp.list.add(d);
		}

		{
			Documento d = new Documento();
			d.docsystem = Utils.getAssijusSystemMovimentos();
			d.docid = "00489623760_51_4950827_1283805660000";
			d.docsecret = "9F60BC8503C57109DBB2EEBCF2A9165A962FDD7D";
			d.doccode = Utils.formatarNumeroProcesso("0010021-86.2005.4.02.5167");
			d.docdescr = "Teste";
			d.dockind = "TEste";
			d.docorigin = "Apolo";
			d.documento = "Teste";
			d.processo = Utils.removePontuacao("0010021-86.2005.4.02.5167");
			d.motivo = "Pronto para ser assinado";
			d.situacao = "Já verifiquei";
			d.responsavel = "JRJTAH";
			d.dataentrada = new Date();
			resp.list.add(d);
		}

		{
			Documento d = new Documento();
			d.docsystem = Utils.getAssijusSystemMovimentos();
			d.docid = "00489623760_51_4950827_1283805660000";
			d.docsecret = "9F60BC8503C57109DBB2EEBCF2A9165A962FDD7D";
			d.doccode = Utils.formatarNumeroProcesso("0000229-40.2015.4.02.0000");
			d.docdescr = "CERTIDÃO - Citação/intimação";
			d.dockind = "Citação/intimação";
			d.docorigin = "Apolo";
			d.documento = "CERTIDÃO - Citação/intimação";
			d.processo = Utils.removePontuacao("0000229-40.2015.4.02.0000");
			d.motivo = "Testando outro processo";
			d.situacao = "Só para ter 3.";
			d.responsavel = "JRJOEE";
			d.dataentrada = new Date(new Date().getTime() - 100000);
			resp.list.add(d);
		}
	}

	@Override
	public String getContext() {
		return "obter classe processual";
	}

}
