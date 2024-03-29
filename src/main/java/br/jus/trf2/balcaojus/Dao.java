package br.jus.trf2.balcaojus;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.jus.trf2.balcaojus.model.Nota;
import br.jus.trf2.balcaojus.model.Notificacao;
import br.jus.trf2.balcaojus.model.Padrao;
import br.jus.trf2.balcaojus.model.Processo;
import br.jus.trf2.balcaojus.model.Sinal;
import br.jus.trf2.balcaojus.model.Sistema;
import br.jus.trf2.balcaojus.model.TipoMarcaItem;

public class Dao implements Closeable {
	private EntityManager em;
	public static ThreadLocal<Dao> current = new ThreadLocal<>();

	public Dao() {
		this.em = PersistenceManager.INSTANCE.getEntityManager();
		current.set(this);
	}

	public Date obtemData() {
		return (Date) em.createNativeQuery("select sysdate() from dual").getSingleResult();
	}

	public Processo obtemProcesso(String numero, String sistemaSigla, boolean criar) {
		// identifica o órgao
		Sistema o = (Sistema) em.createNamedQuery("Sistema.findSigla").setParameter("sigla", sistemaSigla)
				.getSingleResult();

		Processo p = null;
		// verifica se o processo já está cadastrado
		try {
			p = (Processo) em.createNamedQuery("Processo.findNumeroESistema").setParameter("numero", numero)
					.setParameter("sistema", o).getSingleResult();
		} catch (NoResultException e) {
			if (criar) {
				// insere um novo processo na tabela
				p = new Processo();
				p.setSistema(o);
				p.setProcCd(numero);
				this.persist(p);
				// p = obtemProcesso(numero, sistemaSigla, false);
			}
		}

		return p;
	}

	public List<Nota> obtemNotas(Processo p, String ieusuario, String ieunidade) {
		List<Nota> r = (List<Nota>) em.createNamedQuery("Nota.findProcesso").setParameter("processo", p)
				.setParameter("ieusuario", ieusuario).setParameter("ieunidade", ieunidade).getResultList();
		return r;
	}

	public List<TipoMarcaItem> obtemTipoMarcaItens(Long idclasse) {
		List<TipoMarcaItem> r = (List<TipoMarcaItem>) em.createNamedQuery("TipoMarcaItem.findClasse")
				.setParameter("idclasse", idclasse).getResultList();
		return r;
	}

	public List<Object[]> obtemMarcas(Processo p, boolean interno, String ieusuario, String ieunidade) {
		List<Object[]> r = (List<Object[]>) em.createNamedQuery("Marca.findProcessoUsuario").setParameter("processo", p)
				.setParameter("interno", interno).setParameter("ieusuario", ieusuario)
				.setParameter("ieunidade", ieunidade).getResultList();
		return r;
	}

	public List<Sinal> obtemSinais(boolean interno, String usuario) {
		List<Sinal> r = (List<Sinal>) em.createNamedQuery("Sinal.findUsuario").setParameter("interno", interno)
				.setParameter("usuario", usuario).getResultList();
		return r;
	}

	public Sinal obtemSinais(boolean interno, String usuario, String numero) {
		List<Sinal> r = (List<Sinal>) em.createNamedQuery("Sinal.findUsuarioNumero").setParameter("interno", interno)
				.setParameter("usuario", usuario).setParameter("numero", numero).getResultList();
		if (r == null || r.size() == 0)
			return null;
		return r.get(0);
	}

	public List<Padrao> obtemPadroes(String usuario) {
		List<Padrao> l = (List<Padrao>) em.createNamedQuery("Padrao.findPadroesDoUsuario")
				.setParameter("usuario", usuario).getResultList();
		return l;
	}

	public List<String> obtemUsuariosParaNotificar() {
		List<String> r = (List<String>) em.createNamedQuery("Notificacao.findUsuariosParaNotificar").getResultList();
		return r;
	}

	public List<String> obtemTokensDoUsuarioParaNotificar(String usuario) {
		List<String> r = (List<String>) em.createNamedQuery("Notificacao.findTokensDoUsuarioParaNotificar")
				.setParameter("usuario", usuario).getResultList();
		if (r == null || r.size() == 0)
			return null;

		List<String> l = new ArrayList<>();
		for (String a : r)
			l.add(a);

		return l;
	}

	public void removerTokenParaNotificar(String token) {
		Notificacao r = obtemTokenParaNotificar(token);
		if (r == null)
			return;
		this.remove(r);
	}

	public Notificacao obtemTokenParaNotificar(String token) {
		try {
			Notificacao r = (Notificacao) em.createNamedQuery("Notificacao.findTokenParaNotificar")
					.setParameter("token", token).getSingleResult();
			if (r == null)
				return null;
			return r;
		} catch (NoResultException ex) {
			return null;
		}
	}

	public Map<String, Date> obtemProcessosDoUsuarioParaNotificar(String usuario) {
		List<Object[]> r = (List<Object[]>) em.createNamedQuery("Sinal.findProcessosDoUsuarioParaNotificar")
				.setParameter("usuario", usuario).getResultList();
		if (r == null || r.size() == 0)
			return null;

		Map<String, Date> map = new TreeMap<>();
		for (Object[] a : r)
			map.put((String) a[0], (Date) a[1]);

		return map;
	}

	public void beginTransaction() {
		this.em.getTransaction().begin();
	}

	public void rollbackTransaction() {
		if (this.em.getTransaction().isActive())
			this.em.getTransaction().rollback();
	}

	public static void rollbackCurrentTransaction() {
		current.get().rollbackTransaction();
	}

	public <T> T find(Class<T> clazz, Long id) {
		return em.find(clazz, id);
	}

	public void persist(Object o) {
		if (!em.getTransaction().isActive())
			beginTransaction();
		this.em.persist(o);
		this.em.flush();
	}

	public void remove(Object o) {
		if (!em.getTransaction().isActive())
			beginTransaction();
		this.em.remove(o);
		this.em.flush();
	}

	@Override
	public void close() throws IOException {
		if (em != null) {
			if (em.getTransaction().isActive()) {
				em.flush();
				em.getTransaction().commit();
			}
			em.close();
		}
	}

}
