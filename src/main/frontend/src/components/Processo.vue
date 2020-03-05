<template>
  <div class="hello">
    <!--=== Profile ===-->
    <div class="container content profile">
      <div class="row pt-5" v-if="errormsg">
        <div class="col col-sm-12">
          <p class="alert alert-danger">
            {{ errormsg }}
          </p>
        </div>
      </div>

      <div class="row pt-5" v-if="warningmsg">
        <div class="col col-sm-12">
          <p class="alert alert-warning">
            <strong>Atenção!</strong> {{ warningmsg }}
          </p>
        </div>
      </div>

      <div v-if="!errormsg &amp;&amp; proc &amp;&amp; proc.dadosBasicos">
        <div class="row xd-print-block mt-3 mb-3">
          <div class="col-md-12">
            <h4 class="text-center mb-0">
              Processo
              <span
                v-if="proc.dadosBasicos.outroParametro.indEletronico == 'S'"
              >
                Digital&nbsp;
              </span>
              <span
                v-if="sistema"
                v-html="
                  $parent.test.properties['balcaovirtual.' + sistema + '.name']
                "
              ></span>
              <span
                v-if="proc &amp;&amp; proc.dadosBasicos.outroParametro.indEletronico &amp;&amp; proc.dadosBasicos.outroParametro.indEletronico != 'S'"
              >
                Físico</span
              >
              {{ proc.dadosBasicos.numero }}
              <a
                v-if="this.favorito !== undefined && !this.favorito"
                href=""
                @click.prevent="favoritar(true)"
              >
                <span
                  class="fa fa-star-o icone-em-linha"
                  title="Acrescentar à lista de processos favoritos"
                ></span>
              </a>
              <a
                v-if="this.favorito !== undefined && this.favorito"
                href=""
                @click.prevent="favoritar(false)"
              >
                <span
                  class="fa fa-star icone-em-linha"
                  title="Remover da lista de processos favoritos"
                ></span>
              </a>

              <template v-if="marcasativas">
                &nbsp;
                <a
                  href=""
                  v-if="$parent.settings.mostrarNotas &amp;&amp; notas"
                  @click.prevent="mostrarNotas(false)"
                >
                  <span
                    class="fa fa-sticky-note icone-em-linha"
                    title="Esconder anotações"
                  ></span>
                </a>
                <a
                  href=""
                  v-else-if="$parent.settings.mostrarNotas"
                  @click.prevent="mostrarNotas(false)"
                >
                  <span
                    class="fa fa-sticky-note-o icone-em-linha"
                    title="Acrescentar anotações"
                  ></span>
                </a>
                <a
                  href=""
                  v-else-if="notas"
                  @click.prevent="mostrarNotas(true)"
                >
                  <span
                    class="fa fa-sticky-note icone-em-linha"
                    title="Exibir anotações"
                  ></span>
                </a>
                <a href="" v-else @click.prevent="mostrarNotas(true)">
                  <span
                    class="fa fa-sticky-note-o icone-em-linha"
                    title="Acrescentar anotações"
                  ></span>
                </a>
              </template>
            </h4>
          </div>
        </div>

        <processo-notas
          v-if="marcasativas"
          :processo="numero"
          :sistema="sistema"
          @ativar="notas = true"
          @desativar="notas = false"
        ></processo-notas>

        <timeline :timeline="timeline"></timeline>

        <div class="row no-gutters mt-2">
          <div
            class="col col-auto mr-1"
            v-if="!$parent.settings.filtrarMovimentos"
          >
            <button
              type="button"
              @click="filtrarMovimentos('')"
              class="btn btn-secondary d-print-none"
            >
              <span class="fa fa-filter"></span>
              Filtrar Movimentos
            </button>
          </div>
          <div
            class="col col-auto mr-1 mb-3"
            v-if="
              filtro !== '#marca' && $parent.jwt && $parent.jwt.user[sistema]
            "
          >
            <button
              type="button"
              @click="filtrarMovimentos('#marca')"
              class="btn btn-secondary d-print-none"
            >
              <span class="fa fa-filter"></span>
              Filtrar Marcas
            </button>
          </div>
          <div class="col col-auto ml-auto mb-3">
            <button
              type="button"
              @click="mostrarCompleto()"
              id="download"
              class="btn btn-info d-print-none"
            >
              <span class="fa fa-download"></span>
              PDF Completo
            </button>
          </div>
          <div class="col col-auto ml-1 mb-3">
            <button
              type="button"
              @click="imprimir()"
              id="imprimir"
              class="btn btn-info d-print-none"
            >
              <span class="fa fa-print"></span>
              Imprimir
            </button>
          </div>
          <div
            class="col col-auto ml-1 mb-3"
            v-if="marcasativas && ($parent.test.properties['balcaovirtual.env'] !== 'prod' || (perfil === 'procurador' &amp;&amp; $parent.jwt &amp;&amp; ($parent.jwt.company === 'pgfn.gov.br' || $parent.jwt.company === 'dpu.gov.br')))"
          >
            <button
              type="button"
              @click="cotar()"
              id="cotar"
              class="btn btn-info d-print-none"
            >
              <span class="fa fa-comment"></span>
              Enviar Cota
            </button>
          </div>
        </div>

        <template v-if="proc &amp;&amp; proc.dadosBasicos">
          <!-- QUADROS COLORIDOS -->
          <div class="d-print-none" v-if="errormsg === undefined">
            <div class="card-deck">
              <div
                class="card text-white bg-primary card-consulta-processual mb-3"
              >
                <div class="card-body">
                  <img
                    id="logo-header"
                    class="float-right"
                    src="../assets/users.png"
                    height="64"
                  />
                  <p class="card-text" v-if="fixed.partesAtivas">
                    <small>{{ fixed.partesAtivas[0].tipoAtuacao }}</small>
                    <br />
                    <b>{{ fixed.partesAtivas[0].pessoa.nome }}</b>
                    <b v-if="fixed.partesAtivas.length > 1"
                      >&nbsp;
                      <a href="" @click.prevent="mostrarPartes(true)"
                        >+{{ fixed.partesAtivas.length - 1 }}</a
                      >
                    </b>
                  </p>
                  <p class="card-text" v-if="fixed.partesPassivas">
                    <small>{{ fixed.partesPassivas[0].tipoAtuacao }}</small>
                    <br />
                    <b>{{ fixed.partesPassivas[0].pessoa.nome }}</b>
                    <b v-if="fixed.partesPassivas.length > 1"
                      >&nbsp;
                      <a href="" @click.prevent="mostrarPartes(true)"
                        >+{{ fixed.partesPassivas.length - 1 }}</a
                      >
                    </b>
                  </p>
                  <a
                    v-if="!$parent.settings.mostrarPartes"
                    @click.prevent="mostrarPartes(true)"
                    class="card-link float-right"
                    href=""
                    >Ver partes...</a
                  >
                </div>
              </div>

              <div
                class="card text-white bg-success card-consulta-processual mb-3"
              >
                <div class="card-body">
                  <img
                    id="logo-header"
                    class="float-right"
                    src="../assets/judicia.png"
                    height="64"
                  />
                  <p class="card-text" v-if="false">
                    <small
                      >PROCESSO
                      <span
                        v-if="
                          proc.dadosBasicos.outroParametro.indEletronico == 'S'
                        "
                      >
                        DIGITAL</span
                      >
                      <span
                        v-if="proc &amp;&amp; proc.dadosBasicos.outroParametro.indEletronico != 'S'"
                      >
                        FÍSICO</span
                      >
                    </small>
                    <br />
                    <b>{{ proc.dadosBasicos.numero }}</b>
                  </p>
                  <p class="card-text">
                    <small>DATA DE DISTRIBUIÇÃO</small>
                    <br />
                    <b v-html="fixed.dataAjuizamento"></b>
                  </p>
                  <p class="card-text">
                    <small>ÓRGÃO JULGADOR</small>
                    <br />
                    <b style="color: #fff">{{
                      proc.dadosBasicos.orgaoJulgador.nomeOrgao
                    }}</b>
                  </p>

                  <p class="card-text">
                    <small>MAGISTRADO</small>
                    <br />
                    <b>{{ proc.dadosBasicos.outroParametro.nomeMagistrado }}</b>
                  </p>
                  <a
                    v-if="!$parent.settings.mostrarProcessosRelacionados"
                    class="card-link float-right"
                    href=""
                    @click.prevent="mostrarProcessosRelacionados(true)"
                    >Ver processos relacionados...</a
                  >
                </div>
              </div>

              <div
                class="card text-white bg-warning card-consulta-processual mb-3"
              >
                <div class="card-body">
                  <img
                    id="logo-header"
                    class="float-right"
                    src="../assets/monitor.png"
                    height="64"
                  />
                  <p class="card-text" v-if="fixed.valorCausa">
                    <small>VALOR DA CAUSA</small>
                    <br />
                    <b>{{ fixed.valorCausa }}</b>
                  </p>
                  <p class="card-text">
                    <small>CLASSE</small>
                    <br />
                    <b>{{ fixed.classeProcessualDescricao }}</b>
                  </p>
                  <p class="card-text" v-if="fixed.assuntoPrincipalDescricao">
                    <small>ASSUNTO PRINCIPAL</small>
                    <br />
                    <b>{{ fixed.assuntoPrincipalDescricao }}</b>
                  </p>
                  <p
                    class="card-text"
                    v-if="proc.dadosBasicos.outroParametro &amp;&amp; proc.dadosBasicos.outroParametro.numCDA"
                  >
                    <small>CDA</small>
                    <br />
                    <b>{{ proc.dadosBasicos.outroParametro.numCDA[0] }}</b>
                    <b v-if="proc.dadosBasicos.outroParametro.numCDA.length > 1"
                      >&nbsp;
                      <a
                        href=""
                        @click.prevent="mostrarDadosComplementares(true)"
                        >+{{
                          proc.dadosBasicos.outroParametro.numCDA.length - 1
                        }}</a
                      >
                    </b>
                  </p>
                  <p
                    class="card-text"
                    v-if="
                      fixed.informacoesAdicionais &&
                        fixed.informacoesAdicionais.cdas &&
                        fixed.informacoesAdicionais.cdas.length
                    "
                  >
                    <small>{{ fixed.informacoesAdicionais.cdas.length }} CDAs</small>
                    <br />
                    <b>
                      <a
                        href=""
                        @click.prevent="mostrarDadosComplementares(true)"
                      >
                        R$ {{ valorTotalCDAs }}</a
                      >
                    </b>
                  </p>
                  <a
                    v-if="!$parent.settings.mostrarDadosComplementares"
                    class="card-link float-right"
                    href=""
                    @click.prevent="mostrarDadosComplementares(true)"
                    >Ver mais...</a
                  >
                </div>
              </div>
            </div>
          </div>

          <!-- PARTES -->
          <div
            v-bind:class="{
              row: true,
              'd-none d-print-block': !$parent.settings.mostrarPartes
            }"
          >
            <div class="col col-sm-12">
              <div class="card mb-3 border-primary">
                <div class="card-header">
                  Partes
                  <button
                    type="button"
                    class="close d-print-none"
                    @click="mostrarPartes(false)"
                  >
                    <span aria-hidden="true">&times;</span>
                  </button>
                </div>
                <div
                  class="card-body pb-0"
                  v-for="polo in proc.dadosBasicos.polo"
                  :key="polo.modalidadePoloProcessual"
                >
                  <h6 class="card-subtitle mb-2 text-muted">
                    <u>{{ polo.modalidadePoloProcessual }}</u>
                  </h6>

                  <template v-for="parte in polo.parte">
                    <div class="row" :key="parte.id">
                      <div class="col col-sm-6">
                        <label>{{ parte.tipoAtuacao }}</label>
                        <p>{{ parte.pessoa.nome }}</p>
                      </div>
                      <div
                        class="col"
                        :class="{'col-sm-3': !$parent.jwt || !$parent.jwt.isInterno(sistema), 'col-sm-2': $parent.jwt &amp;&amp; $parent.jwt.isInterno(sistema)}"
                      >
                        <label>Tipo</label>
                        <p>{{ parte.pessoa.tipoPessoa }}</p>
                      </div>
                      <div
                        v-if="$parent.jwt &amp;&amp; $parent.jwt.isInterno(sistema)"
                        class="col col-sm-2"
                      >
                        <label>Documento</label>
                        <p>{{ parte.documento }}</p>
                      </div>
                      <div
                        class="col"
                        :class="{'col-sm-3': !$parent.jwt || !$parent.jwt.isInterno(sistema), 'col-sm-2': $parent.jwt &amp;&amp; $parent.jwt.isInterno(sistema)}"
                      >
                        <label>Assistência Judiciária</label>
                        <p>{{ parte.assistenciaJudiciaria ? "Sim" : "Não" }}</p>
                      </div>
                    </div>
                    <div
                      class="row"
                      v-for="adv in parte.advogado"
                      :key="adv.id"
                    >
                      <div class="col offset-sm-1 col-sm-5">
                        <label>{{
                          tipoRepresentante[adv.tipoRepresentante]
                        }}</label>
                        <p>{{ adv.nome }}</p>
                      </div>
                      <div class="col col-sm-3">
                        <label>Inscrição</label>
                        <p>{{ adv.inscricao }}</p>
                      </div>
                      <div class="col col-sm-3">
                        <label>Intimação</label>
                        <p>{{ adv.intimacao ? "Sim" : "Não" }}</p>
                      </div>
                    </div>
                  </template>
                </div>
              </div>
            </div>
          </div>

          <!-- QUADRO COLORIDO DE PROCESSOS VINCULADOS -->
          <div
            v-if="$parent.settings.mostrarProcessosRelacionados &amp;&amp; (fixed.processoVinculado || fixed.recurso)"
            class="mt-1 d-print-none"
          >
            <!-- Profile Content -->
            <div class="card-deck">
              <div
                class="card border-success card-consulta-processual mb-3"
                v-if="fixed.processoVinculado"
              >
                <div class="card-header">
                  Processos Vinculados
                  <button
                    type="button"
                    class="close d-print-none"
                    @click="mostrarProcessosRelacionados(false)"
                  >
                    <span aria-hidden="true">&times;</span>
                  </button>
                </div>
                <div class="card-body" style="padding: 0">
                  <div class="card-text mb-0">
                    <table class="table table-sm mb-0">
                      <tbody>
                        <tr v-for="pv in fixed.processoVinculado" :key="pv.id">
                          <td style="padding-left: 20px">
                            <span
                              v-html="
                                !$parent.jwt || !$parent.jwt.user[sistema]
                                  ? pv.link.replace('/processo/', '/consultar/')
                                  : pv.link
                              "
                            ></span
                            ><span v-if="pv.nomeClasse">
                              - {{ pv.nomeClasse }}</span
                            ><span v-if="pv.suporte"> - {{ pv.suporte }}</span>
                          </td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                </div>
              </div>
              <div
                class="card border-success card-consulta-processual mb-3"
                v-if="fixed.recurso"
              >
                <div class="card-header">
                  Recursos
                  <button
                    type="button"
                    class="close d-print-none"
                    @click="mostrarProcessosRelacionados(false)"
                  >
                    <span aria-hidden="true">&times;</span>
                  </button>
                </div>
                <div class="card-body" style="padding: 0">
                  <div class="card-text mb-0">
                    <table class="table table-sm mb-0">
                      <tbody>
                        <tr v-for="pv in fixed.recurso" :key="pv.id">
                          <td style="padding-left: 20px">
                            <span
                              v-html="
                                !$parent.jwt || !$parent.jwt.user[sistema]
                                  ? pv.link.replace('/processo/', '/consultar/')
                                  : pv.link
                              "
                            ></span>
                            - {{ pv.nomeClasse }} - {{ pv.suporte }}
                          </td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- QUADRO SUBSTITUTO PARA A IMPRESSAO -->
          <div class="row d-none d-print-block">
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
                      <p>{{ proc.dadosBasicos.orgaoJulgador.nomeOrgao }}</p>
                    </div>
                    <div class="col col-sm-3">
                      <label>Magistrado</label>
                      <p>
                        {{ proc.dadosBasicos.outroParametro.nomeMagistrado }}
                      </p>
                    </div>
                    <div class="col col-sm-3">
                      <label>Valor da Causa</label>
                      <p>{{ fixed.valorCausa }}</p>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col col-sm-3">
                      <label>Classe Processual</label>
                      <p>{{ fixed.classeProcessualDescricao }}</p>
                    </div>
                    <div
                      class="col col-sm-3"
                      v-if="fixed.assuntoPrincipalDescricao"
                    >
                      <label>Assunto Principal</label>
                      <p>{{ fixed.assuntoPrincipalDescricao }}</p>
                    </div>
                  </div>
                  <div class="row">
                    <div
                      class="col col-sm-6"
                      v-if="proc.dadosBasicos.outroParametro.processoVinculado"
                    >
                      <label>Processos Vinculados</label>
                      <table class="table table-sm mb-1 table-striped">
                        <tbody>
                          <tr
                            v-for="pv in fixed.processoVinculado"
                            :key="pv.id"
                          >
                            <td style="padding-left:0;padding-right:0;">
                              <span v-html="pv.link"></span> -
                              {{ pv.nomeClasse }} - {{ pv.suporte }}
                            </td>
                          </tr>
                        </tbody>
                      </table>
                    </div>
                    <div
                      class="col col-sm-6"
                      v-if="proc.dadosBasicos.outroParametro.recurso"
                    >
                      <label>Recursos</label>
                      <table class="table table-sm mb-1 table-striped">
                        <tbody>
                          <tr v-for="pv in fixed.recurso" :key="pv.id">
                            <td style="padding-left:0;padding-right:0;">
                              <span v-html="pv.link"></span> -
                              {{ pv.nomeClasse }} - {{ pv.suporte }}
                            </td>
                          </tr>
                        </tbody>
                      </table>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div
            v-if="$parent.settings.mostrarDadosComplementares"
            v-bind:class="{ row: true }"
          >
            <div class="col col-sm-12">
              <div class="card mb-3 border-warning">
                <div class="card-header">
                  Dados Complementares
                  <button
                    type="button"
                    class="close d-print-none"
                    @click="mostrarDadosComplementares(false)"
                  >
                    <span aria-hidden="true">&times;</span>
                  </button>
                </div>
                <div class="card-body pb-0">
                  <div class="row">
                    <div
                      class="col col-sm-3"
                      v-if="proc.dadosBasicos.outroParametro.dataConsulta"
                    >
                      <label>Data de Protocolo</label>
                      <p
                        v-html="proc.dadosBasicos.outroParametro.dataProtocolo"
                      ></p>
                    </div>

                    <div
                      class="col col-sm-3"
                      v-if="proc.dadosBasicos.outroParametro.numProcAdm"
                    >
                      <label>Processos Administrativos</label>
                      <p
                        v-html="proc.dadosBasicos.outroParametro.numProcAdm"
                      ></p>
                    </div>

                    <div class="col col-sm-3" v-if="fixed.numCDAs">
                      <label>CDA's</label>
                      <p v-html="fixed.numCDAs"></p>
                    </div>

                    <div
                      class="col col-sm-3"
                      v-if="proc.dadosBasicos.outrosnumeros &amp;&amp; proc.dadosBasicos.outrosnumeros[0]"
                    >
                      <label>Número Antigo</label>
                      <p v-html="proc.dadosBasicos.outrosnumeros[0]"></p>
                    </div>

                    <div
                      class="col col-sm-6"
                      v-if="
                        proc.dadosBasicos.outroParametro.peticaoPendenteJuntada
                      "
                    >
                      <label>Petições Pendentes de Juntada</label>
                      <p
                        v-html="
                          proc.dadosBasicos.outroParametro
                            .peticaoPendenteJuntada
                        "
                      ></p>
                    </div>

                    <div
                      class="col col-sm-3"
                      v-if="proc.dadosBasicos.outroParametro.processoOriginario"
                    >
                      <label>Processo Originário</label>
                      <p
                        v-html="
                          proc.dadosBasicos.outroParametro.processoOriginario
                        "
                      ></p>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col col-sm-12">
                      <label>Classe Processual</label>
                      <p>{{ fixed.classeProcessualDescricaoCompleta }}</p>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col col-sm-12">
                      <label>Assunto(s) CNJ</label>
                      <p v-for="ass in proc.dadosBasicos.assunto" :key="ass.id">
                        {{ ass.descricaoCompleta }}
                      </p>
                    </div>
                  </div>
                  <div
                    class="row"
                    v-if="
                      fixed.informacoesAdicionais &&
                        fixed.informacoesAdicionais.cdas
                    "
                  >
                    <div class="col col-sm-12">
                      <label>CDAs</label>
                      <table class="table table-sm table-striped">
                        <thead>
                          <tr>
                            <th>Número</th>
                            <th>Status</th>
                            <th>Tributo</th>
                            <th>Data de Atualização</th>
                            <th class="text-right">Valor</th>
                          </tr>
                        </thead>
                        <tbody>
                          <tr
                            v-for="cda in fixed.informacoesAdicionais.cdas"
                            :key="cda.numero"
                          >
                            <td>{{ cda.numero }}</td>
                            <td>{{ cda.status }}</td>
                            <td>{{ cda.tributo }}</td>
                            <td v-html="cda.datainclusaoFormatada"></td>
                            <td class="text-right">{{ cda.valorFormatado }}</td>
                          </tr>
                        </tbody>
                      </table>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div
            class="row"
            v-if="$parent.settings.filtrarMovimentos"
            id="filtrarMovimentos"
          >
            <div class="col col-sm-12">
              <p
                class="alert alert-info alert-dismissible fade show"
                role="alert"
              >
                <button
                  type="button"
                  @click="filtrarMovimentos()"
                  class="close"
                  aria-label="Close"
                >
                  <span aria-hidden="true">&times;</span>
                </button>
                <span class="input-group" style="width: 50%">
                  <span class="input-group-addon">&#128269;</span>
                  <input
                    type="text"
                    list="lst_filtrarMovimentosInput"
                    id="filtrarMovimentosInput"
                    class="form-control"
                    placeholder="Filtrar Movimentos"
                    v-model="filtro"
                    ref="filtro"
                  />
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
                    <option>#execucao</option>
                    <option>#baixa</option>
                    <option>#arquivamento</option>
                  </datalist>
                </span>
              </p>
            </div>
          </div>

          <div class="row" v-if="errormsg === undefined">
            <div class="col col-sm-12">
              <div class="table-responsive">
                <table class="table table-sm mb-0">
                  <thead class="thead-dark">
                    <tr>
                      <th>Data/Hora</th>
                      <th
                        v-if="sistema &amp;&amp; sistema.includes('.eproc')"
                        class="text-right"
                      >
                        #
                      </th>
                      <th>
                        {{
                          sistema && sistema.includes(".eproc")
                            ? "Evento"
                            : "Movimento"
                        }}
                      </th>
                      <th>Peça</th>
                      <th style="text-align: right">Página Inicial</th>
                      <th style="text-align: right">Página Final</th>
                    </tr>
                  </thead>
                  <tbody>
                    <template v-for="movdoc in filtrados">
                      <tr
                        v-bind:class="{ odd: movdoc.odd, highlightable: true }"
                        v-bind:id="'mov' + movdoc.mov.dataHora"
                        :key="movdoc.id"
                      >
                        <td
                          v-if="movdoc.rowspan && !movdoc.hidemov"
                          :rowspan="movdoc.rowspan"
                        >
                          <span
                            v-html="formatDDMMYYYHHMM(movdoc.mov.dataHora)"
                          ></span>
                        </td>
                        <td
                          v-if="
                            sistema &&
                              sistema.includes('.eproc') &&
                              movdoc.rowspan &&
                              !movdoc.hidemov
                          "
                          :rowspan="movdoc.rowspan"
                          class="text-right"
                        >
                          {{ movdoc.mov.identificadorMovimento }}
                        </td>
                        <td
                          v-if="movdoc.rowspan && !movdoc.hidemov"
                          :rowspan="movdoc.rowspan"
                          v-bind:class="{
                            'text-success-dark':
                              movdoc.doc.exibirTexto !== undefined
                          }"
                          v-html="
                            movdoc.mov.movimentoLocal
                              ? movdoc.mov.movimentoLocal.descricao
                              : ''
                          "
                        ></td>
                        <template v-if="movdoc.doc">
                          <td>
                            <p class="mb-0" v-if="movdoc.doc.idDocumento">
                              <a
                                href=""
                                target="_blank"
                                @click.prevent="
                                  mostrarPeca(movdoc.doc.idDocumento)
                                "
                                >{{ movdoc.doc.descricao }}</a
                              >
                              <a
                                href=""
                                @click.prevent="
                                  mostrarPeca(
                                    movdoc.doc.idDocumento,
                                    'attachment'
                                  )
                                "
                              >
                                <span
                                  class="fa fa-download icone-em-linha"
                                ></span>
                              </a>
                              <a
                                href=""
                                v-if="marcasativas &amp;&amp; movdoc.doc.idDocumento"
                                @click.prevent="
                                  exibirProcessoPecaDetalhes(movdoc)
                                "
                              >
                                <span
                                  class="fa fa-pencil icone-em-linha"
                                ></span>
                              </a>
                              <a
                                href=""
                                v-if="movdoc.doc.exibirTexto == false"
                                @click.prevent="mostrarTexto(movdoc.doc, true)"
                              >
                                <span
                                  class="fa fa-search icone-em-linha"
                                ></span>
                              </a>
                            </p>
                            <br
                              v-if="false &amp;&amp; movdoc.marca &amp;&amp; movdoc.marca.length > 0"
                            />
                            <template v-for="m in movdoc.marca">
                              <p
                                class="mt-1 mb-0"
                                style="line-height: 1.4"
                                :key="m.id"
                              >
                                <a
                                  href=""
                                  class="marca-ref"
                                  @click.prevent="
                                    exibirProcessoPecaDetalhes(movdoc, m)
                                  "
                                >
                                  <span
                                    class="marca"
                                    :class="{
                                      'marca-yellow': m.idestilo == 2,
                                      'marca-green': m.idestilo == 4,
                                      'marca-pink': m.idestilo == 3,
                                      'marca-blue': m.idestilo == 1
                                    }"
                                    >{{ m.texto }}
                                    <span
                                      class="inquebravel"
                                      v-if="m.paginicial"
                                      v-html="
                                        'p.&nbsp' +
                                          m.paginicial +
                                          (m.paginicial !== m.pagfinal
                                            ? '&#8209;' + m.pagfinal
                                            : '')
                                      "
                                    >
                                    </span>
                                  </span>
                                </a>
                              </p>
                            </template>
                          </td>
                          <td style="text-align: right">
                            {{
                              movdoc.doc && movdoc.doc.outroParametro
                                ? movdoc.doc.outroParametro.paginaInicial
                                : ""
                            }}
                          </td>
                          <td style="text-align: right">
                            {{
                              movdoc.doc && movdoc.doc.outroParametro
                                ? movdoc.doc.outroParametro.paginaFinal
                                : ""
                            }}
                          </td>
                        </template>
                        <template v-else>
                          <td colspan="3"></td>
                        </template>
                      </tr>
                      <tr
                        v-if="movdoc.doc.exibirTexto"
                        v-bind:class="{ odd: movdoc.odd }"
                        :key="movdoc.id"
                      >
                        <td colspan="5">
                          <p
                            class="alert alert-success mb-0 alert-dismissible fade show"
                            role="alert"
                          >
                            <button
                              type="button"
                              class="close d-print-none"
                              @click="mostrarTexto(movdoc.doc, false)"
                            >
                              <span aria-hidden="true">&times;</span>
                            </button>
                            <span
                              v-html="movdoc.doc.outroParametro.textoMovimento"
                            ></span>
                          </p>
                        </td>
                      </tr>
                    </template>
                  </tbody>
                </table>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col col-sm-12">
              <hr class="mt-5 mb-1" />
              <p class="text-center">
                As informações aqui contidas não produzem efeitos legais.
                Somente a publicação no D.O. tem validade para contagem de
                prazos.
                <br />Consulta
                <span v-if="!$parent.jwt || !$parent.jwt.user[sistema]"
                  >pública</span
                >
                realizada em:
                <span
                  v-html="
                    proc.dadosBasicos.outroParametro.dataConsulta
                      ? proc.dadosBasicos.outroParametro.dataConsulta
                      : dataValidacao
                  "
                ></span>
                -
                {{
                  $parent.test.properties["balcaovirtual." + sistema + ".name"]
                }}.
              </p>
            </div>
          </div>
        </template>

        <processo-peca-detalhes
          v-if="marcasativas"
          ref="processoPecaDetalhes"
          @ok="salvarProcessoPecaDetalhes"
          @remove="excluirProcessoPecaDetalhes"
        ></processo-peca-detalhes>
        <processo-cota
          v-if="marcasativas"
          ref="processoCota"
          :processo="numero"
          :sistema="sistema"
          :unidade="proc.dadosBasicos.orgaoJulgador.nomeOrgao"
          @ok="cotaEnviada"
          @erro="cotaNaoEnviada"
        ></processo-cota>
      </div>
    </div>
  </div>
</template>

<script>
import TimelineBL from "../bl/timeline.js";
import ProcessoBL from "../bl/processo.js";
import UtilsBL from "../bl/utils.js";
import Timeline from "./Timeline";
import ProcessoPecaDetalhes from "./ProcessoPecaDetalhes";
import ProcessoNotas from "./ProcessoNotas";
import ProcessoCota from "./ProcessoCota";
import CnjClasseBL from "../bl/cnj-classe.js";
import CnjAssuntoBL from "../bl/cnj-assunto.js";
import { Bus } from "../bl/bus.js";

export default {
  name: "processo",
  mounted() {
    this.$on("filtrar", texto => {
      this.filtrarMovimentos(texto);
    });

    // Validar o número do processo
    this.$nextTick(function() {
      if (this.$route.params.validar)
        this.aplicarValidar(this.$route.params.validar, this.carregar);
      else this.validar(this.carregar);
    });
  },
  data() {
    return {
      favorito: undefined,
      timeline: TimelineBL.emptyTimeline(),
      modified: undefined,
      numero: ProcessoBL.somenteNumeros(this.$route.params.numero),
      token: this.$route.params.token
        ? this.$route.params.token
        : this.$route.query.token,
      sistema: undefined,
      dataValidacao: undefined,
      perfil: undefined,
      gui: {},
      filtro: undefined,
      tipoRepresentante: {
        A: "Advogado",
        E: "Escritório de Advocacia",
        M: "Ministério Público",
        D: "Defensor Público",
        P: "Advogado Público"
      },
      errormsg: undefined,
      warningmsg: undefined,
      partes: false,
      dadosComplementares: false,
      proc: undefined,
      fixed: undefined,
      marcadores: [],
      notas: false
    };
  },
  computed: {
    marcasativas: function() {
      return (
        this.$parent.jwt &&
        this.$parent.jwt.user &&
        this.$parent.jwt.user[this.sistema]
      );
    },
    filtrados: function() {
      // Referência à this.modified é necessária para recalcular quando mostra o texto
      console.log("recalculando filtrados...", this.modified);
      var a = ProcessoBL.filtrar(this.fixed.movdoc, this.filtro);
      return a;
    },
    valorTotalCDAs: function() {
      if (
        !this.fixed ||
        !this.fixed.informacoesAdicionais ||
        !this.fixed.informacoesAdicionais.cdas ||
        this.fixed.informacoesAdicionais.cdas.length === 0
      )
        return undefined;
      var v = 0;
      for (var i = 0; i < this.fixed.informacoesAdicionais.cdas.length; i++) {
        if (this.fixed.informacoesAdicionais.cdas[i].valor)
          v += this.fixed.informacoesAdicionais.cdas[i].valor;
      }
      return UtilsBL.formatMoney(v);
    }
  },
  methods: {
    validar: function(cont) {
      Bus.$emit("block", 20, 30);
      this.$http
        .get(
          "processo/" +
            this.numero +
            "/validar" +
            (this.token ? "?token=" + this.token : "")
        )
        .then(
          response => {
            if (response.data.list && response.data.list.length > 0)
              this.aplicarValidar(response.data, cont);
            Bus.$emit("release");
          },
          error => {
            Bus.$emit("release");
            this.errormsg =
              "Não foi possível obter informações sobre o processo " +
              this.numero +
              ": " +
              error.data.errormsg;
          }
        );
    },

    aplicarValidar: function(data, cont) {
      var p = data.list[0];
      this.sistema = p.sistema;
      this.dataValidacao = UtilsBL.formatJSDDMMYYYYHHMM(data.datavalidacao);

      if (
        this.$parent.jwt &&
        this.$parent.jwt.user &&
        this.$parent.jwt.user[this.sistema]
      ) {
        this.perfil = this.$parent.jwt.user[this.sistema].perfil;
      }
      if (cont) cont();
    },

    carregar: function() {
      Bus.$emit("block", 30, 100);
      this.$http
        .get(
          "processo/" +
            this.numero +
            "/consultar?sistema=" +
            this.sistema +
            (this.token ? "&token=" + this.token : "")
        )
        .then(
          response => {
            Bus.$emit("release");
            try {
              this.proc = response.data.value;
              if (this.proc.movimento) {
                this.proc.movimento = this.proc.movimento.sort(function(a, b) {
                  if (a.dataHora < b.dataHora) {
                    return 1;
                  }
                  if (a.dataHora > b.dataHora) {
                    return -1;
                  }
                  return 0;
                });
              }

              // Desabilitando o cálculo de tempos na timeline enquanto não ajustamos perfeitamente
              var calcularTempos =
                this.$parent.jwt &&
                this.$parent.jwt.isInterno(this.sistema) &&
                false;
              this.fixed = ProcessoBL.fixProc(this.proc);
              this.timeline = TimelineBL.updateTimeline(
                this.sistema,
                this.fixed.movdoc,
                calcularTempos,
                this.proc.dadosBasicos.classeProcessual
              );
              this.getDescriptions();
              if (this.$parent.jwt) {
                this.getMarcadores();
                this.getMarcas();
                if (this.sistema.includes(".eproc"))
                  this.getInformacoesAdicionais();
                this.$http
                  .post("processo/" + this.numero + "/sinalizar", {
                    recente: true
                  })
                  .then(
                    response => {
                      this.favorito = !!response.data.processo.favorito;
                    },
                    error => {
                      this.warningmsg = error.data.errormsg;
                    }
                  );
              }
            } catch (e) {
              console.error(e);
            }
          },
          error => {
            Bus.$emit("release");
            UtilsBL.errormsg(error, this);
          }
        );
    },

    getMarcadores: function() {
      // Carregar os marcadores da classe
      this.$http
        .get(
          "classe/" + this.proc.dadosBasicos.classeProcessual + "/marcadores"
        )
        .then(
          response => {
            if (!response.data.list) return;
            for (var i = 0; i < response.data.list.length; i++) {
              this.marcadores.push(response.data.list[i].texto);
            }
          },
          error => {
            if (error.data.errormsg === "disabled") {
              this.marcasativas = false;
              return;
            }
            UtilsBL.errormsg(error, this);
          }
        );
    },
    getMarcas: function() {
      if (!this.marcasativas) return;
      // Carregar os marcadores da classe
      this.$http
        .get("processo/" + this.numero + "/marcas?sistema=" + this.sistema)
        .then(
          response => {
            // if (!response.data.list) return
            for (var i = 0; i < response.data.list.length; i++) {
              var marca = response.data.list[i];
              for (var j = 0; j < this.fixed.movdoc.length; j++) {
                var movdoc = this.fixed.movdoc[j];
                if (movdoc.doc && movdoc.doc.idDocumento === marca.idpeca) {
                  movdoc.marca.push({
                    idmarca: marca.idmarca,
                    texto: marca.texto,
                    idestilo: marca.idestilo,
                    paginicial: marca.paginicial,
                    pagfinal: marca.pagfinal
                  });
                }
              }
            }
          },
          error => {
            if (error.data.errormsg === "disabled") {
              this.marcasativas = false;
              return;
            }
            UtilsBL.errormsg(error, this);
          }
        );
    },
    getInformacoesAdicionais: function() {
      // Carregar informações adicionais
      this.$http
        .get(
          "processo/" +
            this.numero +
            "/informacoes-adicionais?sistema=" +
            this.sistema
        )
        .then(
          response => {
            this.$set(this.fixed, "informacoesAdicionais", response.data);
            if (
              this.fixed.informacoesAdicionais &&
              this.fixed.informacoesAdicionais.cdas
            ) {
              for (
                var i = 0;
                i < this.fixed.informacoesAdicionais.cdas.length;
                i++
              ) {
                var cda = this.fixed.informacoesAdicionais.cdas[i];
                this.$set(
                  this.fixed.informacoesAdicionais.cdas[i],
                  "datainclusaoFormatada",
                  UtilsBL.formatJSDDMMYYYYHHMM(cda.datainclusao)
                );
                this.$set(
                  this.fixed.informacoesAdicionais.cdas[i],
                  "valorFormatado",
                  UtilsBL.formatMoney(cda.valor)
                );
              }
            }
          },
          error => {
            UtilsBL.errormsg(error, this);
          }
        );
    },
    getDescriptions: function() {
      var db = this.proc.dadosBasicos;

      // Carregar a classe
      this.$set(
        this.fixed,
        "classeProcessualDescricao",
        CnjClasseBL.nome(db.classeProcessual)
      );
      this.$set(
        this.fixed,
        "classeProcessualDescricaoCompleta",
        CnjClasseBL.nomeCompleto(db.classeProcessual)
      );

      // Carregar assuntos (partimos do princípio que sempre
      // há um assunto principal e que sempre é o primeiro)
      if (
        db.assunto &&
        db.assunto.length > 0 &&
        Number(db.assunto[0].codigoNacional) > 0
      ) {
        for (var i = 0; i < db.assunto.length; i++) {
          var ass = db.assunto[i];
          ass.descricao = CnjAssuntoBL.nome(ass.codigoNacional);
          ass.descricaoCompleta = CnjAssuntoBL.nomeCompleto(ass.codigoNacional);
          if (ass.principal) {
            this.$set(this.fixed, "assuntoPrincipalDescricao", ass.descricao);
            this.$set(
              this.fixed,
              "assuntoPrincipalDescricaoCompleta",
              ass.descricaoCompleta
            );
          }
        }
      }
    },
    mostrarTexto: function(doc, f) {
      ProcessoBL.mostrarTexto(this.fixed.movdoc, doc, f);
      this.modified = new Date();
    },
    mostrarDadosComplementares: function(ativo) {
      this.$parent.$emit("setting", "mostrarDadosComplementares", ativo);
    },
    mostrarProcessosRelacionados: function(ativo) {
      this.$parent.$emit("setting", "mostrarProcessosRelacionados", ativo);
    },
    mostrarPeca: function(idDocumento, disposition) {
      this.$http
        .get(
          "processo/" +
            this.numero +
            "/peca/" +
            idDocumento +
            "/pdf?sistema=" +
            this.sistema +
            (this.token ? "&token=" + this.token : "")
        )
        .then(
          response => {
            var jwt = response.data.jwt;
            var url =
              this.$http.options.root +
              "/download/" +
              jwt +
              "/" +
              this.numero +
              "-peca-" +
              idDocumento +
              ".pdf";
            if (disposition) window.location = url + "?disposition=attachment";
            else window.open(url);
            UtilsBL.logEvento("consulta-processual", "mostrar pdf peça");
          },
          error => {
            Bus.$emit("message", "Erro", error.data.errormsg);
          }
        );
    },
    mostrarCompleto: function() {
      this.$http
        .get(
          "processo/" +
            this.numero +
            "/pdf?sistema=" +
            this.sistema +
            (this.token ? "&token=" + this.token : "")
        )
        .then(
          response => {
            Bus.$emit(
              "prgAsyncStart",
              "PDF Completo",
              response.data.uuid,
              () => {
                var jwt = response.data.jwt;
                window.open(
                  this.$http.options.root +
                    "/download/" +
                    jwt +
                    "/" +
                    this.numero +
                    ".pdf"
                );
              }
            );
            UtilsBL.logEvento(
              "consulta-processual",
              "mostrar pdf completo",
              "individual"
            );
          },
          error => {
            Bus.$emit("message", "Erro", error.data.errormsg);
          }
        );
    },
    filtrarMovimentos: function(texto) {
      this.$parent.$emit("setting", "filtrarMovimentos", texto !== undefined);
      var f = this.filtro;
      if (texto) {
        if (
          texto.length > 0 &&
          texto.substring(0, 1) === "#" &&
          f &&
          f.length > 0 &&
          f.substring(0, 1) === "#"
        ) {
          this.filtro = f + " " + texto;
          return;
        }
      }
      this.filtro = texto;
      this.$nextTick(() => this.$refs.filtro.focus());
    },
    mostrarPartes: function(ativo) {
      this.$parent.$emit("setting", "mostrarPartes", ativo);
    },
    imprimir: function() {
      window.print();
    },
    formatDDMMYYYHHMM: function(s) {
      if (s === undefined) {
        return;
      }
      var r =
        s.substring(6, 8) +
        "/" +
        s.substring(4, 6) +
        "/" +
        s.substring(0, 4) +
        " " +
        s.substring(8, 10) +
        ":" +
        s.substring(10, 12);
      r = r.replace(" ", "&nbsp;");
      return r;
    },

    exibirProcessoPecaDetalhes: function(movdoc, marca) {
      this.currentMovDoc = movdoc;
      this.currentMarca = marca;
      this.$refs.processoPecaDetalhes.show(
        marca,
        this.marcadores,
        movdoc.doc && movdoc.doc.outroParametro
          ? movdoc.doc.outroParametro.paginaInicial
          : undefined,
        movdoc.doc && movdoc.doc.outroParametro
          ? movdoc.doc.outroParametro.paginaFinal
          : undefined
      );
    },

    cotar: function() {
      this.$refs.processoCota.show();
    },

    cotaEnviada: function(msg) {
      Bus.$emit("message", "Sucesso", "Cota enviada com sucesso. " + msg);
    },

    cotaNaoEnviada: function(msg, texto) {
      Bus.$emit(
        "message",
        "Erro",
        'Não foi possível enviar a cota "' +
          texto +
          '". Ocorreu o erro: "' +
          msg +
          '"'
      );
    },

    salvarProcessoPecaDetalhes: function(marca) {
      if (!this.currentMovDoc) return;

      var movdoc = this.currentMovDoc;
      var inicial =
        movdoc.doc && movdoc.doc.outroParametro
          ? movdoc.doc.outroParametro.paginaInicial
          : undefined;
      var final =
        movdoc.doc && movdoc.doc.outroParametro
          ? movdoc.doc.outroParametro.paginaFinal
          : undefined;
      if (inicial === marca.paginicial && final === marca.pagfinal) {
        marca.paginicial = undefined;
        marca.pagfinal = undefined;
      }

      var data = {
        idclasse: this.proc.dadosBasicos.classeProcessual,
        idmarca: this.currentMarca ? this.currentMarca.idmarca : undefined,
        texto: marca.texto,
        idestilo: marca.idestilo,
        paginicial: marca.paginicial,
        pagfinal: marca.pagfinal
      };

      this.$http
        .post(
          "processo/" +
            this.numero +
            "/peca/" +
            this.currentMovDoc.doc.idDocumento +
            "/marca?sistema=" +
            this.sistema,
          data,
          { block: true }
        )
        .then(
          response => {
            if (this.currentMarca) {
              var index = this.currentMovDoc.marca.indexOf(this.currentMarca);
              UtilsBL.overrideProperties(marca, response.data.marca);
              UtilsBL.overrideProperties(
                this.currentMovDoc.marca[index],
                marca
              );
            } else {
              UtilsBL.overrideProperties(marca, response.data.marca);
              this.currentMovDoc.marca.push(marca);
            }
          },
          error => {
            Bus.$emit("message", "Erro", error.data.errormsg);
          }
        );
    },

    excluirProcessoPecaDetalhes: function() {
      if (!this.currentMovDoc || !this.currentMarca) return;

      this.$http
        .delete("marca/" + this.currentMarca.idmarca, { block: true })
        .then(
          () => {
            var index = this.currentMovDoc.marca.indexOf(this.currentMarca);
            if (index > -1) this.currentMovDoc.marca.splice(index, 1);
          },
          error => {
            Bus.$emit("message", "Erro", error.data.errormsg);
          }
        );
    },

    favoritar: function(favorito) {
      this.errormsg = undefined;
      this.$http
        .post(
          "processo/" + this.numero + "/sinalizar",
          { favorito: favorito },
          { block: true }
        )
        .then(
          response => {
            var d = response.data;
            this.favorito = d.processo.favorito;
          },
          error => {
            this.warningmsg = error.data.errormsg;
          }
        );
    },

    mostrarNotas: function(show) {
      this.$parent.$emit("setting", "mostrarNotas", show);

      this.$nextTick(() => {
        this.$refs.notaUnidade.focus();
        this.notasAlteradas();
      });
    },

    notasAlteradas: function() {
      if (this.notaUnidade !== undefined && this.notaUnidade.trim() === "") {
        this.notaUnidade = undefined;
      }
      if (this.notaPessoal !== undefined && this.notaPessoal.trim() === "") {
        this.notaPessoal = undefined;
      }
      this.$refs.notaUnidade.style.height = "5px";
      this.$refs.notaPessoal.style.height = "5px";
      var h = Math.max(
        this.$refs.notaUnidade.scrollHeight,
        this.$refs.notaPessoal.scrollHeight
      );
      this.$refs.notaUnidade.style.height = h + "px";
      this.$refs.notaPessoal.style.height = h + "px";
    }
  },

  components: {
    timeline: Timeline,
    "processo-peca-detalhes": ProcessoPecaDetalhes,
    "processo-notas": ProcessoNotas,
    "processo-cota": ProcessoCota
  }
};
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
  padding-left: 0rem;
  padding-right: 0rem;
  margin-right: 0.5rem;
  margin-bottom: 0;
  margin-top: 0rem;
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
  background-color: rgba(0, 0, 0, 0.05);
}

.card-consulta-processual div p b {
  color: #fff;
}

.card-consulta-processual div p {
  margin-bottom: 0.5rem;
}

.card-consulta-processual div i {
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

textarea {
  border: none;
  background: none;
  width: 100%;
  resize: none;
  overflow: hidden;
  min-height: 50px;
}
</style>
