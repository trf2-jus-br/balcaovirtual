package br.jus.trf2.balcaovirtual.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the orgao database table.
 * 
 */
@Entity
@NamedQueries({ @NamedQuery(name = "Orgao.findAll", query = "SELECT o FROM Orgao o"),
		@NamedQuery(name = "Orgao.findSigla", query = "SELECT o FROM Orgao o WHERE orgaSg = :sigla") })
public class Orgao implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ORGA_ID")
	private int orgaId;

	@Column(name = "ORGA_SG")
	private String orgaSg;

	// bi-directional many-to-one association to Processo
	@OneToMany(mappedBy = "orgao")
	private List<Processo> processos;

	public Orgao() {
	}

	public int getOrgaId() {
		return this.orgaId;
	}

	public void setOrgaId(int orgaId) {
		this.orgaId = orgaId;
	}

	public String getOrgaSg() {
		return this.orgaSg;
	}

	public void setOrgaSg(String orgaSg) {
		this.orgaSg = orgaSg;
	}

	public List<Processo> getProcessos() {
		return this.processos;
	}

	public void setProcessos(List<Processo> processos) {
		this.processos = processos;
	}

	public Processo addProcesso(Processo processo) {
		getProcessos().add(processo);
		processo.setOrgao(this);

		return processo;
	}

	public Processo removeProcesso(Processo processo) {
		getProcessos().remove(processo);
		processo.setOrgao(null);

		return processo;
	}

}