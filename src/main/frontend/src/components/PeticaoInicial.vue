<template>
  <div class="container-fluid content">
    <div class="row">
      <div class="col-md-12">
        <h4 class="text-center mt-3 mb-3">Petição Inicial</h4>
      </div>
    </div>

    <div class="row" v-if="errormsg">
      <div class="col col-sm-12">
        <p class="alert alert-danger">
          <strong>Erro!</strong> {{errormsg}}
        </p>
      </div>
    </div>

    <div class="row" v-if="warningmsg">
      <div class="col col-sm-12">
        <p class="alert alert-warning">
          <strong>Atenção!</strong> {{warningmsg}}
        </p>
      </div>
    </div>

    <div class="row" v-if="alerta &amp;&amp; !successmsg">
      <div class="col col-sm-12"><p class="alert alert-warning" v-html="alerta"></p>
      </div>
    </div>

    <template v-if="successmsg">
      <div class="row">
        <div class="col col-sm-12">
          <p class="alert alert-success">
            <strong>Pronto!</strong> {{successmsg}}
          </p>
        </div>
      </div>

      <div class="row justify-content-end align-items-center mt-3">
        <div class="col-sm-auto">
          <div>
            <label class="form-check-label">
              <input type="checkbox" v-model="manterCampos">
              Manter preenchimento anterior
            </label>
          </div>
        </div>
        <div class="col-sm-auto">
          <button type="button " @click="limpar()" id="voltar" class="btn btn-secondary d-print-none">Enviar Outra Petição</button>
        </div>
      </div>

    </template>

    <template v-if="!successmsg">
      <div class="row pb-4" v-show="arquivos.length == 0">
        <div class="col-md-12">
          <vue-clip ref="clip" :options="vueclipOptions" :on-added-file="addedFileProxy" :on-complete="completeProxy">
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
          <table class="table table-peticao table-responsive">
            <thead class="thead-inverse">
              <tr>
                <th>Tipo</th>
                <th>Descrição</th>
                <th>Arquivo</th>
                <th>Status</th>
                <th style="text-align: center"></th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(f, index) in arquivos" :class="{odd: f.odd}">
                <td>
                  <select style="min-width: 8em;" class="form-control mr-sm-2" v-model="f.tipo" :disabled="f.protocolado" @change="selecionarTipo(f, f.tipo)" :name="'tipopeca[' + index +']'" :class="{ 'is-invalid': errors.has('tipopeca[' + index +']') }" v-validate.initial="'required'">
                    <option v-for="tipo in tipospeca" :value="tipo.id">{{tipo.nome}}</option>
                    <option disabled hidden selected value=""> [Selecionar]</option>
                  </select>
                </td>

                <td>
                  <div class="input-group">
                    <input type="text" class="form-control" style="min-width: 16em;" placeholder="Descrição" v-model="f.nome" @input="alterarArquivo(f)" :disabled="f.bloq || f.protocolado">
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

                <td align="right">
                  <a href="" @click.prevent="removerArquivo(f)">
                    <span class="fa fa-remove icone-em-linha"></span>
                  </a>
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
      <div class="pt-3 pb-3 pl-3 pr-3" style="background-color: rgb(233, 236, 239)">
        <div class="row">
          <div class="form-group col-md-2">
            <label for="orgao">Órgão</label>
            <select id="orgao" class="form-control" v-model="orgao" @change="selecionarOrgao(orgao)" name="orgao" :class="{ 'is-invalid': errors.has('orgao') }" v-validate.initial="'required'">
              <option disabled selected hidden :value="undefined">[Selecionar]</option>
              <option v-for="l in orgaos" :value="l.id">{{l.nome}}</option>
            </select>
          </div>
          <div class="form-group col-md-3">
            <label for="localidade">Localidade</label>
            <select id="localidade" class="form-control" v-model="localidade" @change="selecionarLocalidade(localidade)" name="localidade" :class="{ 'is-invalid': errors.has('localidade') }" v-validate.initial="'required'">
              <option disabled selected hidden :value="undefined">[Selecionar]</option>
              <option v-for="l in localidades" :value="l.id">{{l.nome}}</option>
            </select>
          </div>
          <div class="form-group col-md-2">
            <label for="especialidade">Especialidade</label>
            <select id="especialidade" class="form-control" v-model="especialidade" @change="selecionarEspecialidade(especialidade)" name="especialidade" :class="{ 'is-invalid': errors.has('especialidade') }" v-validate.initial="'required'">
              <option disabled selected hidden :value="undefined">[Selecionar]</option>
              <option v-for="l in especialidades" :value="l.id">{{l.nome}}</option>
            </select>
          </div>
          <div class="form-group col-md-3">
            <label for="classe">Classe</label>
            <select id="classe" class="form-control" v-model="classe" name="classe" :class="{ 'is-invalid': errors.has('classe') }" v-validate.initial="'required'">
              <option disabled selected hidden :value="undefined">[Selecionar]</option>
              <option v-for="l in classes" :value="l.id">{{l.nome}}</option>
            </select>
          </div>

          <div class="form-group col-md-2">
            <label for="valor">Valor da Causa</label> <input type="text" class="form-control" id="valor" v-model="valorcausa" aria-describedby="valorDaCausa" placeholder="0,00" v-mask="'money'">
          </div>

          <div class="form-group col-md-6" v-if="ef">
            <label for="cda">CDA</label>
            <input type="text" class="form-control" :class="{ 'is-invalid': errors.has('cda') }" v-model="cda" name="cda" placeholder="" v-validate.initial="'required'" />
            <small id="cdaHelp" class="form-text text-muted">Se houver mais de uma, separar com vírgulas.</small>
          </div>

          <div class="form-group col-md-6" v-if="ef">
            <label for="pa">Processo Administrativo</label>
            <input type="text" class="form-control" :class="{ 'is-invalid': errors.has('pa') }" v-model="pa" name="pa" placeholder="" v-validate.initial="'required'" />
            <small id="paHelp" class="form-text text-muted">Se houver mais de um, separar com vírgulas.</small>
          </div>

          <div class="form-check col-md-3">
            <label class="form-check-label"> <input type="checkbox" class="form-check-input" v-model="nivelsigilo"> Segredo de Justiça
            </label>
          </div>

          <div class="form-check col-md-3">
            <label class="form-check-label"> <input type="checkbox" class="form-check-input" v-model="justicagratuita"> Justiça Gratuita
            </label>
          </div>
          <div class="form-check col-md-3">
            <label class="form-check-label"> <input type="checkbox" class="form-check-input" v-model="tutelaantecipada"> Tutela Liminar/Antecipada
            </label>
          </div>
          <div class="form-check col-md-3">
            <label class="form-check-label"> <input type="checkbox" class="form-check-input" v-model="prioridadeidoso"> Prioridade de Idoso
            </label>
          </div>
        </div>
      </div>

      <div class="row mt-3">
        <div class="col col-auto">
          <h5 class="mt-3 mb-2">Partes</h5>
        </div>
        <div class="col col-auto ml-auto">
          <button type="button" @click="adicionarParte()" class="btn btn-info btn-sm mt-2">Adicionar Parte</button>
        </div>
      </div>
      <div class="row">
        <div class="col-sm-12">
          <table class="table table-peticao table-responsive">
            <thead class="thead-inverse">
              <tr>
                <th>Polo</th>
                <th>Tipo</th>
                <th>Documento</th>
                <th>Nome</th>
                <th style="text-align: center"></th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(p, index) in partes">
                <td>
                  <select class="form-control mr-sm-2" v-model="p.polo" @change="selecionarPolo(p, p.polo)">
                    <option disabled hidden selected value="">[Selecionar]</option>
                    <option v-for="polo in polos" :value="polo.id">{{polo.nome}}</option>
                  </select>
                </td>
                <td>
                  <select class="form-control mr-sm-2" v-model="p.tipopessoa" @change="selecionarTipoPessoa(p, p.tipopessoa)">
                    <option disabled hidden selected value="">[Selecionar]</option>
                    <option v-for="tipopessoa in tipospessoa" :value="tipopessoa.id">{{tipopessoa.nome}}</option>
                  </select>
                </td>

                <td :colspan="p.tipopessoa == '3' ? 2 : 1">
                  <input type="text" :disabled="!orgao" class="form-control mr-sm-2" :class="{ 'is-invalid': errors.has('documento[' + index +']') }" v-model="p.documento" :name="'documento[' + index +']'" placeholder="CPF" v-if="p.tipopessoa == '1'" v-validate.initial="'required|cpf'" v-mask="'999.999.999-99'" @change="alterouCpf(p)" />
                  <input type="text" :disabled="!orgao" class="form-control mr-sm-2" :class="{ 'is-invalid': errors.has('documento[' + index +']') }" v-model="p.documento" :name="'documento[' + index +']'" placeholder="CNPJ" v-if="p.tipopessoa == '2'" v-validate.initial="'required|cnpj'" v-mask="'99.999.999/9999-99'" @change="alterouCnpj(p)" />
                  
                  <select :disabled="!orgao" class="form-control mr-sm-2" :class="{ 'is-invalid': errors.has('documento[' + index +']') }" v-model="p.documento" :name="'documento[' + index +']'" placeholder="Entidade" v-if="p.tipopessoa == '3'" v-validate.initial="'required'" @change="alterouEntidade(p)">
                    <option disabled selected hidden :value="undefined">[Selecionar]</option>
                    <option v-for="l in entidadesFiltradas" :value="l.documento">{{l.nome}}</option>
                  </select>

                  
                  <input type="text" :disabled="!orgao" class="form-control mr-sm-2" :class="{ 'is-invalid': errors.has('documento[' + index +']') }" v-model="p.documento" :name="'documento[' + index +']'" placeholder="OAB" v-if="p.tipopessoa == '4'" v-validate.initial="'required|oab'" @change="alterouOab(p)" />
                </td>

                <td v-if="p.tipopessoa !== '3'"><input type=" text " class="form-control mr-sm-2 " :class="{ 'is-invalid': errors.has('nome[' + index +']') }" v-model="p.nome " :name="'nome[' + index +']'" placeholder="Nome Completo " v-validate.initial="'required'" /></td>

                <td align="right">
                  <a href="" @click.prevent="removerParte(p)">
                    <span class="fa fa-remove icone-em-linha"></span>
                  </a>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div class="row ">
        <div class="col-sm-12 ">
          <button type="button " @click="peticionar()" id="prosseguir" :disabled="arquivos.length==0 || !isAllValid() || errors.any()" class="btn btn-primary float-right d-print-none mt-3 ">Protocolar</button>
        </div>
      </div>
    </template>
  </div>
</template>

<script>
import AuthBL from '../bl/auth.js'
import ProcessoBL from '../bl/processo.js'
import UtilsBL from '../bl/utils.js'
// import CnjClasseBL from '../bl/cnj-classe.js'
import { Bus } from '../bl/bus.js'
import AwesomeMask from 'awesome-mask'
import ValidacaoBL from '../bl/validacao.js'

const polos = [{
  id: 1,
  nome: 'Ativo'
}, {
  id: 2,
  nome: 'Passivo'
}]

const partes = [{ polo: 1, tipopessoa: '1', documento: undefined, nome: undefined }, { polo: 2, tipopessoa: '1', documento: undefined, nome: undefined }]

const tipospeca = [{
  id: 1,
  nome: 'Teor da Petição'
}, {
  id: 2,
  nome: 'Cópia de CPF/CNPJ'
}, {
  id: 3,
  nome: 'Comprovante de Residência'
}, {
  id: 4,
  nome: 'Outros Documentos'
}, {
  id: 5,
  nome: 'Outros Documentos Sigilosos'
}, {
  id: 11,
  nome: 'Inquérito/Procedimento Criminal'
}]

const tipospessoa = [{
  id: '1',
  nome: 'Pessoa Física'
}, {
  id: '2',
  nome: 'Pessoa Jurídica'
}, {
  id: '3',
  nome: 'Entidade'
}, {
  id: '4',
  nome: 'Advogado'
}]

const alertas = {
  'jfrj': '<strong>Atenção</strong>: O serviço de petição inicial pela internet não deve ser utilizado para o ajuizamento de TURMA RECURSAL e plantão judiciário. Nestes casos, as petições devem ser protocoladas por meio físico, junto a Seção de Distribuição da TURMA RECURSAL ou no JUÍZO DE PLANTÃO, conforme o caso.'
}

export default {
  name: 'peticao-inicial',

  mounted () {
    this.$nextTick(() => {
      this.carregarOrgaos()
      this.carregarEntidades()
    })
  },

  data () {
    return {
      // remover
      files: [],
      invalidFiles: [],

      polos: polos,
      partes: JSON.parse(JSON.stringify(partes)),
      tipospeca: tipospeca,
      tipospessoa: tipospessoa,

      alertas: alertas,

      orgao: undefined,
      localidade: undefined,
      especialidade: undefined,
      classe: undefined,
      valorcausa: undefined,

      cda: undefined,
      pa: undefined,

      nivelsigilo: false,
      justicagratuita: false,
      tutelaantecipada: false,
      prioridadeidoso: false,

      entidades: [],
      orgaos: [],
      localidades: [],
      especialidades: [],
      classes: [],

      tipos: [],
      dataDeProtocolo: undefined,
      resumoPorData: [],
      filtroProtocolo: undefined,
      mostrarQuantidadePorData: undefined,
      quantidadePorData: [],
      arquivos: [],
      arquivosAProtocolar: undefined,
      arquivoCorrente: undefined,
      errormsg: undefined,
      warningmsg: undefined,
      successmsg: undefined,
      manterCampos: false,
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
    entidadesFiltradas: function () {
      console.log('recalculando filtrados...', this.modified)
      var a = this.entidades
      if (!a || !this.orgao) return []
      var org = this.orgao.toUpperCase()
      a = a.filter((item) => {
        return item.orgao === org
      })
      return a
    },

    ef: function () {
      // Substituir pelo uso de um parâmetro de retorno referente à classe escolhida
      return this.classe === '1116' || this.classe === '203' || this.classe === '99'
    },

    alerta: function () {
      return this.alertas[this.orgao]
    }
  },

  methods: {
    //
    // Selects
    //
    carregar: function (url, items, item) {
      this.$http.get(url, { block: true }).then(response => {
        this[items].length = 0
        this[item] = undefined
        for (var i = 0; i < response.data.list.length; i++) this[items].push(response.data.list[i])
        // if (items === 'classes') {
        // for (i = 0; i < this.classes.length; i++) this.classes[i].nome = CnjClasseBL.nomeCompleto(this.classes[i].id)
        // }
        this[items].sort(function (a, b) {
          if (a.nome !== b.nome) return a.nome < b.nome ? -1 : 1
          return 0
        })
        // if (!this[item]) this[item] = response.data.list[0].id
      }, error => {
        Bus.$emit('message', 'Erro', error.data.errormsg)
      })
    },

    carregarEntidades: function () {
      this.carregar('config/entidades', 'entidades', 'entidade')
    },

    carregarOrgaos: function () {
      this.carregar('config/orgaos', 'orgaos', 'orgao')
    },

    selecionarOrgao: function () {
      this.localidade = undefined
      this.localidades.length = 0
      this.especialidade = undefined
      this.especialidades.length = 0
      this.classe = undefined
      this.classes.length = 0
      this.carregarLocalidades()
    },

    carregarLocalidades: function () {
      this.carregar('config/localidades?orgao=' + this.orgao, 'localidades', 'localidade')
    },

    selecionarLocalidade: function () {
      this.especialidade = undefined
      this.especialidades.length = 0
      this.classe = undefined
      this.classes.length = 0
      this.carregarEspecialidades()
    },

    carregarEspecialidades: function () {
      this.carregar('config/localidade/' + this.localidade + '/especialidades?orgao=' + this.orgao, 'especialidades', 'especialidade')
    },

    selecionarEspecialidade: function () {
      this.classe = undefined
      this.classes.length = 0
      this.carregarClasses()
    },

    carregarClasses: function () {
      this.carregar('config/localidade/' + this.localidade + '/especialidade/' + this.especialidade + '/classes?orgao=' + this.orgao, 'classes', 'classe')
    },

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

    adicionarArquivo: function () {
      this.$refs.clip.$el.firstChild.click()
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
        if (!this.arquivos[j].tipo) return false
      }
      return true
    },

    //
    // Partes
    //

    adicionarParte: function () {
      this.partes.push({ polo: 2, tipopessoa: 1, documento: undefined, nome: undefined })
      this.validar()
    },

    removerParte: function (parte) {
      this.partes.splice(this.partes.indexOf(parte), 1)
    },

    selecionarPolo: function (parte, polo) {
      this.ordenarPartes()
      this.validar()
    },

    selecionarTipoPessoa: function (parte, tipoPessoa) {
      this.ordenarPartes()
      this.validar()
    },

    validar: function () {
      this.$nextTick(() => this.$validator.validateAll())
    },

    ordenarPartes: function () {
      this.partes.sort((a, b) => {
        if (a.polo && !b.polo) return -1
        if (!a.polo && b.polo) return 1
        if (a.polo !== b.polo) return a.polo < b.polo ? -1 : 1
        if (a.tipopessoa && !b.tipopessoa) return -1
        if (!a.tipopessoa && b.tipopessoa) return 1
        if (a.tipopessoa !== b.tipopessoa) return a.tipopessoa < b.tipopessoa ? -1 : 1
        return 0
      })
    },

    alterouCpf: function(parte) {
      var cpf = ProcessoBL.somenteNumeros(parte.documento)
      if (!cpf || !ValidacaoBL.validarCPF(cpf)) {
        parte.nome = undefined
        this.validar()
        return
      }
      this.$http.get('config/pessoa-fisica/' + cpf + '?orgao=' + this.orgao, { block: true }).then(response => {
        parte.nome = response.data.nome
        this.validar()
      }, error => UtilsBL.errormsg(error, this))
    },

    alterouCnpj: function(parte) {
      var cnpj = ProcessoBL.somenteNumeros(parte.documento)
      if (!cnpj || !ValidacaoBL.validarCNPJ(cnpj)) {
        parte.nome = undefined
        this.validar()
        return
      }
      this.$http.get('config/pessoa-juridica/' + cnpj + '?orgao=' + this.orgao, { block: true }).then(response => {
        parte.nome = response.data.nome
        this.validar()
      }, error => UtilsBL.errormsg(error, this))
    },

    alterouOab: function(parte) {
      var oab = parte.documento
      if (!oab || !ValidacaoBL.validarOAB(oab)) {
        parte.nome = undefined
        this.validar()
        return
      }
      this.$http.get('config/advogado/' + oab + '?orgao=' + this.orgao, { block: true }).then(response => {
        parte.nome = response.data.nome
        this.validar()
      }, error => UtilsBL.errormsg(error, this))
    },

    alterouEntidade: function(parte) {
      parte.nome = undefined
      for (var i = 0; i < this.entidadesFiltradas.length; i++) {
        var e = this.entidadesFiltradas[i]
        if (e.documento === parte.documento) {
          parte.nome = e.nome
        }
      }
      this.validar()
    },

    //
    // Peticionar
    //
    peticionar: function () {
      this.errormsg = undefined
      this.$validator.validateAll().then((result) => { if (!result) return })
      var pdfs, classificacoes, i
      for (i = 0; i < this.arquivos.length; i++) {
        if (pdfs) pdfs += ','
        else pdfs = ''
        pdfs += this.arquivos[i].id

        if (classificacoes) classificacoes += ','
        else classificacoes = ''
        classificacoes += this.arquivos[i].tipo
      }

      console.log('valorcausa', this.valorcausa)

      this.$http.post('peticao-inicial/protocolar', {
        orgao: this.orgao,
        localidade: this.localidade,
        especialidade: this.especialidade,
        classe: this.classe,
        valorcausa: this.valorcausa,
        cdas: this.ef ? this.cda : undefined,
        pas: this.ef ? this.pa : undefined,
        nivelsigilo: this.nivelsigilo ? 1 : 0,
        justicagratuita: this.justicagratuita,
        tutelaantecipada: this.tutelaantecipada,
        prioridadeidoso: this.prioridadeidoso,
        partes: JSON.stringify(this.partes),
        pdfs: pdfs,
        classificacoes: classificacoes
      }, { block: true }).then(response => {
        this.successmsg = response.data.status
        UtilsBL.logEvento('peticionamento', 'enviar', 'petição inicial')
      }, error => UtilsBL.errormsg(error, this))
    },

    limpar: function () {
      this.successmsg = undefined
      if (!this.manterCampos) {
        this.arquivos.length = 0
        this.orgao = undefined
        this.localidade = undefined
        this.especialidade = undefined
        this.classe = undefined

        this.nivelsigilo = false
        this.justicagratuita = false
        this.tutelaantecipada = false
        this.prioridadeidoso = false

        this.valorcausa = undefined
        this.cda = undefined
        this.pa = undefined
        this.$set(this, 'partes', JSON.parse(JSON.stringify(partes)))
      }
      this.ordenarPartes()
      this.validar()
    },

    imprimir: function () {
      window.print()
    }
  },

  directives: {
    'mask': AwesomeMask
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
