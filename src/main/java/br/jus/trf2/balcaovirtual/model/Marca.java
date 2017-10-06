package br.jus.trf2.balcaovirtual.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the marca database table.
 * 
 */
@Entity
@Table(name="marca")
@NamedQuery(name = "Marca.findAll", query = "SELECT m FROM Marca m")
public class Marca implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MARC_ID")
	private Long marcId;

	@Column(name = "MARC_CD_USU")
	private String marcCdUsu;

	@Column(name = "MARC_DF_ALTERACAO")
	private Timestamp marcDfAlteracao;

	@Column(name = "MARC_ID_PECA")
	private Long marcIdPeca;

	@Column(name = "MARC_IE_UNIDADE")
	private Long marcIeUnidade;

	@Column(name = "MARC_IE_USU")
	private Long marcIeUsu;

	@Column(name = "MARC_LG_INTERNO")
	private boolean marcLgInterno;

	@Column(name = "MARC_NM_USU")
	private String marcNmUsu;

	@Column(name = "MARC_NR_PAG_FINAL")
	private Integer marcNrPagFinal;

	@Column(name = "MARC_NR_PAG_INICIAL")
	private Integer marcNrPagInicial;

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

	public Long getMarcId() {
		return this.marcId;
	}

	public void setMarcId(Long marcId) {
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

	public Long getMarcIdPeca() {
		return this.marcIdPeca;
	}

	public void setMarcIdPeca(Long marcIdPeca) {
		this.marcIdPeca = marcIdPeca;
	}

	public Long getMarcIeUnidade() {
		return this.marcIeUnidade;
	}

	public void setMarcIeUnidade(Long marcIeUnidade) {
		this.marcIeUnidade = marcIeUnidade;
	}

	public Long getMarcIeUsu() {
		return this.marcIeUsu;
	}

	public void setMarcIeUsu(Long marcIeUsu) {
		this.marcIeUsu = marcIeUsu;
	}

	public boolean getMarcLgInterno() {
		return this.marcLgInterno;
	}

	public void setMarcLgInterno(boolean marcLgInterno) {
		this.marcLgInterno = marcLgInterno;
	}

	public String getMarcNmUsu() {
		return this.marcNmUsu;
	}

	public void setMarcNmUsu(String marcNmUsu) {
		this.marcNmUsu = marcNmUsu;
	}

	public Integer getMarcNrPagFinal() {
		return this.marcNrPagFinal;
	}

	public void setMarcNrPagFinal(Integer marcNrPagFinal) {
		this.marcNrPagFinal = marcNrPagFinal;
	}

	public Integer getMarcNrPagInicial() {
		return this.marcNrPagInicial;
	}

	public void setMarcNrPagInicial(Integer marcNrPagInicial) {
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