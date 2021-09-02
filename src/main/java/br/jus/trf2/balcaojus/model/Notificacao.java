package br.jus.trf2.balcaojus.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "notificacao")
@NamedQueries({
		// findUsuariosParaNotificar
		@NamedQuery(name = "Notificacao.findUsuariosParaNotificar", query = "select distinct notiCdUsu from Notificacao order by notiCdUsu"),

		// findTokensDoUsuarioParaNotificar
		@NamedQuery(name = "Notificacao.findTokensDoUsuarioParaNotificar", query = "select notiCd from Notificacao where notiCdUsu = :usuario"),

		// findTokenParaNotificar
		@NamedQuery(name = "Notificacao.findTokenParaNotificar", query = "select n from Notificacao n where n.notiCd = :token") })
public class Notificacao implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "NOTI_ID")
	private Long notiId;

	@Column(name = "NOTI_CD_USU")
	private String notiCdUsu;

	@Column(name = "NOTI_CD")
	private String notiCd;

	public Notificacao() {
	}

	public Long getNotiId() {
		return notiId;
	}

	public void setNotiId(Long notiId) {
		this.notiId = notiId;
	}

	public String getNotiCdUsu() {
		return notiCdUsu;
	}

	public void setNotiCdUsu(String notiCdUsu) {
		this.notiCdUsu = notiCdUsu;
	}

	public String getNotiCd() {
		return notiCd;
	}

	public void setNotiCd(String notiCd) {
		this.notiCd = notiCd;
	}

}