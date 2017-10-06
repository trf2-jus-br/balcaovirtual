package br.jus.trf2.balcaovirtual.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * The persistent class for the sinal database table.
 * 
 */
@Entity
@Table(name="sinal")
@NamedQuery(name = "Sinal.findAll", query = "SELECT s FROM Sinal s")
public class Sinal implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SINA_ID")
	private Long sinaId;

	@Column(name = "SINA_CD_PROC")
	private String sinaCdProc;

	@Column(name = "SINA_CD_USU")
	private String sinaCdUsu;

	@Column(name = "SINA_DF_RECENTE")
	private Timestamp sinaDfRecente;

	@Column(name = "SINA_LG_FAVORITO")
	private boolean sinaLgFavorito;

	@Column(name = "SINA_LG_INTERNO")
	private boolean sinaLgInterno;

	public Sinal() {
	}

	public Long getSinaId() {
		return this.sinaId;
	}

	public void setSinaId(Long sinaId) {
		this.sinaId = sinaId;
	}

	public String getSinaCdProc() {
		return this.sinaCdProc;
	}

	public void setSinaCdProc(String sinaCdProc) {
		this.sinaCdProc = sinaCdProc;
	}

	public String getSinaCdUsu() {
		return this.sinaCdUsu;
	}

	public void setSinaCdUsu(String sinaCdUsu) {
		this.sinaCdUsu = sinaCdUsu;
	}

	public Timestamp getSinaDfRecente() {
		return this.sinaDfRecente;
	}

	public void setSinaDfRecente(Timestamp sinaDfRecente) {
		this.sinaDfRecente = sinaDfRecente;
	}

	public boolean getSinaLgFavorito() {
		return this.sinaLgFavorito;
	}

	public void setSinaLgFavorito(boolean sinaLgFavorito) {
		this.sinaLgFavorito = sinaLgFavorito;
	}

	public boolean getSinaLgInterno() {
		return this.sinaLgInterno;
	}

	public void setSinaLgInterno(boolean sinaLgInterno) {
		this.sinaLgInterno = sinaLgInterno;
	}

}