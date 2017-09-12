package br.jus.trf2.balcaovirtual;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public enum PersistenceManager {
	INSTANCE;
	private EntityManagerFactory emFactory;

	private PersistenceManager() {
		// "jpa-example" was the value of the name attribute of the
		// persistence-unit element.
		emFactory = Persistence.createEntityManagerFactory("default");
	}

	public EntityManager getEntityManager() {
		return emFactory.createEntityManager();
	}

	public void close() {
		emFactory.close();
	}
}