<template>
  <div class="container-fluid content">
    <div class="row">
      <div class="col-md-12">
        <h4 class="text-center mt-3 mb-3">Petição Intercorrente</h4>
      </div>
    </div>

    <div class="row pb-4" v-if="editando &amp;&amp; !mostrarQuantidadePorData">
      <div class="col-md-12">
        <vue-clip :options="vueclipOptions" :on-added-file="addedFileProxy" :on-complete="completeProxy">
          <template slot="clip-uploader-action">
            <div>
              <div class="dz-message drop-box">
                Arraste suas petições intercorrentes e solte elas nesta área, ou clique aqui para selecioná-las.
                <br>Use arquivos PDF com tamanho máximo de 5MB.
              </div>
            </div>
          </template>

          <template slot="clip-uploader-body" slot-scope="props">
            <div class="col-md-12 mt-3" v-if="hasErrorMessages(props.files)">
              <div class="alert alert-danger mb-0">
                <strong>Arquivos inválidos!</strong> Não foi possível aceitar os seguintes arquivos:
                <ul class="mb-0">
                  <li v-for="ifile in props.files" v-if="ifile.errorMessage">{{ifile.name}} ({{ifile.errorMessage}})</li>
                </ul>
              </div>
            </div>
          </template>

        </vue-clip>
      </div>
    </div>

    <div class="row" v-if="arquivos.length > 0 &amp;&amp; !mostrarQuantidadePorData">
      <div class="col-sm-12" style="padding-top: 1em;">
        <div class="table-responsive">
          <table class="table table-peticao">
            <thead class="thead-dark">
              <tr>
                <th>Processo</th>
                <th>Tipo</th>
                <th>Segredo</th>
                <th>Encerra Prazos</th>
                <th>Arquivo</th>
                <th>Status</th>
                <th style="text-align: center" v-if="editando"></th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="f in arquivos" :class="{odd: f.odd}">
                <td v-show="!f.anexo" :rowspan="f.rowspan">
                  <div v-if="editando" class="input-group">
                    <input type="text" class="form-control" style="min-width: 16em;" placeholder="Número do Processo" v-model="f.processo" @input="alterarArquivo(f)" :disabled="f.bloq || f.protocolado">
                    <button type="button" v-show="f.processo === undefined || f.processo === ''" @click="exibirProcessosMultiplos(f)" class="btn btn-sm btn-outline-primary ml-1" title="Inserir este PDF em múltiplos processos">&#x2795;</button>
                  </div>
                  <span class="unbreakable" v-if="!editando">
                    <router-link :to="{name: 'Processo', params: {numero: f.processo}, query: {avisos: $parent.cAvisos}}" target="_blank">{{f.processo}}</router-link>
                  </span>
                </td>

                <td v-show="!f.anexo" :rowspan="f.rowspan">
                  <select style="min-width: 8em;" v-if="editando" class="form-control mr-sm-2" v-model="f.tipo" :disabled="f.protocolado" @change="selecionarTipo(f, f.tipo)">
                    <option v-for="tipo in f.tipos" :value="tipo.id">{{tipo.descricao}}</option>
                    <option disabled hidden selected value="">[Selecionar]</option>
                  </select>
                  <span v-if="!editando">{{f.tipodescr}}</span>
                </td>

                <td v-show="!f.anexo" :rowspan="f.rowspan">
                  <select style="min-width: 4em;" v-if="editando" class="form-control mr-sm-2" v-model="f.segredo" :disabled="f.protocolado" @change="selecionarSegredo(f, f.segredo)">
                    <option disabled hidden selected value="">[Selecionar]</option>
                    <option :value="0">0. Sem Sigilo</option>
                    <option v-if="f.sigilo >= 1" :value="1">1. Segredo de Justiça</option>
                    <option v-if="f.sigilo >= 2" :value="2">2. Sigiloso Interno</option>
                    <option v-if="f.sigilo >= 3" :value="3">3. Sigiloso Interno</option>
                    <option v-if="f.sigilo >= 4" :value="4">4. Sigiloso Interno</option>
                    <option v-if="f.sigilo >= 5" :value="5">5. Restrito Juiz</option>
                  </select>
                  <span v-if="!editando">{{f.segredo ? 'Sim' : 'Não'}}</span>
                </td>

                <td v-show="!f.anexo" :rowspan="f.rowspan">
                  <v-selectpage  v-if="editando && avisos[f.processo] !== undefined && avisos[f.processo].length > 1" :data="avisos[f.processo]" v-model="f.encerraprazos" key-field="idaviso" show-field="tipo" multiple="true" title="Selecione um ou mais" :pagination="false" placeholder="[Selecione]"></v-selectpage>
                  <select v-if="editando && (avisos[f.processo] === undefined || avisos[f.processo].length <= 1)" style="min-width: 4em;" class="form-control mr-sm-2" v-model="f.encerraprazos" :disabled="f.protocolado" @change="selecionarEncerraPrazos(f, f.encerraprazos)">
                    <option disabled hidden selected value="">[Selecionar]</option>
                    <option :value="false">Não</option>
                    <option v-if="avisos[f.processo]" :value="true">Sim</option>
                  </select>
                  <span v-if="!editando">{{f.encerraprazos ? 'Sim' : 'Não'}}</span>
                </td>

                <td>
                  <a @click="view(doc)">
                    <a :href="$http.options.root + '/arquivo-temporario/' + f.id" target="_blank">{{f.nome}}</span>
                    </a>
                  </a>
                </td>

                <td class="status-td" :rowspan="f.protocolado ? f.rowspan : 1" v-show="f.protocolado ? !f.anexo : true">
                  <span v-show="f.file.progress &amp;&amp; f.file.progress < 100">{{f.file.progress.toFixed(1)}}%</span>
                  <span :class="{green: f.protocolado}" v-show="f.file.progress === 100 &amp;&amp; !f.errormsg">{{f.status}}</span>
                  <span v-show="f.errormsg" :class="{red: true}">{{f.errormsg}}</span>
                  <span v-show="f.$error">{{f.$error}} {{f.$errorParam}}</span>
                </td>

                <td align="center" v-if="editando">
                  <button type="button" @click="removerArquivo(f)" class="btn btn-sm btn-outline-danger">&#x274C;</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <div class="row" v-if="!mostrarQuantidadePorData">
      <div class="col-sm-12">
        <button type="button" @click="carregarProtocoladosRecentemente()" id="protocoladosRecentemente" class="btn btn-secondary d-print-none mr-3 mt-3">Consultar Protocolos
        </button>
        <button type="button" @click="limpar()" id="limpar" v-if="!editando" class="btn btn-secondary d-print-none mt-3">Enviar Outras Petições</button>
        <button type="button" @click="imprimirArquivosComErro()" v-if="arquivosComErro.length > 0" class="btn btn-info float-right ml-3 d-print-none mt-3">
          Imprimir Arquivos com Erro&nbsp;&nbsp;
          <span class="badge badge-pill badge-warning">{{arquivosComErro.length}}</span>
        </button>
        <button type="button" @click="imprimir()" id="imprimir" v-if="!editando" class="btn btn-info float-right ml-3 d-print-none mt-3">Imprimir</button>
        <button type="button" @click="peticionar()" id="prosseguir" v-if="arquivos.length > 0 && (editando || arquivosAProtocolar)" :disabled="!isAllValid()" class="btn btn-primary float-right d-print-none mt-3">
          Protocolar&nbsp;&nbsp;
          <span class="badge badge-pill badge-warning">{{arquivosAProtocolar}}</span>
        </button>
      </div>
    </div>

    <!-- QUANTIDADE POR DATA -->
    <div class="row" v-if="mostrarQuantidadePorData &amp;&amp; dataDeProtocolo === undefined">
      <div class="col col-12" v-if="quantidadePorData.length > 0">
        <div class="protocolos-header">Petição(ões) Intercorrente(s) Protocolada(s) Recentemente</div>
        <div class="table-responsive">
          <table class="table table-striped mb-0">
            <thead class="thead-dark">
              <tr>
                <th>Data</th>
                <th style="text-align: right;">Quantidade</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="p in quantidadePorData">
                <td>
                  <span v-html="p.dataFormatada"></span>
                </td>
                <td style="text-align: right;">
                  <a href="" @click.prevent="carregarResumo(p.data)">{{p.quantidade}}</a>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div class="col col-sm-12" v-if="quantidadePorData.length == 0">
        <p class="alert alert-warning">
          <strong>Atenção!</strong> Nenhuma petição intercorrente protocolada nos últimos 7 dias.
        </p>
      </div>
      <div class="col-sm-12" style="padding-top: 1em;">
        <button type="button" @click="voltarParaEdicao()" v-if="quantidadePorData !== undefined" class="btn btn-success d-print-none">Voltar</button>
      </div>
    </div>

    <!-- RELATÓRIO DIÁRIO -->
    <div class="row" v-if="dataDeProtocolo">
      <div class="col col-sm-12">
        <div class="protocolos-header">
          Petições Intercorrentes Protocoladas em
          <span v-html="dataDeProtocolo"></span>
        </div>
        <div class="d-print-none float-right pb-2">
          Filtrar:
          <input type="text" v-model="filtroProtocolo"></input>
        </div>
        <div class="table-responsive">
          <table class="table table-striped mb-0 table-protocolo">
            <thead class="thead-dark">
              <tr>
                <th>Processo</th>
                <th>Classe</th>
                <th>Data/Hora</th>
                <th>Protocolo</th>
                <th>Sistema/Órgão</th>
                <th>Unidade</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="r in resumoPorDataFiltrado">
                <td>
                  <router-link :to="{name: 'Processo', params: {numero: r.processo}, query: {avisos: $parent.cAvisos}}" target="_blank">{{r.processo}}</router-link>
                </td>
                <td>{{r.classe}}</td>
                <td>
                  <span v-html="r.dataprotocoloFormatada"></span>
                </td>
                <td>{{r.protocolo}}</td>
                <td>{{$parent.test.properties['balcaovirtual.' + r.sistema + '.name']}}</td>
                <td>{{r.unidade}}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div class="col-sm-12" style="padding-top: 1em;">
        <button type="button" @click="voltarParaQuantidade()" v-if="dataDeProtocolo !== undefined" class="btn btn-success d-print-none">Voltar</button>
        <button type="button" @click="imprimir()" id="imprimir" v-if="resumoPorData.length > 0" class="btn btn-info float-right ml-3 d-print-none">Imprimir</button>
      </div>
    </div>
    <processo-multiplos ref="processosMultiplos" @ok="multiplosProcessos"></processo-multiplos>
  </div>
</template>

<script>
import AuthBL from '../bl/auth.js'
import ProcessoBL from '../bl/processo.js'
import UtilsBL from '../bl/utils.js'
import ProcessoMultiplos from './ProcessoMultiplos'
import { Bus } from '../bl/bus.js'

const reProc = /^(\d{7})-?(\d{2})\.?(\d{4})\.?(4)\.?(02)\.?(\d{4})\/?-?(\d{2})?$/

export default {
  name: 'peticao-intercorrente',
  components: {
    ProcessoMultiplos
  },

  mounted () {
    // Carragar a lista de avisos pendentes
    if (this.avisos === undefined) {
//      this.$http.get('aviso/listar?mni=true', { block: true }).then(response => {
//        this.fixAvisos(response.data)
//      }, error => UtilsBL.errormsg(error, this))

      this.fixAvisos({
        'list': [
          {
            'idaviso': '201907220000426',
            'dataaviso': '2019-07-22T01:06:10.000-03:00',
            'tipo': 'Intimação',
            'processo': '00276051920184025101',
            'unidade': 'RJ000037S',
            'unidadenome': 'Juízo Substituto da 13ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001249',
            'dataaviso': '2019-07-23T01:07:48.000-03:00',
            'tipo': 'Intimação',
            'processo': '50171709520184025101',
            'unidade': 'RJ000037F',
            'unidadenome': 'Juízo Federal da 13ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001251',
            'dataaviso': '2019-07-23T01:07:48.000-03:00',
            'tipo': 'Intimação',
            'processo': '50003965720184025111',
            'unidade': 'RJ000030F',
            'unidadenome': 'Juízo Federal da 30ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001252',
            'dataaviso': '2019-07-23T01:07:48.000-03:00',
            'tipo': 'Intimação',
            'processo': '50014663620184025103',
            'unidade': 'RJ000201F',
            'unidadenome': 'Juízo Federal da 1ª VF de Campos',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001259',
            'dataaviso': '2019-07-23T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '00069183120124025101',
            'unidade': 'RJ000017F',
            'unidadenome': 'Juízo Federal da 17ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001260',
            'dataaviso': '2019-07-23T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '01119946820174025101',
            'unidade': 'RJ000002F',
            'unidadenome': 'Juízo Federal da 2ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001265',
            'dataaviso': '2019-07-23T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '00369237220184025118',
            'unidade': 'RJ001802S',
            'unidadenome': 'Juízo Substituto da 2ª VF de Duque de Caxias',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001266',
            'dataaviso': '2019-07-23T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '01031165720174025101',
            'unidade': 'RJ000022F',
            'unidadenome': 'Juízo Federal da 22ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001269',
            'dataaviso': '2019-07-23T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50004717420194025107',
            'unidade': 'RJ000701S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Itaboraí',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001270',
            'dataaviso': '2019-07-23T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '00070837820124025101',
            'unidade': 'RJ000017S',
            'unidadenome': 'Juízo Substituto da 17ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001273',
            'dataaviso': '2019-07-23T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '00689722320184025101',
            'unidade': 'RJ000022F',
            'unidadenome': 'Juízo Federal da 22ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001274',
            'dataaviso': '2019-07-23T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50219296820194025101',
            'unidade': 'RJ000006S',
            'unidadenome': 'Juízo Substituto da 6ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001275',
            'dataaviso': '2019-07-23T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50222977720194025101',
            'unidade': 'RJ000006F',
            'unidadenome': 'Juízo Federal da 6ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001276',
            'dataaviso': '2019-07-23T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50223860320194025101',
            'unidade': 'RJ000006S',
            'unidadenome': 'Juízo Substituto da 6ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001277',
            'dataaviso': '2019-07-23T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '00650677820164025101',
            'unidade': 'RJ000007S',
            'unidadenome': 'Juízo Substituto da 7ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001280',
            'dataaviso': '2019-07-23T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '00164690620104025101',
            'unidade': 'RJ000007S',
            'unidadenome': 'Juízo Substituto da 7ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001282',
            'dataaviso': '2019-07-23T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '02134849720174025113',
            'unidade': 'RJ001301F',
            'unidadenome': 'Juízo Federal da 1ª VF de Três Rios',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001286',
            'dataaviso': '2019-07-23T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50015566520194025117',
            'unidade': 'RJ001703S',
            'unidadenome': 'Juízo Substituto da 3ª VF de São Gonçalo',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001289',
            'dataaviso': '2019-07-23T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50043228220194025120',
            'unidade': 'RJ002001S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Nova Iguaçu',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001292',
            'dataaviso': '2019-07-23T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '02135057320174025113',
            'unidade': 'RJ001301S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Três Rios',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001294',
            'dataaviso': '2019-07-23T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '01414916220154025113',
            'unidade': 'RJ001301S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Três Rios',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001295',
            'dataaviso': '2019-07-23T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '00820906620184025101',
            'unidade': 'RJ000018F',
            'unidadenome': 'Juízo Federal da 18ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001296',
            'dataaviso': '2019-07-23T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '01554154520164025101',
            'unidade': 'RJ000023S',
            'unidadenome': 'Juízo Substituto da 23ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001298',
            'dataaviso': '2019-07-23T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '00058771920184025101',
            'unidade': 'RJ000054S',
            'unidadenome': 'Juízo Substituto da 4ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001314',
            'dataaviso': '2019-07-23T01:07:50.000-03:00',
            'tipo': 'Intimação',
            'processo': '50476658820194025101',
            'unidade': 'RJ000015F',
            'unidadenome': 'Juízo Federal da 15ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001315',
            'dataaviso': '2019-07-23T01:07:50.000-03:00',
            'tipo': 'Citação',
            'processo': '50476658820194025101',
            'unidade': 'RJ000015F',
            'unidadenome': 'Juízo Federal da 15ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001316',
            'dataaviso': '2019-07-23T01:07:50.000-03:00',
            'tipo': 'Intimação',
            'processo': '50439859520194025101',
            'unidade': 'RJ000029F',
            'unidadenome': 'Juízo Federal da 29ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001318',
            'dataaviso': '2019-07-23T01:07:50.000-03:00',
            'tipo': 'Intimação',
            'processo': '01419489420154025113',
            'unidade': 'RJ001301F',
            'unidadenome': 'Juízo Federal da 1ª VF de Três Rios',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001322',
            'dataaviso': '2019-07-23T01:07:50.000-03:00',
            'tipo': 'Intimação',
            'processo': '50442430820194025101',
            'unidade': 'RJ000006F',
            'unidadenome': 'Juízo Federal da 6ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001325',
            'dataaviso': '2019-07-23T01:07:50.000-03:00',
            'tipo': 'Intimação',
            'processo': '00101967120184025152',
            'unidade': 'RJ005202F',
            'unidadenome': 'Juízo Federal do 2º JEF de Niterói',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001327',
            'dataaviso': '2019-07-23T01:07:50.000-03:00',
            'tipo': 'Citação',
            'processo': '50470449120194025101',
            'unidade': 'RJ000017S',
            'unidadenome': 'Juízo Substituto da 17ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001330',
            'dataaviso': '2019-07-23T01:07:50.000-03:00',
            'tipo': 'Intimação',
            'processo': '01863173720174025168',
            'unidade': 'RJ006801S',
            'unidadenome': 'Juízo Substituto da 3ª VF de Duque de Caxias',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001331',
            'dataaviso': '2019-07-23T01:07:50.000-03:00',
            'tipo': 'Intimação',
            'processo': '50014100920184025101',
            'unidade': 'RJ005108F',
            'unidadenome': 'Juízo Federal do 8º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001338',
            'dataaviso': '2019-07-23T01:07:50.000-03:00',
            'tipo': 'Intimação',
            'processo': '00242474220184025167',
            'unidade': 'RJ006701S',
            'unidadenome': 'Juízo Substituto do 1º JEF de São Gonçalo',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001339',
            'dataaviso': '2019-07-23T01:07:50.000-03:00',
            'tipo': 'Intimação',
            'processo': '50011984020184025116',
            'unidade': 'RJ001601S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Macaé',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001340',
            'dataaviso': '2019-07-23T01:07:50.000-03:00',
            'tipo': 'Intimação',
            'processo': '00839306620184025116',
            'unidade': 'RJ001601F',
            'unidadenome': 'Juízo Federal da 1ª VF de Macaé',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001348',
            'dataaviso': '2019-07-23T01:07:50.000-03:00',
            'tipo': 'Intimação',
            'processo': '01398849320154025119',
            'unidade': 'RJ001901F',
            'unidadenome': 'Juízo Federal da 1ª VF de Barra do Piraí',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001349',
            'dataaviso': '2019-07-23T01:07:50.000-03:00',
            'tipo': 'Intimação',
            'processo': '50036514720184025103',
            'unidade': 'RJ005302S',
            'unidadenome': 'Juízo Substituto da 4ª VF de Campos',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001356',
            'dataaviso': '2019-07-23T01:07:50.000-03:00',
            'tipo': 'Intimação',
            'processo': '50220676920184025101',
            'unidade': 'RJ005105F',
            'unidadenome': 'Juízo Federal do 5º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001359',
            'dataaviso': '2019-07-23T01:07:50.000-03:00',
            'tipo': 'Intimação',
            'processo': '00681357020154025101',
            'unidade': 'RJ000021S',
            'unidadenome': 'Juízo Substituto da 21ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001363',
            'dataaviso': '2019-07-23T01:07:50.000-03:00',
            'tipo': 'Intimação',
            'processo': '50477074020194025101',
            'unidade': 'RJ000022F',
            'unidadenome': 'Juízo Federal da 22ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001364',
            'dataaviso': '2019-07-23T01:07:50.000-03:00',
            'tipo': 'Citação',
            'processo': '50477074020194025101',
            'unidade': 'RJ000022F',
            'unidadenome': 'Juízo Federal da 22ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001365',
            'dataaviso': '2019-07-23T01:07:50.000-03:00',
            'tipo': 'Intimação',
            'processo': '50062332620184025101',
            'unidade': 'RJ005101F',
            'unidadenome': 'Juízo Federal do 1º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001367',
            'dataaviso': '2019-07-23T01:07:50.000-03:00',
            'tipo': 'Urgente',
            'processo': '50429717620194025101',
            'unidade': 'RJ000017F',
            'unidadenome': 'Juízo Federal da 17ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001368',
            'dataaviso': '2019-07-23T01:07:50.000-03:00',
            'tipo': 'Citação',
            'processo': '50429717620194025101',
            'unidade': 'RJ000017F',
            'unidadenome': 'Juízo Federal da 17ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001370',
            'dataaviso': '2019-07-23T01:07:50.000-03:00',
            'tipo': 'Intimação',
            'processo': '50034386220194025117',
            'unidade': 'RJ001703F',
            'unidadenome': 'Juízo Federal da 3ª VF de São Gonçalo',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001371',
            'dataaviso': '2019-07-23T01:07:50.000-03:00',
            'tipo': 'Intimação',
            'processo': '50050027620194025117',
            'unidade': 'RJ001703F',
            'unidadenome': 'Juízo Federal da 3ª VF de São Gonçalo',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001372',
            'dataaviso': '2019-07-23T01:07:50.000-03:00',
            'tipo': 'Intimação',
            'processo': '02246761020174025151',
            'unidade': 'RJ005111F',
            'unidadenome': 'Juízo Federal do 11º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001373',
            'dataaviso': '2019-07-23T01:07:50.000-03:00',
            'tipo': 'Intimação',
            'processo': '50008567420184025101',
            'unidade': 'RJ005111S',
            'unidadenome': 'Juízo Substituto do 11º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001374',
            'dataaviso': '2019-07-23T01:07:50.000-03:00',
            'tipo': 'Intimação',
            'processo': '50026497820194025112',
            'unidade': 'RJ001201S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Itaperuna',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001377',
            'dataaviso': '2019-07-23T01:07:50.000-03:00',
            'tipo': 'Intimação',
            'processo': '50026194320194025112',
            'unidade': 'RJ001201F',
            'unidadenome': 'Juízo Federal da 1ª VF de Itaperuna',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001378',
            'dataaviso': '2019-07-23T01:07:50.000-03:00',
            'tipo': 'Intimação',
            'processo': '50026437120194025112',
            'unidade': 'RJ001201F',
            'unidadenome': 'Juízo Federal da 1ª VF de Itaperuna',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001379',
            'dataaviso': '2019-07-23T01:07:50.000-03:00',
            'tipo': 'Intimação',
            'processo': '50026575520194025112',
            'unidade': 'RJ001201F',
            'unidadenome': 'Juízo Federal da 1ª VF de Itaperuna',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001380',
            'dataaviso': '2019-07-23T01:07:50.000-03:00',
            'tipo': 'Intimação',
            'processo': '50026592520194025112',
            'unidade': 'RJ001201F',
            'unidadenome': 'Juízo Federal da 1ª VF de Itaperuna',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001381',
            'dataaviso': '2019-07-23T01:07:50.000-03:00',
            'tipo': 'Intimação',
            'processo': '50026619220194025112',
            'unidade': 'RJ001201F',
            'unidadenome': 'Juízo Federal da 1ª VF de Itaperuna',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001382',
            'dataaviso': '2019-07-23T01:07:50.000-03:00',
            'tipo': 'Intimação',
            'processo': '50026627720194025112',
            'unidade': 'RJ001201F',
            'unidadenome': 'Juízo Federal da 1ª VF de Itaperuna',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001384',
            'dataaviso': '2019-07-23T01:07:50.000-03:00',
            'tipo': 'Intimação',
            'processo': '50026644720194025112',
            'unidade': 'RJ001201S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Itaperuna',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001385',
            'dataaviso': '2019-07-23T01:07:50.000-03:00',
            'tipo': 'Intimação',
            'processo': '50040568920184025101',
            'unidade': 'RJ005106F',
            'unidadenome': 'Juízo Federal do 6º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001386',
            'dataaviso': '2019-07-23T01:07:50.000-03:00',
            'tipo': 'Intimação',
            'processo': '50045592820194025117',
            'unidade': 'RJ000020S',
            'unidadenome': 'Juízo Substituto da 20ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907230001387',
            'dataaviso': '2019-07-23T01:07:50.000-03:00',
            'tipo': 'Citação',
            'processo': '50090745720194025101',
            'unidade': 'RJ000035F',
            'unidadenome': 'Juízo Federal da 25ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001831',
            'dataaviso': '2019-07-24T01:06:51.000-03:00',
            'tipo': 'Intimação',
            'processo': '50104337620184025101',
            'unidade': 'RJ000012S',
            'unidadenome': 'Juízo Substituto da 12ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001832',
            'dataaviso': '2019-07-24T01:06:51.000-03:00',
            'tipo': 'Intimação',
            'processo': '50070490820184025101',
            'unidade': 'RJ000011S',
            'unidadenome': 'Juízo Substituto da 11ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001839',
            'dataaviso': '2019-07-24T01:06:51.000-03:00',
            'tipo': 'Intimação',
            'processo': '50243498020184025101',
            'unidade': 'RJ000027F',
            'unidadenome': 'Juízo Federal da 27ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001841',
            'dataaviso': '2019-07-24T01:06:51.000-03:00',
            'tipo': 'Intimação',
            'processo': '50006121220184025113',
            'unidade': 'RJ001301S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Três Rios',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001842',
            'dataaviso': '2019-07-24T01:06:51.000-03:00',
            'tipo': 'Intimação',
            'processo': '50392574520184025101',
            'unidade': 'RJ000021S',
            'unidadenome': 'Juízo Substituto da 21ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001843',
            'dataaviso': '2019-07-24T01:06:51.000-03:00',
            'tipo': 'Intimação',
            'processo': '50042846120184025102',
            'unidade': 'RJ000101F',
            'unidadenome': 'Juízo Federal da 1ª VF de Niterói',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001845',
            'dataaviso': '2019-07-24T01:06:51.000-03:00',
            'tipo': 'Intimação',
            'processo': '50020064520184025116',
            'unidade': 'RJ001601F',
            'unidadenome': 'Juízo Federal da 1ª VF de Macaé',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001846',
            'dataaviso': '2019-07-24T01:06:51.000-03:00',
            'tipo': 'Intimação',
            'processo': '50462215420184025101',
            'unidade': 'RJ000003F',
            'unidadenome': 'Juízo Federal da 3ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001847',
            'dataaviso': '2019-07-24T01:06:51.000-03:00',
            'tipo': 'Intimação',
            'processo': '50020064520184025116',
            'unidade': 'RJ001601F',
            'unidadenome': 'Juízo Federal da 1ª VF de Macaé',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001850',
            'dataaviso': '2019-07-24T01:06:51.000-03:00',
            'tipo': 'Intimação',
            'processo': '00257864720184025101',
            'unidade': 'RJ000037F',
            'unidadenome': 'Juízo Federal da 13ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001854',
            'dataaviso': '2019-07-24T01:06:51.000-03:00',
            'tipo': 'Citação',
            'processo': '00006951820194025101',
            'unidade': 'RJ000022F',
            'unidadenome': 'Juízo Federal da 22ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001856',
            'dataaviso': '2019-07-24T01:06:51.000-03:00',
            'tipo': 'Intimação',
            'processo': '00945046720164025101',
            'unidade': 'RJ000012F',
            'unidadenome': 'Juízo Federal da 12ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001862',
            'dataaviso': '2019-07-24T01:06:51.000-03:00',
            'tipo': 'Intimação',
            'processo': '50045683820194025101',
            'unidade': 'RJ000002F',
            'unidadenome': 'Juízo Federal da 2ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001866',
            'dataaviso': '2019-07-24T01:06:51.000-03:00',
            'tipo': 'Intimação',
            'processo': '00408527720124025101',
            'unidade': 'RJ001401F',
            'unidadenome': 'Juízo Federal da 1ª VF de Magé',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001867',
            'dataaviso': '2019-07-24T01:06:51.000-03:00',
            'tipo': 'Intimação',
            'processo': '50095993920194025101',
            'unidade': 'RJ000037S',
            'unidadenome': 'Juízo Substituto da 13ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001874',
            'dataaviso': '2019-07-24T01:06:51.000-03:00',
            'tipo': 'Intimação',
            'processo': '00097252420124025101',
            'unidade': 'RJ000007S',
            'unidadenome': 'Juízo Substituto da 7ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001876',
            'dataaviso': '2019-07-24T01:06:51.000-03:00',
            'tipo': 'Intimação',
            'processo': '00820230420184025101',
            'unidade': 'RJ000008S',
            'unidadenome': 'Juízo Substituto da 8ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001877',
            'dataaviso': '2019-07-24T01:06:51.000-03:00',
            'tipo': 'Intimação',
            'processo': '50018073420194025101',
            'unidade': 'RJ000011F',
            'unidadenome': 'Juízo Federal da 11ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001878',
            'dataaviso': '2019-07-24T01:06:51.000-03:00',
            'tipo': 'Intimação',
            'processo': '02216470220174025102',
            'unidade': 'RJ000103S',
            'unidadenome': 'Juízo Substituto da 3ª VF de Niterói',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001879',
            'dataaviso': '2019-07-24T01:06:51.000-03:00',
            'tipo': 'Intimação',
            'processo': '00092705920124025101',
            'unidade': 'RJ000055F',
            'unidadenome': 'Juízo Federal da 32ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001880',
            'dataaviso': '2019-07-24T01:06:51.000-03:00',
            'tipo': 'Intimação',
            'processo': '00142767620144025101',
            'unidade': 'RJ000027F',
            'unidadenome': 'Juízo Federal da 27ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001882',
            'dataaviso': '2019-07-24T01:06:51.000-03:00',
            'tipo': 'Intimação',
            'processo': '00019328720194025101',
            'unidade': 'RJ000022F',
            'unidadenome': 'Juízo Federal da 22ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001883',
            'dataaviso': '2019-07-24T01:06:51.000-03:00',
            'tipo': 'Intimação',
            'processo': '00737599720154025102',
            'unidade': 'RJ000103S',
            'unidadenome': 'Juízo Substituto da 3ª VF de Niterói',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001884',
            'dataaviso': '2019-07-24T01:06:51.000-03:00',
            'tipo': 'Intimação',
            'processo': '00096204220154025101',
            'unidade': 'RJ000018F',
            'unidadenome': 'Juízo Federal da 18ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001887',
            'dataaviso': '2019-07-24T01:06:51.000-03:00',
            'tipo': 'Intimação',
            'processo': '50208626820194025101',
            'unidade': 'RJ000039F',
            'unidadenome': 'Juízo Federal da 9ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001889',
            'dataaviso': '2019-07-24T01:06:51.000-03:00',
            'tipo': 'Intimação',
            'processo': '50223661220194025101',
            'unidade': 'RJ000023S',
            'unidadenome': 'Juízo Substituto da 23ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001894',
            'dataaviso': '2019-07-24T01:06:51.000-03:00',
            'tipo': 'Intimação',
            'processo': '00095676620124025101',
            'unidade': 'RJ000022S',
            'unidadenome': 'Juízo Substituto da 22ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001895',
            'dataaviso': '2019-07-24T01:06:51.000-03:00',
            'tipo': 'Intimação',
            'processo': '50371806320184025101',
            'unidade': 'RJ000039F',
            'unidadenome': 'Juízo Federal da 9ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001896',
            'dataaviso': '2019-07-24T01:06:51.000-03:00',
            'tipo': 'Intimação',
            'processo': '50344548220194025101',
            'unidade': 'RJ000002F',
            'unidadenome': 'Juízo Federal da 2ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001898',
            'dataaviso': '2019-07-24T01:06:51.000-03:00',
            'tipo': 'Citação',
            'processo': '50376211020194025101',
            'unidade': 'RJ000029F',
            'unidadenome': 'Juízo Federal da 29ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001901',
            'dataaviso': '2019-07-24T01:06:51.000-03:00',
            'tipo': 'Intimação',
            'processo': '50302560220194025101',
            'unidade': 'RJ000002F',
            'unidadenome': 'Juízo Federal da 2ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001909',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Intimação',
            'processo': '50002212320194025113',
            'unidade': 'RJ001301S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Três Rios',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001910',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Intimação',
            'processo': '50002212320194025113',
            'unidade': 'RJ001301S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Três Rios',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001959',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Intimação',
            'processo': '00209394620184025151',
            'unidade': 'RJ005102S',
            'unidadenome': 'Juízo Substituto do 2º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001960',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Intimação',
            'processo': '00738456620184025101',
            'unidade': 'RJ000015S',
            'unidadenome': 'Juízo Substituto da 15ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001963',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Intimação',
            'processo': '00536085120184025120',
            'unidade': 'RJ002001F',
            'unidadenome': 'Juízo Federal da 1ª VF de Nova Iguaçu',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001964',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Intimação',
            'processo': '05001154420184025107',
            'unidade': 'RJ000701S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Itaboraí',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001966',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Intimação',
            'processo': '50494303120184025101',
            'unidade': 'RJ005101S',
            'unidadenome': 'Juízo Substituto do 1º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001968',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Intimação',
            'processo': '00595811520164025101',
            'unidade': 'RJ000011S',
            'unidadenome': 'Juízo Substituto da 11ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001969',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Intimação',
            'processo': '01057414020124025101',
            'unidade': 'RJ000006S',
            'unidadenome': 'Juízo Substituto da 6ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001971',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Intimação',
            'processo': '00485469720124025101',
            'unidade': 'RJ000006F',
            'unidadenome': 'Juízo Federal da 6ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001972',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Intimação',
            'processo': '00757042020184025101',
            'unidade': 'RJ000006F',
            'unidadenome': 'Juízo Federal da 6ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001973',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Intimação',
            'processo': '00063608820144025101',
            'unidade': 'RJ000006F',
            'unidadenome': 'Juízo Federal da 6ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001974',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Intimação',
            'processo': '00169373320114025101',
            'unidade': 'RJ000006S',
            'unidadenome': 'Juízo Substituto da 6ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001976',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Citação',
            'processo': '50476944120194025101',
            'unidade': 'RJ000029S',
            'unidadenome': 'Juízo Substituto da 29ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001977',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Citação',
            'processo': '50469452420194025101',
            'unidade': 'RJ000029S',
            'unidadenome': 'Juízo Substituto da 29ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001978',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Citação',
            'processo': '50469452420194025101',
            'unidade': 'RJ000029S',
            'unidadenome': 'Juízo Substituto da 29ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001979',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Citação',
            'processo': '50469452420194025101',
            'unidade': 'RJ000029S',
            'unidadenome': 'Juízo Substituto da 29ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001980',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Citação',
            'processo': '50469452420194025101',
            'unidade': 'RJ000029S',
            'unidadenome': 'Juízo Substituto da 29ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001981',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Citação',
            'processo': '50469452420194025101',
            'unidade': 'RJ000029S',
            'unidadenome': 'Juízo Substituto da 29ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001984',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Intimação',
            'processo': '50304933620194025101',
            'unidade': 'RJ000018F',
            'unidadenome': 'Juízo Federal da 18ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001985',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Citação',
            'processo': '50252322720184025101',
            'unidade': 'RJ000039S',
            'unidadenome': 'Juízo Substituto da 9ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001986',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Citação',
            'processo': '50329235820194025101',
            'unidade': 'RJ000039F',
            'unidadenome': 'Juízo Federal da 9ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001987',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Intimação',
            'processo': '00429433820154025101',
            'unidade': 'RJ000007S',
            'unidadenome': 'Juízo Substituto da 7ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240001995',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Intimação',
            'processo': '00256496120184025167',
            'unidade': 'RJ006701S',
            'unidadenome': 'Juízo Substituto do 1º JEF de São Gonçalo',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240002007',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Intimação',
            'processo': '00806391020154025166',
            'unidade': 'RJ001601S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Macaé',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240002014',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Intimação',
            'processo': '50012763420184025116',
            'unidade': 'RJ001601F',
            'unidadenome': 'Juízo Federal da 1ª VF de Macaé',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240002020',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Intimação',
            'processo': '50389202220194025101',
            'unidade': 'RJ001703S',
            'unidadenome': 'Juízo Substituto da 3ª VF de São Gonçalo',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240002022',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Intimação',
            'processo': '50027078120194025112',
            'unidade': 'RJ001201F',
            'unidadenome': 'Juízo Federal da 1ª VF de Itaperuna',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240002023',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Intimação',
            'processo': '50027086620194025112',
            'unidade': 'RJ001201F',
            'unidadenome': 'Juízo Federal da 1ª VF de Itaperuna',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240002024',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Intimação',
            'processo': '50011292020184025112',
            'unidade': 'RJ001201F',
            'unidadenome': 'Juízo Federal da 1ª VF de Itaperuna',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240002025',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Intimação',
            'processo': '50027112120194025112',
            'unidade': 'RJ001201F',
            'unidadenome': 'Juízo Federal da 1ª VF de Itaperuna',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240002026',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Intimação',
            'processo': '50027216520194025112',
            'unidade': 'RJ001201F',
            'unidadenome': 'Juízo Federal da 1ª VF de Itaperuna',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240002027',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Intimação',
            'processo': '50477888620194025101',
            'unidade': 'RJ000026F',
            'unidadenome': 'Juízo Federal da 26ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240002028',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Intimação',
            'processo': '50471314720194025101',
            'unidade': 'RJ000026S',
            'unidadenome': 'Juízo Substituto da 26ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240002029',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Intimação',
            'processo': '00019912420194025118',
            'unidade': 'RJ001802S',
            'unidadenome': 'Juízo Substituto da 2ª VF de Duque de Caxias',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240002031',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Citação',
            'processo': '50453935820184025101',
            'unidade': 'RJ000016F',
            'unidadenome': 'Juízo Federal da 16ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240002036',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Intimação',
            'processo': '50026540320194025112',
            'unidade': 'RJ001201S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Itaperuna',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240002037',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Intimação',
            'processo': '50026696920194025112',
            'unidade': 'RJ001201S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Itaperuna',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240002038',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Intimação',
            'processo': '50376964920194025101',
            'unidade': 'RJ000055S',
            'unidadenome': 'Juízo Substituto da 32ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240002039',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Urgente',
            'processo': '50428530320194025101',
            'unidade': 'RJ000003F',
            'unidadenome': 'Juízo Federal da 3ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240002040',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Citação',
            'processo': '50078339420194025118',
            'unidade': 'RJ001802F',
            'unidadenome': 'Juízo Federal da 2ª VF de Duque de Caxias',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240002041',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Intimação',
            'processo': '00201513220114025101',
            'unidade': 'RJ000014S',
            'unidadenome': 'Juízo Substituto da 14ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907240002042',
            'dataaviso': '2019-07-24T01:06:52.000-03:00',
            'tipo': 'Intimação',
            'processo': '00080182120124025101',
            'unidade': 'RJ000054F',
            'unidadenome': 'Juízo Federal da 4ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001480',
            'dataaviso': '2019-07-25T01:06:36.000-03:00',
            'tipo': 'Intimação',
            'processo': '50045635020184025101',
            'unidade': 'RJ000037S',
            'unidadenome': 'Juízo Substituto da 13ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001481',
            'dataaviso': '2019-07-25T01:06:36.000-03:00',
            'tipo': 'Intimação',
            'processo': '50045635020184025101',
            'unidade': 'RJ000037S',
            'unidadenome': 'Juízo Substituto da 13ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001483',
            'dataaviso': '2019-07-25T01:06:36.000-03:00',
            'tipo': 'Intimação',
            'processo': '50282696220184025101',
            'unidade': 'RJ000037S',
            'unidadenome': 'Juízo Substituto da 13ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001487',
            'dataaviso': '2019-07-25T01:06:36.000-03:00',
            'tipo': 'Intimação',
            'processo': '00658587620184025101',
            'unidade': 'RJ000054F',
            'unidadenome': 'Juízo Federal da 4ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001488',
            'dataaviso': '2019-07-25T01:06:36.000-03:00',
            'tipo': 'Intimação',
            'processo': '01073355020164025101',
            'unidade': 'RJ000029S',
            'unidadenome': 'Juízo Substituto da 29ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001489',
            'dataaviso': '2019-07-25T01:06:36.000-03:00',
            'tipo': 'Intimação',
            'processo': '00812107420184025101',
            'unidade': 'RJ000029F',
            'unidadenome': 'Juízo Federal da 29ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001490',
            'dataaviso': '2019-07-25T01:06:36.000-03:00',
            'tipo': 'Intimação',
            'processo': '02249121520174025101',
            'unidade': 'RJ000029F',
            'unidadenome': 'Juízo Federal da 29ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001491',
            'dataaviso': '2019-07-25T01:06:36.000-03:00',
            'tipo': 'Intimação',
            'processo': '00878670320164025101',
            'unidade': 'RJ000054S',
            'unidadenome': 'Juízo Substituto da 4ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001494',
            'dataaviso': '2019-07-25T01:06:36.000-03:00',
            'tipo': 'Intimação',
            'processo': '50024508920194025101',
            'unidade': 'RJ000037F',
            'unidadenome': 'Juízo Federal da 13ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001498',
            'dataaviso': '2019-07-25T01:06:36.000-03:00',
            'tipo': 'Intimação',
            'processo': '00614016920164025101',
            'unidade': 'RJ000008S',
            'unidadenome': 'Juízo Substituto da 8ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001499',
            'dataaviso': '2019-07-25T01:06:36.000-03:00',
            'tipo': 'Intimação',
            'processo': '00781136620184025101',
            'unidade': 'RJ000012S',
            'unidadenome': 'Juízo Substituto da 12ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001500',
            'dataaviso': '2019-07-25T01:06:36.000-03:00',
            'tipo': 'Intimação',
            'processo': '00092410920124025101',
            'unidade': 'RJ000017S',
            'unidadenome': 'Juízo Substituto da 17ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001502',
            'dataaviso': '2019-07-25T01:06:36.000-03:00',
            'tipo': 'Intimação',
            'processo': '00006003220124025101',
            'unidade': 'RJ000002F',
            'unidadenome': 'Juízo Federal da 2ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001503',
            'dataaviso': '2019-07-25T01:06:36.000-03:00',
            'tipo': 'Intimação',
            'processo': '01724329420164025101',
            'unidade': 'RJ000011F',
            'unidadenome': 'Juízo Federal da 11ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001504',
            'dataaviso': '2019-07-25T01:06:37.000-03:00',
            'tipo': 'Intimação',
            'processo': '01890177220174025107',
            'unidade': 'RJ000701S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Itaboraí',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001506',
            'dataaviso': '2019-07-25T01:06:37.000-03:00',
            'tipo': 'Intimação',
            'processo': '01213791120154025101',
            'unidade': 'RJ000054S',
            'unidadenome': 'Juízo Substituto da 4ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001510',
            'dataaviso': '2019-07-25T01:06:37.000-03:00',
            'tipo': 'Intimação',
            'processo': '01085826620164025101',
            'unidade': 'RJ000012F',
            'unidadenome': 'Juízo Federal da 12ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001511',
            'dataaviso': '2019-07-25T01:06:37.000-03:00',
            'tipo': 'Intimação',
            'processo': '00029097920194025101',
            'unidade': 'RJ000029S',
            'unidadenome': 'Juízo Substituto da 29ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001513',
            'dataaviso': '2019-07-25T01:06:37.000-03:00',
            'tipo': 'Intimação',
            'processo': '50025804620194025112',
            'unidade': 'RJ001201F',
            'unidadenome': 'Juízo Federal da 1ª VF de Itaperuna',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001516',
            'dataaviso': '2019-07-25T01:06:37.000-03:00',
            'tipo': 'Intimação',
            'processo': '00991707720174025101',
            'unidade': 'RJ000052F',
            'unidadenome': 'Juízo Federal da 7ª VF de Execução Fiscal do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001520',
            'dataaviso': '2019-07-25T01:06:37.000-03:00',
            'tipo': 'Citação',
            'processo': '50470734420194025101',
            'unidade': 'RJ000017F',
            'unidadenome': 'Juízo Federal da 17ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001523',
            'dataaviso': '2019-07-25T01:06:37.000-03:00',
            'tipo': 'Intimação',
            'processo': '01405828520174025101',
            'unidade': 'RJ000006F',
            'unidadenome': 'Juízo Federal da 6ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001525',
            'dataaviso': '2019-07-25T01:06:37.000-03:00',
            'tipo': 'Intimação',
            'processo': '50002215120184025115',
            'unidade': 'RJ001501S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Teresópolis',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001530',
            'dataaviso': '2019-07-25T01:06:37.000-03:00',
            'tipo': 'Intimação',
            'processo': '50416703120184025101',
            'unidade': 'RJ005103S',
            'unidadenome': 'Juízo Substituto do 3º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001533',
            'dataaviso': '2019-07-25T01:06:37.000-03:00',
            'tipo': 'Intimação',
            'processo': '50130137920184025101',
            'unidade': 'RJ005104F',
            'unidadenome': 'Juízo Federal do 4º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001541',
            'dataaviso': '2019-07-25T01:06:37.000-03:00',
            'tipo': 'Intimação',
            'processo': '01651240420164025102',
            'unidade': 'RJ000103F',
            'unidadenome': 'Juízo Federal da 3ª VF de Niterói',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001542',
            'dataaviso': '2019-07-25T01:06:37.000-03:00',
            'tipo': 'Intimação',
            'processo': '01651240420164025102',
            'unidade': 'RJ000103F',
            'unidadenome': 'Juízo Federal da 3ª VF de Niterói',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001543',
            'dataaviso': '2019-07-25T01:06:37.000-03:00',
            'tipo': 'Intimação',
            'processo': '50002156820184025107',
            'unidade': 'RJ000701S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Itaboraí',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001545',
            'dataaviso': '2019-07-25T01:06:37.000-03:00',
            'tipo': 'Intimação',
            'processo': '01073796920164025101',
            'unidade': 'RJ000054S',
            'unidadenome': 'Juízo Substituto da 4ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001547',
            'dataaviso': '2019-07-25T01:06:37.000-03:00',
            'tipo': 'Intimação',
            'processo': '50003716220184025105',
            'unidade': 'RJ005501F',
            'unidadenome': 'Juízo Federal do 1º JEF de Nova Friburgo',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001548',
            'dataaviso': '2019-07-25T01:06:37.000-03:00',
            'tipo': 'Intimação',
            'processo': '50005222820184025105',
            'unidade': 'RJ005501S',
            'unidadenome': 'Juízo Substituto do 1º JEF de Nova Friburgo',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001550',
            'dataaviso': '2019-07-25T01:06:37.000-03:00',
            'tipo': 'Intimação',
            'processo': '50027042920194025112',
            'unidade': 'RJ001201S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Itaperuna',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001551',
            'dataaviso': '2019-07-25T01:06:37.000-03:00',
            'tipo': 'Intimação',
            'processo': '50016710420194025112',
            'unidade': 'RJ001201S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Itaperuna',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001552',
            'dataaviso': '2019-07-25T01:06:37.000-03:00',
            'tipo': 'Intimação',
            'processo': '50000405920184025112',
            'unidade': 'RJ001201F',
            'unidadenome': 'Juízo Federal da 1ª VF de Itaperuna',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001554',
            'dataaviso': '2019-07-25T01:06:37.000-03:00',
            'tipo': 'Intimação',
            'processo': '50066983520184025101',
            'unidade': 'RJ005108F',
            'unidadenome': 'Juízo Federal do 8º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001570',
            'dataaviso': '2019-07-25T01:06:37.000-03:00',
            'tipo': 'Intimação',
            'processo': '00014868420194025101',
            'unidade': 'RJ000501F',
            'unidadenome': 'Juízo Federal da 1ª VF de Nova Friburgo',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001571',
            'dataaviso': '2019-07-25T01:06:37.000-03:00',
            'tipo': 'Intimação',
            'processo': '01229224920154025101',
            'unidade': 'RJ000023F',
            'unidadenome': 'Juízo Federal da 23ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001572',
            'dataaviso': '2019-07-25T01:06:37.000-03:00',
            'tipo': 'Intimação',
            'processo': '50040944420184025120',
            'unidade': 'RJ002001F',
            'unidadenome': 'Juízo Federal da 1ª VF de Nova Iguaçu',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001573',
            'dataaviso': '2019-07-25T01:06:37.000-03:00',
            'tipo': 'Intimação',
            'processo': '50467573120194025101',
            'unidade': 'RJ000023S',
            'unidadenome': 'Juízo Substituto da 23ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001579',
            'dataaviso': '2019-07-25T01:06:37.000-03:00',
            'tipo': 'Urgente',
            'processo': '50440578220194025101',
            'unidade': 'RJ000003F',
            'unidadenome': 'Juízo Federal da 3ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001582',
            'dataaviso': '2019-07-25T01:06:37.000-03:00',
            'tipo': 'Citação',
            'processo': '50468119420194025101',
            'unidade': 'RJ000037S',
            'unidadenome': 'Juízo Substituto da 13ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001585',
            'dataaviso': '2019-07-25T01:06:37.000-03:00',
            'tipo': 'Intimação',
            'processo': '01026419720174025167',
            'unidade': 'RJ001701S',
            'unidadenome': 'Juízo Substituto do 3º JEF de São Gonçalo',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001586',
            'dataaviso': '2019-07-25T01:06:37.000-03:00',
            'tipo': 'Intimação',
            'processo': '00002215120134025103',
            'unidade': 'RJ000201S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Campos',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001587',
            'dataaviso': '2019-07-25T01:06:37.000-03:00',
            'tipo': 'Intimação',
            'processo': '50025042220194025112',
            'unidade': 'RJ001201F',
            'unidadenome': 'Juízo Federal da 1ª VF de Itaperuna',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001588',
            'dataaviso': '2019-07-25T01:06:37.000-03:00',
            'tipo': 'Intimação',
            'processo': '50027398620194025112',
            'unidade': 'RJ001201F',
            'unidadenome': 'Juízo Federal da 1ª VF de Itaperuna',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001594',
            'dataaviso': '2019-07-25T01:06:37.000-03:00',
            'tipo': 'Intimação',
            'processo': '00037372220124025101',
            'unidade': 'RJ000008S',
            'unidadenome': 'Juízo Substituto da 8ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001596',
            'dataaviso': '2019-07-25T01:06:37.000-03:00',
            'tipo': 'Intimação',
            'processo': '50500089120184025101',
            'unidade': 'RJ000026S',
            'unidadenome': 'Juízo Substituto da 26ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001597',
            'dataaviso': '2019-07-25T01:06:37.000-03:00',
            'tipo': 'Intimação',
            'processo': '50314316520184025101',
            'unidade': 'RJ005103F',
            'unidadenome': 'Juízo Federal do 3º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907250001599',
            'dataaviso': '2019-07-25T01:06:37.000-03:00',
            'tipo': 'Intimação',
            'processo': '50021292120194025112',
            'unidade': 'RJ001201S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Itaperuna',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006387',
            'dataaviso': '2019-07-26T01:07:47.000-03:00',
            'tipo': 'Intimação',
            'processo': '50160086520184025101',
            'unidade': 'RJ000028F',
            'unidadenome': 'Juízo Federal da 28ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006388',
            'dataaviso': '2019-07-26T01:07:47.000-03:00',
            'tipo': 'Intimação',
            'processo': '50158301920184025101',
            'unidade': 'RJ000006S',
            'unidadenome': 'Juízo Substituto da 6ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006392',
            'dataaviso': '2019-07-26T01:07:47.000-03:00',
            'tipo': 'Intimação',
            'processo': '50402465120184025101',
            'unidade': 'RJ000019S',
            'unidadenome': 'Juízo Substituto da 19ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006393',
            'dataaviso': '2019-07-26T01:07:47.000-03:00',
            'tipo': 'Intimação',
            'processo': '50373027620184025101',
            'unidade': 'RJ000002F',
            'unidadenome': 'Juízo Federal da 2ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006395',
            'dataaviso': '2019-07-26T01:07:47.000-03:00',
            'tipo': 'Intimação',
            'processo': '50382926720184025101',
            'unidade': 'RJ000023F',
            'unidadenome': 'Juízo Federal da 23ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006397',
            'dataaviso': '2019-07-26T01:07:47.000-03:00',
            'tipo': 'Intimação',
            'processo': '01870397820174025101',
            'unidade': 'RJ000054S',
            'unidadenome': 'Juízo Substituto da 4ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006398',
            'dataaviso': '2019-07-26T01:07:47.000-03:00',
            'tipo': 'Intimação',
            'processo': '00528157720154025101',
            'unidade': 'RJ000054S',
            'unidadenome': 'Juízo Substituto da 4ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006400',
            'dataaviso': '2019-07-26T01:07:47.000-03:00',
            'tipo': 'Intimação',
            'processo': '00965814920164025101',
            'unidade': 'RJ000029S',
            'unidadenome': 'Juízo Substituto da 29ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006404',
            'dataaviso': '2019-07-26T01:07:47.000-03:00',
            'tipo': 'Intimação',
            'processo': '00840607220164025101',
            'unidade': 'RJ000012F',
            'unidadenome': 'Juízo Federal da 12ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006406',
            'dataaviso': '2019-07-26T01:07:47.000-03:00',
            'tipo': 'Intimação',
            'processo': '05018633220184025101',
            'unidade': 'RJ000029S',
            'unidadenome': 'Juízo Substituto da 29ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006408',
            'dataaviso': '2019-07-26T01:07:47.000-03:00',
            'tipo': 'Intimação',
            'processo': '00813817720184025118',
            'unidade': 'RJ001802S',
            'unidadenome': 'Juízo Substituto da 2ª VF de Duque de Caxias',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006410',
            'dataaviso': '2019-07-26T01:07:47.000-03:00',
            'tipo': 'Intimação',
            'processo': '50309284420184025101',
            'unidade': 'RJ000026S',
            'unidadenome': 'Juízo Substituto da 26ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006412',
            'dataaviso': '2019-07-26T01:07:47.000-03:00',
            'tipo': 'Intimação',
            'processo': '50001219220194025105',
            'unidade': 'RJ000501S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Nova Friburgo',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006414',
            'dataaviso': '2019-07-26T01:07:47.000-03:00',
            'tipo': 'Intimação',
            'processo': '50063375820184025120',
            'unidade': 'RJ002002S',
            'unidadenome': 'Juízo Substituto da 2ª VF de Nova Iguaçu',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006415',
            'dataaviso': '2019-07-26T01:07:47.000-03:00',
            'tipo': 'Intimação',
            'processo': '00900296820164025101',
            'unidade': 'RJ000007S',
            'unidadenome': 'Juízo Substituto da 7ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006417',
            'dataaviso': '2019-07-26T01:07:47.000-03:00',
            'tipo': 'Intimação',
            'processo': '01056504720124025101',
            'unidade': 'RJ000020F',
            'unidadenome': 'Juízo Federal da 20ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006421',
            'dataaviso': '2019-07-26T01:07:47.000-03:00',
            'tipo': 'Intimação',
            'processo': '00008872620114025102',
            'unidade': 'RJ000103S',
            'unidadenome': 'Juízo Substituto da 3ª VF de Niterói',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006422',
            'dataaviso': '2019-07-26T01:07:47.000-03:00',
            'tipo': 'Intimação',
            'processo': '00019436020124025102',
            'unidade': 'RJ000101S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Niterói',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006424',
            'dataaviso': '2019-07-26T01:07:47.000-03:00',
            'tipo': 'Intimação',
            'processo': '50445899020184025101',
            'unidade': 'RJ000037S',
            'unidadenome': 'Juízo Substituto da 13ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006426',
            'dataaviso': '2019-07-26T01:07:47.000-03:00',
            'tipo': 'Intimação',
            'processo': '50226034620194025101',
            'unidade': 'RJ000029S',
            'unidadenome': 'Juízo Substituto da 29ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006428',
            'dataaviso': '2019-07-26T01:07:47.000-03:00',
            'tipo': 'Intimação',
            'processo': '00457742520164025101',
            'unidade': 'RJ000026F',
            'unidadenome': 'Juízo Federal da 26ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006430',
            'dataaviso': '2019-07-26T01:07:47.000-03:00',
            'tipo': 'Intimação',
            'processo': '00013592220194025110',
            'unidade': 'RJ001006S',
            'unidadenome': 'Juízo Substituto da 6ª VF de São João de Meriti',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006433',
            'dataaviso': '2019-07-26T01:07:48.000-03:00',
            'tipo': 'Intimação',
            'processo': '50450887420184025101',
            'unidade': 'RJ000008F',
            'unidadenome': 'Juízo Federal da 8ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006434',
            'dataaviso': '2019-07-26T01:07:48.000-03:00',
            'tipo': 'Intimação',
            'processo': '50335081320194025101',
            'unidade': 'RJ000008S',
            'unidadenome': 'Juízo Substituto da 8ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006450',
            'dataaviso': '2019-07-26T01:07:48.000-03:00',
            'tipo': 'Intimação',
            'processo': '50398807520194025101',
            'unidade': 'RJ000015S',
            'unidadenome': 'Juízo Substituto da 15ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006452',
            'dataaviso': '2019-07-26T01:07:48.000-03:00',
            'tipo': 'Intimação',
            'processo': '00766593920184025105',
            'unidade': 'RJ000501S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Nova Friburgo',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006453',
            'dataaviso': '2019-07-26T01:07:48.000-03:00',
            'tipo': 'Intimação',
            'processo': '01075580320164025101',
            'unidade': 'RJ000027F',
            'unidadenome': 'Juízo Federal da 27ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006454',
            'dataaviso': '2019-07-26T01:07:48.000-03:00',
            'tipo': 'Intimação',
            'processo': '00125422220164025101',
            'unidade': 'RJ000054F',
            'unidadenome': 'Juízo Federal da 4ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006668',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Citação',
            'processo': '00021978920194025101',
            'unidade': 'RJ000008S',
            'unidadenome': 'Juízo Substituto da 8ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006676',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50008206920184025121',
            'unidade': 'RJ005113S',
            'unidadenome': 'Juízo Substituto do 13º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006677',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Citação',
            'processo': '50479447420194025101',
            'unidade': 'RJ000008F',
            'unidadenome': 'Juízo Federal da 8ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006678',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50229025720184025101',
            'unidade': 'RJ005101F',
            'unidadenome': 'Juízo Federal do 1º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006679',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50177303720184025101',
            'unidade': 'RJ005101F',
            'unidadenome': 'Juízo Federal do 1º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006680',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50273885120194025101',
            'unidade': 'RJ005101S',
            'unidadenome': 'Juízo Substituto do 1º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006681',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50009606620184025101',
            'unidade': 'RJ005101F',
            'unidadenome': 'Juízo Federal do 1º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006682',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Citação',
            'processo': '02314980920174025153',
            'unidade': 'RJ000201F',
            'unidadenome': 'Juízo Federal da 1ª VF de Campos',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006683',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50008289420184025105',
            'unidade': 'RJ005501F',
            'unidadenome': 'Juízo Federal do 1º JEF de Nova Friburgo',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006684',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '00583413720184025160',
            'unidade': 'RJ006001S',
            'unidadenome': 'Juízo Substituto da 7ª VF de São João de Meriti',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006685',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '00742415420184025162',
            'unidade': 'RJ001201S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Itaperuna',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006686',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '00332478120184025162',
            'unidade': 'RJ001201S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Itaperuna',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006687',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50348210920194025101',
            'unidade': 'RJ000012F',
            'unidadenome': 'Juízo Federal da 12ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006688',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Citação',
            'processo': '50348210920194025101',
            'unidade': 'RJ000012F',
            'unidadenome': 'Juízo Federal da 12ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006689',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50038288720184025110',
            'unidade': 'RJ006002F',
            'unidadenome': 'Juízo Federal da 8ª VF de São João de Meriti',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006690',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50422710320194025101',
            'unidade': 'RJ000020S',
            'unidadenome': 'Juízo Substituto da 20ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006691',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50041538920184025101',
            'unidade': 'RJ005114F',
            'unidadenome': 'Juízo Federal do 14º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006692',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '02116296320174025152',
            'unidade': 'RJ005202S',
            'unidadenome': 'Juízo Substituto do 2º JEF de Niterói',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006693',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '00246774220184025151',
            'unidade': 'RJ005115S',
            'unidadenome': 'Juízo Substituto do 15º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006694',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '01975044220174025168',
            'unidade': 'RJ006802F',
            'unidadenome': 'Juízo Federal da 4ª VF de Duque de Caxias',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006695',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50007600820184025118',
            'unidade': 'RJ006802F',
            'unidadenome': 'Juízo Federal da 4ª VF de Duque de Caxias',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006697',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50035298620184025118',
            'unidade': 'RJ006802F',
            'unidadenome': 'Juízo Federal da 4ª VF de Duque de Caxias',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006698',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '00256200820184025168',
            'unidade': 'RJ006802F',
            'unidadenome': 'Juízo Federal da 4ª VF de Duque de Caxias',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006699',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50023173020184025118',
            'unidade': 'RJ006802S',
            'unidadenome': 'Juízo Substituto da 4ª VF de Duque de Caxias',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006700',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '00362434120184025101',
            'unidade': 'RJ006802S',
            'unidadenome': 'Juízo Substituto da 4ª VF de Duque de Caxias',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006702',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50008392620184025105',
            'unidade': 'RJ005501F',
            'unidadenome': 'Juízo Federal do 1º JEF de Nova Friburgo',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006703',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '02144725320174025167',
            'unidade': 'RJ006701F',
            'unidadenome': 'Juízo Federal do 1º JEF de São Gonçalo',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006704',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50018431020184025102',
            'unidade': 'RJ005202F',
            'unidadenome': 'Juízo Federal do 2º JEF de Niterói',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006705',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50492983720194025101',
            'unidade': 'RJ000019S',
            'unidadenome': 'Juízo Substituto da 19ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006706',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Citação',
            'processo': '50387877720194025101',
            'unidade': 'RJ000037F',
            'unidadenome': 'Juízo Federal da 13ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006707',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50342706320184025101',
            'unidade': 'RJ005109S',
            'unidadenome': 'Juízo Substituto do 9º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006709',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50062401820184025101',
            'unidade': 'RJ005109S',
            'unidadenome': 'Juízo Substituto do 9º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006710',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Citação',
            'processo': '50013863320184025116',
            'unidade': 'RJ001601F',
            'unidadenome': 'Juízo Federal da 1ª VF de Macaé',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006711',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50004226720184025107',
            'unidade': 'RJ000701F',
            'unidadenome': 'Juízo Federal da 1ª VF de Itaboraí',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006712',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50170666920194025101',
            'unidade': 'RJ000008S',
            'unidadenome': 'Juízo Substituto da 8ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006714',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '01942631420174025151',
            'unidade': 'RJ005107S',
            'unidadenome': 'Juízo Substituto do 7º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006715',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50227414720184025101',
            'unidade': 'RJ005107F',
            'unidadenome': 'Juízo Federal do 7º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006716',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50360512320184025101',
            'unidade': 'RJ005107F',
            'unidadenome': 'Juízo Federal do 7º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006717',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '00059019120184025151',
            'unidade': 'RJ005107S',
            'unidadenome': 'Juízo Substituto do 7º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006718',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '00214495920184025151',
            'unidade': 'RJ005107S',
            'unidadenome': 'Juízo Substituto do 7º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006719',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50196383220184025101',
            'unidade': 'RJ005107S',
            'unidadenome': 'Juízo Substituto do 7º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006721',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50004603720184025121',
            'unidade': 'RJ005114F',
            'unidadenome': 'Juízo Federal do 14º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006723',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '00171451720184025151',
            'unidade': 'RJ005115S',
            'unidadenome': 'Juízo Substituto do 15º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006724',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '00210442320184025151',
            'unidade': 'RJ005115F',
            'unidadenome': 'Juízo Federal do 15º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006725',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Urgente',
            'processo': '50072783120194025101',
            'unidade': 'RJ005110S',
            'unidadenome': 'Juízo Substituto do 10º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006726',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50264031920184025101',
            'unidade': 'RJ000019F',
            'unidadenome': 'Juízo Federal da 19ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006727',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50239658320194025101',
            'unidade': 'RJ000020S',
            'unidadenome': 'Juízo Substituto da 20ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006728',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50012442620184025117',
            'unidade': 'RJ001701F',
            'unidadenome': 'Juízo Federal do 3º JEF de São Gonçalo',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006731',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50475125520194025101',
            'unidade': 'RJ000019F',
            'unidadenome': 'Juízo Federal da 19ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006732',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50293565320184025101',
            'unidade': 'RJ005106F',
            'unidadenome': 'Juízo Federal do 6º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006734',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50000303920184025104',
            'unidade': 'RJ005401F',
            'unidadenome': 'Juízo Federal do 1º JEF de Volta Redonda',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006735',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50002577520184025121',
            'unidade': 'RJ005116S',
            'unidadenome': 'Juízo Substituto do 16º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006738',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '00509520420184025159',
            'unidade': 'RJ005901F',
            'unidadenome': 'Juízo Federal do 1º JEF de Resende',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006739',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50019717320184025120',
            'unidade': 'RJ007001S',
            'unidadenome': 'Juízo Substituto da 3ª VF de Nova Iguaçu',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006740',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50019717020184025121',
            'unidade': 'RJ005113S',
            'unidadenome': 'Juízo Substituto do 13º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006741',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '02302069220174025151',
            'unidade': 'RJ005113F',
            'unidadenome': 'Juízo Federal do 13º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006742',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50027831520184025121',
            'unidade': 'RJ005113S',
            'unidadenome': 'Juízo Substituto do 13º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006743',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50013402920184025121',
            'unidade': 'RJ005113F',
            'unidadenome': 'Juízo Federal do 13º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006744',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50040935620184025121',
            'unidade': 'RJ005113S',
            'unidadenome': 'Juízo Substituto do 13º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006745',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '05029953220154025101',
            'unidade': 'RJ000019S',
            'unidadenome': 'Juízo Substituto da 19ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006746',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50217581420194025101',
            'unidade': 'RJ000019S',
            'unidadenome': 'Juízo Substituto da 19ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006747',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '00837864120154025167',
            'unidade': 'RJ001701F',
            'unidadenome': 'Juízo Federal do 3º JEF de São Gonçalo',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006748',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '00817950820184025108',
            'unidade': 'RJ000801S',
            'unidadenome': 'Juízo Substituto da 1ª VF de São Pedro da Aldeia',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006749',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '00872549320154025108',
            'unidade': 'RJ001601F',
            'unidadenome': 'Juízo Federal da 1ª VF de Macaé',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006750',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50119338020184025101',
            'unidade': 'RJ005111F',
            'unidadenome': 'Juízo Federal do 11º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006751',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50089726920184025101',
            'unidade': 'RJ005111S',
            'unidadenome': 'Juízo Substituto do 11º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006752',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50001047820184025109',
            'unidade': 'RJ005901S',
            'unidadenome': 'Juízo Substituto do 1º JEF de Resende',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006753',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50001454520184025109',
            'unidade': 'RJ005901S',
            'unidadenome': 'Juízo Substituto do 1º JEF de Resende',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006754',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50441366120194025101',
            'unidade': 'RJ000023F',
            'unidadenome': 'Juízo Federal da 23ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006757',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '00216363420184025162',
            'unidade': 'RJ001201F',
            'unidadenome': 'Juízo Federal da 1ª VF de Itaperuna',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006758',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50023093720194025112',
            'unidade': 'RJ001201F',
            'unidadenome': 'Juízo Federal da 1ª VF de Itaperuna',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006759',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '00169477720184025151',
            'unidade': 'RJ005115S',
            'unidadenome': 'Juízo Substituto do 15º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006760',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50019543420184025121',
            'unidade': 'RJ005115F',
            'unidadenome': 'Juízo Federal do 15º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006761',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50052575620184025121',
            'unidade': 'RJ005115S',
            'unidadenome': 'Juízo Substituto do 15º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006762',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '00048158520184025151',
            'unidade': 'RJ005114S',
            'unidadenome': 'Juízo Substituto do 14º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006763',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '01866434820174025151',
            'unidade': 'RJ005114S',
            'unidadenome': 'Juízo Substituto do 14º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006764',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '02003014220174025151',
            'unidade': 'RJ005114S',
            'unidadenome': 'Juízo Substituto do 14º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006765',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '00544188320184025101',
            'unidade': 'RJ000038F',
            'unidadenome': 'Juízo Federal da 31ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006766',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '00220383520174025103',
            'unidade': 'RJ005302F',
            'unidadenome': 'Juízo Federal da 4ª VF de Campos',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006767',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50040126420184025103',
            'unidade': 'RJ005302S',
            'unidadenome': 'Juízo Substituto da 4ª VF de Campos',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006770',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50364582920184025101',
            'unidade': 'RJ005103F',
            'unidadenome': 'Juízo Federal do 3º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006771',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '01110389620174025151',
            'unidade': 'RJ005113F',
            'unidadenome': 'Juízo Federal do 13º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006772',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '01954391920174025154',
            'unidade': 'RJ005402S',
            'unidadenome': 'Juízo Substituto do 2º JEF de Volta Redonda',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006773',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50215749220184025101',
            'unidade': 'RJ005105F',
            'unidadenome': 'Juízo Federal do 5º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006774',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50040885420194025103',
            'unidade': 'RJ000201F',
            'unidadenome': 'Juízo Federal da 1ª VF de Campos',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006775',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '01653342420174025101',
            'unidade': 'RJ000039F',
            'unidadenome': 'Juízo Federal da 9ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006776',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50004895020184025101',
            'unidade': 'RJ005109S',
            'unidadenome': 'Juízo Substituto do 9º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006777',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50013243820184025101',
            'unidade': 'RJ005109F',
            'unidadenome': 'Juízo Federal do 9º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006778',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '50067148620184025101',
            'unidade': 'RJ005109S',
            'unidadenome': 'Juízo Substituto do 9º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006779',
            'dataaviso': '2019-07-26T01:07:49.000-03:00',
            'tipo': 'Intimação',
            'processo': '05000977320184025155',
            'unidade': 'RJ005501S',
            'unidadenome': 'Juízo Substituto do 1º JEF de Nova Friburgo',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006780',
            'dataaviso': '2019-07-26T01:07:50.000-03:00',
            'tipo': 'Citação',
            'processo': '50018812520184025101',
            'unidade': 'RJ005109S',
            'unidadenome': 'Juízo Substituto do 9º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006782',
            'dataaviso': '2019-07-26T01:07:50.000-03:00',
            'tipo': 'Intimação',
            'processo': '00641003320164025101',
            'unidade': 'RJ000012F',
            'unidadenome': 'Juízo Federal da 12ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006783',
            'dataaviso': '2019-07-26T01:07:50.000-03:00',
            'tipo': 'Intimação',
            'processo': '00631841520184025170',
            'unidade': 'RJ007003F',
            'unidadenome': 'Juízo Federal da 5ª VF de Nova Iguaçu',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006784',
            'dataaviso': '2019-07-26T01:07:50.000-03:00',
            'tipo': 'Intimação',
            'processo': '50005349420184025120',
            'unidade': 'RJ007003F',
            'unidadenome': 'Juízo Federal da 5ª VF de Nova Iguaçu',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907260006785',
            'dataaviso': '2019-07-26T01:07:50.000-03:00',
            'tipo': 'Intimação',
            'processo': '50036094420184025120',
            'unidade': 'RJ007003F',
            'unidadenome': 'Juízo Federal da 5ª VF de Nova Iguaçu',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001264',
            'dataaviso': '2019-07-27T01:09:29.000-03:00',
            'tipo': 'Intimação',
            'processo': '50359403920184025101',
            'unidade': 'RJ000012S',
            'unidadenome': 'Juízo Substituto da 12ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001265',
            'dataaviso': '2019-07-27T01:09:29.000-03:00',
            'tipo': 'Intimação',
            'processo': '50474963820184025101',
            'unidade': 'RJ000023S',
            'unidadenome': 'Juízo Substituto da 23ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001266',
            'dataaviso': '2019-07-27T01:09:29.000-03:00',
            'tipo': 'Intimação',
            'processo': '50031595820184025102',
            'unidade': 'RJ000103F',
            'unidadenome': 'Juízo Federal da 3ª VF de Niterói',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001267',
            'dataaviso': '2019-07-27T01:09:29.000-03:00',
            'tipo': 'Intimação',
            'processo': '50376777720184025101',
            'unidade': 'RJ000037S',
            'unidadenome': 'Juízo Substituto da 13ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001268',
            'dataaviso': '2019-07-27T01:09:29.000-03:00',
            'tipo': 'Intimação',
            'processo': '50039673220194025101',
            'unidade': 'RJ000021F',
            'unidadenome': 'Juízo Federal da 21ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001269',
            'dataaviso': '2019-07-27T01:09:29.000-03:00',
            'tipo': 'Intimação',
            'processo': '50499534320184025101',
            'unidade': 'RJ000051F',
            'unidadenome': 'Juízo Federal da 6ª VF de Execução Fiscal do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001270',
            'dataaviso': '2019-07-27T01:09:29.000-03:00',
            'tipo': 'Intimação',
            'processo': '00606172920154025101',
            'unidade': 'RJ000015S',
            'unidadenome': 'Juízo Substituto da 15ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001271',
            'dataaviso': '2019-07-27T01:09:29.000-03:00',
            'tipo': 'Intimação',
            'processo': '50489107120184025101',
            'unidade': 'RJ000026S',
            'unidadenome': 'Juízo Substituto da 26ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001272',
            'dataaviso': '2019-07-27T01:09:29.000-03:00',
            'tipo': 'Intimação',
            'processo': '50064312920194025101',
            'unidade': 'RJ000048S',
            'unidadenome': 'Juízo Substituto da 3ª VF de Execução Fiscal do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001273',
            'dataaviso': '2019-07-27T01:09:29.000-03:00',
            'tipo': 'Intimação',
            'processo': '01153691420164025101',
            'unidade': 'RJ000007S',
            'unidadenome': 'Juízo Substituto da 7ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001275',
            'dataaviso': '2019-07-27T01:09:29.000-03:00',
            'tipo': 'Intimação',
            'processo': '01822859320174025101',
            'unidade': 'RJ000008S',
            'unidadenome': 'Juízo Substituto da 8ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001276',
            'dataaviso': '2019-07-27T01:09:29.000-03:00',
            'tipo': 'Intimação',
            'processo': '00129383320154025101',
            'unidade': 'RJ000001F',
            'unidadenome': 'Juízo Federal da 1ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001277',
            'dataaviso': '2019-07-27T01:09:29.000-03:00',
            'tipo': 'Intimação',
            'processo': '00004845920134025111',
            'unidade': 'RJ001101F',
            'unidadenome': 'Juízo Federal da 1ª VF de Angra dos Reis',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001279',
            'dataaviso': '2019-07-27T01:09:29.000-03:00',
            'tipo': 'Intimação',
            'processo': '00153472120114025101',
            'unidade': 'RJ000007S',
            'unidadenome': 'Juízo Substituto da 7ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001280',
            'dataaviso': '2019-07-27T01:09:29.000-03:00',
            'tipo': 'Intimação',
            'processo': '50027288720194025102',
            'unidade': 'RJ000104F',
            'unidadenome': 'Juízo Federal da 4ª VF de Niterói',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001282',
            'dataaviso': '2019-07-27T01:09:29.000-03:00',
            'tipo': 'Intimação',
            'processo': '01590807620174025152',
            'unidade': 'RJ000103F',
            'unidadenome': 'Juízo Federal da 3ª VF de Niterói',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001284',
            'dataaviso': '2019-07-27T01:09:29.000-03:00',
            'tipo': 'Intimação',
            'processo': '50268645420194025101',
            'unidade': 'RJ000001F',
            'unidadenome': 'Juízo Federal da 1ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001287',
            'dataaviso': '2019-07-27T01:09:29.000-03:00',
            'tipo': 'Intimação',
            'processo': '01592691320174025101',
            'unidade': 'RJ000017S',
            'unidadenome': 'Juízo Substituto da 17ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001288',
            'dataaviso': '2019-07-27T01:09:29.000-03:00',
            'tipo': 'Intimação',
            'processo': '00838086920164025101',
            'unidade': 'RJ000054F',
            'unidadenome': 'Juízo Federal da 4ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001289',
            'dataaviso': '2019-07-27T01:09:29.000-03:00',
            'tipo': 'Intimação',
            'processo': '50347267620194025101',
            'unidade': 'RJ000021F',
            'unidadenome': 'Juízo Federal da 21ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001290',
            'dataaviso': '2019-07-27T01:09:29.000-03:00',
            'tipo': 'Intimação',
            'processo': '50310445020184025101',
            'unidade': 'RJ000039S',
            'unidadenome': 'Juízo Substituto da 9ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001291',
            'dataaviso': '2019-07-27T01:09:29.000-03:00',
            'tipo': 'Intimação',
            'processo': '00016157720194025105',
            'unidade': 'RJ000501S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Nova Friburgo',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001292',
            'dataaviso': '2019-07-27T01:09:29.000-03:00',
            'tipo': 'Intimação',
            'processo': '50108572120184025101',
            'unidade': 'RJ000039S',
            'unidadenome': 'Juízo Substituto da 9ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001293',
            'dataaviso': '2019-07-27T01:09:29.000-03:00',
            'tipo': 'Intimação',
            'processo': '00787265720164025101',
            'unidade': 'RJ000054F',
            'unidadenome': 'Juízo Federal da 4ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001294',
            'dataaviso': '2019-07-27T01:09:29.000-03:00',
            'tipo': 'Intimação',
            'processo': '01547211320154025101',
            'unidade': 'RJ000055S',
            'unidadenome': 'Juízo Substituto da 32ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001295',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '05297013320074025101',
            'unidade': 'RJ000059S',
            'unidadenome': 'Juízo Substituto da 11ª VF de Execução Fiscal do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001296',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Citação',
            'processo': '50445505920194025101',
            'unidade': 'RJ000015F',
            'unidadenome': 'Juízo Federal da 15ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001330',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50107082520184025101',
            'unidade': 'RJ000023F',
            'unidadenome': 'Juízo Federal da 23ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001331',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '00030361720194025101',
            'unidade': 'RJ000017F',
            'unidadenome': 'Juízo Federal da 17ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001332',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '01339391420174025101',
            'unidade': 'RJ000003S',
            'unidadenome': 'Juízo Substituto da 3ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001333',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50012740320184025104',
            'unidade': 'RJ005401S',
            'unidadenome': 'Juízo Substituto do 1º JEF de Volta Redonda',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001334',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50021531020184025104',
            'unidade': 'RJ005401F',
            'unidadenome': 'Juízo Federal do 1º JEF de Volta Redonda',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001335',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50043503520184025104',
            'unidade': 'RJ005401F',
            'unidadenome': 'Juízo Federal do 1º JEF de Volta Redonda',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001336',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50008116120184025104',
            'unidade': 'RJ005401F',
            'unidadenome': 'Juízo Federal do 1º JEF de Volta Redonda',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001337',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50007898520184025109',
            'unidade': 'RJ005901F',
            'unidadenome': 'Juízo Federal do 1º JEF de Resende',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001338',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50374681120184025101',
            'unidade': 'RJ005107F',
            'unidadenome': 'Juízo Federal do 7º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001339',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50037355420184025101',
            'unidade': 'RJ005107S',
            'unidadenome': 'Juízo Substituto do 7º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001340',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '01171663520174025151',
            'unidade': 'RJ005107F',
            'unidadenome': 'Juízo Federal do 7º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001341',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50076086220184025101',
            'unidade': 'RJ005107S',
            'unidadenome': 'Juízo Substituto do 7º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001342',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50184110720184025101',
            'unidade': 'RJ005107S',
            'unidadenome': 'Juízo Substituto do 7º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001343',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '05012271320184025151',
            'unidade': 'RJ005107S',
            'unidadenome': 'Juízo Substituto do 7º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001344',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50058548520184025101',
            'unidade': 'RJ005107S',
            'unidadenome': 'Juízo Substituto do 7º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001345',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50033466920184025101',
            'unidade': 'RJ005107F',
            'unidadenome': 'Juízo Federal do 7º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001346',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50119736220184025101',
            'unidade': 'RJ005107S',
            'unidadenome': 'Juízo Substituto do 7º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001347',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50385602420184025101',
            'unidade': 'RJ005107F',
            'unidadenome': 'Juízo Federal do 7º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001348',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50220511820184025101',
            'unidade': 'RJ005107F',
            'unidadenome': 'Juízo Federal do 7º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001349',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50049861020184025101',
            'unidade': 'RJ005107F',
            'unidadenome': 'Juízo Federal do 7º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001350',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50243039120184025101',
            'unidade': 'RJ005107S',
            'unidadenome': 'Juízo Substituto do 7º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001351',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50071582220184025101',
            'unidade': 'RJ005107F',
            'unidadenome': 'Juízo Federal do 7º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001352',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50395700620184025101',
            'unidade': 'RJ005107S',
            'unidadenome': 'Juízo Substituto do 7º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001353',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '00121254520184025151',
            'unidade': 'RJ005107S',
            'unidadenome': 'Juízo Substituto do 7º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001354',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '00337725720154025101',
            'unidade': 'RJ000015F',
            'unidadenome': 'Juízo Federal da 15ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001355',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '00446582320124025101',
            'unidade': 'RJ000015S',
            'unidadenome': 'Juízo Substituto da 15ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001356',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50001956520184025111',
            'unidade': 'RJ001101S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Angra dos Reis',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001357',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50001134920184025106',
            'unidade': 'RJ000602S',
            'unidadenome': 'Juízo Substituto da 2ª VF de Petrópolis',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001358',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '05000835020184025168',
            'unidade': 'RJ006801S',
            'unidadenome': 'Juízo Substituto da 3ª VF de Duque de Caxias',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001359',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50008134020184025101',
            'unidade': 'RJ005106S',
            'unidadenome': 'Juízo Substituto do 6º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001360',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '00723845920184025101',
            'unidade': 'RJ005101F',
            'unidadenome': 'Juízo Federal do 1º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001361',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '00534708520184025152',
            'unidade': 'RJ005202F',
            'unidadenome': 'Juízo Federal do 2º JEF de Niterói',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001362',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '00534708520184025152',
            'unidade': 'RJ005202F',
            'unidadenome': 'Juízo Federal do 2º JEF de Niterói',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001363',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50054312820184025101',
            'unidade': 'RJ005109F',
            'unidadenome': 'Juízo Federal do 9º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001364',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Citação',
            'processo': '50066698220184025101',
            'unidade': 'RJ005109F',
            'unidadenome': 'Juízo Federal do 9º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001366',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50224874020194025101',
            'unidade': 'RJ000012S',
            'unidadenome': 'Juízo Substituto da 12ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001367',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '00832633320154025101',
            'unidade': 'RJ000016S',
            'unidadenome': 'Juízo Substituto da 16ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001368',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Citação',
            'processo': '50492611020194025101',
            'unidade': 'RJ000010S',
            'unidadenome': 'Juízo Substituto da 10ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001370',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Citação',
            'processo': '50039770420184025104',
            'unidade': 'RJ005401F',
            'unidadenome': 'Juízo Federal do 1º JEF de Volta Redonda',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001371',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50040958620184025101',
            'unidade': 'RJ005109F',
            'unidadenome': 'Juízo Federal do 9º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001372',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50089501120184025101',
            'unidade': 'RJ005109F',
            'unidadenome': 'Juízo Federal do 9º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001373',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50213428020184025101',
            'unidade': 'RJ005109F',
            'unidadenome': 'Juízo Federal do 9º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001374',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50226496920184025101',
            'unidade': 'RJ005109F',
            'unidadenome': 'Juízo Federal do 9º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001375',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50301134720184025101',
            'unidade': 'RJ005109F',
            'unidadenome': 'Juízo Federal do 9º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001376',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50351237220184025101',
            'unidade': 'RJ005109F',
            'unidadenome': 'Juízo Federal do 9º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001378',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50032415820194025101',
            'unidade': 'RJ005107F',
            'unidadenome': 'Juízo Federal do 7º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001379',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50090696920184025101',
            'unidade': 'RJ005107S',
            'unidadenome': 'Juízo Substituto do 7º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001382',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Citação',
            'processo': '50444319820194025101',
            'unidade': 'RJ000030F',
            'unidadenome': 'Juízo Federal da 30ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001383',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50014643520194025102',
            'unidade': 'RJ005201F',
            'unidadenome': 'Juízo Federal do 1º JEF de Niterói',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001385',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '00347965620184025153',
            'unidade': 'RJ005302F',
            'unidadenome': 'Juízo Federal da 4ª VF de Campos',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001386',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50036169320184025101',
            'unidade': 'RJ005101S',
            'unidadenome': 'Juízo Substituto do 1º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001387',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '00120560820144025101',
            'unidade': 'RJ000019F',
            'unidadenome': 'Juízo Federal da 19ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001388',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '00628663820184025168',
            'unidade': 'RJ006801F',
            'unidadenome': 'Juízo Federal da 3ª VF de Duque de Caxias',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001389',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '01173440820154025101',
            'unidade': 'RJ000028F',
            'unidadenome': 'Juízo Federal da 28ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001391',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Citação',
            'processo': '00594454720184025101',
            'unidade': 'RJ000039S',
            'unidadenome': 'Juízo Substituto da 9ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001392',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '00085058820124025101',
            'unidade': 'RJ000005S',
            'unidadenome': 'Juízo Substituto da 5ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001395',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50039493920184025103',
            'unidade': 'RJ005302S',
            'unidadenome': 'Juízo Substituto da 4ª VF de Campos',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001396',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50007124920184025118',
            'unidade': 'RJ006802F',
            'unidadenome': 'Juízo Federal da 4ª VF de Duque de Caxias',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001397',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '01114325920174025101',
            'unidade': 'RJ000054F',
            'unidadenome': 'Juízo Federal da 4ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001398',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50046982220194025103',
            'unidade': 'RJ000201F',
            'unidadenome': 'Juízo Federal da 1ª VF de Campos',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001400',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Citação',
            'processo': '50035499420194025101',
            'unidade': 'RJ000035F',
            'unidadenome': 'Juízo Federal da 25ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001401',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Citação',
            'processo': '01472429520174025101',
            'unidade': 'RJ000016F',
            'unidadenome': 'Juízo Federal da 16ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001402',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '00537579720184025168',
            'unidade': 'RJ006801S',
            'unidadenome': 'Juízo Substituto da 3ª VF de Duque de Caxias',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001403',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50357905820184025101',
            'unidade': 'RJ005103S',
            'unidadenome': 'Juízo Substituto do 3º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001404',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50496586920194025101',
            'unidade': 'RJ000055S',
            'unidadenome': 'Juízo Substituto da 32ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001405',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50498838920194025101',
            'unidade': 'RJ000027F',
            'unidadenome': 'Juízo Federal da 27ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001406',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Citação',
            'processo': '50405675220194025101',
            'unidade': 'RJ000021F',
            'unidadenome': 'Juízo Federal da 21ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001407',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '00469013720124025101',
            'unidade': 'RJ000028S',
            'unidadenome': 'Juízo Substituto da 28ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907270001408',
            'dataaviso': '2019-07-27T01:09:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50480096920194025101',
            'unidade': 'RJ000037S',
            'unidadenome': 'Juízo Substituto da 13ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907280000806',
            'dataaviso': '2019-07-28T01:05:22.000-03:00',
            'tipo': 'Intimação',
            'processo': '01434391220144025101',
            'unidade': 'RJ000007S',
            'unidadenome': 'Juízo Substituto da 7ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907280000927',
            'dataaviso': '2019-07-28T01:05:23.000-03:00',
            'tipo': 'Intimação',
            'processo': '01535496120174025167',
            'unidade': 'RJ009133',
            'unidadenome': '3ª Turma Recursal - 3º Juiz Relator',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907280000937',
            'dataaviso': '2019-07-28T01:05:24.000-03:00',
            'tipo': 'Intimação',
            'processo': '00540605920184025153',
            'unidade': 'RJ005301F',
            'unidadenome': 'Juízo Federal da 3ª VF de Campos',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907280000938',
            'dataaviso': '2019-07-28T01:05:24.000-03:00',
            'tipo': 'Intimação',
            'processo': '00490312820184025153',
            'unidade': 'RJ005301S',
            'unidadenome': 'Juízo Substituto da 3ª VF de Campos',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907280000939',
            'dataaviso': '2019-07-28T01:05:24.000-03:00',
            'tipo': 'Intimação',
            'processo': '01799475620164025110',
            'unidade': 'RJ001006S',
            'unidadenome': 'Juízo Substituto da 6ª VF de São João de Meriti',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907290000541',
            'dataaviso': '2019-07-29T01:04:00.000-03:00',
            'tipo': 'Intimação',
            'processo': '50008889120194025118',
            'unidade': 'RJ001801F',
            'unidadenome': 'Juízo Federal da 1ª VF de Duque de Caxias',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907290000551',
            'dataaviso': '2019-07-29T01:04:00.000-03:00',
            'tipo': 'Intimação',
            'processo': '00315181420154025101',
            'unidade': 'RJ000007F',
            'unidadenome': 'Juízo Federal da 7ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907290000607',
            'dataaviso': '2019-07-29T01:04:01.000-03:00',
            'tipo': 'Intimação',
            'processo': '00069965320184025153',
            'unidade': 'RJ005301F',
            'unidadenome': 'Juízo Federal da 3ª VF de Campos',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907290000608',
            'dataaviso': '2019-07-29T01:04:01.000-03:00',
            'tipo': 'Intimação',
            'processo': '00603924220184025153',
            'unidade': 'RJ005301F',
            'unidadenome': 'Juízo Federal da 3ª VF de Campos',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907290000611',
            'dataaviso': '2019-07-29T01:04:01.000-03:00',
            'tipo': 'Intimação',
            'processo': '50006469320184025110',
            'unidade': 'RJ006001F',
            'unidadenome': 'Juízo Federal da 7ª VF de São João de Meriti',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907290000612',
            'dataaviso': '2019-07-29T01:04:01.000-03:00',
            'tipo': 'Intimação',
            'processo': '50021296120184025110',
            'unidade': 'RJ006001S',
            'unidadenome': 'Juízo Substituto da 7ª VF de São João de Meriti',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907290000613',
            'dataaviso': '2019-07-29T01:04:01.000-03:00',
            'tipo': 'Intimação',
            'processo': '01022225420174025110',
            'unidade': 'RJ006001F',
            'unidadenome': 'Juízo Federal da 7ª VF de São João de Meriti',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907290000614',
            'dataaviso': '2019-07-29T01:04:01.000-03:00',
            'tipo': 'Intimação',
            'processo': '01022225420174025110',
            'unidade': 'RJ006001F',
            'unidadenome': 'Juízo Federal da 7ª VF de São João de Meriti',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907290000615',
            'dataaviso': '2019-07-29T01:04:01.000-03:00',
            'tipo': 'Intimação',
            'processo': '50043736020184025110',
            'unidade': 'RJ006001F',
            'unidadenome': 'Juízo Federal da 7ª VF de São João de Meriti',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907290000616',
            'dataaviso': '2019-07-29T01:04:01.000-03:00',
            'tipo': 'Intimação',
            'processo': '00691846120184025160',
            'unidade': 'RJ006001F',
            'unidadenome': 'Juízo Federal da 7ª VF de São João de Meriti',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907290000617',
            'dataaviso': '2019-07-29T01:04:01.000-03:00',
            'tipo': 'Intimação',
            'processo': '50226026120194025101',
            'unidade': 'RJ000020S',
            'unidadenome': 'Juízo Substituto da 20ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907290000618',
            'dataaviso': '2019-07-29T01:04:01.000-03:00',
            'tipo': 'Intimação',
            'processo': '00235278420154025101',
            'unidade': 'RJ000021S',
            'unidadenome': 'Juízo Substituto da 21ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907290000619',
            'dataaviso': '2019-07-29T01:04:01.000-03:00',
            'tipo': 'Intimação',
            'processo': '00875267420164025101',
            'unidade': 'RJ000021F',
            'unidadenome': 'Juízo Federal da 21ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907290000620',
            'dataaviso': '2019-07-29T01:04:01.000-03:00',
            'tipo': 'Intimação',
            'processo': '01535578020174025153',
            'unidade': 'RJ005301S',
            'unidadenome': 'Juízo Substituto da 3ª VF de Campos',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001840',
            'dataaviso': '2019-07-30T01:09:04.000-03:00',
            'tipo': 'Intimação',
            'processo': '50372654920184025101',
            'unidade': 'RJ000017F',
            'unidadenome': 'Juízo Federal da 17ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001842',
            'dataaviso': '2019-07-30T01:09:04.000-03:00',
            'tipo': 'Intimação',
            'processo': '50399789420184025101',
            'unidade': 'RJ000011F',
            'unidadenome': 'Juízo Federal da 11ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001844',
            'dataaviso': '2019-07-30T01:09:04.000-03:00',
            'tipo': 'Intimação',
            'processo': '00486811220124025101',
            'unidade': 'RJ000012S',
            'unidadenome': 'Juízo Substituto da 12ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001845',
            'dataaviso': '2019-07-30T01:09:04.000-03:00',
            'tipo': 'Intimação',
            'processo': '00819373320184025101',
            'unidade': 'RJ000008S',
            'unidadenome': 'Juízo Substituto da 8ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001846',
            'dataaviso': '2019-07-30T01:09:04.000-03:00',
            'tipo': 'Intimação',
            'processo': '50456265520184025101',
            'unidade': 'RJ000020F',
            'unidadenome': 'Juízo Federal da 20ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001849',
            'dataaviso': '2019-07-30T01:09:04.000-03:00',
            'tipo': 'Intimação',
            'processo': '50481182020184025101',
            'unidade': 'RJ000018S',
            'unidadenome': 'Juízo Substituto da 18ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001850',
            'dataaviso': '2019-07-30T01:09:04.000-03:00',
            'tipo': 'Intimação',
            'processo': '50416937420184025101',
            'unidade': 'RJ000020S',
            'unidadenome': 'Juízo Substituto da 20ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001851',
            'dataaviso': '2019-07-30T01:09:04.000-03:00',
            'tipo': 'Intimação',
            'processo': '00834019220184025101',
            'unidade': 'RJ000029S',
            'unidadenome': 'Juízo Substituto da 29ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001852',
            'dataaviso': '2019-07-30T01:09:04.000-03:00',
            'tipo': 'Intimação',
            'processo': '00818065820184025101',
            'unidade': 'RJ000012F',
            'unidadenome': 'Juízo Federal da 12ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001854',
            'dataaviso': '2019-07-30T01:09:04.000-03:00',
            'tipo': 'Intimação',
            'processo': '00067314720174025101',
            'unidade': 'RJ000012S',
            'unidadenome': 'Juízo Substituto da 12ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001855',
            'dataaviso': '2019-07-30T01:09:04.000-03:00',
            'tipo': 'Intimação',
            'processo': '05116622720034025101',
            'unidade': 'RJ000046F',
            'unidadenome': 'Juízo Federal da 1ª VF de Execução Fiscal do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001856',
            'dataaviso': '2019-07-30T01:09:04.000-03:00',
            'tipo': 'Intimação',
            'processo': '50499257520184025101',
            'unidade': 'RJ000048S',
            'unidadenome': 'Juízo Substituto da 3ª VF de Execução Fiscal do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001859',
            'dataaviso': '2019-07-30T01:09:04.000-03:00',
            'tipo': 'Intimação',
            'processo': '50476063720184025101',
            'unidade': 'RJ000002F',
            'unidadenome': 'Juízo Federal da 2ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001860',
            'dataaviso': '2019-07-30T01:09:04.000-03:00',
            'tipo': 'Intimação',
            'processo': '00826414620184025101',
            'unidade': 'RJ000012S',
            'unidadenome': 'Juízo Substituto da 12ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001861',
            'dataaviso': '2019-07-30T01:09:04.000-03:00',
            'tipo': 'Intimação',
            'processo': '50061957720194025101',
            'unidade': 'RJ000006S',
            'unidadenome': 'Juízo Substituto da 6ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001864',
            'dataaviso': '2019-07-30T01:09:05.000-03:00',
            'tipo': 'Intimação',
            'processo': '50152505220194025101',
            'unidade': 'RJ000021S',
            'unidadenome': 'Juízo Substituto da 21ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001865',
            'dataaviso': '2019-07-30T01:09:05.000-03:00',
            'tipo': 'Intimação',
            'processo': '50150131820194025101',
            'unidade': 'RJ000021S',
            'unidadenome': 'Juízo Substituto da 21ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001866',
            'dataaviso': '2019-07-30T01:09:05.000-03:00',
            'tipo': 'Intimação',
            'processo': '50135625520194025101',
            'unidade': 'RJ000017F',
            'unidadenome': 'Juízo Federal da 17ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001867',
            'dataaviso': '2019-07-30T01:09:05.000-03:00',
            'tipo': 'Intimação',
            'processo': '50071068920194025101',
            'unidade': 'RJ000054F',
            'unidadenome': 'Juízo Federal da 4ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001868',
            'dataaviso': '2019-07-30T01:09:05.000-03:00',
            'tipo': 'Intimação',
            'processo': '05028350720154025101',
            'unidade': 'RJ000026S',
            'unidadenome': 'Juízo Substituto da 26ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001869',
            'dataaviso': '2019-07-30T01:09:05.000-03:00',
            'tipo': 'Intimação',
            'processo': '01345841020154025101',
            'unidade': 'RJ000002F',
            'unidadenome': 'Juízo Federal da 2ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001871',
            'dataaviso': '2019-07-30T01:09:05.000-03:00',
            'tipo': 'Intimação',
            'processo': '50001779220194025116',
            'unidade': 'RJ001601F',
            'unidadenome': 'Juízo Federal da 1ª VF de Macaé',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001872',
            'dataaviso': '2019-07-30T01:09:05.000-03:00',
            'tipo': 'Intimação',
            'processo': '50024993320194025101',
            'unidade': 'RJ000038S',
            'unidadenome': 'Juízo Substituto da 31ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001873',
            'dataaviso': '2019-07-30T01:09:05.000-03:00',
            'tipo': 'Intimação',
            'processo': '50315018220184025101',
            'unidade': 'RJ000014S',
            'unidadenome': 'Juízo Substituto da 14ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001874',
            'dataaviso': '2019-07-30T01:09:05.000-03:00',
            'tipo': 'Intimação',
            'processo': '00837573020184025120',
            'unidade': 'RJ002001S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Nova Iguaçu',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001875',
            'dataaviso': '2019-07-30T01:09:05.000-03:00',
            'tipo': 'Intimação',
            'processo': '50224129820194025101',
            'unidade': 'RJ000011F',
            'unidadenome': 'Juízo Federal da 11ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001876',
            'dataaviso': '2019-07-30T01:09:05.000-03:00',
            'tipo': 'Intimação',
            'processo': '01457414120154025113',
            'unidade': 'RJ001301S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Três Rios',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001877',
            'dataaviso': '2019-07-30T01:09:05.000-03:00',
            'tipo': 'Intimação',
            'processo': '50142882920194025101',
            'unidade': 'RJ000021S',
            'unidadenome': 'Juízo Substituto da 21ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001878',
            'dataaviso': '2019-07-30T01:09:05.000-03:00',
            'tipo': 'Intimação',
            'processo': '00003107020194025101',
            'unidade': 'RJ000012F',
            'unidadenome': 'Juízo Federal da 12ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001879',
            'dataaviso': '2019-07-30T01:09:05.000-03:00',
            'tipo': 'Intimação',
            'processo': '01070497220164025101',
            'unidade': 'RJ000007S',
            'unidadenome': 'Juízo Substituto da 7ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001882',
            'dataaviso': '2019-07-30T01:09:05.000-03:00',
            'tipo': 'Intimação',
            'processo': '50113644520194025101',
            'unidade': 'RJ000008F',
            'unidadenome': 'Juízo Federal da 8ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001883',
            'dataaviso': '2019-07-30T01:09:05.000-03:00',
            'tipo': 'Intimação',
            'processo': '00815926720184025101',
            'unidade': 'RJ000021F',
            'unidadenome': 'Juízo Federal da 21ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001885',
            'dataaviso': '2019-07-30T01:09:05.000-03:00',
            'tipo': 'Intimação',
            'processo': '50298759120194025101',
            'unidade': 'RJ000011S',
            'unidadenome': 'Juízo Substituto da 11ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001886',
            'dataaviso': '2019-07-30T01:09:05.000-03:00',
            'tipo': 'Intimação',
            'processo': '50305029520194025101',
            'unidade': 'RJ000029S',
            'unidadenome': 'Juízo Substituto da 29ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001888',
            'dataaviso': '2019-07-30T01:09:05.000-03:00',
            'tipo': 'Intimação',
            'processo': '50047567720194025118',
            'unidade': 'RJ006802S',
            'unidadenome': 'Juízo Substituto da 4ª VF de Duque de Caxias',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001889',
            'dataaviso': '2019-07-30T01:09:05.000-03:00',
            'tipo': 'Intimação',
            'processo': '50301521020194025101',
            'unidade': 'RJ000020S',
            'unidadenome': 'Juízo Substituto da 20ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001890',
            'dataaviso': '2019-07-30T01:09:05.000-03:00',
            'tipo': 'Intimação',
            'processo': '50226623420194025101',
            'unidade': 'RJ000021S',
            'unidadenome': 'Juízo Substituto da 21ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001891',
            'dataaviso': '2019-07-30T01:09:05.000-03:00',
            'tipo': 'Intimação',
            'processo': '00020324220194025101',
            'unidade': 'RJ000005F',
            'unidadenome': 'Juízo Federal da 5ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001892',
            'dataaviso': '2019-07-30T01:09:05.000-03:00',
            'tipo': 'Intimação',
            'processo': '01603035720164025101',
            'unidade': 'RJ000054S',
            'unidadenome': 'Juízo Substituto da 4ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001893',
            'dataaviso': '2019-07-30T01:09:05.000-03:00',
            'tipo': 'Intimação',
            'processo': '50283662820194025101',
            'unidade': 'RJ000006F',
            'unidadenome': 'Juízo Federal da 6ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001895',
            'dataaviso': '2019-07-30T01:09:05.000-03:00',
            'tipo': 'Intimação',
            'processo': '50225904720194025101',
            'unidade': 'RJ000023S',
            'unidadenome': 'Juízo Substituto da 23ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001896',
            'dataaviso': '2019-07-30T01:09:05.000-03:00',
            'tipo': 'Intimação',
            'processo': '01456571320144025101',
            'unidade': 'RJ000005S',
            'unidadenome': 'Juízo Substituto da 5ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001897',
            'dataaviso': '2019-07-30T01:09:05.000-03:00',
            'tipo': 'Intimação',
            'processo': '50113410220194025101',
            'unidade': 'RJ000020S',
            'unidadenome': 'Juízo Substituto da 20ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001898',
            'dataaviso': '2019-07-30T01:09:05.000-03:00',
            'tipo': 'Intimação',
            'processo': '50212307720194025101',
            'unidade': 'RJ000024F',
            'unidadenome': 'Juízo Federal da 24ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001900',
            'dataaviso': '2019-07-30T01:09:05.000-03:00',
            'tipo': 'Intimação',
            'processo': '50160334420194025101',
            'unidade': 'RJ000024F',
            'unidadenome': 'Juízo Federal da 24ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001901',
            'dataaviso': '2019-07-30T01:09:05.000-03:00',
            'tipo': 'Intimação',
            'processo': '50171628420194025101',
            'unidade': 'RJ000024S',
            'unidadenome': 'Juízo Substituto da 24ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001902',
            'dataaviso': '2019-07-30T01:09:05.000-03:00',
            'tipo': 'Intimação',
            'processo': '50207847420194025101',
            'unidade': 'RJ000024F',
            'unidadenome': 'Juízo Federal da 24ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001905',
            'dataaviso': '2019-07-30T01:09:05.000-03:00',
            'tipo': 'Intimação',
            'processo': '50019664120194025112',
            'unidade': 'RJ001201S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Itaperuna',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001906',
            'dataaviso': '2019-07-30T01:09:05.000-03:00',
            'tipo': 'Intimação',
            'processo': '50375310220194025101',
            'unidade': 'RJ000029S',
            'unidadenome': 'Juízo Substituto da 29ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001908',
            'dataaviso': '2019-07-30T01:09:05.000-03:00',
            'tipo': 'Intimação',
            'processo': '50335774520194025101',
            'unidade': 'RJ000030F',
            'unidadenome': 'Juízo Federal da 30ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001909',
            'dataaviso': '2019-07-30T01:09:05.000-03:00',
            'tipo': 'Intimação',
            'processo': '50174919620194025101',
            'unidade': 'RJ000016F',
            'unidadenome': 'Juízo Federal da 16ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001910',
            'dataaviso': '2019-07-30T01:09:06.000-03:00',
            'tipo': 'Intimação',
            'processo': '00818351120184025101',
            'unidade': 'RJ000021S',
            'unidadenome': 'Juízo Substituto da 21ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001911',
            'dataaviso': '2019-07-30T01:09:06.000-03:00',
            'tipo': 'Citação',
            'processo': '50420960920194025101',
            'unidade': 'RJ000015S',
            'unidadenome': 'Juízo Substituto da 15ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001912',
            'dataaviso': '2019-07-30T01:09:06.000-03:00',
            'tipo': 'Intimação',
            'processo': '00856876620164025116',
            'unidade': 'RJ001601S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Macaé',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001913',
            'dataaviso': '2019-07-30T01:09:06.000-03:00',
            'tipo': 'Intimação',
            'processo': '50424235120194025101',
            'unidade': 'RJ000029S',
            'unidadenome': 'Juízo Substituto da 29ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001914',
            'dataaviso': '2019-07-30T01:09:06.000-03:00',
            'tipo': 'Citação',
            'processo': '50414353020194025101',
            'unidade': 'RJ000014F',
            'unidadenome': 'Juízo Federal da 14ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001915',
            'dataaviso': '2019-07-30T01:09:06.000-03:00',
            'tipo': 'Intimação',
            'processo': '50040755820194025102',
            'unidade': 'RJ000104S',
            'unidadenome': 'Juízo Substituto da 4ª VF de Niterói',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001917',
            'dataaviso': '2019-07-30T01:09:06.000-03:00',
            'tipo': 'Citação',
            'processo': '50436949520194025101',
            'unidade': 'RJ000029S',
            'unidadenome': 'Juízo Substituto da 29ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001918',
            'dataaviso': '2019-07-30T01:09:06.000-03:00',
            'tipo': 'Intimação',
            'processo': '50058773720194025120',
            'unidade': 'RJ007001F',
            'unidadenome': 'Juízo Federal da 3ª VF de Nova Iguaçu',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001919',
            'dataaviso': '2019-07-30T01:09:06.000-03:00',
            'tipo': 'Intimação',
            'processo': '50038216120194025110',
            'unidade': 'RJ006002F',
            'unidadenome': 'Juízo Federal da 8ª VF de São João de Meriti',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001946',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '02096229520174025153',
            'unidade': 'RJ005301F',
            'unidadenome': 'Juízo Federal da 3ª VF de Campos',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001947',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '01166215620174025153',
            'unidade': 'RJ005301S',
            'unidadenome': 'Juízo Substituto da 3ª VF de Campos',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001948',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '00289332220184025153',
            'unidade': 'RJ005301S',
            'unidadenome': 'Juízo Substituto da 3ª VF de Campos',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001949',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '00388177520184025153',
            'unidade': 'RJ005301S',
            'unidadenome': 'Juízo Substituto da 3ª VF de Campos',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001951',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '01716681520174025153',
            'unidade': 'RJ005301F',
            'unidadenome': 'Juízo Federal da 3ª VF de Campos',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001952',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '50003101620184025102',
            'unidade': 'RJ005202S',
            'unidadenome': 'Juízo Substituto do 2º JEF de Niterói',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001953',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '50049427920184025104',
            'unidade': 'RJ005401F',
            'unidadenome': 'Juízo Federal do 1º JEF de Volta Redonda',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001954',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '50444735020194025101',
            'unidade': 'RJ000024F',
            'unidadenome': 'Juízo Federal da 24ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001955',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '00655406420164025101',
            'unidade': 'RJ001101F',
            'unidadenome': 'Juízo Federal da 1ª VF de Angra dos Reis',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001956',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '50084989820184025101',
            'unidade': 'RJ005109S',
            'unidadenome': 'Juízo Substituto do 9º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001957',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '50178308920184025101',
            'unidade': 'RJ005109S',
            'unidadenome': 'Juízo Substituto do 9º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001958',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '50003341120184025113',
            'unidade': 'RJ001301F',
            'unidadenome': 'Juízo Federal da 1ª VF de Três Rios',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001960',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '01730500220174025102',
            'unidade': 'RJ005202F',
            'unidadenome': 'Juízo Federal do 2º JEF de Niterói',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001961',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '01846069820174025102',
            'unidade': 'RJ005202F',
            'unidadenome': 'Juízo Federal do 2º JEF de Niterói',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001962',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '50039084420194025101',
            'unidade': 'RJ005108F',
            'unidadenome': 'Juízo Federal do 8º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001963',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '01252433320174025151',
            'unidade': 'RJ005108S',
            'unidadenome': 'Juízo Substituto do 8º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001964',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '50164734020194025101',
            'unidade': 'RJ000019S',
            'unidadenome': 'Juízo Substituto da 19ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001965',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Urgente',
            'processo': '00629257320184025120',
            'unidade': 'RJ002002S',
            'unidadenome': 'Juízo Substituto da 2ª VF de Nova Iguaçu',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001966',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '50280046020184025101',
            'unidade': 'RJ005111F',
            'unidadenome': 'Juízo Federal do 11º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001969',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '00019354220194025101',
            'unidade': 'RJ000012S',
            'unidadenome': 'Juízo Substituto da 12ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001970',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '50081153520194025118',
            'unidade': 'RJ001802S',
            'unidadenome': 'Juízo Substituto da 2ª VF de Duque de Caxias',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001971',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '50033447220184025110',
            'unidade': 'RJ006002S',
            'unidadenome': 'Juízo Substituto da 8ª VF de São João de Meriti',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001972',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '50040912220184025110',
            'unidade': 'RJ006002S',
            'unidadenome': 'Juízo Substituto da 8ª VF de São João de Meriti',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001973',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '50360466420194025101',
            'unidade': 'RJ000023S',
            'unidadenome': 'Juízo Substituto da 23ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001975',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '50041884920184025101',
            'unidade': 'RJ005106S',
            'unidadenome': 'Juízo Substituto do 6º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001976',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '50009814220184025101',
            'unidade': 'RJ005106S',
            'unidadenome': 'Juízo Substituto do 6º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001977',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '50304769720194025101',
            'unidade': 'RJ000020F',
            'unidadenome': 'Juízo Federal da 20ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001978',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '01980289020174025151',
            'unidade': 'RJ005116F',
            'unidadenome': 'Juízo Federal do 16º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001979',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '02105358320174025151',
            'unidade': 'RJ005116S',
            'unidadenome': 'Juízo Substituto do 16º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001980',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '05002250820184025151',
            'unidade': 'RJ005116S',
            'unidadenome': 'Juízo Substituto do 16º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001981',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '50057465620184025101',
            'unidade': 'RJ005109F',
            'unidadenome': 'Juízo Federal do 9º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001982',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '50326545320184025101',
            'unidade': 'RJ005109F',
            'unidadenome': 'Juízo Federal do 9º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001983',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '00173491020184025168',
            'unidade': 'RJ006803S',
            'unidadenome': 'Juízo Substituto da 5ª VF de Duque de Caxias',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001984',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '02149210820174025168',
            'unidade': 'RJ006803S',
            'unidadenome': 'Juízo Substituto da 5ª VF de Duque de Caxias',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001985',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '50014287620184025118',
            'unidade': 'RJ006803S',
            'unidadenome': 'Juízo Substituto da 5ª VF de Duque de Caxias',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001986',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '00562123520184025168',
            'unidade': 'RJ006803F',
            'unidadenome': 'Juízo Federal da 5ª VF de Duque de Caxias',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001987',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Citação',
            'processo': '50386725620194025101',
            'unidade': 'RJ000030F',
            'unidadenome': 'Juízo Federal da 30ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001988',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '01352297820154025119',
            'unidade': 'RJ001901S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Barra do Piraí',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001989',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '01197799520154025119',
            'unidade': 'RJ001901S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Barra do Piraí',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001990',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '00519991620184025158',
            'unidade': 'RJ000802S',
            'unidadenome': 'Juízo Substituto da 2ª VF de São Pedro da Aldeia',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001991',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '00026481720194025101',
            'unidade': 'RJ000002F',
            'unidadenome': 'Juízo Federal da 2ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001992',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Citação',
            'processo': '50001366420194025104',
            'unidade': 'RJ005401F',
            'unidadenome': 'Juízo Federal do 1º JEF de Volta Redonda',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001993',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '00000677520094025102',
            'unidade': 'RJ000105S',
            'unidadenome': 'Juízo Substituto da 5ª VF de Niterói',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001994',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '00318239020184025101',
            'unidade': 'RJ000039S',
            'unidadenome': 'Juízo Substituto da 9ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001995',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '00813622520184025101',
            'unidade': 'RJ000020F',
            'unidadenome': 'Juízo Federal da 20ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001996',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '50002435420184025101',
            'unidade': 'RJ005105S',
            'unidadenome': 'Juízo Substituto do 5º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001997',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '50498041320194025101',
            'unidade': 'RJ000026S',
            'unidadenome': 'Juízo Substituto da 26ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300001998',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '00815277220184025101',
            'unidade': 'RJ000026S',
            'unidadenome': 'Juízo Substituto da 26ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300002001',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '01133778120174025101',
            'unidade': 'RJ000012S',
            'unidadenome': 'Juízo Substituto da 12ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300002002',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '01722949320174025101',
            'unidade': 'RJ000012F',
            'unidadenome': 'Juízo Federal da 12ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300002003',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '01776657220164025101',
            'unidade': 'RJ000012S',
            'unidadenome': 'Juízo Substituto da 12ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300002004',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '01492879520174025158',
            'unidade': 'RJ000802S',
            'unidadenome': 'Juízo Substituto da 2ª VF de São Pedro da Aldeia',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300002005',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '50068412420184025101',
            'unidade': 'RJ005109S',
            'unidadenome': 'Juízo Substituto do 9º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300002006',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '50027926720194025112',
            'unidade': 'RJ001201F',
            'unidadenome': 'Juízo Federal da 1ª VF de Itaperuna',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300002007',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '50373950520194025101',
            'unidade': 'RJ000037S',
            'unidadenome': 'Juízo Substituto da 13ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300002008',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '50049698320194025118',
            'unidade': 'RJ006801F',
            'unidadenome': 'Juízo Federal da 3ª VF de Duque de Caxias',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300002009',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '50359516820184025101',
            'unidade': 'RJ005111F',
            'unidadenome': 'Juízo Federal do 11º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300002010',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '50308423920194025101',
            'unidade': 'RJ000001F',
            'unidadenome': 'Juízo Federal da 1ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300002011',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '00066872820174025101',
            'unidade': 'RJ000012S',
            'unidadenome': 'Juízo Substituto da 12ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300002013',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '50254072120184025101',
            'unidade': 'RJ000024S',
            'unidadenome': 'Juízo Substituto da 24ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300002015',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '50017014920184025120',
            'unidade': 'RJ007002S',
            'unidadenome': 'Juízo Substituto da 4ª VF de Nova Iguaçu',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300002016',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '50029122320184025120',
            'unidade': 'RJ007002S',
            'unidadenome': 'Juízo Substituto da 4ª VF de Nova Iguaçu',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300002017',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '50039125820184025120',
            'unidade': 'RJ007002F',
            'unidadenome': 'Juízo Federal da 4ª VF de Nova Iguaçu',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300002018',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '50047579020184025120',
            'unidade': 'RJ007002S',
            'unidadenome': 'Juízo Substituto da 4ª VF de Nova Iguaçu',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300002019',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '50057538820184025120',
            'unidade': 'RJ007002S',
            'unidadenome': 'Juízo Substituto da 4ª VF de Nova Iguaçu',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300002020',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '02106023920174025154',
            'unidade': 'RJ005402F',
            'unidadenome': 'Juízo Federal do 2º JEF de Volta Redonda',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300002021',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '01945887720174025154',
            'unidade': 'RJ005402F',
            'unidadenome': 'Juízo Federal do 2º JEF de Volta Redonda',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300002022',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '02297130920174025154',
            'unidade': 'RJ005402S',
            'unidadenome': 'Juízo Substituto do 2º JEF de Volta Redonda',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300002023',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '02086251220174025154',
            'unidade': 'RJ005402S',
            'unidadenome': 'Juízo Substituto do 2º JEF de Volta Redonda',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300002024',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '00562943520164025104',
            'unidade': 'RJ005402F',
            'unidadenome': 'Juízo Federal do 2º JEF de Volta Redonda',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300002025',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '05000969120184025154',
            'unidade': 'RJ005402F',
            'unidadenome': 'Juízo Federal do 2º JEF de Volta Redonda',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300002026',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '50023498020184025103',
            'unidade': 'RJ005302S',
            'unidadenome': 'Juízo Substituto da 4ª VF de Campos',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300002027',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '00318282619924025101',
            'unidade': 'RJ000018F',
            'unidadenome': 'Juízo Federal da 18ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300002028',
            'dataaviso': '2019-07-30T01:09:07.000-03:00',
            'tipo': 'Intimação',
            'processo': '50192165720184025101',
            'unidade': 'RJ005109F',
            'unidadenome': 'Juízo Federal do 9º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201907300003174',
            'dataaviso': '2019-07-30T05:03:39.000-03:00',
            'tipo': 'Intimação',
            'processo': '01227646720174025151',
            'unidade': 'RJ000019F',
            'unidadenome': 'Juízo Federal da 19ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000001',
            'dataaviso': '2019-09-17T12:12:08.000-03:00',
            'tipo': 'Intimação',
            'processo': '05000949020174025111',
            'unidade': 'RJ000050S',
            'unidadenome': 'Juízo Substituto da 5ª VF de Execução Fiscal do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000002',
            'dataaviso': '2019-09-17T12:12:08.000-03:00',
            'tipo': 'Intimação',
            'processo': '00288926120184025151',
            'unidade': 'RJ005111F',
            'unidadenome': 'Juízo Federal do 11º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000003',
            'dataaviso': '2019-09-17T12:12:08.000-03:00',
            'tipo': 'Intimação',
            'processo': '50020423520184025101',
            'unidade': 'RJ005111S',
            'unidadenome': 'Juízo Substituto do 11º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000004',
            'dataaviso': '2019-09-17T12:12:08.000-03:00',
            'tipo': 'Intimação',
            'processo': '01811546520174025107',
            'unidade': 'RJ000702F',
            'unidadenome': 'Juízo Federal da 2ª VF de Itaboraí',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000005',
            'dataaviso': '2019-09-17T12:12:08.000-03:00',
            'tipo': 'Intimação',
            'processo': '50006971620184025107',
            'unidade': 'RJ000702F',
            'unidadenome': 'Juízo Federal da 2ª VF de Itaboraí',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000006',
            'dataaviso': '2019-09-17T12:12:09.000-03:00',
            'tipo': 'Intimação',
            'processo': '50048757220184025118',
            'unidade': 'RJ001801F',
            'unidadenome': 'Juízo Federal da 1ª VF de Duque de Caxias',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000007',
            'dataaviso': '2019-09-17T12:12:09.000-03:00',
            'tipo': 'Intimação',
            'processo': '50420609820184025101',
            'unidade': 'RJ000007F',
            'unidadenome': 'Juízo Federal da 7ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000008',
            'dataaviso': '2019-09-17T12:12:09.000-03:00',
            'tipo': 'Intimação',
            'processo': '50144753720194025101',
            'unidade': 'RJ000007F',
            'unidadenome': 'Juízo Federal da 7ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000009',
            'dataaviso': '2019-09-17T12:12:09.000-03:00',
            'tipo': 'Intimação',
            'processo': '00032994920194025101',
            'unidade': 'RJ000007S',
            'unidadenome': 'Juízo Substituto da 7ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000010',
            'dataaviso': '2019-09-17T12:12:09.000-03:00',
            'tipo': 'Intimação',
            'processo': '00817060620184025101',
            'unidade': 'RJ000007F',
            'unidadenome': 'Juízo Federal da 7ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000011',
            'dataaviso': '2019-09-17T12:12:10.000-03:00',
            'tipo': 'Intimação',
            'processo': '00809081620164025101',
            'unidade': 'RJ000007F',
            'unidadenome': 'Juízo Federal da 7ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000012',
            'dataaviso': '2019-09-17T12:12:10.000-03:00',
            'tipo': 'Intimação',
            'processo': '50207547320184025101',
            'unidade': 'RJ000039F',
            'unidadenome': 'Juízo Federal da 9ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000013',
            'dataaviso': '2019-09-17T12:12:10.000-03:00',
            'tipo': 'Intimação',
            'processo': '00013188220194025101',
            'unidade': 'RJ000018F',
            'unidadenome': 'Juízo Federal da 18ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000014',
            'dataaviso': '2019-09-17T12:12:10.000-03:00',
            'tipo': 'Intimação',
            'processo': '05002329720184025151',
            'unidade': 'RJ005115F',
            'unidadenome': 'Juízo Federal do 15º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000015',
            'dataaviso': '2019-09-17T12:12:10.000-03:00',
            'tipo': 'Intimação',
            'processo': '00781361220184025101',
            'unidade': 'RJ000001F',
            'unidadenome': 'Juízo Federal da 1ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000016',
            'dataaviso': '2019-09-17T12:12:10.000-03:00',
            'tipo': 'Intimação',
            'processo': '50399789420184025101',
            'unidade': 'RJ000011F',
            'unidadenome': 'Juízo Federal da 11ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000017',
            'dataaviso': '2019-09-17T12:12:10.000-03:00',
            'tipo': 'Intimação',
            'processo': '50189827520184025101',
            'unidade': 'RJ000037S',
            'unidadenome': 'Juízo Substituto da 13ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000018',
            'dataaviso': '2019-09-17T12:12:11.000-03:00',
            'tipo': 'Intimação',
            'processo': '50027372620184025121',
            'unidade': 'RJ005113F',
            'unidadenome': 'Juízo Federal do 13º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000019',
            'dataaviso': '2019-09-17T12:12:11.000-03:00',
            'tipo': 'Intimação',
            'processo': '00032967520184025151',
            'unidade': 'RJ005113F',
            'unidadenome': 'Juízo Federal do 13º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000020',
            'dataaviso': '2019-09-17T12:12:11.000-03:00',
            'tipo': 'Intimação',
            'processo': '50021388720184025121',
            'unidade': 'RJ005113F',
            'unidadenome': 'Juízo Federal do 13º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000021',
            'dataaviso': '2019-09-17T12:12:11.000-03:00',
            'tipo': 'Intimação',
            'processo': '00009210620134025110',
            'unidade': 'RJ001032S',
            'unidadenome': 'Juízo Substituto da 2ª VF de São João de Meriti',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000022',
            'dataaviso': '2019-09-17T12:12:11.000-03:00',
            'tipo': 'Intimação',
            'processo': '50507256920194025101',
            'unidade': 'RJ009162',
            'unidadenome': '6ª Turma Recursal - 2º Juiz Relator',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000023',
            'dataaviso': '2019-09-17T12:12:11.000-03:00',
            'tipo': 'Intimação',
            'processo': '02186235420174025105',
            'unidade': 'RJ005501S',
            'unidadenome': 'Juízo Substituto do 1º JEF de Nova Friburgo',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000024',
            'dataaviso': '2019-09-17T12:12:12.000-03:00',
            'tipo': 'Intimação',
            'processo': '02134770820174025113',
            'unidade': 'RJ001301S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Três Rios',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000025',
            'dataaviso': '2019-09-17T12:12:12.000-03:00',
            'tipo': 'Intimação',
            'processo': '00817683420184025105',
            'unidade': 'RJ000501F',
            'unidadenome': 'Juízo Federal da 1ª VF de Nova Friburgo',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000026',
            'dataaviso': '2019-09-17T12:12:12.000-03:00',
            'tipo': 'Intimação',
            'processo': '50029101320184025101',
            'unidade': 'RJ005116S',
            'unidadenome': 'Juízo Substituto do 16º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000027',
            'dataaviso': '2019-09-17T12:12:12.000-03:00',
            'tipo': 'Intimação',
            'processo': '00032265520184025152',
            'unidade': 'RJ005202F',
            'unidadenome': 'Juízo Federal do 2º JEF de Niterói',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000028',
            'dataaviso': '2019-09-17T12:12:12.000-03:00',
            'tipo': 'Intimação',
            'processo': '50037298320194025110',
            'unidade': 'RJ000003S',
            'unidadenome': 'Juízo Substituto da 3ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000029',
            'dataaviso': '2019-09-17T12:12:12.000-03:00',
            'tipo': 'Intimação',
            'processo': '50480584720184025101',
            'unidade': 'RJ005108S',
            'unidadenome': 'Juízo Substituto do 8º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000030',
            'dataaviso': '2019-09-17T12:12:12.000-03:00',
            'tipo': 'Intimação',
            'processo': '50214440520184025101',
            'unidade': 'RJ005108F',
            'unidadenome': 'Juízo Federal do 8º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000031',
            'dataaviso': '2019-09-17T12:12:13.000-03:00',
            'tipo': 'Intimação',
            'processo': '00335003420134025101',
            'unidade': 'RJ000012F',
            'unidadenome': 'Juízo Federal da 12ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000032',
            'dataaviso': '2019-09-17T12:12:13.000-03:00',
            'tipo': 'Intimação',
            'processo': '50022432720184025101',
            'unidade': 'RJ005108S',
            'unidadenome': 'Juízo Substituto do 8º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000033',
            'dataaviso': '2019-09-17T12:12:13.000-03:00',
            'tipo': 'Intimação',
            'processo': '50215211420184025101',
            'unidade': 'RJ005108S',
            'unidadenome': 'Juízo Substituto do 8º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000034',
            'dataaviso': '2019-09-17T12:12:13.000-03:00',
            'tipo': 'Intimação',
            'processo': '50015034220184025110',
            'unidade': 'RJ006001F',
            'unidadenome': 'Juízo Federal da 7ª VF de São João de Meriti',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000035',
            'dataaviso': '2019-09-17T12:12:13.000-03:00',
            'tipo': 'Intimação',
            'processo': '00014598320194025107',
            'unidade': 'RJ000702S',
            'unidadenome': 'Juízo Substituto da 2ª VF de Itaboraí',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000036',
            'dataaviso': '2019-09-17T12:12:13.000-03:00',
            'tipo': 'Intimação',
            'processo': '00279768020184025101',
            'unidade': 'RJ000007F',
            'unidadenome': 'Juízo Federal da 7ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000037',
            'dataaviso': '2019-09-17T12:12:13.000-03:00',
            'tipo': 'Intimação',
            'processo': '50010693720194025104',
            'unidade': 'RJ000403S',
            'unidadenome': 'Juízo Substituto da 3ª VF de Volta Redonda',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000038',
            'dataaviso': '2019-09-17T12:12:14.000-03:00',
            'tipo': 'Intimação',
            'processo': '02064626820174025151',
            'unidade': 'RJ000012F',
            'unidadenome': 'Juízo Federal da 12ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000039',
            'dataaviso': '2019-09-17T12:12:14.000-03:00',
            'tipo': 'Intimação',
            'processo': '50050346620184025101',
            'unidade': 'RJ000035F',
            'unidadenome': 'Juízo Federal da 25ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000040',
            'dataaviso': '2019-09-17T12:12:14.000-03:00',
            'tipo': 'Intimação',
            'processo': '50488194420194025101',
            'unidade': 'RJ000035F',
            'unidadenome': 'Juízo Federal da 25ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000041',
            'dataaviso': '2019-09-17T12:12:14.000-03:00',
            'tipo': 'Intimação',
            'processo': '50466667220184025101',
            'unidade': 'RJ000035S',
            'unidadenome': 'Juízo Substituto da 25ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000042',
            'dataaviso': '2019-09-17T12:12:14.000-03:00',
            'tipo': 'Intimação',
            'processo': '00821890920184025110',
            'unidade': 'RJ001006S',
            'unidadenome': 'Juízo Substituto da 6ª VF de São João de Meriti',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000043',
            'dataaviso': '2019-09-17T12:12:14.000-03:00',
            'tipo': 'Intimação',
            'processo': '01701759320164025102',
            'unidade': 'RJ000103S',
            'unidadenome': 'Juízo Substituto da 3ª VF de Niterói',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000044',
            'dataaviso': '2019-09-17T12:12:14.000-03:00',
            'tipo': 'Intimação',
            'processo': '50015163120194025102',
            'unidade': 'RJ000103S',
            'unidadenome': 'Juízo Substituto da 3ª VF de Niterói',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000045',
            'dataaviso': '2019-09-17T12:12:15.000-03:00',
            'tipo': 'Intimação',
            'processo': '50023365020194025102',
            'unidade': 'RJ000103F',
            'unidadenome': 'Juízo Federal da 3ª VF de Niterói',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000046',
            'dataaviso': '2019-09-17T12:12:15.000-03:00',
            'tipo': 'Intimação',
            'processo': '50024542620194025102',
            'unidade': 'RJ000103S',
            'unidadenome': 'Juízo Substituto da 3ª VF de Niterói',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000047',
            'dataaviso': '2019-09-17T12:12:15.000-03:00',
            'tipo': 'Intimação',
            'processo': '00299955920184025101',
            'unidade': 'RJ000014S',
            'unidadenome': 'Juízo Substituto da 14ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000048',
            'dataaviso': '2019-09-17T12:12:15.000-03:00',
            'tipo': 'Intimação',
            'processo': '50302465520194025101',
            'unidade': 'RJ000014F',
            'unidadenome': 'Juízo Federal da 14ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000049',
            'dataaviso': '2019-09-17T12:12:15.000-03:00',
            'tipo': 'Intimação',
            'processo': '05010463120194025101',
            'unidade': 'RJ000014S',
            'unidadenome': 'Juízo Substituto da 14ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000050',
            'dataaviso': '2019-09-17T12:12:15.000-03:00',
            'tipo': 'Intimação',
            'processo': '01086721120154025101',
            'unidade': 'RJ000014F',
            'unidadenome': 'Juízo Federal da 14ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000051',
            'dataaviso': '2019-09-17T12:12:15.000-03:00',
            'tipo': 'Intimação',
            'processo': '01738088120174025101',
            'unidade': 'RJ000014F',
            'unidadenome': 'Juízo Federal da 14ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000052',
            'dataaviso': '2019-09-17T12:12:16.000-03:00',
            'tipo': 'Intimação',
            'processo': '50004293820184025114',
            'unidade': 'RJ001401S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Magé',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000053',
            'dataaviso': '2019-09-17T12:12:16.000-03:00',
            'tipo': 'Intimação',
            'processo': '50023402420184025102',
            'unidade': 'RJ000101F',
            'unidadenome': 'Juízo Federal da 1ª VF de Niterói',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000054',
            'dataaviso': '2019-09-17T12:12:16.000-03:00',
            'tipo': 'Intimação',
            'processo': '50179737820184025101',
            'unidade': 'RJ000039F',
            'unidadenome': 'Juízo Federal da 9ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000055',
            'dataaviso': '2019-09-17T12:12:16.000-03:00',
            'tipo': 'Intimação',
            'processo': '50002832420184025105',
            'unidade': 'RJ005501S',
            'unidadenome': 'Juízo Substituto do 1º JEF de Nova Friburgo',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000056',
            'dataaviso': '2019-09-17T12:12:16.000-03:00',
            'tipo': 'Intimação',
            'processo': '50013092420184025116',
            'unidade': 'RJ001601F',
            'unidadenome': 'Juízo Federal da 1ª VF de Macaé',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000057',
            'dataaviso': '2019-09-17T12:12:16.000-03:00',
            'tipo': 'Intimação',
            'processo': '00170586620184025117',
            'unidade': 'RJ006701F',
            'unidadenome': 'Juízo Federal do 1º JEF de São Gonçalo',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000058',
            'dataaviso': '2019-09-17T12:12:16.000-03:00',
            'tipo': 'Intimação',
            'processo': '00239044620184025167',
            'unidade': 'RJ006701F',
            'unidadenome': 'Juízo Federal do 1º JEF de São Gonçalo',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000059',
            'dataaviso': '2019-09-17T12:12:17.000-03:00',
            'tipo': 'Intimação',
            'processo': '50363003720194025101',
            'unidade': 'RJ000012S',
            'unidadenome': 'Juízo Substituto da 12ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000060',
            'dataaviso': '2019-09-17T12:12:17.000-03:00',
            'tipo': 'Intimação',
            'processo': '00170914120174025101',
            'unidade': 'RJ000007S',
            'unidadenome': 'Juízo Substituto da 7ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000061',
            'dataaviso': '2019-09-17T12:12:17.000-03:00',
            'tipo': 'Intimação',
            'processo': '00135648620144025101',
            'unidade': 'RJ000007F',
            'unidadenome': 'Juízo Federal da 7ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000062',
            'dataaviso': '2019-09-17T12:12:17.000-03:00',
            'tipo': 'Intimação',
            'processo': '50007304520194025115',
            'unidade': 'RJ001501F',
            'unidadenome': 'Juízo Federal da 1ª VF de Teresópolis',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000063',
            'dataaviso': '2019-09-17T12:12:17.000-03:00',
            'tipo': 'Intimação',
            'processo': '50007304520194025115',
            'unidade': 'RJ001501F',
            'unidadenome': 'Juízo Federal da 1ª VF de Teresópolis',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000064',
            'dataaviso': '2019-09-17T12:12:17.000-03:00',
            'tipo': 'Intimação',
            'processo': '00692736720184025101',
            'unidade': 'RJ000008S',
            'unidadenome': 'Juízo Substituto da 8ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000065',
            'dataaviso': '2019-09-17T12:12:17.000-03:00',
            'tipo': 'Intimação',
            'processo': '00820179420184025101',
            'unidade': 'RJ000008S',
            'unidadenome': 'Juízo Substituto da 8ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000066',
            'dataaviso': '2019-09-17T12:12:18.000-03:00',
            'tipo': 'Intimação',
            'processo': '50148836220184025101',
            'unidade': 'RJ000008F',
            'unidadenome': 'Juízo Federal da 8ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000067',
            'dataaviso': '2019-09-17T12:12:18.000-03:00',
            'tipo': 'Intimação',
            'processo': '50229978720184025101',
            'unidade': 'RJ000008S',
            'unidadenome': 'Juízo Substituto da 8ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000068',
            'dataaviso': '2019-09-17T12:12:18.000-03:00',
            'tipo': 'Citação',
            'processo': '50480524020184025101',
            'unidade': 'RJ000028S',
            'unidadenome': 'Juízo Substituto da 28ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000069',
            'dataaviso': '2019-09-17T12:12:18.000-03:00',
            'tipo': 'Intimação',
            'processo': '50063160820194025101',
            'unidade': 'RJ005109F',
            'unidadenome': 'Juízo Federal do 9º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000070',
            'dataaviso': '2019-09-17T12:12:18.000-03:00',
            'tipo': 'Intimação',
            'processo': '50041304620184025101',
            'unidade': 'RJ005109F',
            'unidadenome': 'Juízo Federal do 9º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000071',
            'dataaviso': '2019-09-17T12:12:18.000-03:00',
            'tipo': 'Intimação',
            'processo': '50219853820184025101',
            'unidade': 'RJ005109S',
            'unidadenome': 'Juízo Substituto do 9º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000072',
            'dataaviso': '2019-09-17T12:12:18.000-03:00',
            'tipo': 'Intimação',
            'processo': '50055664020184025101',
            'unidade': 'RJ005109S',
            'unidadenome': 'Juízo Substituto do 9º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000073',
            'dataaviso': '2019-09-17T12:12:19.000-03:00',
            'tipo': 'Intimação',
            'processo': '50030002120184025101',
            'unidade': 'RJ005109S',
            'unidadenome': 'Juízo Substituto do 9º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000074',
            'dataaviso': '2019-09-17T12:12:19.000-03:00',
            'tipo': 'Intimação',
            'processo': '50025438620184025101',
            'unidade': 'RJ005109F',
            'unidadenome': 'Juízo Federal do 9º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000075',
            'dataaviso': '2019-09-17T12:12:19.000-03:00',
            'tipo': 'Intimação',
            'processo': '00673170320184025170',
            'unidade': 'RJ007002S',
            'unidadenome': 'Juízo Substituto da 4ª VF de Nova Iguaçu',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000076',
            'dataaviso': '2019-09-17T12:12:19.000-03:00',
            'tipo': 'Intimação',
            'processo': '50012114720194025102',
            'unidade': 'RJ000104F',
            'unidadenome': 'Juízo Federal da 4ª VF de Niterói',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000077',
            'dataaviso': '2019-09-17T12:12:19.000-03:00',
            'tipo': 'Intimação',
            'processo': '50101479820184025101',
            'unidade': 'RJ005108S',
            'unidadenome': 'Juízo Substituto do 8º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000078',
            'dataaviso': '2019-09-17T12:12:19.000-03:00',
            'tipo': 'Intimação',
            'processo': '50034211120184025101',
            'unidade': 'RJ005108F',
            'unidadenome': 'Juízo Federal do 8º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000079',
            'dataaviso': '2019-09-17T12:12:19.000-03:00',
            'tipo': 'Intimação',
            'processo': '01626278020174025102',
            'unidade': 'RJ000101S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Niterói',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000080',
            'dataaviso': '2019-09-17T12:12:19.000-03:00',
            'tipo': 'Intimação',
            'processo': '50473202520194025101',
            'unidade': 'RJ000001F',
            'unidadenome': 'Juízo Federal da 1ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000081',
            'dataaviso': '2019-09-17T12:12:20.000-03:00',
            'tipo': 'Intimação',
            'processo': '01066648420174025103',
            'unidade': 'RJ000201F',
            'unidadenome': 'Juízo Federal da 1ª VF de Campos',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000082',
            'dataaviso': '2019-09-17T12:12:20.000-03:00',
            'tipo': 'Citação',
            'processo': '50003074920184025106',
            'unidade': 'RJ000602S',
            'unidadenome': 'Juízo Substituto da 2ª VF de Petrópolis',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000083',
            'dataaviso': '2019-09-17T12:12:20.000-03:00',
            'tipo': 'Citação',
            'processo': '50013696020194025116',
            'unidade': 'RJ001601S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Macaé',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000084',
            'dataaviso': '2019-09-17T12:12:20.000-03:00',
            'tipo': 'Intimação',
            'processo': '50218092520194025101',
            'unidade': 'RJ000012S',
            'unidadenome': 'Juízo Substituto da 12ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000085',
            'dataaviso': '2019-09-17T12:12:20.000-03:00',
            'tipo': 'Intimação',
            'processo': '50159641220194025101',
            'unidade': 'RJ000012S',
            'unidadenome': 'Juízo Substituto da 12ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000086',
            'dataaviso': '2019-09-17T12:12:20.000-03:00',
            'tipo': 'Intimação',
            'processo': '00085643720164025101',
            'unidade': 'RJ000020F',
            'unidadenome': 'Juízo Federal da 20ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000087',
            'dataaviso': '2019-09-17T12:12:20.000-03:00',
            'tipo': 'Intimação',
            'processo': '50409044120194025101',
            'unidade': 'RJ000019S',
            'unidadenome': 'Juízo Substituto da 19ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000088',
            'dataaviso': '2019-09-17T12:12:21.000-03:00',
            'tipo': 'Intimação',
            'processo': '50097434720184025101',
            'unidade': 'RJ005105F',
            'unidadenome': 'Juízo Federal do 5º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000089',
            'dataaviso': '2019-09-17T12:12:21.000-03:00',
            'tipo': 'Intimação',
            'processo': '50004660720184025101',
            'unidade': 'RJ005105S',
            'unidadenome': 'Juízo Substituto do 5º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000090',
            'dataaviso': '2019-09-17T12:12:21.000-03:00',
            'tipo': 'Citação',
            'processo': '50369900320184025101',
            'unidade': 'RJ005109S',
            'unidadenome': 'Juízo Substituto do 9º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000091',
            'dataaviso': '2019-09-17T12:12:21.000-03:00',
            'tipo': 'Intimação',
            'processo': '50360512320184025101',
            'unidade': 'RJ005107F',
            'unidadenome': 'Juízo Federal do 7º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000092',
            'dataaviso': '2019-09-17T12:12:21.000-03:00',
            'tipo': 'Intimação',
            'processo': '50020787720184025101',
            'unidade': 'RJ005107F',
            'unidadenome': 'Juízo Federal do 7º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000093',
            'dataaviso': '2019-09-17T12:12:21.000-03:00',
            'tipo': 'Intimação',
            'processo': '50071720620184025101',
            'unidade': 'RJ005107F',
            'unidadenome': 'Juízo Federal do 7º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000094',
            'dataaviso': '2019-09-17T12:12:21.000-03:00',
            'tipo': 'Intimação',
            'processo': '50039503020184025101',
            'unidade': 'RJ005107S',
            'unidadenome': 'Juízo Substituto do 7º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000095',
            'dataaviso': '2019-09-17T12:12:22.000-03:00',
            'tipo': 'Intimação',
            'processo': '50022571120184025101',
            'unidade': 'RJ005107F',
            'unidadenome': 'Juízo Federal do 7º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000096',
            'dataaviso': '2019-09-17T12:12:22.000-03:00',
            'tipo': 'Intimação',
            'processo': '00092272520124025101',
            'unidade': 'RJ000019S',
            'unidadenome': 'Juízo Substituto da 19ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000097',
            'dataaviso': '2019-09-17T12:12:22.000-03:00',
            'tipo': 'Intimação',
            'processo': '00290492420174025101',
            'unidade': 'RJ000019S',
            'unidadenome': 'Juízo Substituto da 19ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000098',
            'dataaviso': '2019-09-17T12:12:22.000-03:00',
            'tipo': 'Intimação',
            'processo': '50076103220184025101',
            'unidade': 'RJ005106F',
            'unidadenome': 'Juízo Federal do 6º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000099',
            'dataaviso': '2019-09-17T12:12:22.000-03:00',
            'tipo': 'Intimação',
            'processo': '00280317620154025120',
            'unidade': 'RJ002002S',
            'unidadenome': 'Juízo Substituto da 2ª VF de Nova Iguaçu',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000100',
            'dataaviso': '2019-09-17T12:12:22.000-03:00',
            'tipo': 'Intimação',
            'processo': '50357663020184025101',
            'unidade': 'RJ000027S',
            'unidadenome': 'Juízo Substituto da 27ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000101',
            'dataaviso': '2019-09-17T12:12:22.000-03:00',
            'tipo': 'Intimação',
            'processo': '50041231720194025102',
            'unidade': 'RJ000104F',
            'unidadenome': 'Juízo Federal da 4ª VF de Niterói',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000102',
            'dataaviso': '2019-09-17T12:12:22.000-03:00',
            'tipo': 'Intimação',
            'processo': '50002545920184025109',
            'unidade': 'RJ005901F',
            'unidadenome': 'Juízo Federal do 1º JEF de Resende',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000103',
            'dataaviso': '2019-09-17T12:12:23.000-03:00',
            'tipo': 'Intimação',
            'processo': '50258694120194025101',
            'unidade': 'RJ000022F',
            'unidadenome': 'Juízo Federal da 22ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000104',
            'dataaviso': '2019-09-17T12:12:23.000-03:00',
            'tipo': 'Intimação',
            'processo': '50182566720194025101',
            'unidade': 'RJ000021F',
            'unidadenome': 'Juízo Federal da 21ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000105',
            'dataaviso': '2019-09-17T12:12:23.000-03:00',
            'tipo': 'Intimação',
            'processo': '50445470720194025101',
            'unidade': 'RJ000021S',
            'unidadenome': 'Juízo Substituto da 21ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000106',
            'dataaviso': '2019-09-17T12:12:23.000-03:00',
            'tipo': 'Intimação',
            'processo': '50419766320194025101',
            'unidade': 'RJ000014F',
            'unidadenome': 'Juízo Federal da 14ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000107',
            'dataaviso': '2019-09-17T12:12:23.000-03:00',
            'tipo': 'Intimação',
            'processo': '50229160720194025101',
            'unidade': 'RJ000018F',
            'unidadenome': 'Juízo Federal da 18ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000108',
            'dataaviso': '2019-09-17T12:12:23.000-03:00',
            'tipo': 'Intimação',
            'processo': '00015549820104025117',
            'unidade': 'RJ001703F',
            'unidadenome': 'Juízo Federal da 3ª VF de São Gonçalo',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000109',
            'dataaviso': '2019-09-17T12:12:23.000-03:00',
            'tipo': 'Intimação',
            'processo': '00605475620184025117',
            'unidade': 'RJ001703S',
            'unidadenome': 'Juízo Substituto da 3ª VF de São Gonçalo',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000110',
            'dataaviso': '2019-09-17T12:12:24.000-03:00',
            'tipo': 'Intimação',
            'processo': '50272629820194025101',
            'unidade': 'RJ000005F',
            'unidadenome': 'Juízo Federal da 5ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000111',
            'dataaviso': '2019-09-17T12:12:24.000-03:00',
            'tipo': 'Intimação',
            'processo': '50004088920184025105',
            'unidade': 'RJ005501S',
            'unidadenome': 'Juízo Substituto do 1º JEF de Nova Friburgo',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000112',
            'dataaviso': '2019-09-17T12:12:24.000-03:00',
            'tipo': 'Intimação',
            'processo': '02082519020174025155',
            'unidade': 'RJ005501S',
            'unidadenome': 'Juízo Substituto do 1º JEF de Nova Friburgo',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000113',
            'dataaviso': '2019-09-17T12:12:24.000-03:00',
            'tipo': 'Intimação',
            'processo': '50001853920184025105',
            'unidade': 'RJ005501S',
            'unidadenome': 'Juízo Substituto do 1º JEF de Nova Friburgo',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000114',
            'dataaviso': '2019-09-17T12:12:24.000-03:00',
            'tipo': 'Intimação',
            'processo': '50057677220184025120',
            'unidade': 'RJ007003F',
            'unidadenome': 'Juízo Federal da 5ª VF de Nova Iguaçu',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000115',
            'dataaviso': '2019-09-17T12:12:24.000-03:00',
            'tipo': 'Intimação',
            'processo': '50002513720194025120',
            'unidade': 'RJ007003F',
            'unidadenome': 'Juízo Federal da 5ª VF de Nova Iguaçu',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000116',
            'dataaviso': '2019-09-17T12:12:24.000-03:00',
            'tipo': 'Intimação',
            'processo': '50064423120194025110',
            'unidade': 'RJ001005F',
            'unidadenome': 'Juízo Federal da 5ª VF de São João de Meriti',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000117',
            'dataaviso': '2019-09-17T12:12:25.000-03:00',
            'tipo': 'Intimação',
            'processo': '00269983520184025156',
            'unidade': 'RJ000602F',
            'unidadenome': 'Juízo Federal da 2ª VF de Petrópolis',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000118',
            'dataaviso': '2019-09-17T12:12:25.000-03:00',
            'tipo': 'Intimação',
            'processo': '50017798520184025106',
            'unidade': 'RJ000602F',
            'unidadenome': 'Juízo Federal da 2ª VF de Petrópolis',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000119',
            'dataaviso': '2019-09-17T12:12:25.000-03:00',
            'tipo': 'Intimação',
            'processo': '50410144020194025101',
            'unidade': 'RJ000037S',
            'unidadenome': 'Juízo Substituto da 13ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000120',
            'dataaviso': '2019-09-17T12:12:25.000-03:00',
            'tipo': 'Intimação',
            'processo': '50465312620194025101',
            'unidade': 'RJ000037F',
            'unidadenome': 'Juízo Federal da 13ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000121',
            'dataaviso': '2019-09-17T12:12:25.000-03:00',
            'tipo': 'Intimação',
            'processo': '50072088520184025121',
            'unidade': 'RJ005114F',
            'unidadenome': 'Juízo Federal do 14º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000122',
            'dataaviso': '2019-09-17T12:12:25.000-03:00',
            'tipo': 'Intimação',
            'processo': '01661650920164025101',
            'unidade': 'RJ000021S',
            'unidadenome': 'Juízo Substituto da 21ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000123',
            'dataaviso': '2019-09-17T12:12:25.000-03:00',
            'tipo': 'Citação',
            'processo': '50076416420194025118',
            'unidade': 'RJ006802S',
            'unidadenome': 'Juízo Substituto da 4ª VF de Duque de Caxias',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000124',
            'dataaviso': '2019-09-17T12:12:26.000-03:00',
            'tipo': 'Intimação',
            'processo': '50411853120184025101',
            'unidade': 'RJ000028S',
            'unidadenome': 'Juízo Substituto da 28ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000125',
            'dataaviso': '2019-09-17T12:12:26.000-03:00',
            'tipo': 'Citação',
            'processo': '50391939820194025101',
            'unidade': 'RJ000015S',
            'unidadenome': 'Juízo Substituto da 15ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000126',
            'dataaviso': '2019-09-17T12:12:26.000-03:00',
            'tipo': 'Citação',
            'processo': '50395481120194025101',
            'unidade': 'RJ000015S',
            'unidadenome': 'Juízo Substituto da 15ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000127',
            'dataaviso': '2019-09-17T12:12:26.000-03:00',
            'tipo': 'Citação',
            'processo': '50483604220194025101',
            'unidade': 'RJ000015S',
            'unidadenome': 'Juízo Substituto da 15ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000128',
            'dataaviso': '2019-09-17T12:12:26.000-03:00',
            'tipo': 'Intimação',
            'processo': '50000170720184025115',
            'unidade': 'RJ005501F',
            'unidadenome': 'Juízo Federal do 1º JEF de Nova Friburgo',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000129',
            'dataaviso': '2019-09-17T12:12:26.000-03:00',
            'tipo': 'Intimação',
            'processo': '50005480520184025112',
            'unidade': 'RJ001201F',
            'unidadenome': 'Juízo Federal da 1ª VF de Itaperuna',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000130',
            'dataaviso': '2019-09-17T12:12:26.000-03:00',
            'tipo': 'Intimação',
            'processo': '50021448720194025112',
            'unidade': 'RJ001201F',
            'unidadenome': 'Juízo Federal da 1ª VF de Itaperuna',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000131',
            'dataaviso': '2019-09-17T12:12:26.000-03:00',
            'tipo': 'Intimação',
            'processo': '50022219620194025112',
            'unidade': 'RJ001201F',
            'unidadenome': 'Juízo Federal da 1ª VF de Itaperuna',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000132',
            'dataaviso': '2019-09-17T12:12:27.000-03:00',
            'tipo': 'Intimação',
            'processo': '50023383020184025110',
            'unidade': 'RJ006002F',
            'unidadenome': 'Juízo Federal da 8ª VF de São João de Meriti',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000133',
            'dataaviso': '2019-09-17T12:12:27.000-03:00',
            'tipo': 'Intimação',
            'processo': '02108396120174025158',
            'unidade': 'RJ000802S',
            'unidadenome': 'Juízo Substituto da 2ª VF de São Pedro da Aldeia',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000134',
            'dataaviso': '2019-09-17T12:12:27.000-03:00',
            'tipo': 'Intimação',
            'processo': '50006088720184025108',
            'unidade': 'RJ000802F',
            'unidadenome': 'Juízo Federal da 2ª VF de São Pedro da Aldeia',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000135',
            'dataaviso': '2019-09-17T12:12:27.000-03:00',
            'tipo': 'Intimação',
            'processo': '50039493920184025103',
            'unidade': 'RJ005302S',
            'unidadenome': 'Juízo Substituto da 4ª VF de Campos',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000136',
            'dataaviso': '2019-09-17T12:12:27.000-03:00',
            'tipo': 'Intimação',
            'processo': '50040126420184025103',
            'unidade': 'RJ005302S',
            'unidadenome': 'Juízo Substituto da 4ª VF de Campos',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000137',
            'dataaviso': '2019-09-17T12:12:27.000-03:00',
            'tipo': 'Citação',
            'processo': '50506919420194025101',
            'unidade': 'RJ000019S',
            'unidadenome': 'Juízo Substituto da 19ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000138',
            'dataaviso': '2019-09-17T12:12:27.000-03:00',
            'tipo': 'Intimação',
            'processo': '00985361820164025101',
            'unidade': 'RJ000002F',
            'unidadenome': 'Juízo Federal da 2ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000139',
            'dataaviso': '2019-09-17T12:12:28.000-03:00',
            'tipo': 'Intimação',
            'processo': '02199062720174025101',
            'unidade': 'RJ000018F',
            'unidadenome': 'Juízo Federal da 18ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000140',
            'dataaviso': '2019-09-17T12:12:28.000-03:00',
            'tipo': 'Intimação',
            'processo': '00242114520184025152',
            'unidade': 'RJ005201S',
            'unidadenome': 'Juízo Substituto do 1º JEF de Niterói',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000141',
            'dataaviso': '2019-09-17T12:12:28.000-03:00',
            'tipo': 'Intimação',
            'processo': '50010785720194025117',
            'unidade': 'RJ001702F',
            'unidadenome': 'Juízo Federal da 2ª VF de São Gonçalo',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000142',
            'dataaviso': '2019-09-17T12:12:28.000-03:00',
            'tipo': 'Intimação',
            'processo': '50051412820194025117',
            'unidade': 'RJ001702F',
            'unidadenome': 'Juízo Federal da 2ª VF de São Gonçalo',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000143',
            'dataaviso': '2019-09-17T12:12:28.000-03:00',
            'tipo': 'Intimação',
            'processo': '01296642220174025101',
            'unidade': 'RJ000026F',
            'unidadenome': 'Juízo Federal da 26ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000144',
            'dataaviso': '2019-09-17T12:12:28.000-03:00',
            'tipo': 'Intimação',
            'processo': '50155530320184025101',
            'unidade': 'RJ000026F',
            'unidadenome': 'Juízo Federal da 26ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000145',
            'dataaviso': '2019-09-17T12:12:28.000-03:00',
            'tipo': 'Intimação',
            'processo': '50098257820184025101',
            'unidade': 'RJ000026F',
            'unidadenome': 'Juízo Federal da 26ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000146',
            'dataaviso': '2019-09-17T12:12:28.000-03:00',
            'tipo': 'Intimação',
            'processo': '01146084220174025167',
            'unidade': 'RJ006702F',
            'unidadenome': 'Juízo Federal do 2º JEF de São Gonçalo',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000147',
            'dataaviso': '2019-09-17T12:12:29.000-03:00',
            'tipo': 'Intimação',
            'processo': '50273602020184025101',
            'unidade': 'RJ005109F',
            'unidadenome': 'Juízo Federal do 9º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000148',
            'dataaviso': '2019-09-17T12:12:29.000-03:00',
            'tipo': 'Intimação',
            'processo': '50018464720184025107',
            'unidade': 'RJ000702S',
            'unidadenome': 'Juízo Substituto da 2ª VF de Itaboraí',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000149',
            'dataaviso': '2019-09-17T12:12:29.000-03:00',
            'tipo': 'Intimação',
            'processo': '00984536520174025101',
            'unidade': 'RJ000028S',
            'unidadenome': 'Juízo Substituto da 28ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000150',
            'dataaviso': '2019-09-17T12:12:29.000-03:00',
            'tipo': 'Citação',
            'processo': '50285629520194025101',
            'unidade': 'RJ000028S',
            'unidadenome': 'Juízo Substituto da 28ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000151',
            'dataaviso': '2019-09-17T12:12:29.000-03:00',
            'tipo': 'Intimação',
            'processo': '50068189020194025118',
            'unidade': 'RJ001802S',
            'unidadenome': 'Juízo Substituto da 2ª VF de Duque de Caxias',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000152',
            'dataaviso': '2019-09-17T12:12:29.000-03:00',
            'tipo': 'Intimação',
            'processo': '01358783420144025101',
            'unidade': 'RJ000012F',
            'unidadenome': 'Juízo Federal da 12ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000153',
            'dataaviso': '2019-09-17T12:12:29.000-03:00',
            'tipo': 'Intimação',
            'processo': '50325892420194025101',
            'unidade': 'RJ000020F',
            'unidadenome': 'Juízo Federal da 20ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000154',
            'dataaviso': '2019-09-17T12:12:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '50014775620184025106',
            'unidade': 'RJ000602F',
            'unidadenome': 'Juízo Federal da 2ª VF de Petrópolis',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000155',
            'dataaviso': '2019-09-17T12:12:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '00030994220194025101',
            'unidade': 'RJ000002S',
            'unidadenome': 'Juízo Substituto da 2ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000156',
            'dataaviso': '2019-09-17T12:12:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '00820923620184025101',
            'unidade': 'RJ000002F',
            'unidadenome': 'Juízo Federal da 2ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000157',
            'dataaviso': '2019-09-17T12:12:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '00018075220114025117',
            'unidade': 'RJ001703S',
            'unidadenome': 'Juízo Substituto da 3ª VF de São Gonçalo',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000158',
            'dataaviso': '2019-09-17T12:12:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '01181593420174025101',
            'unidade': 'RJ000012S',
            'unidadenome': 'Juízo Substituto da 12ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000159',
            'dataaviso': '2019-09-17T12:12:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '01056265720154025119',
            'unidade': 'RJ001901F',
            'unidadenome': 'Juízo Federal da 1ª VF de Barra do Piraí',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000160',
            'dataaviso': '2019-09-17T12:12:30.000-03:00',
            'tipo': 'Intimação',
            'processo': '01056490320154025119',
            'unidade': 'RJ001901S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Barra do Piraí',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000161',
            'dataaviso': '2019-09-17T12:12:31.000-03:00',
            'tipo': 'Intimação',
            'processo': '01056569220154025119',
            'unidade': 'RJ001901F',
            'unidadenome': 'Juízo Federal da 1ª VF de Barra do Piraí',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000162',
            'dataaviso': '2019-09-17T12:12:31.000-03:00',
            'tipo': 'Intimação',
            'processo': '01180823920154025119',
            'unidade': 'RJ001901F',
            'unidadenome': 'Juízo Federal da 1ª VF de Barra do Piraí',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000163',
            'dataaviso': '2019-09-17T12:12:31.000-03:00',
            'tipo': 'Intimação',
            'processo': '50028220520194025112',
            'unidade': 'RJ001201S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Itaperuna',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000164',
            'dataaviso': '2019-09-17T12:12:31.000-03:00',
            'tipo': 'Intimação',
            'processo': '50028238720194025112',
            'unidade': 'RJ001201S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Itaperuna',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000165',
            'dataaviso': '2019-09-17T12:12:31.000-03:00',
            'tipo': 'Intimação',
            'processo': '01204118220164025153',
            'unidade': 'RJ000201S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Campos',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000166',
            'dataaviso': '2019-09-17T12:12:31.000-03:00',
            'tipo': 'Intimação',
            'processo': '01091368220134025108',
            'unidade': 'RJ000801F',
            'unidadenome': 'Juízo Federal da 1ª VF de São Pedro da Aldeia',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000167',
            'dataaviso': '2019-09-17T12:12:31.000-03:00',
            'tipo': 'Intimação',
            'processo': '01653135320174025164',
            'unidade': 'RJ001401S',
            'unidadenome': 'Juízo Substituto da 1ª VF de Magé',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000168',
            'dataaviso': '2019-09-17T12:12:31.000-03:00',
            'tipo': 'Intimação',
            'processo': '50000336120184025114',
            'unidade': 'RJ001401F',
            'unidadenome': 'Juízo Federal da 1ª VF de Magé',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000169',
            'dataaviso': '2019-09-17T12:12:32.000-03:00',
            'tipo': 'Intimação',
            'processo': '50002630620184025114',
            'unidade': 'RJ001401F',
            'unidadenome': 'Juízo Federal da 1ª VF de Magé',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000170',
            'dataaviso': '2019-09-17T12:12:32.000-03:00',
            'tipo': 'Intimação',
            'processo': '50499907020184025101',
            'unidade': 'RJ000051F',
            'unidadenome': 'Juízo Federal da 6ª VF de Execução Fiscal do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000171',
            'dataaviso': '2019-09-17T12:12:32.000-03:00',
            'tipo': 'Intimação',
            'processo': '01324382520174025101',
            'unidade': 'RJ000059F',
            'unidadenome': 'Juízo Federal da 11ª VF de Execução Fiscal do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000172',
            'dataaviso': '2019-09-17T12:12:32.000-03:00',
            'tipo': 'Intimação',
            'processo': '01538046920174025118',
            'unidade': 'RJ001802F',
            'unidadenome': 'Juízo Federal da 2ª VF de Duque de Caxias',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000173',
            'dataaviso': '2019-09-17T12:12:32.000-03:00',
            'tipo': 'Intimação',
            'processo': '00557319420164025151',
            'unidade': 'RJ005114S',
            'unidadenome': 'Juízo Substituto do 14º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000174',
            'dataaviso': '2019-09-17T12:12:32.000-03:00',
            'tipo': 'Intimação',
            'processo': '50016053120184025121',
            'unidade': 'RJ005114F',
            'unidadenome': 'Juízo Federal do 14º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000175',
            'dataaviso': '2019-09-17T12:12:32.000-03:00',
            'tipo': 'Intimação',
            'processo': '50034803620184025121',
            'unidade': 'RJ005114F',
            'unidadenome': 'Juízo Federal do 14º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000176',
            'dataaviso': '2019-09-17T12:12:33.000-03:00',
            'tipo': 'Intimação',
            'processo': '50075994020184025121',
            'unidade': 'RJ005114S',
            'unidadenome': 'Juízo Substituto do 14º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201909170000177',
            'dataaviso': '2019-09-17T12:12:33.000-03:00',
            'tipo': 'Intimação',
            'processo': '50048524620194025101',
            'unidade': 'RJ005105S',
            'unidadenome': 'Juízo Substituto do 5º JEF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201910010000001',
            'dataaviso': '2019-10-01T17:14:45.000-03:00',
            'tipo': 'Intimação',
            'processo': '50191464020184025101',
            'unidade': 'RJ000020F',
            'unidadenome': 'Juízo Federal da 20ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          },
          {
            'idaviso': '201911140000001',
            'dataaviso': '2019-11-14T10:37:29.000-02:00',
            'tipo': 'Intimação',
            'processo': '00442084120164025101',
            'unidade': 'RJ000010F',
            'unidadenome': 'Juízo Federal da 10ª VF do Rio de Janeiro',
            'orgao': 'JFRJ',
            'sistema': 'br.jus.jfrj.eproc'
          }
        ],
        'status': [
          {
            'system': 'br.jus.jfrj.eproc'
          }
        ]
      })
    }
  },

  data () {
    return {
      // remover
      files: [],
      invalidFiles: [],
      avisos: undefined,

      editando: true,
      dataDeProtocolo: undefined,
      resumoPorData: [],
      filtroProtocolo: undefined,
      mostrarQuantidadePorData: undefined,
      quantidadePorData: [],
      arquivos: [],
      arquivosAProtocolar: undefined,
      arquivoCorrente: undefined,
      segredos: [{
        codigo: 0,
        nome: 'Não'
      }, {
        codigo: 1,
        nome: 'Sim'
      }],
      vueclipOptions: {
        url: this.$http.options.root + '/../upload',
        headers: {
          Authorization: AuthBL.getIdToken()
        },
        maxFilesize: {
          limit: 5,
          message: '{{ filesize }} tamanho é maior que {{ maxFilesize }}'
        },
        acceptedFiles: {
          extensions: ['application/pdf'],
          message: 'Só é permitido o envio de PDFs'
        }
      }
    }
  },

  computed: {
    resumoPorDataFiltrado: function () {
      console.log('recalculando filtrados...', this.modified)
      var a = this.resumoPorData
      a = UtilsBL.filtrarPorSubstring(a, this.filtroProtocolo)
      return a
    },

    arquivosComErro: function () {
      var a = []
      for (var i = 0; i < this.arquivos.length; i++) {
        if (this.arquivos[i].errormsg) a.push(this.arquivos[i])
      }
      return a
    }
  },

  methods: {
    fixAvisos: function (data) {
      for (var i = 0; i < data.status.length; i++) {
        if (data.status[i].errormsg) {
          if (this.errormsg === undefined) this.errormsg = ''
          else this.errormsg += '; '
          this.errormsg += data.status[i].system + ': ' + data.status[i].errormsg
        }
      }

      if (data.list) {
        var aAvisos = []
        for (i = 0; i < data.list.length; i++) {
          var aviso = {}
          UtilsBL.applyDefauts(aviso, data.list[i])
          aviso.errormsg = undefined
          aviso.checked = false
          aviso.disabled = false
          aviso.processoFormatado = ProcessoBL.formatarProcesso(aviso.processo)
          aAvisos.push(aviso)
        }

        const obj = aAvisos.reduce((accumulator, currentValue) => {
          if (accumulator[currentValue.processoFormatado] === undefined) accumulator[currentValue.processoFormatado] = []
          else console.log(currentValue.processoFormatado)
          accumulator[currentValue.processoFormatado].push(currentValue)
          return accumulator
        }, {})

        this.$set(this, 'avisos', obj)
      }
    },

    addedFileProxy: function (file) {
      var proc = ProcessoBL.formatarProcesso(file.name)

      this.arquivos.push({
        file: file,
        nome: file.name,
        processo: proc,
        bloq: !!proc,
        protocolado: undefined,
        status: undefined,
        validando: undefined,
        valido: undefined,
        sistema: undefined,
        errormsg: undefined,
        tipos: undefined,
        tipo: undefined,
        tipodescr: undefined,
        segredo: undefined,
        encerraprazos: undefined,
        identencerraprazos: undefined,
        sigilo: undefined,
        parte: undefined
      })
    },

    completeProxy: function (file, status, xhr) {
      var json = JSON.parse(xhr.response)
      var arq = this.arquivo(file)
      arq.size = json.size
      arq.id = json.id
      this.validarArquivo(arq)
      this.organizarArquivos()
    },

    hasErrorMessages: function (arr) {
      if (!arr) return false
      for (var i = 0; i < arr.length; i++) {
        if (arr[i].errorMessage) return true
      }
      return false
    },

    carregarResumo: function (data) {
      this.dataDeProtocolo = UtilsBL.formatDDMMYYYY(data)
      this.$http.get('peticao-intercorrente/listar?data=' + data, { block: true }).then(response => {
        this.resumoPorData = response.data.list
        for (var i = 0; i < this.resumoPorData.length; i++) {
          var r = this.resumoPorData[i]
          r.dataprotocoloFormatada = UtilsBL.formatJSDDMMYYYYHHMM(r.dataprotocolo)
        }
      }, error => {
        Bus.$emit('message', 'Erro', error.data.errormsg)
      })
    },

    voltarParaQuantidade: function () {
      this.dataDeProtocolo = undefined
      this.resumoPorData = undefined
    },

    carregarProtocoladosRecentemente: function () {
      this.$http.get('peticao-intercorrente/contar', { block: true }).then(response => {
        this.mostrarQuantidadePorData = true
        for (var i = 0; i < response.data.list.length; i++) {
          var qd = response.data.list[i]
          qd.dataFormatada = UtilsBL.formatDDMMYYYY(qd.data)
          this.quantidadePorData.push(qd)
        }
      }, error => {
        Bus.$emit('message', 'Erro', error.data.errormsg)
      })
    },

    voltarParaEdicao: function () {
      this.mostrarQuantidadePorData = undefined
      this.quantidadePorData.length = 0
    },

    validarArquivo: function (arq) {
      var a = arq
      if (a.processo) {
        a.status = 'Validando...'
        a.validando = true
        a.valido = false
        this.$http.get('processo/' + ProcessoBL.somenteNumeros(a.processo) + '/validar', { block: true }).then(response => {
          a.validando = false
          if (response.data.list && response.data.list.length > 0 && response.data.list[0].numero) {
            var d = response.data.list[0]
            a.sistema = d.sistema
            a.status = d.unidade + ' - ' + this.$parent.test.properties['balcaovirtual.' + d.sistema + '.name']
            a.valido = true
            this.$http.get('processo/' + ProcessoBL.somenteNumeros(a.processo) + '/peticao-intercorrente/validar?sistema=' + a.sistema, { block: true }).then(response => {
              var d = response.data
              a.tipos = d.tipos
              a.identencerraprazos = d.identencerraprazos
              a.sigilo = d.sigilo
              a.parte = d.parte
              if (a.identencerraprazos === undefined) a.encerraprazos = false
            }, error => {
              a.validando = false
              a.valido = false
              a.errormsg = error.data.errormsg
              a.status = undefined
              a.sistema = undefined
            })
          } else {
            a.status = undefined
            a.errormsg = 'Processo não encontrado'
          }
        }, error => {
          a.validando = false
          a.valido = false
          a.errormsg = error.data.errormsg
        })
      }
    },

    enviarPeticao: function (item) {
      Bus.$emit('prgCaption', 'Enviando ' + item.arq.processo)
      this.$http.post('processo/' + ProcessoBL.somenteNumeros(item.arq.processo) + '/peticionar', {
        sistema: item.arq.sistema,
        tipopeticao: item.arq.tipo,
        nivelsigilo: item.arq.segredo,
        encerraprazos: item.arq.encerraprazos ? item.arq.identencerraprazos : undefined,
        pdfs: item.pdfs
      }).then(response => {
        for (var i = item.index; i <= item.indexFinal; i++) {
          this.arquivos[i].status = response.data.status
          this.arquivos[i].protocolado = true
        }
        UtilsBL.logEvento('peticionamento', 'enviar', 'petição intercorrente')
        Bus.$emit('prgNext')
      }, error => {
        for (var i = item.index; i <= item.indexFinal; i++) {
          this.arquivos[i].errormsg = error.data.errormsg
        }
        Bus.$emit('prgNext')
      })
    },

    // Falta criar callback de retorno
    peticionar: function () {
      this.editando = false
      var lote = []

      for (var i = 0; i < this.arquivos.length; i++) {
        var arq = this.arquivos[i]
        if (arq.protocolado) continue
        arq.errormsg = undefined
        arq.protocolado = undefined
        var r = {
          arq: arq,
          index: i,
          indexFinal: i,
          pdfs: arq.id
        }
        lote.push(r)
        while (i < this.arquivos.length - 1) {
          if (!this.arquivos[i + 1].anexo) break
          i++
          r.indexFinal = i
          r.pdfs += ',' + this.arquivos[i].id
        }
      }
      var func = this.enviarPeticao
      Bus.$emit('prgStart', 'Protocolando Petições Intercorrentes', lote.length, function (i) { func(lote[i]) }, this.organizarArquivos)
    },

    exibirAviso: function (aviso) {
      this.aviso = aviso
    },

    marcarTodos: function () {
      var docs = this.filtrados
      for (var i = 0; i < docs.length; i++) {
        var doc = docs[i]
        if (!doc.disabled) doc.checked = this.todos
      }
    },

    printPdf: function (url) {
      var iframe = this._printIframe
      if (!this._printIframe) {
        iframe = this._printIframe = document.createElement('iframe')
        document.body.appendChild(iframe)

        iframe.style.display = 'none'
        iframe.onload = function () {
          setTimeout(function () {
            iframe.focus()
            iframe.contentWindow.print()
          }, 1)
        }
      }

      iframe.src = url
    },

    imprimirArquivo: function (arquivo, lote) {
      console.log('vai imprimir', url)
      this.errormsg = undefined
      if (lote) Bus.$emit('prgCaption', 'Imprimindo ' + arquivo.nome)

      var url = this.$http.options.root + '/arquivo-temporario/' + arquivo.id
      console.log(url)

      var iframe = document.createElement('iframe')
      document.body.appendChild(iframe)
      iframe.style.width = '0px'
      iframe.style.height = '0px'
      iframe.onload = function () {
        console.log('carregou')
        setTimeout(function () {
          console.log('imprimindo...')
          iframe.focus()
          iframe.contentWindow.print()
          setTimeout(function () {
            if (lote) Bus.$emit('prgNext')
            setTimeout(function () {
              console.log('vai fechar...')
              iframe.remove()
              console.log('fechou')
            }, 60000)
          }, 2000)
        }, 1)
      }
      iframe.src = url

      // setTimeout(function () {
      //   var w = window.open(url, '_blank')
      //   window.focus()
      //   setTimeout(function () {
      //     console.log('carregou')
      //     setTimeout(function () {
      //       console.log('imprimindo...')
      //       w.print()
      //       setTimeout(function () {
      //         console.log('vai fechar')
      //         w.close()
      //         UtilsBL.logEvento('peticao-inicial', 'imprimir')
      //         if (lote) Bus.$emit('prgNext')
      //       }, 100)
      //     }, 500)
      //   }, 5000)
      // }, 500)
    },

    imprimirArquivosComErro: function () {
      var a = this.arquivosComErro
      Bus.$emit('prgStart', 'Imprimindo Arquivos', a.length, (i) => this.imprimirArquivo(a[i], true))
    },

    limpar: function () {
      this.editando = true
      this.arquivos.length = 0
    },

    imprimir: function () {
      window.print()
    },

    exibirProcessosMultiplos: function (arq) {
      this.$refs.processosMultiplos.show()
      this.arquivoCorrente = arq
    },

    multiplosProcessos: function (arr) {
      var arq = this.arquivoCorrente
      var i
      if (!arr || arr.length === 0) return
      arq.processo = ProcessoBL.formatarProcesso(ProcessoBL.somenteNumeros(arr[0]))
      for (i = 1; i < arr.length; i++) {
        var newArq = {
          file: arq.file,
          nome: arq.nome,
          processo: ProcessoBL.formatarProcesso(ProcessoBL.somenteNumeros(arr[i])),
          bloq: arq.bloq,
          perc: arq.perc,
          size: arq.size,
          id: arq.id,
          status: undefined
        }
        this.arquivos.push(newArq)
      }
      this.organizarArquivos()
      for (i = 0; i < this.arquivos.length; i++) {
        var a = this.arquivos[i]
        if (a.file === arq.file) {
          a.status === undefined
          this.validarArquivo(a)
        }
      }
    },

    alterarArquivo: function (arq) {
      var m = reProc.exec(arq.processo)
      if (!m) {
        arq.status = 'Número de processo inválido'
        this.organizarArquivos()
        return
      }
      arq.processo = ProcessoBL.formatarProcesso(ProcessoBL.somenteNumeros(arq.processo))
      this.validarArquivo(arq)
      this.organizarArquivos()
    },

    removerArquivo: function (arq) {
      for (var i = 0; i < this.arquivos.length; i++) {
        if (arq === this.arquivos[i]) this.arquivos.splice(i, 1)
      }
      this.organizarArquivos()
    },

    descricaoTipoPorCodigo: function (arq, tipo) {
      for (var i = 0; i < arq.tipos.length; i++) {
        if (arq.tipos[i].id === tipo) {
          return arq.tipos[i].descricao
        }
      }
    },

    selecionarTipo: function (arq, tipo) {
      arq.tipodescr = this.descricaoTipoPorCodigo(arq, tipo)
      for (var i = 0; i < this.arquivos.length; i++) {
        var a = this.arquivos[i]
        if (a !== arq && a.sistema === arq.sistema && !a.tipo) {
          a.tipo = tipo
          a.tipodescr = arq.tipodescr
        }
      }
    },

    selecionarSegredo: function (arq, segredo) {
      for (var i = 0; i < this.arquivos.length; i++) {
        var a = this.arquivos[i]
        if (a !== arq && a.segredo === undefined) a.segredo = segredo
      }
    },

    selecionarEncerraPrazos: function (arq, encerraprazos) {
      for (var i = 0; i < this.arquivos.length; i++) {
        var a = this.arquivos[i]
        if (a !== arq && a.encerraprazos === undefined) a.encerraprazos = encerraprazos
      }
    },

    organizarArquivos: function () {
      this.arquivosAProtocolar = 0
      this.arquivos.sort(function (a, b) {
        if (a.processo && !b.processo) return -1
        if (!a.processo && b.processo) return 1
        if (a.processo !== b.processo) return a.processo < b.processo ? -1 : 1
        // if (a.bloq !== b.bloq)
        // return a.bloq ? -1 : 1
        if (a.nome !== b.nome) { return a.nome.replace('.pdf', '') < b.nome.replace('.pdf', '') ? -1 : 1 }
        return 0
      })

      var arq = { odd: false }

      for (var i = 0; i < this.arquivos.length; i++) {
        var a = this.arquivos[i]
        a.anexo = a.processo !== undefined && a.processo === arq.processo
        if (a.anexo) {
          arq.rowspan = arq.rowspan + 1
          a.tipo = arq.tipo
          a.segredo = arq.segredo
          a.identencerraprazos = arq.identencerraprazos
          a.sigilo = arq.sigilo
          a.parte = arq.parte
          a.odd = arq.odd
        } else {
          a.rowspan = 1
          a.odd = !arq.odd
          arq = a
        }
        if (a.protocolado !== true && !a.anexo) this.arquivosAProtocolar++
      }
    },

    arquivo: function (file) {
      for (var j = 0; j < this.arquivos.length; j++) {
        if (this.arquivos[j].file === file) return this.arquivos[j]
      }
    },

    isAllValid: function () {
      for (var j = 0; j < this.arquivos.length; j++) {
        if (this.arquivos[j].file.status !== 'success') return false
        if (!this.arquivos[j].processo) return false
        if (!this.arquivos[j].tipo) return false
        if (this.arquivos[j].segredo === undefined) return false
        if (this.arquivos[j].encerraprazos === undefined) return false
      }
      return true
    }
  }
}
</script>

<style scoped>
.protocolos-header {
  font-size: 150%;
  padding-bottom: 0.5rem;
}

.unbreakable {
  white-space: nowrap;
  word-break: keep-all;
  hyphens: none;
}

@media print {
  .table-peticao {
    font-size: 10pt;
  }
  .table-protocolo {
    font-size: 8pt;
  }
}
</style>
