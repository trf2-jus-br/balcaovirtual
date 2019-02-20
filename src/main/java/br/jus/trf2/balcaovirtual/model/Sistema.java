package br.jus.trf2.balcaovirtual.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The persistent class for the orgao database table.
 * 
 */
@Entity
@Table(name = "orgao")
@NamedQueries({ @NamedQuery(name = "Sistema.findAll", query = "SELECT o FROM Sistema o"),
		@NamedQuery(name = "Sistema.findSigla", query = "SELECT o FROM Sistema o WHERE sistSg = :sigla") })
public class Sistema implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SIST_ID")
	private Long sistId;

	@Column(name = "SIST_SG")
	private String sistSg;

	// bi-directional many-to-one association to Processo
	@OneToMany(mappedBy = "sistema")
	private List<Processo> processos;

	public Sistema() {
	}

	public Long getSistId() {
		return this.sistId;
	}

	public void setSistId(Long sistId) {
		this.sistId = sistId;
	}

	public String getSistSg() {
		return this.sistSg;
	}

	public void setSistSg(String sistSg) {
		this.sistSg = sistSg;
	}

	public List<Processo> getProcessos() {
		return this.processos;
	}

	public void setProcessos(List<Processo> processos) {
		this.processos = processos;
	}

	public Processo addProcesso(Processo processo) {
		getProcessos().add(processo);
		processo.setSistema(this);

		return processo;
	}

	public Processo removeProcesso(Processo processo) {
		getProcessos().remove(processo);
		processo.setSistema(null);

		return processo;
	}

}