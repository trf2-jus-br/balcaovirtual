<template>
  <div class="container content profile">
    <div class="row mt-3" v-if="errormsg !== undefined">
      <div class="col col-sm-12">
        <p class="alert alert-danger">
          {{ errormsg }}
        </p>
      </div>
    </div>

    <div class="row mt-3 mb-3">
      <div class="col-md-12">
        <h4 class="text-center mb-0">
          {{ documento.tipoDoDocumento }} {{ documento.numeroDoDocumento }}
        </h4>
      </div>
    </div>

    <div v-show="editando">
      <div class="row no-gutters mt-2">
        <div class="col col-auto mr-auto mb-3">
          <button
            @click="cancelar()"
            type="button"
            id="download"
            class="btn btn-light d-print-none"
          >
            <span class="fa fa-arrow-left"></span> Cancelar
          </button>
        </div>
        <div class="col col-auto mr-1 mb-3">
          <button
            @click="salvar()"
            type="button"
            class="btn btn-primary d-print-none"
          >
            <span class="fa fa-pencil"></span> Salvar
          </button>
        </div>
      </div>
      <div class="row" v-if="editando">
        <div class="col col-12">
          <div
            ref="editarea"
            class="bveditor"
            contenteditable="true"
            v-html="buffer"
          ></div>
        </div>
      </div>
    </div>

    <div v-show="!editando">
      <div class="row">
        <div class="col col-lg-8">
          <div class="row no-gutters mt-2">
            <div class="col col-auto mb-3">
              <router-link
                :to="{ name: 'Mesa', params: { manter: false } }"
                tag="button"
                class="btn btn-light d-print-none"
                ><span class="fa fa-times"></span> Voltar</router-link
              >
            </div>
            <div class="col col-auto mb-3">
              <router-link
                :disabled="!anteriorDocumento"
                :to="{
                  name: 'Documento',
                  params: {
                    numero: (anteriorDocumento || {}).id,
                    documento: anteriorDocumento,
                    lista: this.lista,
                    transitionName: 'slide-right'
                  }
                }"
                tag="button"
                class="btn btn-light d-print-none"
                ><span class="fa fa-arrow-left"></span> Anterior</router-link
              >
            </div>
            <div class="col col-auto mb-3">
              <router-link
                :disabled="!proximoDocumento"
                :to="{
                  name: 'Documento',
                  params: {
                    numero: (proximoDocumento || {}).id,
                    documento: proximoDocumento,
                    lista: this.lista,
                    transitionName: 'slide-left'
                  }
                }"
                tag="button"
                class="btn btn-light d-print-none"
                ><span class="fa fa-arrow-right"></span> Próximo</router-link
              >
            </div>
          </div>
          <div class="card mb-3">
            <div class="card-body alert-warning">
              <p ref="conteudo" class="card-text" v-html="conteudo"></p>
            </div>
          </div>
        </div>
        <div class="col col-lg-4">
          <div class="row no-gutters mt-2">
            <div class="col col-auto ml-auto mb-3 d-none d-lg-block"></div>
            <div class="col col-auto ml-1 mb-3" v-if="!documento.disabled">
              <button
                @click.prevent="exibirDevolver()"
                type="button"
                class="btn btn-info d-print-none"
              >
                <span class="fa fa-comment"></span> Devolver
              </button>
            </div>
            <div class="col col-auto ml-1 mb-3" v-if="!documento.disabled">
              <button
                @click.prevent="editar()"
                type="button"
                class="btn btn-primary d-print-none"
              >
                <span class="fa fa-pencil"></span> Editar
              </button>
            </div>
            <div class="col col-auto ml-1 mb-3" v-if="!documento.disabled">
              <button
                @click.prevent="assinarComSenha()"
                type="button"
                class="btn btn-success d-print-none"
              >
                <span class="fa fa-certificate"></span> Assinar
              </button>
            </div>
          </div>
          <div class="card mb-3">
            <div class="card-header">
              Detalhes
            </div>
            <div class="card-body">
              <p class="card-text">
                Processo:
                <router-link
                  :to="{
                    name: 'Processo',
                    params: { numero: documento.numeroDoProcesso },
                    query: { avisos: $parent.cAvisos }
                  }"
                  target="_blank"
                  >{{ documento.processoFormatado }}</router-link
                >
                <br />Cadastro:
                <span v-html="documento.dataDeInclusaoFormatada"></span>
                <br />Responsável: {{ documento.nomeDoUsuarioQueIncluiu }}
                <br />Status: {{ documento.descricaoDoStatus }}
              </p>
            </div>
          </div>
          <div class="card" v-if="documento.lembretes">
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
                    <tr
                      v-for="lembrete in documento.lembretes"
                      :key="lembrete.id"
                    >
                      <th scope="row">{{ lembrete.conteudo }}</th>
                      <td>{{ lembrete.identificadorDoUsuario }}</td>
                      <td>{{ lembrete.dataDeInclusaoFormatada }}</td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <documento-devolver
      ref="documentoDevolver"
      @ok="devolver"
    ></documento-devolver>
  </div>
</template>

<script>
import DocumentoDevolver from "./DocumentoDevolver";
import UtilsBL from "../bl/utils.js";
import { Bus } from "../bl/bus.js";

export default {
  mounted() {},
  data() {
    return {
      numero: this.$route.params.numero,
      documento: this.$route.params.documento,
      conteudo: this.preprocess(this.$route.params.documento.conteudo),
      lista: this.$route.params.lista,
      editando: false,
      errormsg: undefined
    };
  },
  computed: {
    indice: function() {
      for (var i = 0; i < this.lista.length; i++) {
        if (this.documento === this.lista[i]) return i;
      }
      return 0;
    },
    proximoDocumento: function() {
      if (this.indice >= this.lista.length - 1) return;
      return this.lista[this.indice + 1];
    },
    anteriorDocumento: function() {
      if (this.indice === 0) return;
      return this.lista[this.indice - 1];
    }
  },
  methods: {
    preprocess: function(s) {
      console.log(s);
      return s
        .replace(
          'contentEditable="true"',
          'contentEditable="false" data-bv_edit="true"'
        )
        .replace(/&#x2013;/g, "-");
    },
    posprocess: function(s) {
      return s.replace(
        'contentEditable="false" data-bv_edit="true"',
        'contentEditable="true"'
      );
    },
    voltar: function() {
      this.$router.go(-1);
    },

    editar: function() {
      var el = this.$refs["conteudo"].querySelector(
        "section[data-bv_edit=true]"
      );
      // el.setAttribute("contenteditable", "true")
      this.buffer = el.innerHTML;
      // this.buffer = this.documento.conteudo
      this.editando = true;
    },

    salvar: function() {
      this.buffer = this.$refs["editarea"].innerHTML;
      this.$refs["conteudo"].querySelector(
        "section[data-bv_edit=true]"
      ).innerHTML = this.buffer;
      this.$http
        .post(
          "mesa/" +
            "null" +
            "/documento/" +
            this.documento.id +
            "/salvar?sistema=" +
            this.documento.sistema,
          {
            html: this.posprocess(
              this.$refs["conteudo"].querySelector("article").outerHTML
            )
          },
          { block: true }
        )
        .then(
          response => {
            this.editando = false;
            this.documento.conteudo = this.$refs["conteudo"].innerHTML;
            UtilsBL.logEvento("mesa", "salvar", "minuta");
          },
          error => UtilsBL.errormsg(error, this)
        );
    },

    exibirDevolver: function() {
      this.$refs.documentoDevolver.show();
    },

    devolver: function(lembrete) {
      this.$http
        .post(
          "mesa/" +
            "null" +
            "/documento/" +
            this.documento.id +
            "/devolver?sistema=" +
            this.documento.sistema,
          {
            lembrete: lembrete
          },
          { block: true }
        )
        .then(
          response => {
            UtilsBL.logEvento("mesa", "devolver", "minuta");
            this.documento.status = 7;
            this.documento.descricaoDoStatus = "Devolvida";
            this.documento.checked = false;
            this.documento.disabled = true;
            this.exibeProximoDocumento();
          },
          error => UtilsBL.errormsg(error, this)
        );
    },

    cancelar: function() {
      this.editando = false;
    },

    assinarComSenha: function() {
      Bus.$emit(
        "iniciarAssinaturaComSenha",
        [this.documento],
        this.exibeProximoDocumento
      );
      // Bus.$emit('assinarComSenha', [this.doc], () => this.$router.go(-1))
    },

    exibeProximoDocumento: function() {
      if (this.proximoDocumento) {
        this.$router.push({
          name: "Documento",
          params: {
            numero: this.proximoDocumento.id,
            documento: this.proximoDocumento,
            lista: this.lista,
            transitionName: "slide-left"
          }
        });
      } else {
        this.$router.push({ name: "Mesa", params: { manter: false } });
      }
    },

    imprimir: function() {
      window.print();
    }
  },

  components: {
    DocumentoDevolver
  }
};
</script>
<style>
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
</style>
