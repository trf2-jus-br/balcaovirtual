package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.crivano.swaggerservlet.SwaggerCall;
import com.crivano.swaggerservlet.SwaggerCallParameters;
import com.crivano.swaggerservlet.SwaggerMultipleCallResult;

import br.jus.trf2.assijus.system.api.IAssijusSystem.DocListGetRequest;
import br.jus.trf2.assijus.system.api.IAssijusSystem.DocListGetResponse;
import br.jus.trf2.assijus.system.api.IAssijusSystem.Document;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Documento;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IMesaIdGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.MesaIdGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.MesaIdGetResponse;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.MesaDocumento;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioUsernameLocalIdMesaId2DocumentosGetRequest;
import br.jus.trf2.sistemaprocessual.ISistemaProcessual.UsuarioUsernameLocalIdMesaId2DocumentosGetResponse;

public class MesaIdGet implements IMesaIdGet {

	@Override
	public void run(MesaIdGetRequest req, MesaIdGetResponse resp) throws Exception {
		Usuario u = SessionsCreatePost.assertUsuario();
		if (!u.isInterno())
			throw new Exception("Mesas só podem ser acessadas por usuários internos");

		Map<String, SwaggerCallParameters> mapp = new HashMap<>();
		for (String system : u.usuarios.keySet()) {
			DocListGetRequest q = new DocListGetRequest();
			q.cpf = u.cpf;
			q.urlapi = Utils.getAssijusUrl(system);
			mapp.put(system, new SwaggerCallParameters(system + " - listar minutas", Utils.getAssijusPassword(system),
					"GET", Utils.getAssijusUrl(system) + "/doc/list", q, DocListGetResponse.class));
		}
		SwaggerMultipleCallResult mcr = SwaggerCall.callMultiple(mapp, 15000);

		resp.list = new ArrayList<>();
		resp.status = Utils.getStatus(mcr);

		Map<String, SwaggerCallParameters> mapp2 = new HashMap<>();
		Map<String, Documento> mapDocs = new HashMap();

		for (String system : mcr.responses.keySet()) {
			DocListGetResponse r = (DocListGetResponse) mcr.responses.get(system);

			StringBuilder sb = new StringBuilder();
			for (Document rd : r.list) {
				Documento d = new Documento();
				d.docsystem = system;
				d.docid = rd.id;
				d.docsecret = rd.secret;
				d.doccode = rd.code;
				d.docdescr = rd.descr;
				d.dockind = rd.kind;
				d.docorigin = Utils.getName(system);
				d.documento = "";
				d.processo = "";
				d.motivo = "";
				d.situacao = "";
				d.responsavel = "";
				d.dataentrada = null;
				mapDocs.put(system + "|" + d.docid, d);
				if (sb.length() > 0)
					sb.append(",");
				sb.append(d.docid);
				resp.list.add(d);
			}

			// Complementar informações, necessário apenas para o Eproc
			if (system.contains(".eproc") && sb.length() > 0) {
				UsuarioUsernameLocalIdMesaId2DocumentosGetRequest q = new UsuarioUsernameLocalIdMesaId2DocumentosGetRequest();
				q.ids = sb.toString();
				mapp2.put(system,
						new SwaggerCallParameters(system + " - complementar minutas", Utils.getAssijusPassword(system),
								"GET", Utils.getApiUrl(system) + "/usuario/" + u.usuario + "/local/null/mesa/null/documentos",
								q, UsuarioUsernameLocalIdMesaId2DocumentosGetResponse.class));

			}
		}

		SwaggerMultipleCallResult mcr2 = SwaggerCall.callMultiple(mapp2, 15000);
		for (String system : mcr2.responses.keySet()) {
			UsuarioUsernameLocalIdMesaId2DocumentosGetResponse r2 = (UsuarioUsernameLocalIdMesaId2DocumentosGetResponse) mcr2.responses
					.get(system);
			for (MesaDocumento rd : r2.list) {
				Documento d = mapDocs.get(system + "|" + rd.id);
				if (d == null)
					continue;
				d.conteudo = rd.conteudo;
				d.processo = rd.numerodoprocesso;
			}

		}

	}

	@Override
	public String getContext() {
		return "obter classe processual";
	}

}
