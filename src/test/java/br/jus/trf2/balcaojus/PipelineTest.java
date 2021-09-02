package br.jus.trf2.balcaojus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.google.gson.Gson;

import br.jus.trf2.balcaojus.IBalcaojus.MesaDocumento;
import br.jus.trf2.balcaojus.util.PadraoUtils;
import br.jus.trf2.balcaojus.util.PadraoUtils.Minuta;

@TestInstance(Lifecycle.PER_CLASS)
public class PipelineTest {

	public static class Mesa {
		public List<MesaDocumento> list;
	}

	private List<Minuta> minutas;
	private List<Minuta> padroes;

	@BeforeAll
	public void before() {
		Gson gson = new Gson();

		InputStream isMinutas = this.getClass().getResourceAsStream("minutas.json");
		InputStreamReader rMinutas = new InputStreamReader(isMinutas);
		Mesa mMinutas = gson.fromJson(rMinutas, Mesa.class);
		minutas = new ArrayList<>();
		for (MesaDocumento doc : mMinutas.list) {
			minutas.add(new Minuta(doc));
		}

		InputStream isPadroes = this.getClass().getResourceAsStream("padroes.json");
		InputStreamReader rPadroes = new InputStreamReader(isPadroes);
		Mesa mPadroes = gson.fromJson(rPadroes, Mesa.class);
		padroes = new ArrayList<>();
		for (MesaDocumento doc : mPadroes.list) {
			padroes.add(new Minuta(doc));
		}

		PadraoUtils.pipeline(minutas, padroes);
	}

	@Test
	public void testHtmlToTextSucceed() {
		for (Minuta minuta : minutas) {
			assertNotNull(minuta.markdown);
//			System.out.println(minuta.conteudo);
		}

	}

	@Test
	public void testSimilaritySucceed() {
		assertEquals(minutas.get(0).padrao, padroes.get(0));
		assertEquals(minutas.get(1).similaridade, 0.0);
		assertEquals(minutas.get(2).similaridade, 0.0);
		assertEquals(minutas.get(3).similaridade, 0.0);
		assertEquals(minutas.get(4).padrao, padroes.get(1));
		assertEquals(minutas.get(5).similaridade, 0.0);
		assertEquals(minutas.get(6).similaridade, 0.0);
		assertEquals(minutas.get(7).similaridade, 0.0);
		assertEquals(minutas.get(8).similaridade, 0.0);
		assertEquals(minutas.get(9).similaridade, 0.0);
	}

}
