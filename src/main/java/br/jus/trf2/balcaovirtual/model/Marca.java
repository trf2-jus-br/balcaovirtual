package br.jus.trf2.balcaovirtual.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

/**
 * The persistent class for the marca database table.
 * 
 */
@Entity
@NamedQuery(name = "Marca.findAll", query = "SELECT m FROM Marca m")
public class Marca implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MARC_ID")
	private int marcId;

	@Column(name = "MARC_CD_USU")
	private String marcCdUsu;

	@Column(name = "MARC_DF_ALTERACAO")
	private Timestamp marcDfAlteracao;

	@Column(name = "MARC_ID_PECA")
	private int marcIdPeca;

	@Column(name = "MARC_IE_UNIDADE")
	private Integer marcIeUnidade;

	@Column(name = "MARC_IE_USU")
	private int marcIeUsu;

	@Column(name = "MARC_LG_INTERNO")
	private byte marcLgInterno;

	@Column(name = "MARC_NM_USU")
	private String marcNmUsu;

	@Column(name = "MARC_NR_PAG_FINAL")
	private BigDecimal marcNrPagFinal;

	@Column(name = "MARC_NR_PAG_INICIAL")
	private BigDecimal marcNrPagInicial;

	@Column(name = "MARC_TX_CONTEUDO")
	private String marcTxConteudo;

	// bi-directional many-to-one association to Estilo
	@ManyToOne
	@JoinColumn(name = "ESTI_ID")
	private Estilo estilo;

	// bi-directional many-to-one association to Processo
	@ManyToOne
	@JoinColumn(name = "PROC_ID")
	private Processo processo;

	// bi-directional many-to-one association to TipoMarcaItem
	@ManyToOne
	@JoinColumn(name = "TIMI_ID")
	private TipoMarcaItem tipoMarcaItem;

	public Marca() {
	}

	public int getMarcId() {
		return this.marcId;
	}

	public void setMarcId(int marcId) {
		this.marcId = marcId;
	}

	public String getMarcCdUsu() {
		return this.marcCdUsu;
	}

	public void setMarcCdUsu(String marcCdUsu) {
		this.marcCdUsu = marcCdUsu;
	}

	public Timestamp getMarcDfAlteracao() {
		return this.marcDfAlteracao;
	}

	public void setMarcDfAlteracao(Timestamp marcDfAlteracao) {
		this.marcDfAlteracao = marcDfAlteracao;
	}

	public int getMarcIdPeca() {
		return this.marcIdPeca;
	}

	public void setMarcIdPeca(int marcIdPeca) {
		this.marcIdPeca = marcIdPeca;
	}

	public Integer getMarcIeUnidade() {
		return this.marcIeUnidade;
	}

	public void setMarcIeUnidade(int marcIeUnidade) {
		this.marcIeUnidade = marcIeUnidade;
	}

	public int getMarcIeUsu() {
		return this.marcIeUsu;
	}

	public void setMarcIeUsu(int marcIeUsu) {
		this.marcIeUsu = marcIeUsu;
	}

	public byte getMarcLgInterno() {
		return this.marcLgInterno;
	}

	public void setMarcLgInterno(byte marcLgInterno) {
		this.marcLgInterno = marcLgInterno;
	}

	public String getMarcNmUsu() {
		return this.marcNmUsu;
	}

	public void setMarcNmUsu(String marcNmUsu) {
		this.marcNmUsu = marcNmUsu;
	}

	public BigDecimal getMarcNrPagFinal() {
		return this.marcNrPagFinal;
	}

	public void setMarcNrPagFinal(BigDecimal marcNrPagFinal) {
		this.marcNrPagFinal = marcNrPagFinal;
	}

	public BigDecimal getMarcNrPagInicial() {
		return this.marcNrPagInicial;
	}

	public void setMarcNrPagInicial(BigDecimal marcNrPagInicial) {
		this.marcNrPagInicial = marcNrPagInicial;
	}

	public String getMarcTxConteudo() {
		return this.marcTxConteudo;
	}

	public void setMarcTxConteudo(String marcTxConteudo) {
		this.marcTxConteudo = marcTxConteudo;
	}

	public Estilo getEstilo() {
		return this.estilo;
	}

	public void setEstilo(Estilo estilo) {
		this.estilo = estilo;
	}

	public Processo getProcesso() {
		return this.processo;
	}

	public void setProcesso(Processo processo) {
		this.processo = processo;
	}

	public TipoMarcaItem getTipoMarcaItem() {
		return this.tipoMarcaItem;
	}

	public void setTipoMarcaItem(TipoMarcaItem tipoMarcaItem) {
		this.tipoMarcaItem = tipoMarcaItem;
	}

}