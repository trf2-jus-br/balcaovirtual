<template>
  <div class="container-fluid content">
    <div class="row">
      <div class="col-md-12">
        <h4 class="text-center mt-3 mb-3">Lista de Processos</h4>
      </div>
      <div class="col col-sm-12" v-if="versionTRF2DownloadChromeExtension === '0'">
        <p class="alert alert-danger">
          <strong>Atenção!</strong> Não foi possível detectar a extensão do Chrome que é utilizada para realizar downloads em lote. Ela pode ser instalada acessando a página da
          <a href="https://chrome.google.com/webstore/detail/trf2-download-manager/komegelldppbjndifhabfpjpddjaocfa">
            <u>Chrome Web Store</u>
          </a>. Também verifique na Lista de Extensões (Menu/Mais ferramentas/Extensões) se 'TRF2 Download Manager' está presente e ativa.
        </p>
      </div>

    </div>

    <div class="row mb-3 d-print-none">
      <div class="col-sm-auto">
        <div class="btn-group">
          <label class="btn" :class="{'active btn-primary': pasta === 'encontrado',  'btn-outline-primary': pasta !== 'encontrado'}">
            <input v-show="false" type="radio" v-model="pasta" value="encontrado" autocomplete="off">
            <span class="fa fa-inbox"></span> Encontrados
          </label>
          <label class="btn btn-outline-primary" :class="{'active btn-primary': pasta === 'recente', 'btn-outline-primary': pasta !== 'recente'}">
            <input v-show="false" type="radio" v-model="pasta" value="recente" autocomplete="off">
            <span class="fa fa-history"></span> Recentes
          </label>
          <label class="btn btn-outline-primary" :class="{'active btn-primary': pasta === 'favorito', 'btn-outline-primary': pasta !== 'favorito'}">
            <input v-show="false" type="radio" v-model="pasta" value="favorito" autocomplete="off">
            <span class="fa fa-star"></span> Favoritos
          </label>
        </div>
      </div>

      <div class="col-sm-auto mr-sm-auto">
        <div class="input-group">
          <div class="input-group-addon">&#128269;</div>
          <input type="text" class="form-control" placeholder="Filtrar" v-model="filtro" ng-model-options="{ debounce: 200 }">
        </div>
      </div>
      <div class="col-sm-auto">
        <div class="btn-group btn-block" role="group">
          <button id="btnGroupDrop1" type="button" class="btn btn-secondary dropdown-toggle btn-block" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Avançado</button>
          <div class="dropdown-menu" aria-labelledby="btnGroupDrop1">
            <a class="dropdown-item" @click="exibirProcessosMultiplos">Inserir Múltiplos Processos</a>
          </div>
        </div>
      </div>
      <div class="col-sm-auto">
        <button type="button" @click="baixarEmLote()" class="btn btn-primary ml-1" title="Inserir este PDF em múltiplos processos ">
          Baixar Completo&nbsp;&nbsp
          <span class="badge badge-pill badge-warning ">{{filtradosMarcadosDigitaisEAcessiveis.length}}</span>
        </button>
      </div>
    </div>

    <div class="row" v-if="filtrados.length == 0">
      <div class="col col-sm-12">
        <p class="alert alert-warning">
          <strong>Atenção!</strong> Nenhum processo na lista.
        </p>
      </div>
    </div>

    <div class="row" v-if="filtrados.length > 0">
      <div class="col-sm-12">
        <table class="table table-striped table-sm table-responsive">
          <thead class="thead-inverse">
            <tr>
              <th style="text-align: center">
                <input type="checkbox" id="progress_checkall" name="progress_checkall" v-model="todos" @change="marcarTodos()"></input>
              </th>
              <th>Processo</th>
              <th>Órgão</th>
              <th>Unidade</th>
              <th>Suporte</th>
              <th>Segredo</th>
              <th>Status</th>
              <th style="text-align: center"></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="p in filtrados">
              <td style="text-align: center">
                <input type="checkbox" v-model="p.checked" :disabled="p.disabled"></input>
              </td>
              <td>
                <span class=" unbreakable ">
                  <router-link :to="{name: 'Processo', params: {numero: p.numero}}" target="_blank ">{{p.numero}}</router-link>
                </span>
              </td>
              <td>{{p.orgao}}</td>
              <td>{{p.unidade}}</td>
              <td>{{p.digitalFormatado}}</td>
              <td>{{p.segredoFormatado}}</td>
              <td class="status-td ">
                <span v-if="p.state=='ready' ">Preparado</span>
                <span v-if="p.state=='set' ">Solicitado</span>
                <span v-if="p.state=='go' ">Iniciado</span>
                <span v-if="p.state=='in_progress' &amp;&amp; p.perc===undefined ">Aguardando...</span>
                <span v-if="p.state=='in_progress' &amp;&amp; p.perc !==undefined ">{{p.perc}}%</span>
                <span class="green" v-if="p.state=='complete' ">Baixado</span>
                <span v-if="p.errormsg" :class="{red: true} ">Erro {{p.errormsg}}
                </span>
              </td>

              <td align="right ">
                <a v-if="!p.favorito" href="" @click.prevent="sinalizar(p, {favorito: true})">
                  <span class="fa fa-star-o icone-em-linha"></span>
                </a>
                <a v-if="p.favorito" href="" @click.prevent="sinalizar(p, {favorito: false})">
                  <span class="fa fa-star icone-em-linha"></span>
                </a>
                <template v-if="pasta !== 'favorito'">
                  <a href="" @click.prevent="remover(p)">
                    <span class="fa fa-remove icone-em-linha"></span>
                  </a>
                </template>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    <div class="row ">
      <div class="col-sm-12" style="padding-top: 1em; ">
      </div>
    </div>
    <processo-multiplos ref="processosMultiplos" :show.sync="exibirProcessoMultiplos" @ok="acrescentarProcessosNaLista "></processo-multiplos>
  </div>
</template>

<script>
import ProcessoBL from '../bl/processo.js'
import UtilsBL from '../bl/utils.js'
import ProcessoMultiplos from './ProcessoMultiplos'
import { Bus } from '../bl/bus.js'

/* global chrome */
export default {
  components: {
    ProcessoMultiplos
  },

  mounted () {
    var i

    setTimeout(() => {
      this.versionTRF2DownloadChromeExtension = document.getElementById('trf2-download-chrome-extension-active').value
    }, 500)

    this.errormsg = undefined

    setTimeout(() => {
      if (this.$route.params && this.$route.params.processos) {
        this.pasta = 'encontrado'
        for (i = 0; i < this.$route.params.processos.length; i++) {
          var p = this.fixProcesso(this.$route.params.processos[i])
          p.encontrado = true
          this.processos.push(p)
        }
      }
      this.$http.get('processo/listar-sinalizados', { block: true }).then(
        response => {
          var list = response.data.list
          for (var i = 0; i < list.length; i++) {
            var s = list[i]
            var p
            for (var j = 0; j < this.processos.length; j++) {
              if (s.numero === this.processos[j].numero) {
                p = this.processos[j]
                break
              }
            }
            if (p) {
              this.processos[j].recente = s.recente
              this.processos[j].favorito = s.favorito
            } else {
              this.processos.push(this.fixProcesso(s))
            }
          }
          this.validarEmLote()
        },
        error => UtilsBL.errormsg(error, this))
    })
  },

  data () {
    return {
      pasta: undefined,
      filtro: undefined,
      todos: true,
      processos: [],
      versionTRF2DownloadChromeExtension: undefined,
      downloadExtensionId: 'komegelldppbjndifhabfpjpddjaocfa',
      map: {},
      exibirProcessoMultiplos: false
    }
  },

  computed: {
    filtrados: function () {
      console.log('recalculando filtrados...', this.modified)
      var a = this.processos
      a = UtilsBL.filtrarPorSubstring(a, this.filtro)
      a = a.filter(o => !!o[this.pasta])
      return a
    },

    filtradosEMarcados: function () {
      return this.filtrados.filter(function (item) {
        return item.checked
      })
    },
    filtradosMarcadosDigitaisEAcessiveis: function () {
      return this.filtradosEMarcados.filter(function (item) {
        return item.digital === true && item.segredodejustica === false
      })
    }
  },
  methods: {
    fixProcesso: function (p) {
      UtilsBL.applyDefauts(p, {
        numero: undefined,
        orgao: undefined,
        unidade: undefined,
        segredodejustica: undefined,
        segredodejusticadesistema: undefined,
        segredodejusticaabsoluto: undefined,
        segredoFormatado: undefined,
        digital: undefined,
        digitalFormatado: undefined,
        validade: undefined,
        checked: true,
        disabled: false,
        state: undefined,
        errormsg: undefined,
        perc: undefined,
        encontrado: undefined,
        favorito: undefined,
        recente: undefined
      })
      if (p.numero !== undefined) p.numero = ProcessoBL.formatarProcesso(ProcessoBL.somenteNumeros(p.numero))
      if (p.digital !== undefined) p.digitalFormatado = p.digital ? 'Digital' : 'Físico'
      if (p.segredodejustica !== undefined) {
        p.segredoFormatado = p.segredodejusticaabsoluto ? 'Absoluto' : p.segredodejusticadesistema ? 'Sistema' : p.segredodejustica ? 'Segredo' : 'Público'
      }
      return p
    },

    validarEmLote: function () {
      var a = this.processos.filter(function (item) {
        return !item.validado
      })
      Bus.$emit('prgStart', 'Validando Processos', a.length, (i) => this.validarProcesso(a[i], true))
    },

    validarProcesso: function (processo, lote) {
      if (lote) Bus.$emit('prgCaption', 'Validando ' + processo.numero)
      var n = ProcessoBL.somenteNumeros(processo.numero)
      this.$http.get('processo/' + n + '/validar', { block: !lote }).then(
        (response) => {
          UtilsBL.overrideProperties(processo, response.data)
          processo.validado = true
          this.fixProcesso(processo)
          if (lote) Bus.$emit('prgNext')
        },
        (error) => {
          processo.checked = false
          processo.disabled = true
          processo.errormsg = 'Não foi possível obter informações sobre o processo. (' + error.data.errormsg + ')'
          if (lote) Bus.$emit('prgNext')
        })
    },

    baixarEmLote: function () {
      var processos = this.filtradosMarcadosDigitaisEAcessiveis
      if (processos.length > 0) {
        this.errormsg = undefined
        this.obterJwt(processos, 0)
      }
    },

    obterJwt: function (processos, i) {
      processos[i].jwt = undefined
      processos[i].state = undefined
      processos[i].perc = undefined
      this.$http.get('processo/' + ProcessoBL.somenteNumeros(processos[i].numero) + '/pdf?orgao=' + processos[i].orgao).then(response => {
        if (i + 1 < processos.length) this.$nextTick(() => this.obterJwt(processos, i + 1))
        else this.$nextTick(this.continuarBaixando)
        processos[i].jwt = response.data.jwt
        processos[i].state = 'ready'
      }, error => {
        if (i + 1 < processos.length) this.obterJwt(processos, i + 1)
        processos[i].errormsg = error.data.errormsg
      })
    },

    baixando: function () {
      var c = 0
      var processos = this.filtradosEMarcados
      for (var i = 0; i < processos.length; i++) {
        if (processos[i].jwt && ['set', 'go', 'in_progress'].indexOf(processos[i].state) >= 0) c++
      }
      return c
    },

    continuarBaixando: function () {
      if (this.baixando() === 0 && !this.baixarProximo()) return
      this.atualizar()
    },

    atualizar: function () {
      chrome.runtime.sendMessage(this.downloadExtensionId, {
        command: 'updates'
      }, response => {
        setTimeout(this.continuarBaixando, 500)
        if (!response) return
        if (response.success) {
          var updates = response.data
          for (var id in updates) {
            if (!updates.hasOwnProperty(id)) continue
            this.atualizarProcesso(updates[id])
          }
        } else {
          console.log('error updating')
        }
      })
    },

    atualizarProcesso: function (update) {
      var processo = this.map[update.id]
      if (update.state && update.state.current) processo.state = update.state.current
      if (update.error) processo.errormsg = update.error.current
      if (update.received && update.total) processo.perc = Math.round(update.received / update.total * 100)
      if (update.state && update.state.current !== 'in_progress') delete processo.perc
      if (update.state && update.state.current === 'complete') processo.checked = false
    },

    baixarProximo: function () {
      var processos = this.filtradosEMarcados
      for (var i = 0; i < processos.length; i++) {
        if (processos[i].jwt && processos[i].state === 'ready') {
          processos[i].state = 'set'
          this.baixar(processos[i])
          return true
        }
      }
      return false
    },

    baixar: function (processo) {
      var url = this.$http.options.root + '/download/' + processo.jwt + '/' + processo.numero + '.pdf?disposition=attachment'
      chrome.runtime.sendMessage(this.downloadExtensionId, {
        command: 'download',
        url: url,
        filename: processo.numero + '.pdf'
      }, response => {
        if (!response.success) processo.erromsg = response.data.erromsg

        processo.state = 'go'
        processo.downloadId = response.data.id
        this.$set(this.map, processo.downloadId, processo)
      })
      UtilsBL.logEvento('consulta-processual', 'mostrar pdf completo', 'em lote')
    },

    remover: function (p) {
      if (this.pasta === 'encontrado') {
        p.encontrado = undefined
        this.removerProcessoDesnecessario(p)
      }
      if (this.pasta === 'recente') this.sinalizar(p, { recente: false })
      if (this.pasta === 'favorito') this.sinalizar(p, { favorito: false })
    },

    removerProcessoDesnecessario: function (p) {
      if (!p.encontrado && !p.recente && !p.favorito) {
        for (var i = 0; i < this.processos.length; i++) {
          if (p === this.processos[i]) this.processos.splice(i, 1)
        }
      }
    },

    exibirProcessosMultiplos: function () {
      this.$refs.processosMultiplos.show()
    },

    acrescentarProcessosNaLista: function (arr) {
      if (!arr || arr.length === 0) return
      this.pasta = 'encontrado'
      for (var i = 0; i < arr.length; i++) {
        var p = this.fixProcesso({
          numero: arr[i],
          encontrado: true
        })
        this.processos.push(p)
      }
      this.validarEmLote()
    },

    marcarTodos: function () {
      var docs = this.filtrados
      for (var i = 0; i < docs.length; i++) {
        var doc = docs[i]
        doc.checked = this.todos
      }
    },

    sinalizar: function (p, sinais, lote) {
      this.errormsg = undefined
      if (sinais.favorito !== undefined) p.favorito = sinais.favorito
      if (lote) Bus.$emit('prgCaption', 'Sinalizando ' + p.numero)

      this.$http.post('processo/' + ProcessoBL.somenteNumeros(p.numero) + '/sinalizar', sinais, { block: !lote }).then(response => {
        var d = response.data
        p.favorito = d.processo.favorito
        p.recente = d.processo.recente
        this.removerProcessoDesnecessario(p)
        UtilsBL.logEvento('processo', 'sinalizar')
        if (lote) Bus.$emit('prgNext')
      }, error => {
        p.errormsg = error.data.errormsg
        if (lote) Bus.$emit('prgNext')
      })
    }

    // confirmarEmLote: function () {
    //   var a = this.filtradosEMarcados
    //   Bus.$emit('prgStart', 'Confirmando Intimações/Citações', a.length, (i) => this.confirmarAviso(a[i], ))
    // }

  }
}
</script>
