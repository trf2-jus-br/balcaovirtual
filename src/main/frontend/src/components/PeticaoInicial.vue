<template>
  <div class="container-fluid content">
    <div class="row">
      <div class="col-md-12">
        <h4 class="text-center mt-3 mb-3">Petição Inicial</h4>
      </div>
    </div>

    <div class="row pb-4" v-show="arquivos.length == 0">
      <div class="col-md-12">
        <vue-clip :options="vueclipOptions" :on-added-file="addedFileProxy" :on-complete="completeProxy">
          <template slot="clip-uploader-action">
            <div>
              <div class="dz-message drop-box">
                Arraste os arquivos que compoem a petição inicial e solte eles nesta área, ou clique aqui para selecioná-los.<br />Use o nome dos PDFs para ordenar as peças, por exemplo: 01-Termo.pdf, 02-Identidade.pdf, etc.
                <br />Cada PDF está limitado a 5MB.
              </div>
            </div>
          </template>

          <template slot="clip-uploader-body" scope="props">
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

    <div class="row" v-if="arquivos.length > 0">
      <div class="col-sm-6">
        <h5 class="mt-3 mb-2">Arquivos</h5>
      </div>
      <div class="col-sm-6" style="height: 100%">
        <button type="button" @click="adicionarArquivo()" class="btn btn-info btn-sm mt-2 float-right">Adicionar Arquivo
        </button>
      </div>
    </div>

    <div class="row" v-if="arquivos.length > 0">
      <div class="col-sm-12">
        <table class="table table-peticao">
          <thead class="thead-inverse">
            <tr>
              <th>Tipo</th>
              <th>Segredo</th>
              <th>Descrição</th>
              <th>Arquivo</th>
              <th>Status</th>
              <th style="text-align: center" v-if="editando"></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="f in arquivos" :class="{odd: f.odd}">
              <td>
                <select style="min-width: 8em;" v-if="editando" class="form-control mr-sm-2" v-model="f.tipo" :disabled="f.protocolado" @change="selecionarTipo(f, f.tipo)">
                  <option v-for="tipo in tipospeca" :value="tipo.id">{{tipo.descricao}}</option>
                  <option disabled hidden selected value=""> [Selecionar]</option>
                </select>
                <span v-if="!editando">{{f.tipodescr}}</span>
              </td>

              <td>
                <select style="min-width: 4em;" v-if="editando" class="form-control mr-sm-2" v-model="f.segredo" :disabled="f.protocolado" @change="selecionarSegredo(f, f.segredo)">
                  <option v-for="segredo in segredos" :value="segredo.codigo">{{segredo.nome}}</option>
                  <option disabled hidden selected value="">[Selecionar]</option>
                </select>
                <span v-if="!editando">{{f.segredo ? 'Sim' : 'Não'}}</span>
              </td>

              <td>
                <div class="input-group">
                  <input v-if="editando" type="text" class="form-control" style="min-width: 16em;" placeholder="Descrição" v-model="f.descricao" @input="alterarArquivo(f)" :disabled="f.bloq || f.protocolado">
                  <span v-if="!editando">{{f.descricao}}</span>
                </div>
              </td>

              <td>
                <a @click="view(doc)">
                  <a :href="'api/v1/arquivo-temporario/' + f.id" target="_blank">{{f.nome}}</span>
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

    <div class="row">
      <div class="col-md-12">
        <h5 class="mt-3 mb-2">Dados Básicos</h5>
      </div>
    </div>
    <div class="pt-3 pb-3 pl-3 pr-3" style="background-color: #f7f7f9">
      <div class="row">
        <div class="form-group col-md-2">
          <label for="orgao">Órgão</label>
          <select id="orgao" class="form-control" v-model="orgao">
            <option v-for="l in orgaos" :value="l.id">{{l.descricao}}</option>
            <option disabled hidden selected value="">[Selecionar]</option>
          </select>
        </div>
        <div class="form-group col-md-3">
          <label for="localidade">Localidade</label>
          <select id="localidade" class="form-control" v-model="localidade">
            <option v-for="l in localidades" :value="l.id">{{l.descricao}}</option>
            <option disabled hidden selected value="">[Selecionar]</option>
          </select>
        </div>
        <div class="form-group col-md-2">
          <label for="especialidade">Especialidade</label>
          <select class="form-control" id="especialidade">
            <option>1</option>
            <option>2</option>
            <option>3</option>
            <option>4</option>
            <option>5</option>
          </select>
        </div>
        <div class="form-group col-md-3">
          <label for="classe">Classe</label>
          <select class="form-control" id="classe">
            <option>1</option>
            <option>2</option>
            <option>3</option>
            <option>4</option>
            <option>5</option>
          </select>
        </div>

        <div class="form-group col-md-2">
          <label for="valor">Valor da Causa</label> <input type="text" class="form-control" id="valor" aria-describedby="valorDaCausa" placeholder="0,00">
        </div>
        <div class="form-check col-md-3">
          <label class="form-check-label"> <input type="checkbox" class="form-check-input"> Segredo de Justiça
          </label>
        </div>
        <div class="form-check col-md-3">
          <label class="form-check-label"> <input type="checkbox" class="form-check-input"> Justiça Gratuita
          </label>
        </div>
        <div class="form-check col-md-3">
          <label class="form-check-label"> <input type="checkbox" class="form-check-input"> Tutela Liminar/Antecipada
          </label>
        </div>
        <div class="form-check col-md-3">
          <label class="form-check-label"> <input type="checkbox" class="form-check-input"> Prioridade de Idoso
          </label>
        </div>
      </div>
    </div>

    <div class="row mt-3">
      <div class="col-sm-6">
        <h5 class="mt-3 mb-2">Partes</h5>
      </div>
      <div class="col-sm-6" style="height: 100%">
        <button type="button" @click="adicionarParte()" class="btn btn-info btn-sm mt-2 float-right">Adicionar Parte</button>
      </div>
    </div>
    <div class="row">
      <div class="col-sm-12">
        <table class="table table-peticao">
          <thead class="thead-inverse">
            <tr>
              <th>Polo</th>
              <th>Tipo</th>
              <th>Nome</th>
              <th>Documento</th>
              <th style="text-align: center"></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="p in partes">
              <td>
                <select class="form-control mr-sm-2" v-model="p.polo" @change="selecionarPolo(p, p.polo)">
                  <option disabled hidden selected value="">[Selecionar]</option>
                  <option v-for="polo in polos" :value="polo.id">{{polo.descricao}}</option>
                </select>
              </td>
              <td>
                <select class="form-control mr-sm-2" v-model="p.tipopessoa" @change="selecionarTipoPessoa(p, p.tipopessoa)">
                  <option disabled hidden selected value="">[Selecionar]</option>
                  <option v-for="tipopessoa in tipospessoa" :value="tipopessoa.id">{{tipopessoa.descricao}}</option>
                </select>
              </td>

              <td><input type="text" class="form-control mr-sm-2" v-model="p.nome" placeholder="Nome Completo" /></td>

              <td><input type="text" class="form-control mr-sm-2" v-model="p.documento" placeholder="CPF" /></td>

              <td align="center" v-if="editando">
                <button type="button" @click="removerParte(p)" class="btn btn-sm btn-outline-danger">&#x274C;</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <div class="row">
      <div class="col-sm-12">
        <button type="button" @click="peticionar()" id="prosseguir" :disabled="!isAllValid() &amp;&amp; arquivos.length == 0" class="btn btn-primary float-right no-print mt-3">Protocolar</button>
      </div>
    </div>
  </div>
</template>

<script>
import AuthBL from '../bl/auth.js'
import ProcessoBL from '../bl/processo.js'
import UtilsBL from '../bl/utils.js'
import { Bus } from '../bl/bus.js'

const polos = [{
  id: 1,
  descricao: 'Ativo'
}, {
  id: 2,
  descricao: 'Passivo'
}]

const partes = [{ polo: 1 }, { polo: 2 }]

const tipospeca = [{
  id: 1,
  descricao: 'Teor da Petição'
}, {
  id: 2,
  descricao: 'CPF/CNPJ da Parte'
}, {
  id: 3,
  descricao: 'Comprovante de Residência'
}, {
  id: 4,
  descricao: 'Outros Documentos'
}, {
  id: 5,
  descricao: 'Outros Documentos Sigilosos'
}]

const tipospessoa = [{
  id: 1,
  descricao: 'Pessoa Física'
}, {
  id: 2,
  descricao: 'Pessoa Jurídica'
}, {
  id: 3,
  descricao: 'Entidade'
}, {
  id: 4,
  descricao: 'Advogado'
}]

const orgaos = [{
  id: 1,
  descricao: 'TRF2'
}, {
  id: 2,
  descricao: 'JFRJ'
}, {
  id: 3,
  descricao: 'JFES'
}]

const localidades = [
  {
    id: 11,
    descricao: 'ANGRA DOS REIS'
  },
  {
    id: 19,
    descricao: 'BARRA DO PIRAÍ'
  },
  {
    id: 21,
    descricao: 'CAMPO GRANDE'
  },
  {
    id: 3,
    descricao: 'CAMPOS DOS GOYTACAZES'
  },
  {
    id: 18,
    descricao: 'DUQUE DE CAXIAS'
  },
  {
    id: 7,
    descricao: 'ITABORAÍ'
  }
]

export default {
  name: 'peticao-inicial',

  mounted () {
    this.$nextTick(() => {
      this.$http.get('config/peticao/intercorrente/tipos', { block: true }).then(response => {
        for (var i = 0; i < response.data.list.length; i++) this.tipos.push(response.data.list[i])
      }, error => {
        Bus.$emit('message', 'Erro', error.data.errormsg)
      })
    })
  },

  data () {
    return {
      // remover
      files: [],
      invalidFiles: [],

      polos: polos,
      partes: partes,
      tipospeca: tipospeca,
      tipospessoa: tipospessoa,

      orgaos: orgaos,
      localidades: localidades,

      editando: true,
      tipos: [],
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
    }
  },

  methods: {
    //
    // Arquivos
    //

    addedFileProxy: function (file) {
      this.arquivos.push({
        file: file,
        nome: file.name,
        bloq: false,
        protocolado: undefined,
        status: undefined,
        validando: undefined,
        valido: undefined,
        orgao: undefined,
        errormsg: undefined,
        tipo: undefined,
        tipodescr: undefined,
        segredo: undefined
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

    validarArquivo: function (arq) {
      var a = arq
      if (a.processo) {
        a.status = 'Validando...'
        a.validando = true
        a.valido = false
        this.$http.get('processo/' + ProcessoBL.somenteNumeros(a.processo) + '/validar', { block: true }).then(response => {
          var d = response.data
          a.status = d.unidade + '/' + d.orgao
          a.orgao = d.orgao
          a.validando = false
          a.valido = true
        }, error => {
          a.validando = false
          a.valido = false
          a.errormsg = error.data.errormsg
        })
      }
    },

    alterarArquivo: function (arq) {
      this.validarArquivo(arq)
      this.organizarArquivos()
    },

    removerArquivo: function (arq) {
      for (var i = 0; i < this.arquivos.length; i++) {
        if (arq === this.arquivos[i]) this.arquivos.splice(i, 1)
      }
      this.organizarArquivos()
    },

    selecionarTipo: function (arq, tipo) {
    },

    selecionarSegredo: function (arq, segredo) {
      for (var i = 0; i < this.arquivos.length; i++) {
        var a = this.arquivos[i]
        if (a !== arq && a.segredo === undefined) a.segredo = segredo
      }
    },

    organizarArquivos: function () {
      this.arquivosAProtocolar = 0
      this.arquivos.sort(function (a, b) {
        if (a.processo && !b.processo) return -1
        if (!a.processo && b.processo) return 1
        if (a.processo !== b.processo) return a.processo < b.processo ? -1 : 1
        if (a.nome !== b.nome) { return a.nome.replace('.pdf', '') < b.nome.replace('.pdf', '') ? -1 : 1 }
        return 0
      })

      var arq = { odd: false }

      for (var i = 0; i < this.arquivos.length; i++) {
        var a = this.arquivos[i]
        a.odd = !arq.odd
        arq = a
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
      }
      return true
    },

    //
    // Partes
    //

    adicionarParte: function () {
      this.partes.push({})
    },

    removerParte: function (parte) {
      this.partes.splice(this.partes.indexOf(parte), 1)
    },

    selecionarPolo: function (parte, polo) {
    },

    selecionarTipoPessoa: function (parte, tipoPessoa) {
    },

    //
    // Peticionar
    //

    enviarPeticao: function (item) {
      this.$http.post('processo/peticionar', {
        orgao: item.arq.orgao,
        tipopeticao: item.arq.tipo,
        nivelsigilo: item.arq.segredo,
        pdfs: item.pdfs
      }, { block: true }).then(response => {
        for (var i = item.index; i <= item.indexFinal; i++) {
          this.arquivos[i].status = response.data.status
          this.arquivos[i].protocolado = true
        }
        UtilsBL.logEvento('peticionamento', 'enviar', 'petição inicial')
      }, error => {
        for (var i = item.index; i <= item.indexFinal; i++) {
          this.arquivos[i].errormsg = error.data.errormsg
        }
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

    limpar: function () {
      this.editando = true
      this.arquivos.length = 0
    },

    imprimir: function () {
      window.print()
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
