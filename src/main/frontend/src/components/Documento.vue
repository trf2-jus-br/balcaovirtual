<template>
<div class="container content profile">
    <div class="row mt-3" v-if="errormsg !== undefined">
        <div class="col col-sm-12">
            <p class="alert alert-danger">
                {{errormsg}}
            </p>
        </div>
    </div>

    <div class="row mt-3 mb-3">
        <div class="col-md-12">
            <h4 class="text-center mb-0">{{documento.tipoDoDocumento}} {{documento.numeroDoDocumento}}</h4>
        </div>
    </div>

    <div v-show="editando">
        <div class="row no-gutters mt-2">
            <div class="col col-auto mr-auto mb-3">
                <button @click="cancelar()" type="button" id="download" class="btn btn-light d-print-none"><span class="fa fa-arrow-left"></span> Cancelar
                </button>
            </div>
            <div class="col col-auto mr-1 mb-3">
                <button @click="salvar()" type="button" class="btn btn-primary d-print-none"><span class="fa fa-pencil"></span> Salvar
                </button>
            </div>
        </div>
        <div class="row" v-if="editando">
            <div class="col col-12">
                <ckeditor :editor="editor" v-model="buffer" :config="editorConfig"></ckeditor>
            </div>
        </div>
    </div>

    <div v-show="!editando">
        <div class="row no-gutters mt-2">
            <div class="col col-auto mb-3">
                <router-link :to="{name: 'Mesa', params: {manter: true}}" tag="button" class="btn btn-light d-print-none"><span class="fa fa-arrow-left"></span> Voltar</router-link>
            </div>
            <div class="col col-auto mb-3">
                <router-link :disabled="!anteriorDocumento" :to="{name: 'Documento', params: {numero: (anteriorDocumento||{}).id, documento: anteriorDocumento, lista: this.lista}}" tag="button" class="btn btn-light d-print-none"><span class="fa fa-arrow-up"></span> Anterior</router-link>
            </div>
            <div class="col col-auto mb-3">
                <router-link :disabled="!proximoDocumento" :to="{name: 'Documento', params: {numero: (proximoDocumento||{}).id, documento: proximoDocumento, lista: this.lista}}" tag="button" class="btn btn-light d-print-none"><span class="fa fa-arrow-down"></span> Próximo</router-link>
            </div>
            <div class="col col-auto ml-auto mb-3">
                <button @click.prevent="exibirDevolver()" type="button" class="btn btn-info d-print-none"><span class="fa fa-comment"></span> Devolver
                </button>
            </div>
            <div class="col col-auto ml-1 mb-3">
                <button @click.prevent="editar()" type="button" class="btn btn-primary d-print-none"><span class="fa fa-pencil"></span> Editar
                </button>
            </div>
            <div class="col col-auto ml-1 mb-3">
                <button @click.prevent="assinarComSenha()" type="button" class="btn btn-success d-print-none"><span class="fa fa-certificate"></span> Assinar
                </button>
            </div>
        </div>

        <div class="row">
            <div class="col col-lg-8">
                <div class="card mb-3">
                    <div class="card-body alert-warning">
                        <p ref="conteudo" class="card-text" v-html="documento.conteudo"></p>
                    </div>
                </div>
            </div>
            <div class="col col-lg-4">
                <div class="card mb-3">
                    <div class="card-header">
                        Detalhes
                    </div>
                    <div class="card-body">
                        <p class="card-text">
                            Processo:
                            <router-link :to="{name: 'Processo', params: {numero: documento.numeroDoProcesso}, query: {avisos: $parent.cAvisos}}" target="_blank">{{documento.processoFormatado}}</router-link>
                            <br>Cadastro:
                            <span v-html="documento.dataDeInclusaoFormatada"></span>
                            <br>Responsável: {{documento.nomeDoUsuarioQueIncluiu}}
                            <br>Status: {{documento.descricaoDoStatus}}
                        </p>
                    </div>
                </div>
                <div class="card" v-if="documento.lembretes">
                    <div class="card-header">
                        Lembretes
                    </div>
                    <div class="card-body p-0">
                        <p class="card-text">
                            <table class="table table-striped">
                                <thead>
                                    <tr>
                                        <th scope="col">Texto</th>
                                        <th scope="col">Responsável</th>
                                        <th scope="col">Data</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr v-for="lembrete in documento.lembretes">
                                        <th scope="row">{{lembrete.conteudo}}</th>
                                        <td>{{lembrete.identificadorDoUsuario}}</td>
                                        <td>{{lembrete.dataDeInclusaoFormatada}}</td>
                                    </tr>
                                </tbody>
                            </table>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <documento-devolver ref="documentoDevolver" @ok="devolver"></documento-devolver>
</div>
</template>

<script>
import DocumentoDevolver from './DocumentoDevolver'
import ClassicEditor from '@ckeditor/ckeditor5-build-classic/build/ckeditor.js'
import UtilsBL from '../bl/utils.js'
import { Bus } from '../bl/bus.js'

export default {
  data () {
    return {
      numero: this.$route.params.numero,
      documento: this.$route.params.documento,
      lista: this.$route.params.lista,
      errormsg: undefined,
      editor: ClassicEditor,
      editorData: '<p>Content of the editor.</p>',
      editorConfig: {},
      buffer: undefined,
      editando: false
    }
  },
  computed: {
    indice: function () {
      for (var i = 0; i < this.lista.length; i++) {
        if (this.documento === this.lista[i]) return i
      }
    },
    proximoDocumento: function () {
      if (this.indice >= this.lista.length - 1) return
      return this.lista[this.indice + 1]
    },
    anteriorDocumento: function () {
      if (this.indice === 0) return
      return this.lista[this.indice - 1]
    }
  },
  methods: {
    voltar: function () {
      this.$router.go(-1)
    },

    editar: function () {
      this.buffer = this.$refs['conteudo'].querySelector('section[contenteditable=true]').innerHTML
      // this.buffer = this.documento.conteudo
      this.editando = true
    },

    salvar: function () {
      this.$refs['conteudo'].querySelector('section[contenteditable=true]').innerHTML = this.buffer
      this.$http.post('mesa/' + 'null' + '/documento/' + this.documento.id + '/salvar?sistema=' + this.documento.sistema, {
        html: this.$refs['conteudo'].querySelector('article').outerHTML
      }, { block: true }).then(response => {
        this.editando = false
        this.documento.conteudo = this.$refs['conteudo'].innerHTML
        UtilsBL.logEvento('mesa', 'salvar', 'minuta')
      }, error => UtilsBL.errormsg(error, this))
    },

    exibirDevolver: function () {
      this.$refs.documentoDevolver.show()
    },

    devolver: function (lembrete) {
      this.$http.post('mesa/' + 'null' + '/documento/' + this.documento.id + '/devolver?sistema=' + this.documento.sistema, {
        lembrete: lembrete
      }, { block: true }).then(response => {
        UtilsBL.logEvento('mesa', 'devolver', 'minuta')
        this.$router.go(-1)
      }, error => UtilsBL.errormsg(error, this))
    },

    cancelar: function () {
      this.editando = false
    },

    assinarComSenha: function() {
      Bus.$emit('iniciarAssinaturaComSenha', [this.documento], () => { this.$router.go(-1) })
      // Bus.$emit('assinarComSenha', [this.doc], () => this.$router.go(-1))
    },

    imprimir: function () {
      window.print()
    }
  },

  components: {
    DocumentoDevolver
  }
}
</script>
