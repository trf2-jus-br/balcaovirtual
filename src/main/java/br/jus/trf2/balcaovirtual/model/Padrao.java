package br.jus.trf2.balcaovirtual.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the sinal database table.
 * 
 */
@Entity
@Table(name = "padrao")
@NamedQueries({
		// findPadroesDoUsuario
		@NamedQuery(name = "Padrao.findPadroesDoUsuario", query = "select s from Padrao s where s.padrCdUsu = :usuario order by s.padrDfInclusao desc") })
public class Padrao implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PADR_ID")
	private Long padrId;

	@ManyToOne
	@JoinColumn(name = "SIST_ID", nullable = true)
	private Sistema sistema;

	@Column(name = "PADR_CD_PROC", nullable = true)
	private String padrCdProc;

	@Column(name = "PADR_CD_USU", nullable = false)
	private String padrCdUsu;

	@Column(name = "PADR_DF_INCLUSAO", nullable = false)
	private Date padrDfInclusao;

	@Column(name = "PADR_DF_MODIFICACAO", nullable = false)
	private Date padrDfModificacao;

	@Lob
	@Column(name = "PADR_TX_CONTEUDO", nullable = false)
	private String padrTxConteudo;

	public Padrao() {
	}

	public Long getPadrId() {
		return padrId;
	}

	public void setPadrId(Long padrId) {
		this.padrId = padrId;
	}

	public Sistema getSistema() {
		return sistema;
	}

	public void setSistema(Sistema sistema) {
		this.sistema = sistema;
	}

	public String getPadrCdProc() {
		return padrCdProc;
	}

	public void setPadrCdProc(String padrCdProc) {
		this.padrCdProc = padrCdProc;
	}

	public String getPadrCdUsu() {
		return padrCdUsu;
	}

	public void setPadrCdUsu(String padrCdUsu) {
		this.padrCdUsu = padrCdUsu;
	}

	public Date getPadrDfInclusao() {
		return padrDfInclusao;
	}

	public void setPadrDfInclusao(Date padrDfInclusao) {
		this.padrDfInclusao = padrDfInclusao;
	}

	public Date getPadrDfModificacao() {
		return padrDfModificacao;
	}

	public void setPadrDfModificacao(Date padrDfModificacao) {
		this.padrDfModificacao = padrDfModificacao;
	}

	public String getPadrTxConteudo() {
		return padrTxConteudo;
	}

	public void setPadrTxConteudo(String padrTxConteudo) {
		this.padrTxConteudo = padrTxConteudo;
	}

}