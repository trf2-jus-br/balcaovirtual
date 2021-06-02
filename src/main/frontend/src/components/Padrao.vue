<template>
  <div class="container-fluid content profile">
    <div class="row mt-3 mb-3">
      <div class="col-md-12">
        <h4 class="text-center mb-0">Padrão {{ numero }}</h4>
      </div>
    </div>

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
  name: "padrao",
  mounted() {
    if (this.numero)
      this.$nextTick(function() {
        this.$http.get("padrao/" + this.numero, { block: true }).then(
          (response) => {
            this.buffer = response.data.padrao.conteudo;
          },
          (error) => UtilsBL.errormsg(error, this)
        );
      });
  },
  data() {
    return {
      numero: this.$route.params.numero,
      buffer: undefined,
    };
  },
  computed: {},
  methods: {
    salvar: function() {
      this.buffer = this.$refs["editarea"].innerHTML;
      this.$http
        .post(
          "padrao",
          {
            id: this.numero,
            html: this.buffer,
          },
          { block: true }
        )
        .then(
          (response) => {
            UtilsBL.logEvento("padrao", "salvar");
            this.$router.back();
          },
          (error) => UtilsBL.errormsg(error, this)
        );
    },
    cancelar() {
      this.$router.back();
    },
  },
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

.bveditor {
  border: 1px solid black;
  padding: 1em;
}
</style>
