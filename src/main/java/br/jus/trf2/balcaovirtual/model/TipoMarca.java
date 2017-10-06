package br.jus.trf2.balcaovirtual.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the tipo_marca database table.
 * 
 */
@Entity
@Table(name = "tipo_marca")
@NamedQuery(name = "TipoMarca.findAll", query = "SELECT t FROM TipoMarca t")
public class TipoMarca implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TIMA_ID")
	private Long timaId;

	@Column(name = "TIMA_NM")
	private String timaNm;

	// bi-directional many-to-one association to TipoMarcaItem
	@OneToMany(mappedBy = "tipoMarca")
	private List<TipoMarcaItem> tipoMarcaItems;

	// bi-directional many-to-many association to CnjClasse
	@ManyToMany
	@JoinTable(name = "tx_tipo_marca_cnj_classe", joinColumns = {
			@JoinColumn(name = "TIMA_ID") }, inverseJoinColumns = { @JoinColumn(name = "CNCL_ID") })
	private List<CnjClasse> cnjClasses;

	public TipoMarca() {
	}

	public Long getTimaId() {
		return this.timaId;
	}

	public void setTimaId(Long timaId) {
		this.timaId = timaId;
	}

	public String getTimaNm() {
		return this.timaNm;
	}

	public void setTimaNm(String timaNm) {
		this.timaNm = timaNm;
	}

	public List<TipoMarcaItem> getTipoMarcaItems() {
		return this.tipoMarcaItems;
	}

	public void setTipoMarcaItems(List<TipoMarcaItem> tipoMarcaItems) {
		this.tipoMarcaItems = tipoMarcaItems;
	}

	public TipoMarcaItem addTipoMarcaItem(TipoMarcaItem tipoMarcaItem) {
		getTipoMarcaItems().add(tipoMarcaItem);
		tipoMarcaItem.setTipoMarca(this);

		return tipoMarcaItem;
	}

	public TipoMarcaItem removeTipoMarcaItem(TipoMarcaItem tipoMarcaItem) {
		getTipoMarcaItems().remove(tipoMarcaItem);
		tipoMarcaItem.setTipoMarca(null);

		return tipoMarcaItem;
	}

	public List<CnjClasse> getCnjClasses() {
		return this.cnjClasses;
	}

	public void setCnjClasses(List<CnjClasse> cnjClasses) {
		this.cnjClasses = cnjClasses;
	}

}