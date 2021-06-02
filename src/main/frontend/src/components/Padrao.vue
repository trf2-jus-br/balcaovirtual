<template>
  <div class="container-fluid content profile" v-if="documento">
    <div class="row mt-3" v-if="errormsg !== undefined">
      <div class="col col-sm-12">
        <p class="alert alert-danger">
          {{ errormsg }}
        </p>
      </div>
    </div>

    <div class="row mt-3 mb-3">
      <div class="col-md-12">
        <h4 class="text-center mb-0">{{ numero }}</h4>
      </div>
    </div>

    <div class="row no-gutters mt-2">
      <div class="col col-auto mr-auto mb-3">
        <button @click="cancelar()" type="button" id="download" class="btn btn-light d-print-none"><span class="fa fa-arrow-left"></span> Cancelar</button>
      </div>
      <div class="col col-auto mr-1 mb-3">
        <button @click="salvar()" type="button" class="btn btn-primary d-print-none"><span class="fa fa-pencil"></span> Salvar</button>
      </div>
    </div>
    <div class="row">
      <div class="col col-12">
        <div ref="editarea" class="bveditor" contenteditable="true" v-html="buffer"></div>
      </div>
    </div>
  </div>
</template>

<script>
import UtilsBL from "../bl/utils.js";

export default {
  mounted() {},
  data() {
    return {
      numero: this.$route.params.numero,
      conteudo: this.preprocess(this.$route.params.conteudo)
    };
  },
  computed: {},
  methods: {
    preprocess: function(s) {
      if (!s) return;
      console.log(s);
      return s.replace('contentEditable="true"', 'contentEditable="false" data-bv_edit="true"').replace(/&#x2013;/g, "-");
    },

    salvar: function() {
      this.buffer = this.$refs["editarea"].innerHTML;
      this.$refs["conteudo"].querySelector("section[data-bv_edit=true]").innerHTML = this.buffer;
      this.$http
        .post(
          "mesa/" + "null" + "/documento/" + this.documento.id + "/salvar?sistema=" + this.documento.sistema,
          {
            html: this.posprocess(this.$refs["conteudo"].querySelector("article").outerHTML)
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
    }
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
