package br.jus.trf2.balcaovirtual.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the tipo_marca_item database table.
 * 
 */
@Entity
@Table(name = "tipo_marca_item")
@NamedQueries({
		// findAll
		@NamedQuery(name = "TipoMarcaItem.findAll", query = "SELECT t FROM TipoMarcaItem t"),

		// findClasse
		@NamedQuery(name = "TipoMarcaItem.findClasse", query = "select tmi from TipoMarca tm, in(tm.tipoMarcaItems) as tmi, in(tm.cnjClasses) as cl where cl.cnclId = :idclasse order by tmi.timiNrOrdem") })
public class TipoMarcaItem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TIMI_ID")
	private Long timiId;

	@Column(name = "TIMI_NM")
	private String timiNm;

	@Column(name = "TIMI_NR_ORDEM")
	private int timiNrOrdem;

	// bi-directional many-to-one association to Marca
	@OneToMany(mappedBy = "tipoMarcaItem")
	private List<Marca> marcas;

	// bi-directional many-to-one association to TipoMarca
	@ManyToOne
	@JoinColumn(name = "TIMA_ID")
	private TipoMarca tipoMarca;

	public TipoMarcaItem() {
	}

	public Long getTimiId() {
		return this.timiId;
	}

	public void setTimiId(Long timiId) {
		this.timiId = timiId;
	}

	public String getTimiNm() {
		return this.timiNm;
	}

	public void setTimiNm(String timiNm) {
		this.timiNm = timiNm;
	}

	public int getTimiNrOrdem() {
		return this.timiNrOrdem;
	}

	public void setTimiNrOrdem(int timiNrOrdem) {
		this.timiNrOrdem = timiNrOrdem;
	}

	public List<Marca> getMarcas() {
		return this.marcas;
	}

	public void setMarcas(List<Marca> marcas) {
		this.marcas = marcas;
	}

	public Marca addMarca(Marca marca) {
		getMarcas().add(marca);
		marca.setTipoMarcaItem(this);

		return marca;
	}

	public Marca removeMarca(Marca marca) {
		getMarcas().remove(marca);
		marca.setTipoMarcaItem(null);

		return marca;
	}

	public TipoMarca getTipoMarca() {
		return this.tipoMarca;
	}

	public void setTipoMarca(TipoMarca tipoMarca) {
		this.tipoMarca = tipoMarca;
	}

}