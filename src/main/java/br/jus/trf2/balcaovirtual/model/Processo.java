package br.jus.trf2.balcaovirtual.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 * The persistent class for the processo database table.
 * 
 */
@Entity
@NamedQueries({
		// findAll
		@NamedQuery(name = "Processo.findAll", query = "SELECT p FROM Processo p"),

		// findNumeroEOrgao
		@NamedQuery(name = "Processo.findNumeroEOrgao", query = "SELECT p FROM Processo p WHERE p.procCd = :numero and p.orgao = :orgao") })
public class Processo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PROC_ID")
	private int procId;

	@Column(name = "PROC_CD")
	private String procCd;

	// bi-directional many-to-one association to Marca
	@OneToMany(mappedBy = "processo")
	private List<Marca> marcas;

	// bi-directional many-to-one association to Orgao
	@ManyToOne
	@JoinColumn(name = "ORGA_ID")
	private Orgao orgao;

	public Processo() {
	}

	public int getProcId() {
		return this.procId;
	}

	public void setProcId(int procId) {
		this.procId = procId;
	}

	public String getProcCd() {
		return this.procCd;
	}

	public void setProcCd(String procCd) {
		this.procCd = procCd;
	}

	public List<Marca> getMarcas() {
		return this.marcas;
	}

	public void setMarcas(List<Marca> marcas) {
		this.marcas = marcas;
	}

	public Marca addMarca(Marca marca) {
		getMarcas().add(marca);
		marca.setProcesso(this);

		return marca;
	}

	public Marca removeMarca(Marca marca) {
		getMarcas().remove(marca);
		marca.setProcesso(null);

		return marca;
	}

	public Orgao getOrgao() {
		return this.orgao;
	}

	public void setOrgao(Orgao orgao) {
		this.orgao = orgao;
	}

}