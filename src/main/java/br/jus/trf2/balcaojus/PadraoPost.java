package br.jus.trf2.balcaojus;

import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import br.jus.trf2.balcaojus.AutenticarPost.Usuario;
import br.jus.trf2.balcaojus.IBalcaojus.IPadraoPost;
import br.jus.trf2.balcaojus.model.Padrao;
import br.jus.trf2.balcaojus.util.Markdown;

public class PadraoPost implements IPadraoPost {

	@Override
	public void run(Request req, Response resp, BalcaojusContext ctx) throws Exception {
		Usuario u = BalcaojusServlet.getPrincipal();

		try (Dao dao = new Dao()) {
			Padrao p = (req.id != null) ? dao.find(Padrao.class, Long.valueOf(req.id)) : new Padrao();

			Document documentoOriginal = Jsoup.parse(req.html);
//			conteudo = document.select("section[contentEditable=true]").text();
			String htmlEditavel = documentoOriginal.select("section[contentEditable=true]").html();
			if (htmlEditavel == null || htmlEditavel.isEmpty())
				htmlEditavel = req.html;

			// Converter para markdown
			String markdown = Markdown.convertHtmlToMarkdown(htmlEditavel);

			// Padronizar número, nomes, etc.
//			ocorrencias = new Tudo(doc.autor, doc.reu).extract(markdown);
//			markdownSimplificado = Utils.replaceOcurrencesWithPlaceholders(markdown, ocorrencias);

			String html = Markdown.convertMarkdownToHtml(markdown);

			p.setPadrTxConteudo(html);
			p.setPadrCdUsu(u.usuario);
			if (req.id == null)
				p.setPadrDfInclusao(new Date());
			if (p.getPadrDfModificacao() == null)
				p.setPadrDfModificacao(p.getPadrDfInclusao());
			dao.persist(p);
			resp.padrao = PadraoGet.formatar(p);
		} catch (Exception e) {
			Dao.rollbackCurrentTransaction();
			throw e;
		}
	}

	@Override
	public String getContext() {
		return "gravar padrão";
	}

}
