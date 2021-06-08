package br.jus.trf2.balcaovirtual.util;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.crivano.jregexie.Ocurrence;
import com.crivano.jregexie.expression.pt.br.Tudo;

import br.jus.trf2.balcaovirtual.IBalcaoVirtual.MesaDocumento;
import info.debatty.java.stringsimilarity.SorensenDice;
import info.debatty.java.stringsimilarity.interfaces.NormalizedStringSimilarity;

public class PadraoUtils {
	public static class Minuta {
		public MesaDocumento doc;
		public String id;

		public Document documentoOriginal;
		public String htmlEditavel;
		public String markdown;
		public String markdownSimplificado;
		public List<Ocurrence> ocorrencias;

		public Minuta padrao;
		public double similaridade;

		public String markdownDiff;
		public String htmlDiff;

		public Minuta(MesaDocumento doc) {
			this.doc = doc;

			documentoOriginal = Jsoup.parse(doc.conteudo);
//			conteudo = document.select("section[contentEditable=true]").text();
			htmlEditavel = documentoOriginal.select("section[contentEditable=true]").html();

			// Converter para markdown
			markdown = Markdown.convertHtmlToMarkdown(htmlEditavel);

			// Padronizar número, nomes, etc.
			ocorrencias = new Tudo(doc.autor, doc.reu).extract(markdown);
			markdownSimplificado = com.crivano.jregexie.Utils.replaceOcurrencesWithPlaceholders(markdown, ocorrencias);
		}

		public Minuta(String idPadrao, String conteudo) {
			this.id = idPadrao;

			// Converter para markdown
			markdown = Markdown.convertHtmlToMarkdown(conteudo);

			// Padronizar número, nomes, etc.
			ocorrencias = new Tudo().extract(markdown);
			markdownSimplificado = com.crivano.jregexie.Utils.replaceOcurrencesWithPlaceholders(markdown, ocorrencias);
		}

		public void calcularDiff() {
			markdownDiff = com.crivano.jregexie.Utils.diffAndColapse(padrao.markdown, markdown);

			// Converter diff para HTML
			String html = Markdown.convertMarkdownToHtml(markdownDiff);

			// Injetar de volta no HTML
			documentoOriginal.select("section[contentEditable=true]").html(html);

			htmlDiff = documentoOriginal.html();
		}
	}

	public static void pipeline(List<Minuta> minutas, List<Minuta> padroes) {
		NormalizedStringSimilarity metric = new SorensenDice();
		for (Minuta minuta : minutas) {
			for (Minuta padrao : padroes) {
				double coef = metric.similarity(minuta.markdownSimplificado, padrao.markdownSimplificado);

				if (coef > 0.7d) {
					if (minuta.similaridade == 0d || minuta.similaridade < coef) {
						minuta.padrao = padrao;
						minuta.similaridade = coef;
					}
				}
			}
		}

		for (Minuta minuta : minutas) {
			System.out.println(minuta.similaridade + " -> " + minuta.markdownSimplificado);
			minuta.calcularDiff();
			if (minuta.htmlDiff != null)
				System.out.println(minuta.markdownDiff);
		}
	}

}
