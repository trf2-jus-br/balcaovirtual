package br.jus.trf2.balcaojus.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the sinal database table.
 * 
 */
@Entity
@Table(name = "sinal")
@NamedQueries({
	// findUsuariosParaNotificar
	@NamedQuery(name = "Sinal.findUsuariosParaNotificar", query = "select distinct sinaCdUsu from Sinal order by sinaCdUsu"),

	// findProcessosDoUsuarioParaNotificar
	@NamedQuery(name = "Sinal.findProcessosDoUsuarioParaNotificar", query = "select sinaCdProc, sinaDfRecente from Sinal where sinaCdUsu = :usuario and sinaLgFavorito <> 0"),

	// findUsuario
	@NamedQuery(name = "Sinal.findUsuario", query = "select s from Sinal s where s.sinaCdUsu = :usuario and s.sinaLgInterno = :interno and (s.sinaLgFavorito <> 0 or s.sinaDfRecente is not null) order by s.sinaDfRecente desc"),

		// findUsuarioNumero
		@NamedQuery(name = "Sinal.findUsuarioNumero", query = "select s from Sinal s where s.sinaCdProc = :numero and s.sinaCdUsu = :usuario and s.sinaLgInterno = :interno") })
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
	private Date sinaDfRecente;

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

	public Date getSinaDfRecente() {
		return this.sinaDfRecente;
	}

	public void setSinaDfRecente(Date sinaDfRecente) {
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