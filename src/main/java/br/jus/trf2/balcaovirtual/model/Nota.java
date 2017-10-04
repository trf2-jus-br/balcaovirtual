package br.jus.trf2.balcaovirtual.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * The persistent class for the notaa database table.
 * 
 */
@Entity
@NamedQueries({
		// findAll
		@NamedQuery(name = "Nota.findAll", query = "SELECT n FROM Nota n"),

		// findProcesso
		@NamedQuery(name = "Nota.findProcesso", query = "SELECT n FROM Nota n WHERE n.processo = :processo and ((n.notaLgPessoal = 1 and n.notaIeUsu = :ieusuario and n.notaIeUsu is not null) or (n.notaLgPessoal = 0 and n.notaIeUnidade = :ieunidade and n.notaIeUnidade is not null))") })

public class Nota implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "NOTA_ID")
	private Long notaId;

	@Column(name = "NOTA_TX_CONTEUDO")
	private String notaTxConteudo;

	@Column(name = "NOTA_LG_PESSOAL")
	private boolean notaLgPessoal;

	@Column(name = "NOTA_DF_ALTERACAO")
	private Date notaDfAlteracao;

	@Column(name = "NOTA_CD_USU")
	private String notaCdUsu;

	@Column(name = "NOTA_LG_INTERNO")
	private boolean notaLgInterno;

	@Column(name = "NOTA_NM_USU")
	private String notaNmUsu;

	@Column(name = "NOTA_IE_USU")
	private long notaIeUsu;

	@Column(name = "NOTA_IE_UNIDADE")
	private Long notaIeUnidade;

	// bi-directional many-to-one association to Processo
	@ManyToOne
	@JoinColumn(name = "PROC_ID")
	private Processo processo;

	public Nota() {
	}

	public Long getNotaId() {
		return notaId;
	}

	public void setNotaId(Long notaId) {
		this.notaId = notaId;
	}

	public String getNotaTxConteudo() {
		return notaTxConteudo;
	}

	public void setNotaTxConteudo(String notaTxConteudo) {
		this.notaTxConteudo = notaTxConteudo;
	}

	public boolean getNotaLgPessoal() {
		return notaLgPessoal;
	}

	public void setNotaLgPessoal(boolean notaLgPessoal) {
		this.notaLgPessoal = notaLgPessoal;
	}

	public Date getNotaDfAlteracao() {
		return notaDfAlteracao;
	}

	public void setNotaDfAlteracao(Date date) {
		this.notaDfAlteracao = date;
	}

	public String getNotaCdUsu() {
		return notaCdUsu;
	}

	public void setNotaCdUsu(String notaCdUsu) {
		this.notaCdUsu = notaCdUsu;
	}

	public boolean getNotaLgInterno() {
		return notaLgInterno;
	}

	public void setNotaLgInterno(boolean notaLgInterno) {
		this.notaLgInterno = notaLgInterno;
	}

	public String getNotaNmUsu() {
		return notaNmUsu;
	}

	public void setNotaNmUsu(String notaNmUsu) {
		this.notaNmUsu = notaNmUsu;
	}

	public Long getNotaIeUnidade() {
		return notaIeUnidade;
	}

	public void setNotaIeUnidade(Long notaIeUnidade) {
		this.notaIeUnidade = notaIeUnidade;
	}

	public long getNotaIeUsu() {
		return notaIeUsu;
	}

	public void setNotaIeUsu(long notaIeUsu) {
		this.notaIeUsu = notaIeUsu;
	}

	public Processo getProcesso() {
		return processo;
	}

	public void setProcesso(Processo processo) {
		this.processo = processo;
	}

}