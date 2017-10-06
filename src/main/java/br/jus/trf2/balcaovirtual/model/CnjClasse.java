package br.jus.trf2.balcaovirtual.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the cnj_classe database table.
 * 
 */
@Entity
@Table(name = "cnj_classe")
@NamedQuery(name = "CnjClasse.findAll", query = "SELECT c FROM CnjClasse c")
public class CnjClasse implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CNCL_ID")
	private Long cnclId;

	@Column(name = "CNCL_NM")
	private String cnclNm;

	// bi-directional many-to-many association to TipoMarca
	@ManyToMany(mappedBy = "cnjClasses")
	private List<TipoMarca> tipoMarcas;

	public CnjClasse() {
	}

	public Long getCnclId() {
		return this.cnclId;
	}

	public void setCnclId(Long cnclId) {
		this.cnclId = cnclId;
	}

	public String getCnclNm() {
		return this.cnclNm;
	}

	public void setCnclNm(String cnclNm) {
		this.cnclNm = cnclNm;
	}

	public List<TipoMarca> getTipoMarcas() {
		return this.tipoMarcas;
	}

	public void setTipoMarcas(List<TipoMarca> tipoMarcas) {
		this.tipoMarcas = tipoMarcas;
	}

}