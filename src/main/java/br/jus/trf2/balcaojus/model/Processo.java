package br.jus.trf2.balcaojus.model;

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
import javax.persistence.Table;

/**
 * The persistent class for the processo database table.
 * 
 */
@Entity
@Table(name = "processo")
@NamedQueries({
		// findAll
		@NamedQuery(name = "Processo.findAll", query = "SELECT p FROM Processo p"),

		// findNumeroEOrgao
		@NamedQuery(name = "Processo.findNumeroESistema", query = "SELECT p FROM Processo p WHERE p.procCd = :numero and p.sistema = :sistema") })
public class Processo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PROC_ID")
	private Long procId;

	@Column(name = "PROC_CD")
	private String procCd;

	// bi-directional many-to-one association to Marca
	@OneToMany(mappedBy = "processo")
	private List<Marca> marcas;

	// bi-directional many-to-one association to Orgao
	@ManyToOne
	@JoinColumn(name = "SIST_ID")
	private Sistema sistema;

	public Processo() {
	}

	public Long getProcId() {
		return this.procId;
	}

	public void setProcId(Long procId) {
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

	public Sistema getSistema() {
		return sistema;
	}

	public void setSistema(Sistema sistema) {
		this.sistema = sistema;
	}

}