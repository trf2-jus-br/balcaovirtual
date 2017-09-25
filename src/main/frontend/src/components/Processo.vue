<template>
  <div class="hello">
    <!--=== Profile ===-->
    <div class="container content profile">
      <div class="row pt-5" v-if="errormsg">
        <div class="col col-sm-12">
          <p class="alert alert-danger">
            <strong>Erro!</strong> {{errormsg}}
          </p>
        </div>
      </div>

      <div class="row pt-5" v-if="warningmsg">
        <div class="col col-sm-12">
          <p class="alert alert-warning">
            <strong>Atenção!</strong> {{warningmsg}}
          </p>
        </div>
      </div>

      <div v-if="!errormsg &amp;&amp; proc &amp;&amp; proc.dadosBasicos">
        <div class="row xd-print-block mt-3 mb-3">
          <div class="col-md-12">
            <h4 class="text-center mb-0">
              Processo
              <span v-if="proc.dadosBasicos.outroParametro.indEletronico == 'S'">
                Digital</span>
              <span v-if="proc &amp;&amp; proc.dadosBasicos.outroParametro.indEletronico != 'S'">
                Físico</span>: {{proc.dadosBasicos.numero}}
              <a v-if="this.favorito !== undefined && !this.favorito" href="" @click.prevent="favoritar(true)">
                <span class="fa fa-star-o icone-em-linha" title="Acrescentar à lista de processos favoritos"></span>
              </a>
              <a v-if="this.favorito !== undefined && this.favorito" href="" @click.prevent="favoritar(false)">
                <span class="fa fa-star icone-em-linha" title="Remover da lista de processos favoritos"></span>
              </a>

            </h4>
          </div>
        </div>

        <timeline :timeline="timeline"></timeline>

        <template v-if="proc &amp;&amp; proc.dadosBasicos">
          <!-- QUADROS COLORIDOS -->
          <div class="d-print-none mt-3" v-if="errormsg === undefined">
            <!-- Profile Content -->
            <div class="card-deck">
              <div class="card text-white bg-primary card-consulta-processual mb-3">
                <div class="card-body">
                  <img id="logo-header" class="float-right" src="../assets/users.png" height="64"></img>
                  <p class="card-text">
                    <small>{{fixed.partesAtivas[0].tipoAtuacao}}</small>
                    <br />
                    <b>{{fixed.partesAtivas[0].pessoa.nome}}</b>
                    <b v-if="fixed.partesAtivas.length>1">&nbsp;
                      <a href="" @click.prevent="mostrarPartes(true)">+{{fixed.partesAtivas.length-1}}</a>
                    </b>
                  </p>
                  <p class="card-text">
                    <small>{{fixed.partesPassivas[0].tipoAtuacao}}</small>
                    <br />
                    <b>{{fixed.partesPassivas[0].pessoa.nome}}</b>
                    <b v-if="fixed.partesPassivas.length>1">&nbsp;
                      <a href="" @click.prevent="mostrarPartes(true)">+{{fixed.partesPassivas.length-1}}</a>
                    </b>
                  </p>
                  <a v-if="!$parent.settings.mostrarPartes" @click.prevent="mostrarPartes(true)" class="card-link float-right" href="">Ver partes...</a>
                </div>
              </div>

              <div class="card text-white bg-success card-consulta-processual mb-3">
                <div class="card-body">
                  <img id="logo-header" class="float-right" src="../assets/judicia.png" height="64"></img>
                  <p class="card-text" v-if="false">
                    <small>PROCESSO
                      <span v-if="proc.dadosBasicos.outroParametro.indEletronico == 'S'">
                        DIGITAL</span>
                      <span v-if="proc &amp;&amp; proc.dadosBasicos.outroParametro.indEletronico != 'S'">
                        FÍSICO</span>
                    </small>
                    <br>
                    <b>{{proc.dadosBasicos.numero}}</b>
                  </p>
                  <p class="card-text">
                    <small>DATA DE DISTRIBUIÇÃO</small>
                    <br>
                    <b v-html="fixed.dataAjuizamento"></b>
                  </p>
                  <p class="card-text">
                    <small>ÓRGÃO JULGADOR</small>
                    <br>
                    <b style="color: #fff">{{proc.dadosBasicos.orgaoJulgador.nomeOrgao}}</b>
                  </p>

                  <p class="card-text">
                    <small>MAGISTRADO</small>
                    <br>
                    <b>{{proc.dadosBasicos.outroParametro.nomeMagistrado}}</b>
                  </p>
                  <a v-if="!$parent.settings.mostrarProcessosRelacionados" class="card-link float-right" href="" @click.prevent="mostrarProcessosRelacionados(true)">Ver processos relacionados...</a>
                </div>
              </div>

              <div class="card text-white bg-warning card-consulta-processual mb-3">
                <div class="card-body">
                  <img id="logo-header" class="float-right" src="../assets/monitor.png" height="64"></img>
                  <p class="card-text" v-if="fixed.valorCausa">
                    <small>VALOR DA CAUSA</small>
                    <br>
                    <b>{{fixed.valorCausa}}</b>
                  </p>
                  <p class="card-text">
                    <small>CLASSE</small>
                    <br>
                    <b>{{fixed.classeProcessualDescricao}}</b>
                  </p>
                  <p class="card-text" v-if="fixed.assuntoPrincipalDescricao">
                    <small>ASSUNTO PRINCIPAL</small>
                    <br>
                    <b>{{fixed.assuntoPrincipalDescricao}}</b>
                  </p>
                  <p class="card-text" v-if="proc.dadosBasicos.outroParametro.numCDA">
                    <small>CDA</small>
                    <br>
                    <b>{{proc.dadosBasicos.outroParametro.numCDA[0]}}</b>
                    <b v-if="proc.dadosBasicos.outroParametro.numCDA.length>1">&nbsp;
                      <a href="">+{{proc.dadosBasicos.outroParametro.numCDA.length-1}}</a>
                    </b>
                  </p>
                  <a v-if="!$parent.settings.mostrarDadosComplementares" class="card-link float-right" href="" @click.prevent="mostrarDadosComplementares(true)">Ver mais...</a>
                </div>
              </div>
            </div>
          </div>

          <!-- PARTES -->
          <div v-bind:class="{row: true, 'd-print-block': !$parent.settings.mostrarPartes}">
            <div class="col col-sm-12">
              <div class="card mb-3 border-primary">
                <div class="card-header">
                  Partes
                  <button type="button" class="close d-print-none" @click="mostrarPartes(false)">
                    <span aria-hidden="true">&times;</span>
                  </button>
                </div>
                <div class="card-body pb-0" v-for="polo in proc.dadosBasicos.polo">
                  <h6 class="card-subtitle mb-2 text-muted">
                    <u>Pólo {{(polo.polo == 'AT') ? 'Ativo' : 'Passivo'}}</u>
                  </h6>
                  <template v-for="parte in polo.parte">
                    <div class="row">
                      <div class="col col-sm-6">
                        <label>{{parte.tipoAtuacao}}</label>
                        <p>{{parte.pessoa.nome}}</p>
                      </div>
                      <div class="col col-sm-3">
                        <label>Tipo</label>
                        <p>{{parte.pessoa.tipoPessoa}}</p>
                      </div>
                      <div class="col col-sm-3">
                        <label>Assistência Judiciária</label>
                        <p>{{parte.assistenciaJudiciaria ? "Sim" : "Não"}}</p>
                      </div>
                    </div>
                    <div class="row" v-for="adv in parte.advogado">
                      <div class="col offset-sm-1 col-sm-5">
                        <label>{{tipoRepresentante[adv.tipoRepresentante]}}</label>
                        <p>{{adv.nome}}</p>
                      </div>
                      <div class="col col-sm-3">
                        <label>Inscrição</label>
                        <p>{{adv.inscricao}}</p>
                      </div>
                      <div class="col col-sm-3">
                        <label>Intimação</label>
                        <p>{{adv.intimacao ? "Sim" : "Não"}}</p>
                      </div>
                    </div>
                  </template>
                </div>
              </div>
            </div>
          </div>

          <!-- QUADRO COLORIDO DE PROCESSOS VINCULADOS -->
          <div v-if="$parent.settings.mostrarProcessosRelacionados &amp;&amp; (fixed.processoVinculado || fixed.recursoTrf)" class="mt-1 d-print-none">
            <!-- Profile Content -->
            <div class="card-deck">
              <div class="card border-success card-consulta-processual mb-3" v-if="fixed.processoVinculado">
                <div class="card-header">
                  Processos Vinculados
                  <button type="button" class="close d-print-none" @click="mostrarProcessosRelacionados(false)">
                    <span aria-hidden="true">&times;</span>
                  </button>
                </div>
                <div class="card-body" style="padding: 0">
                  <p class="card-text mb-0">
                    <table class="table table-sm mb-0">
                      <tbody>
                        <tr v-for="pv in fixed.processoVinculado">
                          <td style="padding-left: 20px">
                            <span v-html="pv.link"></span> - {{pv.nomeClasse}} - {{pv.suporte}}</td>
                        </tr>
                      </tbody>
                    </table>
                  </p>
                </div>
              </div>
              <div class="card border-success card-consulta-processual mb-3" v-if="fixed.recursoTrf">
                <div class="card-header">
                  Recursos
                  <button type="button" class="close d-print-none" @click="mostrarProcessosRelacionados(false)">
                    <span aria-hidden="true">&times;</span>
                  </button>
                </div>
                <div class="card-body" style="padding: 0">
                  <p class="card-text mb-0">
                    <table class="table table-sm mb-0">
                      <tbody>
                        <tr v-for="pv in fixed.recursoTrf">
                          <td style="padding-left: 20px">
                            <span v-html="pv.link"></span> - {{pv.nomeClasse}} - {{pv.suporte}}</td>
                        </tr>
                      </tbody>
                    </table>
                  </p>
                </div>
              </div>
            </div>
          </div>

          <!-- QUADRO SUBSTITUTO PARA A IMPRESSAO -->
          <div class="row d-print-block">
            <div class="col col-sm-12">
              <div class="card mb-3 border-success">
                <div class="card-header">Dados Principais</div>
                <div class="card-body pb-0">
                  <div class="row">
                    <div class="col col-sm-3">
                      <label>Data/Hora de Distribuição</label>
                      <p v-html="fixed.dataAjuizamento"></p>
                    </div>
                    <div class="col col-sm-3">
                      <label>Órgão Julgador</label>
                      <p>{{proc.dadosBasicos.orgaoJulgador.nomeOrgao}}</p>
                    </div>
                    <div class="col col-sm-3">
                      <label>Magistrado</label>
                      <p>{{proc.dadosBasicos.outroParametro.nomeMagistrado}}</p>
                    </div>
                    <div class="col col-sm-3">
                      <label>Valor da Causa</label>
                      <p>{{fixed.valorCausa}}</p>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col col-sm-3">
                      <label>Classe Processual</label>
                      <p>{{fixed.classeProcessualDescricao}}</p>
                    </div>
                    <div class="col col-sm-3" v-if="fixed.assuntoPrincipalDescricao">
                      <label>Assunto Principal</label>
                      <p>{{fixed.assuntoPrincipalDescricao}}</p>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col col-sm-6" v-if="proc.dadosBasicos.outroParametro.processoVinculado">
                      <label>Processos Vinculados</label>
                      <table class="table table-sm mb-1 table-striped">
                        <tbody>
                          <tr v-for="pv in fixed.processoVinculado">
                            <td style="padding-left:0;padding-right:0;">
                              <span v-html="pv.link"></span> - {{pv.nomeClasse}} - {{pv.suporte}}</td>
                          </tr>
                        </tbody>
                      </table>
                    </div>
                    <div class="col col-sm-6" v-if="proc.dadosBasicos.outroParametro.recursoTrf">
                      <label>Recursos</label>
                      <table class="table table-sm mb-1 table-striped">
                        <tbody>
                          <tr v-for="pv in fixed.recursoTrf">
                            <td style="padding-left:0;padding-right:0;">
                              <span v-html="pv.link"></span> - {{pv.nomeClasse}} - {{pv.suporte}}</td>
                          </tr>
                        </tbody>
                      </table>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div v-if="$parent.settings.mostrarDadosComplementares" v-bind:class="{row: true}">
            <div class="col col-sm-12">
              <div class="card mb-3 border-warning">
                <div class="card-header">
                  Dados Complementares
                  <button type="button" class="close d-print-none" @click="mostrarDadosComplementares(false)">
                    <span aria-hidden="true">&times;</span>
                  </button>
                </div>
                <div class="card-body pb-0">
                  <div class="row">
                    <div class="col col-sm-3" v-if="proc.dadosBasicos.outroParametro.dataConsulta">
                      <label>Data de Protocolo</label>
                      <p v-html="proc.dadosBasicos.outroParametro.dataProtocolo"></p>
                    </div>

                    <div class="col col-sm-3" v-if="proc.dadosBasicos.outroParametro.numProcAdm">
                      <label>Processos Administrativos</label>
                      <p v-html="proc.dadosBasicos.outroParametro.numProcAdm"></p>
                    </div>

                    <div class="col col-sm-3" v-if="fixed.numCDAs">
                      <label>CDA's</label>
                      <p v-html="fixed.numCDAs"></p>
                    </div>

                    <div class="col col-sm-3" v-if="proc.dadosBasicos.outrosnumeros[0]">
                      <label>Número Antigo</label>
                      <p v-html="proc.dadosBasicos.outrosnumeros[0]"></p>
                    </div>

                    <div class="col col-sm-6" v-if="proc.dadosBasicos.outroParametro.peticaoPendenteJuntada">
                      <label>Petições Pendentes de Juntada</label>
                      <p v-html="proc.dadosBasicos.outroParametro.peticaoPendenteJuntada"></p>
                    </div>

                    <div class="col col-sm-3" v-if="proc.dadosBasicos.outroParametro.processoOriginario">
                      <label>Processo Originário</label>
                      <p v-html="proc.dadosBasicos.outroParametro.processoOriginario"></p>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col col-sm-12">
                      <label>Classe Processual</label>
                      <p>{{fixed.classeProcessualDescricaoCompleta}}</p>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col col-sm-12">
                      <label>Assunto(s) CNJ</label>
                      <p v-for="ass in proc.dadosBasicos.assunto">{{ass.descricaoCompleta}}</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="row" v-if="$parent.settings.filtrarMovimentos" id="filtrarMovimentos">
            <div class="col col-sm-12">
              <p class="alert alert-info alert-dismissible fade show" role="alert">
                <button type="button" @click="filtrarMovimentos()" class="close" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
                <span class="input-group" style="width: 50%">
                  <span class="input-group-addon">&#128269;</span>
                  <input type="text" list="lst_filtrarMovimentosInput" id="filtrarMovimentosInput" class="form-control" placeholder="Filtrar Movimentos" v-model="filtro" ref="filtro">
                  <datalist id="lst_filtrarMovimentosInput">
                    <option>#marca</option>
                    <option>#distribuicao</option>
                    <option>#intimacao</option>
                    <option>#remessa</option>
                    <option>#devolucao</option>
                    <option>#juntada</option>
                    <option>#audiencia</option>
                    <option>#conclusao</option>
                    <option>#sentenca</option>
                    <option>#suspensao</option>
                    <option>#apelacao</option>
                    <option>#baixa</option>
                    <option>#arquivamento</option>
                  </datalist>
                </span>
              </p>
            </div>
          </div>

          <div class="row" v-if="errormsg === undefined">
            <div class="col col-sm-12">
              <table class="table table-sm mb-0 table-responsive">
                <thead class="thead-inverse">
                  <tr>
                    <th>Data/Hora</th>
                    <th>Movimento</th>
                    <th>Peça</th>
                    <th style="text-align: right">Página Inicial</th>
                    <th style="text-align: right">Página Final</th>
                  </tr>
                </thead>
                <tbody>
                  <template v-for="movdoc in filtrados">
                    <tr v-bind:class="{odd: movdoc.odd, highlightable: true}" v-bind:id="'mov' + movdoc.mov.dataHora">
                      <td v-if="movdoc.rowspan && !movdoc.hidemov" :rowspan="movdoc.rowspan">
                        <span v-html="formatDDMMYYYHHMM(movdoc.mov.dataHora)"></span>
                      </td>
                      <td v-if="movdoc.rowspan && !movdoc.hidemov" :rowspan="movdoc.rowspan" v-bind:class="{'text-success-dark': movdoc.doc.exibirTexto !== undefined}">{{movdoc.mov.movimentoLocal ? movdoc.mov.movimentoLocal.descricao : ''}}</td>
                      <template v-if="movdoc.doc">
                        <td>
                          <p class="mb-0">
                            <a href="" target="_blank" @click.prevent="mostrarPeca(movdoc.doc.idDocumento)">{{movdoc.doc.descricao}}</a>
                            <a href="" v-if="marcasativas &amp;&amp; movdoc.doc.idDocumento" @click.prevent="exibirProcessoPecaDetalhes(movdoc)">
                              <span class="fa fa-pencil icone-em-linha"></span>
                            </a>
                            <a href="" v-if="movdoc.doc.exibirTexto == false" @click.prevent="mostrarTexto(movdoc.doc, true)">
                              <span class="fa fa-search icone-em-linha"></span>
                            </a>
                          </p>
                          <br v-if="false &amp;&amp; movdoc.marca &amp;&amp; movdoc.marca.length > 0">
                          <template v-for="m in movdoc.marca">
                            <p class="mt-1 mb-0" style="line-height: 1.4">
                              <a href="" class="marca-ref" @click.prevent="exibirProcessoPecaDetalhes(movdoc, m)">
                                <span class="marca" :class="{'marca-yellow' : m.idestilo == 2, 'marca-green': m.idestilo == 4, 'marca-pink': m.idestilo == 3, 'marca-blue': m.idestilo == 1}">{{m.texto}}
                                  <span class="inquebravel" v-if="m.paginicial" v-html="'p.&nbsp' + m.paginicial + (m.paginicial !== m.pagfinal ? '&#8209;' + m.pagfinal : '') ">
                                  </span>
                                </span>
                              </a>
                            </p>
                          </template>
                        </td>
                        <td style="text-align: right">{{movdoc.doc && movdoc.doc.outroParametro ? movdoc.doc.outroParametro.paginaInicial : ''}}</td>
                        <td style="text-align: right">{{movdoc.doc && movdoc.doc.outroParametro ? movdoc.doc.outroParametro.paginaFinal : ''}}</td>
                      </template>
                      <template v-else>
                        <td colspan="3"></td>
                      </template>
                    </tr>
                    <tr v-if="movdoc.doc.exibirTexto" v-bind:class="{odd: movdoc.odd}">
                      <td colspan="5">
                        <p class="alert alert-success mb-0 alert-dismissible fade show" role="alert">
                          <button type="button" class="close d-print-none" @click="mostrarTexto(movdoc.doc, false)">
                            <span aria-hidden="true">&times;</span>
                          </button>
                          <span v-html="movdoc.doc.outroParametro.textoMovimento"></span>
                        </p>
                      </td>
                    </tr>
                  </template>
                </tbody>
              </table>
            </div>
          </div>

          <div class="row no-gutters">
            <div class="col col-auto mr-1">
              <button type="button" v-if="!$parent.settings.filtrarMovimentos" @click="filtrarMovimentos('')" class="btn btn-secondary d-print-none mt-3">Filtrar Movimentos
              </button>
            </div>
            <div class="col col-auto mr-1">
              <button type="button" v-if="marcasativas &amp;&amp; (filtro !== '#marca')" @click="filtrarMovimentos('#marca')" class="btn btn-secondary d-print-none mt-3">Filtrar Marcas
              </button>
            </div>
            <div class="col col-auto ml-auto">
              <button type="button" @click="mostrarCompleto()" id="download" class="btn btn-info d-print-none mt-3">PDF Completo
              </button>
            </div>
            <div class="col col-auto ml-1">
              <button type="button" @click="imprimir()" id="imprimir" class="btn btn-info d-print-none mt-3">Imprimir</button>
            </div>
          </div>

          <div class="row">
            <div class="col col-sm-12">
              <hr class="mt-5 mb-1" />
              <p class="text-center">
                As informações aqui contidas não produzem efeitos legais. Somente a publicação no D.O. tem validade para contagem de prazos.
                <br>Consulta realizada em: {{proc.dadosBasicos.outroParametro.dataConsulta}}.
              </p>
            </div>
          </div>
        </template>
      </div>
    </div>

    <processo-peca-detalhes ref="processoPecaDetalhes" @ok="salvarProcessoPecaDetalhes" @remove="excluirProcessoPecaDetalhes"></processo-peca-detalhes>
  </div>
</template>

<script>
import TimelineBL from '../bl/timeline.js'
import ProcessoBL from '../bl/processo.js'
import UtilsBL from '../bl/utils.js'
import Timeline from './timeline'
import ProcessoPecaDetalhes from './ProcessoPecaDetalhes'
import CnjClasseBL from '../bl/cnj-classe.js'
import CnjAssuntoBL from '../bl/cnj-assunto.js'
import { Bus } from '../bl/bus.js'

export default {
  name: 'processo',
  mounted () {
    this.$on('filtrar', (texto) => { this.filtrarMovimentos(texto) })

    // Validar o número do processo
    this.$nextTick(function () {
      Bus.$emit('block', 20)
      this.$http.get('processo/' + this.numero + '/validar').then(response => {
        this.orgao = response.data.orgao
        this.$http.get('processo/' + this.numero + '/consultar?orgao=' + this.orgao).then(
          response => {
            console.log('carregou')

            Bus.$emit('release')
            try {
              this.proc = response.data.value
              if (this.proc.movimento) {
                this.proc.movimento = this.proc.movimento.sort(function (a, b) {
                  if (a.dataHora < b.dataHora) {
                    return 1
                  }
                  if (a.dataHora > b.dataHora) {
                    return -1
                  }
                  return 0
                })
              }
              var interno = !!this.$parent.jwt.origin
              this.timeline = TimelineBL.updateTimeline(this.orgao, this.proc, interno)
              this.fixed = ProcessoBL.fixProc(this.proc)
              this.getDescriptions()
              this.getMarcadores()
              this.getMarcas()

              this.$http.post('processo/' + this.numero + '/sinalizar', { recente: true }).then(response => {
                this.favorito = !!response.data.processo.favorito
              }, error => {
                this.warningmsg = error.data.errormsg
              })
            } catch (e) {
              console.error(e)
            }
          },
          error => {
            Bus.$emit('release')
            UtilsBL.errormsg(error, this)
          })
      }, error => {
        Bus.$emit('release')
        this.errormsg = 'Não foi possível obter informações sobre o processo ' + this.numero + ': ' + error.data.errormsg
      })
    })
  },
  data () {
    return {
      favorito: undefined,
      timeline: TimelineBL.emptyTimeline(),
      fixed: undefined,
      modified: undefined,
      numero: ProcessoBL.somenteNumeros(this.$route.params.numero),
      gui: {},
      filtro: undefined,
      tipoRepresentante: {
        A: 'Advogado',
        E: 'Escritório de Advocacia',
        M: 'Ministério Público',
        D: 'Defensor Público',
        P: 'Advogado Público'
      },
      errormsg: undefined,
      warningmsg: undefined,
      partes: false,
      dadosComplementares: false,
      proc: undefined,
      marcadores: [],
      marcasativas: true
    }
  },
  computed: {
    filtrados: function () {
      // Referência à this.modified é necessária para recalcular quando mostra o texto
      console.log('recalculando filtrados...', this.modified)
      var a = ProcessoBL.filtrar(this.fixed.movdoc, this.filtro)
      return a
    }
  },
  methods: {
    getMarcadores: function () {
      // Carregar os marcadores da classe
      this.$http.get('classe/' + this.proc.dadosBasicos.classeProcessual + '/marcadores').then(response => {
        if (!response.data.list) return
        for (var i = 0; i < response.data.list.length; i++) {
          this.marcadores.push(response.data.list[i].texto)
        }
      }, error => {
        if (error.data.errormsg === 'disabled') {
          this.marcasativas = false
          return
        }
        UtilsBL.errormsg(error, this)
      })
    },
    getMarcas: function () {
      // Carregar os marcadores da classe
      this.$http.get('processo/' + this.numero + '/marcas?orgao=' + this.orgao).then(response => {
        // if (!response.data.list) return
        for (var i = 0; i < response.data.list.length; i++) {
          var marca = response.data.list[i]
          for (var j = 0; j < this.fixed.movdoc.length; j++) {
            var movdoc = this.fixed.movdoc[j]
            if (movdoc.doc && movdoc.doc.idDocumento === marca.idpeca) {
              movdoc.marca.push({
                idmarca: marca.idmarca,
                texto: marca.texto,
                idestilo: marca.idestilo,
                paginicial: marca.paginicial,
                pagfinal: marca.pagfinal
              })
            }
          }
        }
      }, error => {
        if (error.data.errormsg === 'disabled') {
          this.marcasativas = false
          return
        }
        UtilsBL.errormsg(error, this)
      })
    },
    getDescriptions: function () {
      var db = this.proc.dadosBasicos

      // Carregar a classe
      this.$set(this.fixed, 'classeProcessualDescricao', CnjClasseBL.nome(db.classeProcessual))
      this.$set(this.fixed, 'classeProcessualDescricaoCompleta', CnjClasseBL.nomeCompleto(db.classeProcessual))

      // Carregar assuntos (partimos do princípio que sempre
      // há um assunto principal e que sempre é o primeiro)
      if (db.assunto && db.assunto.length > 0 && Number(db.assunto[0].codigoNacional) > 0) {
        for (var i = 0; i < db.assunto.length; i++) {
          var ass = db.assunto[i]
          ass.descricao = CnjAssuntoBL.nome(ass.codigoNacional)
          ass.descricaoCompleta = CnjAssuntoBL.nomeCompleto(ass.codigoNacional)
          if (ass.principal) {
            this.$set(this.fixed, 'assuntoPrincipalDescricao', ass.descricao)
            this.$set(this.fixed, 'assuntoPrincipalDescricaoCompleta', ass.descricaoCompleta)
          }
        }
      }
    },
    mostrarTexto: function (doc, f) {
      ProcessoBL.mostrarTexto(this.fixed.movdoc, doc, f)
      this.modified = new Date()
    },
    mostrarDadosComplementares: function (ativo) {
      this.$parent.$emit('setting', 'mostrarDadosComplementares', ativo)
    },
    mostrarProcessosRelacionados: function (ativo) {
      this.$parent.$emit('setting', 'mostrarProcessosRelacionados', ativo)
    },
    mostrarPeca: function (idDocumento) {
      this.$http.get('processo/' + this.numero + '/peca/' + idDocumento + '/pdf?orgao=' + this.orgao).then(response => {
        var jwt = response.data.jwt
        window.open(this.$http.options.root + '/download/' + jwt + '/' + this.numero + '-peca-' + idDocumento + '.pdf')
        UtilsBL.logEvento('consulta-processual', 'mostrar pdf peça')
      }, error => {
        Bus.$emit('message', 'Erro', error.data.errormsg)
      })
    },
    mostrarCompleto: function () {
      this.$http.get('processo/' + this.numero + '/pdf?orgao=' + this.orgao).then(response => {
        var jwt = response.data.jwt
        window.open(this.$http.options.root + '/download/' + jwt + '/' + this.numero + '.pdf')
        UtilsBL.logEvento('consulta-processual', 'mostrar pdf completo', 'individual')
      }, error => {
        Bus.$emit('message', 'Erro', error.data.errormsg)
      })
    },
    filtrarMovimentos: function (texto) {
      this.$parent.$emit('setting', 'filtrarMovimentos', texto !== undefined)
      var f = this.filtro
      if (texto) {
        if (texto.length > 0 && texto.substring(0, 1) === '#' && f && f.length > 0 && f.substring(0, 1) === '#') {
          this.filtro = f + ' ' + texto
          return
        }
      }
      this.filtro = texto
      this.$nextTick(() => this.$refs.filtro.focus())
    },
    mostrarPartes: function (ativo) {
      this.$parent.$emit('setting', 'mostrarPartes', ativo)
    },
    imprimir: function () {
      window.print()
    },
    formatDDMMYYYHHMM: function (s) {
      if (s === undefined) {
        return
      }
      var r = s.substring(6, 8) + '/' + s.substring(4, 6) +
        '/' + s.substring(0, 4) + ' ' +
        s.substring(8, 10) + ':' +
        s.substring(10, 12)
      r = r.replace(' ', '&nbsp;')
      return r
    },

    exibirProcessoPecaDetalhes: function (movdoc, marca) {
      this.currentMovDoc = movdoc
      this.currentMarca = marca
      this.$refs.processoPecaDetalhes.show(marca, this.marcadores,
        movdoc.doc && movdoc.doc.outroParametro ? movdoc.doc.outroParametro.paginaInicial : undefined,
        movdoc.doc && movdoc.doc.outroParametro ? movdoc.doc.outroParametro.paginaFinal : undefined
      )
    },

    salvarProcessoPecaDetalhes: function (marca) {
      if (!this.currentMovDoc) return

      var movdoc = this.currentMovDoc
      var inicial = movdoc.doc && movdoc.doc.outroParametro ? movdoc.doc.outroParametro.paginaInicial : undefined
      var final = movdoc.doc && movdoc.doc.outroParametro ? movdoc.doc.outroParametro.paginaFinal : undefined
      if (inicial === marca.paginicial && final === marca.pagfinal) {
        marca.paginicial = undefined
        marca.pagfinal = undefined
      }

      var data = {
        idclasse: this.proc.dadosBasicos.classeProcessual,
        idmarca: this.currentMarca ? this.currentMarca.idmarca : undefined,
        texto: marca.texto,
        idestilo: marca.idestilo,
        paginicial: marca.paginicial,
        pagfinal: marca.pagfinal
      }

      console.log('salvando... ')
      console.log(data)

      this.$http.post('processo/' + this.numero + '/peca/' + this.currentMovDoc.doc.idDocumento + '/marca?orgao=' + this.orgao,
        data, { block: true }).then(response => {
          if (this.currentMarca) {
            console.log('alterado... ')
            var index = this.currentMovDoc.marca.indexOf(this.currentMarca)
            UtilsBL.overrideProperties(marca, response.data.marca)
            UtilsBL.overrideProperties(this.currentMovDoc.marca[index], marca)
            console.log(this.currentMovDoc.marca[index])
          } else {
            console.log('novo... ')
            UtilsBL.overrideProperties(marca, response.data.marca)
            this.currentMovDoc.marca.push(marca)
            console.log(marca)
          }
        }, error => {
          Bus.$emit('message', 'Erro', error.data.errormsg)
        })
    },

    excluirProcessoPecaDetalhes: function () {
      if (!this.currentMovDoc || !this.currentMarca) return

      console.log('removendo... ')
      this.$http.delete('marca/' + this.currentMarca.idmarca, { block: true }).then(response => {
        var index = this.currentMovDoc.marca.indexOf(this.currentMarca)
        if (index > -1) this.currentMovDoc.marca.splice(index, 1)
      }, error => {
        Bus.$emit('message', 'Erro', error.data.errormsg)
      })
    },

    favoritar: function (favorito) {
      this.errormsg = undefined
      this.$http.post('processo/' + this.numero + '/sinalizar', { favorito: favorito }, { block: true }).then(response => {
        var d = response.data
        this.favorito = d.processo.favorito
      }, error => {
        this.warningmsg = error.data.errormsg
      })
    }

  },

  components: {
    timeline: Timeline,
    'processo-peca-detalhes': ProcessoPecaDetalhes
  }
}
</script>

<!-- Add 'scoped' attribute to limit CSS to this component only -->
<style scoped>
.marca-ref:hover,
.marca-ref:link,
.marca-ref:visited,
.marca-ref:active {
  color: black;
}

.inquebravel {
  white-space: nowrap;
}

.marca {
  padding-left: 0.0rem;
  padding-right: 0.0rem;
  margin-right: 0.5rem;
  margin-bottom: 0;
  margin-top: 0.0rem;
}

.marca-yellow {
  background-color: yellow;
}

.marca-blue {
  background-color: #41f1f4;
}

.marca-green {
  background-color: #00ff00;
}

.marca-pink {
  background-color: #faf;
}

.red {
  color: red;
}

.protocolado {
  color: green;
}

.odd {
  background-color: rgba(0, 0, 0, .05);
}

.card-consulta-processual DIV P B {
  color: #fff;
}

.card-consulta-processual DIV P {
  margin-bottom: 0.5rem;
}

.card-consulta-processual DIV I {
  line-height: 3rem;
  height: 3rem;
  color: #fff;
  float: right;
  font-size: 4rem;
  margin: 0rem -0.5rem 0rem 0rem;
}

.card-text-descr {
  margin-bottom: 0;
}
</style>
