package br.jus.trf2.balcaovirtual.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the estilo database table.
 * 
 */
@Entity
@NamedQuery(name="Estilo.findAll", query="SELECT e FROM Estilo e")
public class Estilo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ESTI_ID")
	private int estiId;

	@Column(name="ESTI_LG_INTERNO")
	private byte estiLgInterno;

	@Column(name="ESTI_LG_PESSOAL")
	private byte estiLgPessoal;

	@Column(name="ESTI_NM")
	private String estiNm;

	@Column(name="ESTI_NR_ORDEM")
	private int estiNrOrdem;

	@Column(name="ESTI_TP_COR")
	private String estiTpCor;

	//bi-directional many-to-one association to Marca
	@OneToMany(mappedBy="estilo")
	private List<Marca> marcas;

	public Estilo() {
	}

	public int getEstiId() {
		return this.estiId;
	}

	public void setEstiId(int estiId) {
		this.estiId = estiId;
	}

	public byte getEstiLgInterno() {
		return this.estiLgInterno;
	}

	public void setEstiLgInterno(byte estiLgInterno) {
		this.estiLgInterno = estiLgInterno;
	}

	public byte getEstiLgPessoal() {
		return this.estiLgPessoal;
	}

	public void setEstiLgPessoal(byte estiLgPessoal) {
		this.estiLgPessoal = estiLgPessoal;
	}

	public String getEstiNm() {
		return this.estiNm;
	}

	public void setEstiNm(String estiNm) {
		this.estiNm = estiNm;
	}

	public int getEstiNrOrdem() {
		return this.estiNrOrdem;
	}

	public void setEstiNrOrdem(int estiNrOrdem) {
		this.estiNrOrdem = estiNrOrdem;
	}

	public String getEstiTpCor() {
		return this.estiTpCor;
	}

	public void setEstiTpCor(String estiTpCor) {
		this.estiTpCor = estiTpCor;
	}

	public List<Marca> getMarcas() {
		return this.marcas;
	}

	public void setMarcas(List<Marca> marcas) {
		this.marcas = marcas;
	}

	public Marca addMarca(Marca marca) {
		getMarcas().add(marca);
		marca.setEstilo(this);

		return marca;
	}

	public Marca removeMarca(Marca marca) {
		getMarcas().remove(marca);
		marca.setEstilo(null);

		return marca;
	}

}