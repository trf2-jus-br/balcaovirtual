package br.jus.trf2.balcaovirtual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.crivano.swaggerservlet.SwaggerCall;
import com.crivano.swaggerservlet.SwaggerCallParameters;
import com.crivano.swaggerservlet.SwaggerMultipleCallResult;
import com.crivano.swaggerservlet.SwaggerMultipleCallResult.ListStatus;

import br.jus.trf2.assijus.system.api.IAssijusSystem.DocListGetRequest;
import br.jus.trf2.assijus.system.api.IAssijusSystem.DocListGetResponse;
import br.jus.trf2.assijus.system.api.IAssijusSystem.Document;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.Documento;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.IMesaIdGet;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.MesaIdGetRequest;
import br.jus.trf2.balcaovirtual.IBalcaoVirtual.MesaIdGetResponse;
import br.jus.trf2.balcaovirtual.SessionsCreatePost.Usuario;

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

		for (String system : mcr.responses.keySet()) {
			DocListGetResponse r = (DocListGetResponse) mcr.responses.get(system);

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
				resp.list.add(d);
			}
		}
	}

	@Override
	public String getContext() {
		return "obter classe processual";
	}

}
