package br.jus.trf2.balcaovirtual.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the estilo database table.
 * 
 */
@Entity
@Table(name = "estilo")
@NamedQuery(name = "Estilo.findAll", query = "SELECT e FROM Estilo e")
public class Estilo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ESTI_ID")
	private Long estiId;

	@Column(name = "ESTI_LG_INTERNO")
	private boolean estiLgInterno;

	@Column(name = "ESTI_LG_PESSOAL")
	private boolean estiLgPessoal;

	@Column(name = "ESTI_NM")
	private String estiNm;

	@Column(name = "ESTI_NR_ORDEM")
	private int estiNrOrdem;

	@Column(name = "ESTI_TP_COR")
	private String estiTpCor;

	// bi-directional many-to-one association to Marca
	@OneToMany(mappedBy = "estilo")
	private List<Marca> marcas;

	public Estilo() {
	}

	public Long getEstiId() {
		return estiId;
	}

	public void setEstiId(Long estiId) {
		this.estiId = estiId;
	}

	public boolean isEstiLgInterno() {
		return estiLgInterno;
	}

	public void setEstiLgInterno(boolean estiLgInterno) {
		this.estiLgInterno = estiLgInterno;
	}

	public boolean isEstiLgPessoal() {
		return estiLgPessoal;
	}

	public void setEstiLgPessoal(boolean estiLgPessoal) {
		this.estiLgPessoal = estiLgPessoal;
	}

	public String getEstiNm() {
		return estiNm;
	}

	public void setEstiNm(String estiNm) {
		this.estiNm = estiNm;
	}

	public int getEstiNrOrdem() {
		return estiNrOrdem;
	}

	public void setEstiNrOrdem(int estiNrOrdem) {
		this.estiNrOrdem = estiNrOrdem;
	}

	public String getEstiTpCor() {
		return estiTpCor;
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