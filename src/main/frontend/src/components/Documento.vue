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
            <div class="col col-auto mr-auto mb-3">
                <button @click="voltar()" type="button" id="download" class="btn btn-light d-print-none"><span class="fa fa-arrow-left"></span> Voltar
                </button>
            </div>
            <div class="col col-auto ml-1 mb-3" v-if="$parent.test.properties['balcaovirtual.env'] !== 'prod'">
                <button @click="devolver()" type="button" class="btn btn-info d-print-none"><span class="fa fa-comment"></span> Devolver
                </button>
            </div>
            <div class="col col-auto ml-1 mb-3">
                <button @click="editar()" type="button" class="btn btn-primary d-print-none"><span class="fa fa-pencil"></span> Editar
                </button>
            </div>
            <div class="col col-auto ml-1 mb-3" v-if="$parent.test.properties['balcaovirtual.env'] !== 'prod'">
                <button @click="assinar()" type="button" class="btn btn-success d-print-none"><span class="fa fa-certificate"></span> Assinar
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
                            <router-link :to="{name: 'Processo', params: {numero: documento.processo}, query: {avisos: $parent.cAvisos}}" target="_blank">{{documento.processoFormatado}}</router-link>
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
</div>
</template>

<script>
import ClassicEditor from '@ckeditor/ckeditor5-build-classic/build/ckeditor.js'
import UtilsBL from '../bl/utils.js'

export default {
  data () {
    return {
      documento: this.$route.params.documento,
      numero: this.$route.params.numero,
      errormsg: undefined,
      editor: ClassicEditor,
      editorData: '<p>Content of the editor.</p>',
      editorConfig: {},
      buffer: undefined,
      editando: false
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
      this.$http.post('mesa/' + 'null' + '/documento/' + this.numero + '/salvar?sistema=' + this.documento.sistema, {
        html: this.$refs['conteudo'].querySelector('article').outerHTML
      }, { block: true }).then(response => {
        this.editando = false
        this.documento.conteudo = this.$refs['conteudo'].innerHTML
        UtilsBL.logEvento('mesa', 'salvar', 'minuta')
      }, error => UtilsBL.errormsg(error, this))
    },

    cancelar: function () {
      this.editando = false
    },

    assinar: function () {
      this.$router.go(-1)
    },

    imprimir: function () {
      window.print()
    }
  }
}
</script>
