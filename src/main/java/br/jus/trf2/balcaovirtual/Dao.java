package br.jus.trf2.balcaovirtual;

import java.io.Closeable;
import java.io.IOException;

import javax.persistence.EntityManager;

import br.jus.trf2.balcaovirtual.model.Orgao;
import br.jus.trf2.balcaovirtual.model.Processo;

public class Dao implements Closeable {
	private EntityManager em;

	public Dao() {
		this.em = PersistenceManager.INSTANCE.getEntityManager();
		this.em.getTransaction().begin();
	}

	public Processo obtemProcesso(String numero, String orgaoSigla) {
		// identifica o órgao
		Orgao o = (Orgao) em.createNamedQuery("Orgao.findSigla").setParameter("sigla", orgaoSigla).getSingleResult();

		// verifica se o processo já está cadastrado
		Processo p = (Processo) em.createNamedQuery("Processo.findNumeroEOrgao").setParameter("numero", numero)
				.setParameter("orgao", o).getSingleResult();

		if (p == null) {
			// insere um novo processo na tabela
			p = new Processo();
			p.setOrgao(o);
			p.setProcCd(numero);
			em.persist(p);
		}

		return p;
	}

	public <T> T find(Class<T> clazz, Long id) {
		return em.find(clazz, id);
	}

	public void persist(Object o) {
		this.em.persist(o);
	}

	public void remove(Object o) {
		this.em.remove(o);
	}

	@Override
	public void close() throws IOException {
		if (em != null) {
			em.flush();
			em.getTransaction().commit();
			em.close();
		}
	}
}
