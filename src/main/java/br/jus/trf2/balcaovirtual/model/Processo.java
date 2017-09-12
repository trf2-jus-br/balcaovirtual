package br.jus.trf2.balcaovirtual.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the processo database table.
 * 
 */
@Entity
@NamedQuery(name="Processo.findAll", query="SELECT p FROM Processo p")
public class Processo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="PROC_ID")
	private int procId;

	@Column(name="PROC_CD")
	private String procCd;

	//bi-directional many-to-one association to Marca
	@OneToMany(mappedBy="processo")
	private List<Marca> marcas;

	//bi-directional many-to-one association to Orgao
	@ManyToOne
	@JoinColumn(name="ORGA_ID")
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