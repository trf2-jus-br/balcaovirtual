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
  
      <div class="col col-sm-12" v-if="filtrados.length == 0">
        <p class="alert alert-warning">
          <strong>Atenção!</strong> Nenhum processo na lista.
        </p>
      </div>
    </div>
  
    <div class="row" v-if="filtrados.length > 0">
      <div class="col-sm-12">
        <table class="table table-striped table-sm">
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
                <button type="button" @click="remover(p)" class="btn btn-sm btn-outline-danger ">&#x274C;</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    <div class="row ">
      <div class="col-sm-12" style="padding-top: 1em; ">
        <button type="button" @click="exibirProcessosMultiplos" class="btn btn-primary ml-1" title="Inserir múltiplos processos ">Inserir Processos</button>
        <button type="button" @click="baixarEmLote()" class="btn btn-primary ml-1" title="Inserir este PDF em múltiplos processos ">
          Baixar Completo&nbsp;&nbsp
          <span class="badge badge-pill badge-warning ">{{filtradosMarcadosDigitaisEAcessiveis.length}}</span>
        </button>
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
    if (this.$route.params && this.$route.params.processos) {
      for (i = 0; i < this.$route.params.processos.length; i++) {
        var p = this.fixProcesso(this.$route.params.processos[i])
        this.processos.push(p)
      }
      this.validarEmLote()
    }
  },

  data () {
    return {
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
      // var outmap = this.outlineMap
      // a = a.filter(item => outmap[item.filtro].ativo)
      // a = UtilsBL.filtrarPorSubstring(a, this.filtro)
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
        perc: undefined
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
      for (var i = 0; i < this.processos.length; i++) {
        if (p === this.processos[i]) this.processos.splice(i, 1)
      }
    },

    exibirProcessosMultiplos: function () {
      this.$refs.processosMultiplos.show()
    },

    acrescentarProcessosNaLista: function (arr) {
      if (!arr || arr.length === 0) return
      for (var i = 0; i < arr.length; i++) {
        var p = this.fixProcesso({
          numero: arr[i]
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
    }
  }
}
</script>
