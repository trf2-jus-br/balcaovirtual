package br.jus.trf2.balcaovirtual.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the sinal database table.
 * 
 */
@Entity
@NamedQuery(name="Sinal.findAll", query="SELECT s FROM Sinal s")
public class Sinal implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="SINA_ID")
	private int sinaId;

	@Column(name="SINA_CD_PROC")
	private String sinaCdProc;

	@Column(name="SINA_CD_USU")
	private String sinaCdUsu;

	@Column(name="SINA_DF_RECENTE")
	private Timestamp sinaDfRecente;

	@Column(name="SINA_LG_FAVORITO")
	private byte sinaLgFavorito;

	@Column(name="SINA_LG_INTERNO")
	private byte sinaLgInterno;

	public Sinal() {
	}

	public int getSinaId() {
		return this.sinaId;
	}

	public void setSinaId(int sinaId) {
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

	public byte getSinaLgFavorito() {
		return this.sinaLgFavorito;
	}

	public void setSinaLgFavorito(byte sinaLgFavorito) {
		this.sinaLgFavorito = sinaLgFavorito;
	}

	public byte getSinaLgInterno() {
		return this.sinaLgInterno;
	}

	public void setSinaLgInterno(byte sinaLgInterno) {
		this.sinaLgInterno = sinaLgInterno;
	}

}