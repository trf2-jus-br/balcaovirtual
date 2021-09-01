<template>
  <div class="container-fluid content profile" v-if="documento">
    <div class="row mt-3" v-if="errormsg !== undefined">
      <div class="col col-sm-12">
        <p class="alert alert-danger">
          {{ errormsg }}
        </p>
      </div>
    </div>

    <div class="row mt-3 mb-3 d-none d-lg-block">
      <div class="col-md-12">
        <h4 class="text-center mb-0" v-if="documento">{{ documento.tipoDoDocumento }} {{ documento.numeroDoDocumento }}</h4>
      </div>
    </div>

    <div v-show="editando">
      <div class="row no-gutters mt-2">
        <div class="col col-auto mr-auto mb-3">
          <button @click="cancelar()" type="button" id="download" class="btn btn-light d-print-none">
            <span class="fa fa-arrow-left"></span> Cancelar
          </button>
        </div>
        <div class="col col-auto mr-1 mb-3">
          <button @click="salvar()" type="button" class="btn btn-primary d-print-none"><span class="fa fa-pencil"></span> Salvar</button>
        </div>
      </div>
      <div class="row" v-if="editando">
        <div class="col col-12">
          <div ref="editarea" class="bveditor" contenteditable="true" v-html="buffer"></div>
        </div>
      </div>
    </div>

    <div v-show="!editando">
      <div class="row">
        <div class="col col-12 col-lg-8 mt-3 mt-lg-2">
          <div class="row no-gutters">
            <div class="col col-auto mb-3">
              <router-link :to="{ name: 'Mesa', params: { manter: true } }" tag="button" class="btn btn-light d-print-none"
                ><span class="fa fa-list"></span><span class="d-none d-lg-inline"> Lista</span> {{ progresso }}</router-link
              >
            </div>
            <div class="col col-auto ml-auto mb-3">
              <router-link
                :disabled="!anteriorDocumento"
                :to="
                  anteriorDocumento
                    ? {
                        name: 'Documento',
                        params: {
                          numero: anteriorDocumento.id,
                          transitionName: 'slide-right',
                        },
                      }
                    : {}
                "
                tag="button"
                class="btn btn-light d-print-none"
                ><span class="fa fa-arrow-left"></span><span class="d-none d-lg-inline"> Anterior</span></router-link
              >
            </div>
            <div class="col col-auto mb-3 ml-1">
              <router-link
                :disabled="!proximoDocumento"
                :to="
                  proximoDocumento
                    ? {
                        name: 'Documento',
                        params: {
                          numero: (proximoDocumento || { id: '' }).id,
                          transitionName: 'slide-left',
                        },
                      }
                    : {}
                "
                tag="button"
                class="btn btn-light d-print-none"
                ><span class="fa fa-arrow-right"></span><span class="d-none d-lg-inline"> Próximo</span></router-link
              >
            </div>
          </div>

          <div class="card mb-0">
            <div
              :class="{
                'card-body': true,
                'alert-warning': !diferencas,
                'alert-success': documento.similaridade === 1.0,
                'alert-primary': documento.similaridade < 1.0,
              }"
            >
              <p class="card-text" v-html="diferencas && exibirDiferencas && documento.similaridade !== 1.0 ? diferencas : conteudo"></p>
            </div>
          </div>
          <legenda-diferencas :diferencas="diferencas" :exibirDiferencas="exibirDiferencas" :documento="documento" />

          <div class="d-none d-lg-block mt-2 mt-lg-3">
            <b-form-checkbox v-show="diferencas && documento.similaridade < 1.0" v-model="exibirDiferencas" name="check-button" switch>
              Exibir diferenças em relação ao padrão
            </b-form-checkbox>
          </div>
        </div>

        <div class="col col-12 col-lg-4 mt-3 mt-lg-2">
          <div class="row no-gutters">
            <div class="col col-auto ml-1 mb-3" v-if="!documento.disabled && documento.similaridade !== 1.0">
              <button @click.prevent="adicionarPadrao()" type="button" class="btn btn-light d-print-none">
                <span class="fa fa-plus"></span><span class="d-none d-lg-inline"> Padrão</span>
              </button>
            </div>
            <div class="col col-auto ml-1 mb-3" v-if="!documento.disabled && documento.similaridade === 1.0">
              <button @click.prevent="removerPadrao()" type="button" class="btn btn-light d-print-none">
                <span class="fa fa-minus"></span><span class="d-none d-lg-inline"> Padrão</span>
              </button>
            </div>
            <div class="col col-auto ml-1 mb-3" v-if="!documento.disabled">
              <button @click.prevent="exibirDevolver()" type="button" class="btn btn-info d-print-none">
                <span class="fa fa-comment"></span><span class="d-none d-lg-inline"> Devolver</span>
              </button>
            </div>
            <div class="col col-auto ml-1 mb-3" v-if="!documento.disabled">
              <button @click.prevent="editar()" type="button" class="btn btn-primary d-print-none">
                <span class="fa fa-pencil"></span><span class="d-none d-lg-inline"> Editar</span>
              </button>
            </div>
            <div class="col col-auto ml-1 mb-3 ml-auto" v-if="!documento.disabled">
              <button @click.prevent="assinarComSenha()" type="button" class="btn btn-success d-print-none">
                <span class="fa fa-certificate"></span><span class="d-inline d-lg-inline"> Assinar</span>
              </button>
            </div>
          </div>
          <div class="card mb-3">
            <div class="card-header">
              Detalhes
            </div>
            <div class="card-body">
              <p class="d-inline d-lg-none">{{ documento.tipoDoDocumento }}: {{ documento.numeroDoDocumento }}</p>
              <p class="card-text">
                Processo:
                <router-link
                  :to="{
                    name: 'Processo',
                    params: { numero: documento.numeroDoProcesso },
                    query: { avisos: $parent.cAvisos },
                  }"
                  target="_blank"
                  >{{ documento.processoFormatado }}</router-link
                >
                <br />Cadastro:
                <span v-html="documento.dataDeInclusaoFormatada"></span>
                <br />Responsável: {{ documento.nomeDoUsuarioQueIncluiu }} <br />Status: {{ documento.descricaoDoStatus }}
              </p>
            </div>
          </div>
          <div class="card" v-if="documento.lembretes && documento.lembretes.length">
            <div class="card-header">
              Lembretes
            </div>
            <div class="card-body p-0">
              <div class="card-text">
                <table class="table table-striped">
                  <thead>
                    <tr>
                      <th scope="col">Texto</th>
                      <th scope="col">Responsável</th>
                      <th scope="col">Data</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="lembrete in documento.lembretes" :key="lembrete.id">
                      <th scope="row">{{ lembrete.conteudo }}</th>
                      <td>{{ lembrete.identificadorDoUsuario }}</td>
                      <td>{{ lembrete.dataDeInclusaoFormatada }}</td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
          <b-form-checkbox
            class="d-inline d-lg-none mt-2 mt-lg-3"
            v-show="diferencas && documento.similaridade < 1.0"
            v-model="exibirDiferencas"
            name="check-button"
            switch
          >
            Exibir diferenças em relação ao padrão
          </b-form-checkbox>
        </div>
      </div>
    </div>
    <documento-devolver ref="documentoDevolver" @ok="devolver"></documento-devolver>
  </div>
</template>

<script>
import DocumentoDevolver from "./DocumentoDevolver";
import LegendaDiferencas from "./LegendaDiferencas";
import UtilsBL from "../bl/utils.js";
import DocumentoBL from "../bl/documento.js";
import { Bus } from "../bl/bus.js";

export default {
  name: "Documento",
  mounted() {
    if (!this.$store.state.documentos) this.$store.dispatch("carregarMesas");
  },
  data() {
    return {
      numero: this.$route.params.numero,
      editando: false,
      errormsg: undefined,
    };
  },
  computed: {
    exibirDiferencas: {
      get() {
        return this.$store.state.exibirDiferencas;
      },
      set(newValue) {
        return this.$store.commit("setExibirDiferencas", newValue);
      },
    },
    lista() {
      return this.$store.getters.documentosFiltrados;
    },
    documento() {
      if (!this.$store.state.documentos) return;
      for (var i = 0; i < this.$store.state.documentos.length; i++) {
        if (this.$store.state.documentos[i].id === this.$route.params.numero) return this.$store.state.documentos[i];
      }
      return undefined;
    },
    conteudo() {
      if (!this.documento) return;
      return DocumentoBL.preprocess(this.documento.conteudo);
    },
    diferencas() {
      if (!this.documento) return;
      return DocumentoBL.preprocess(this.documento.diferencas);
    },
    indice: function() {
      if (this.lista)
        for (var i = 0; i < this.lista.length; i++) {
          if (this.documento === this.lista[i]) return i;
        }
      return 0;
    },
    proximoDocumento: function() {
      if (!this.lista) return;
      if (this.indice >= this.lista.length - 1) return;
      return this.lista[this.indice + 1];
    },
    anteriorDocumento: function() {
      if (!this.lista) return;
      if (this.indice === 0) return;
      return this.lista[this.indice - 1];
    },
    progresso: function() {
      if (this.indice === undefined) return;
      if (!this.lista || this.lista.length === 0) return;
      return this.indice + 1 + "/" + this.lista.length;
    },
  },
  methods: {
    voltar: function() {
      this.$router.go(-1);
    },

    adicionarPadrao: function() {
      this.$store.dispatch("adicionarPadrao", this.documento.id);
    },

    removerPadrao: function() {
      this.$store.dispatch("removerPadrao", this.documento.id);
    },

    editar: function() {
      var el = this.$refs["conteudo"].querySelector("section[data-bv_edit=true]");
      // el.setAttribute("contenteditable", "true")
      this.buffer = el.innerHTML;
      // this.buffer = this.documento.conteudo
      this.editando = true;
    },

    async salvar() {
      this.buffer = this.$refs["editarea"].innerHTML;
      this.$refs["conteudo"].querySelector("section[data-bv_edit=true]").innerHTML = this.buffer;
      var html = DocumentoBL.posprocess(this.$refs["conteudo"].querySelector("article").outerHTML);
      await this.$store.dispatch("salvarDocumento", { documento: this.documento, html: html });
      this.editando = false;
    },

    exibirDevolver: function() {
      this.$refs.documentoDevolver.show();
    },

    devolver: function(lembrete) {
      this.$http
        .post(
          "mesa/" + "null" + "/documento/" + this.documento.id + "/devolver?sistema=" + this.documento.sistema,
          {
            lembrete: lembrete,
          },
          { block: true }
        )
        .then(
          (response) => {
            UtilsBL.logEvento("mesa", "devolver", "minuta");
            this.documento.status = 7;
            this.documento.descricaoDoStatus = "Devolvida";
            this.documento.checked = false;
            this.documento.disabled = true;
            this.exibeProximoDocumento();
          },
          (error) => UtilsBL.errormsg(error, this)
        );
    },

    cancelar: function() {
      this.editando = false;
    },

    assinarComSenha: function() {
      Bus.$emit("iniciarAssinaturaComSenha", [this.documento], this.exibeProximoDocumento);
      // Bus.$emit('assinarComSenha', [this.doc], () => this.$router.go(-1))
    },

    exibeProximoDocumento: function() {
      if (this.proximoDocumento) {
        this.$router.push({
          name: "Documento",
          params: {
            numero: this.proximoDocumento.id,
            transitionName: "slide-left",
          },
        });
      } else {
        this.$router.push({ name: "Mesa", params: { manter: false } });
      }
    },

    imprimir: function() {
      window.print();
    },
  },

  components: {
    DocumentoDevolver,
    LegendaDiferencas,
  },
};
</script>
<style>
.ende LegendaDiferencasreco,
.timbre_brasao,
.timbre_poder,
.timbre_instancia,
.timbre_secao_judiciaria,
.timbre_orgao,
.notas,
[data-nome="endereco"],
[data-nome="notas"],
.rodape_esquerda,
.rodape_direita,
hr {
  display: none;
}
.parte {
  margin-bottom: 0;
}
.paragrafoPadrao {
  font-size: 13pt;
  text-indent: 0.98in;
  margin-right: 0.02in;
  font-family: "Times New Roman", Georgia, Times, serif;
  text-align: justify;
  margin-top: 5mm;
  margin-bottom: 5mm;
  line-height: 1.2em;
}

.paragrafoComRecuo {
  font-size: 13pt;
  margin-left: 0.98in;
  margin-right: 0.02in;
  font-family: "Times New Roman", Georgia, Times, serif;
  text-align: justify;
  margin-top: 5mm;
  margin-bottom: 5mm;
  line-height: 1.2em;
}

.paragrafoSemRecuo {
  font-size: 13pt;
  font-family: "Times New Roman", Georgia, Times, serif;
  text-align: justify;
  margin-top: 5mm;
  margin-bottom: 5mm;
  line-height: 1.2em;
}
.paragrafoCentralizado {
  font-size: 13pt;
  font-family: "Times New Roman", Georgia, Times, serif;
  text-align: center;
  margin-top: 5mm;
  margin-bottom: 5mm;
  line-height: 1.2em;
}
.destinatario {
  font-size: 11pt;
  font-family: "Times New Roman", Georgia, Times, serif;
  text-align: justify;
  padding: 0px;
  margin-top: 0mm;
  margin-bottom: 0mm;
}
.titulo {
  font-weight: bold;
  font-size: 16pt;
  padding-bottom: 10px;
  text-transform: uppercase;
  padding-top: 10px;
  text-align: center;
  font-family: "Times New Roman", Georgia, Times, serif;
}
.subtitulo {
  font-weight: bold;
  font-size: 13pt;
  padding-bottom: 5px;
  text-transform: uppercase;
  padding-top: 5px;
  text-align: justify;
  font-family: "Times New Roman", Georgia, Times, serif;
}

.citacao {
  font-size: 11pt;
  margin-left: 0.98in;
  font-style: italic;
  font-family: "Times New Roman", Georgia, Times, serif;
  text-align: justify;
  margin-top: 5mm;
  margin-bottom: 5mm;
  line-height: 1.2em;
}

.citacao2 {
  font-size: 11pt;
  margin-left: 1.18in;
  margin-right: 0.2in;
  font-style: italic;
  font-family: "Times New Roman", Georgia, Times, serif;
  text-align: justify;
  margin-top: 5mm;
  margin-bottom: 5mm;
  line-height: 1.2em;
}

.caputEmenta {
  font-size: 13pt;
  margin-left: 0.98in;
  text-transform: uppercase;
  margin-right: 0.2in;
  font-family: "Times New Roman", Georgia, Times, serif;
  text-align: justify;
  margin-top: 5mm;
  margin-bottom: 5mm;
  line-height: 1.2em;
}

.cartaPadrao {
  font-size: 10pt;
  font-family: "Times New Roman", Georgia, Times, serif;
  text-align: justify;
  text-indent: 0.98in;
  margin-right: 0.02in;
  margin-top: 2mm;
  margin-bottom: 2mm;
  line-height: 11pt;
}

.cartaComRecuo {
  font-size: 10pt;
  font-family: "Times New Roman", Georgia, Times, serif;
  text-align: justify;
  margin-left: 0.98in;
  margin-right: 0.02in;
  margin-top: 2mm;
  margin-bottom: 2mm;
  line-height: 11pt;
}

.cartaSemRecuo {
  font-size: 10pt;
  font-family: "Times New Roman", Georgia, Times, serif;
  text-align: justify;
  margin-top: 2mm;
  margin-bottom: 2mm;
  line-height: 11pt;
}

.cartaCompacta {
  font-size: 9pt;
  font-family: "Times New Roman", Georgia, Times, serif;
  text-align: justify;
  margin-top: 1mm;
  margin-bottom: 1mm;
  line-height: 9pt;
}

.resumo {
  background-color: #d3d3d3;
}

.dispositivo {
  color: #0000ff;
}

section[contenteditable="true"] {
  background-color: white;
}

.bveditor {
  border: 1px solid black;
  padding: 1em;
}

.replaceInline {
  color: #6610f2;
}

.editOldInline {
  text-decoration: line-through;
  color: #dc3545;
}

.editNewInline {
  color: #28a745;
}
</style>
