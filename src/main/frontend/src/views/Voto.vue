<template>
  <div class="container-fluid content profile" v-if="voto">
    <div class="row mt-3" v-if="errormsg !== undefined">
      <div class="col col-12">
        <p class="alert alert-danger">
          {{ errormsg }}
        </p>
      </div>
    </div>

    <div class="row mt-3 mb-3">
      <div class="col col-12">
        <h4 class="text-center mb-0" v-if="voto">
          {{ voto.tipoDoDocumento ? voto.tipoDoDocumento : "Voto" }} {{ voto.numeroDoDocumento }}
        </h4>
      </div>
    </div>

    <div class="row">
      <div class="col col-12 col-lg-8">
        <div class="row no-gutters mt-2">
          <div class="col col-auto mb-3">
            <router-link
              :to="{ name: 'Lista de Votos', params: { manter: false } }"
              tag="button"
              class="btn btn-light d-print-none"
              v-b-popover.hover.top="'Utilize esse botão para retornar para a lista de votos.'"
              title="Lista"
              ><span class="fa fa-list"></span> Lista {{ progresso }}</router-link
            >
          </div>
          <div class="col col-auto mb-3 ml-1">
            <router-link
              :disabled="!anteriorVoto"
              :to="{
                name: 'Voto',
                params: {
                  numero: (anteriorVoto || {}).id,
                  lista: this.lista,
                  transitionName: 'slide-right',
                },
              }"
              tag="button"
              class="btn btn-light d-print-none"
              v-b-popover.hover.top="'Utilize esse botão para navegar para o voto anterior.'"
              title="Anterior"
              ><span class="fa fa-arrow-left"></span> Anterior</router-link
            >
          </div>
          <div class="col col-auto mb-3 ml-1">
            <router-link
              :disabled="!proximoVoto"
              :to="{
                name: 'Voto',
                params: {
                  numero: (proximoVoto || {}).id,
                  lista: this.lista,
                  transitionName: 'slide-left',
                },
              }"
              tag="button"
              class="btn btn-light d-print-none"
              v-b-popover.hover.top="'Utilize esse botão para navegar para o próximo voto.'"
              title="Próximo"
              ><span class="fa fa-arrow-right"></span> Próximo</router-link
            >
          </div>
        </div>
        <div class="card mb-3">
          <div class="card-body alert-warning">
            <p class="card-text" v-html="conteudo"></p>
          </div>
        </div>
      </div>
      <div class="col col-12 col-lg-4">
        <div class="row no-gutters mt-2">
          <div class="col col-auto ml-auto mb-3 d-none d-lg-block"></div>
          <div class="col col-auto ml-1 mb-3" v-if="!voto.disabled">
            <button
              @click.prevent="acompanhar()"
              type="button"
              class="btn btn-success d-print-none"
              v-b-popover.hover.top="'Utilize esse botão para indicar que deseja acompanhar o relator.'"
              title="Acompanhar o Relator"
            >
              <span class="fa fa-thumbs-o-up"></span>
            </button>
          </div>
          <div class="col col-auto ml-1 mb-3" v-if="!voto.disabled">
            <button
              @click.prevent="divergir()"
              type="button"
              class="btn btn-danger d-print-none"
              v-b-popover.hover.top="'Utilize esse botão para indicar que deseja acompanhar a divergência.'"
              title="Acompanhar a Divergência"
            >
              <span class="fa fa-thumbs-o-down"></span>
            </button>
          </div>
          <div class="col col-auto ml-1 mb-3" v-if="!voto.disabled">
            <button
              @click.prevent="pedirVista()"
              type="button"
              class="btn btn-warning d-print-none"
              v-b-popover.hover.top="'Utilize esse botão para indicar que deseja pedir vista.'"
              title="Pedir Vista"
            >
              <span class="fa fa-eye"></span>
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
                  params: { numero: voto.numeroDoProcesso },
                  query: { avisos: $parent.cAvisos },
                }"
                target="_blank"
                >{{ voto.processoFormatado }}</router-link
              >
              <br />Cadastro:
              <span v-html="voto.dataDeInclusaoFormatada"></span>
              <br />Relator: {{ voto.relator }}
            </p>
          </div>
        </div>
        <div class="card mb-3">
          <div class="card-header">
            Votos Proferidos
            <span class="float-right" v-if="voto.acompanhamentos != '0' || voto.divergencias != '0'">
              {{ voto.acompanhamentos }} x {{ voto.divergencias }}</span
            >
          </div>
          <div class="card-body p-0">
            <table class="table table-striped table-sm mb-0">
              <thead class="">
                <tr>
                  <th>Data</th>
                  <th>Magistrado</th>
                  <th>Voto</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="v in voto.votosProferidos" :key="v.magistrado">
                  <td>{{ v.dataDeInclusaoFormatada }}</td>
                  <td>{{ v.magistrado }}</td>
                  <td>{{ v.voto }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div class="card" v-if="voto.lembretes">
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
                  <tr v-for="lembrete in voto.lembretes" :key="lembrete.id">
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
</template>

<script>
import { Bus } from "../bl/bus.js";

export default {
  name: "Voto",
  mounted() {
    if (!this.$store.state.votos) this.$store.dispatch("carregarVotos");
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
      return this.$route.params.lista ? this.$route.params.lista : this.$store.state.votos;
    },
    voto() {
      if (!this.$store.state.votos) return;
      for (var i = 0; i < this.$store.state.votos.length; i++) {
        if (this.$store.state.votos[i].id === this.$route.params.numero) return this.$store.state.votos[i];
      }
      return undefined;
    },
    indice: function() {
      if (this.lista)
        for (var i = 0; i < this.lista.length; i++) {
          if (this.voto.id === this.lista[i].id) return i;
        }
      return 0;
    },
    proximoVoto: function() {
      if (!this.lista) return;
      if (this.indice >= this.lista.length - 1) return;
      return this.lista[this.indice + 1];
    },
    anteriorVoto: function() {
      if (!this.lista) return;
      if (this.indice === 0) return;
      return this.lista[this.indice - 1];
    },
    conteudo() {
      return this.preprocess(this.voto.conteudo);
    },
    progresso: function() {
      if (this.indice === undefined) return;
      if (!this.lista || this.lista.length === 0) return;
      return this.indice + 1 + "/" + this.lista.length;
    },
  },
  methods: {
    preprocess: function(s) {
      if (!s) return;
      if (s.includes("<body>")) s = s.substring(s.indexOf("<body>") + 6);
      if (s.includes("</body>")) s = s.substring(0, s.indexOf("</body>"));
      return s
        .replaceAll("\\t", " ")
        .replaceAll("\\n", " ")
        .replace('contentEditable="true"', 'contentEditable="false" data-bv_edit="true"')
        .replace('contenteditable="true"', 'contentEditable="false" data-bv_edit="true"')
        .replace(/&#x2013;/g, "-");
    },
    posprocess: function(s) {
      if (!s) return;
      return s.replace('contentEditable="false" data-bv_edit="true"', 'contentEditable="true"');
    },
    voltar: function() {
      this.$router.go(-1);
    },

    acompanhar: function() {
      Bus.$emit("acompanhar", [this.voto], this.exibeProximoVoto);
    },

    divergir: function() {
      Bus.$emit("divergir", [this.voto], this.exibeProximoVoto);
    },

    pedirVista: function() {
      Bus.$emit("pedirVista", [this.voto], this.exibeProximoVoto);
    },

    exibeProximoVoto: function() {
      if (this.$store.state.errorMsg) return;
      if (this.proximoVoto) {
        this.$router.push({
          name: "Voto",
          params: {
            numero: this.proximoVoto.id,
            lista: this.lista,
            transitionName: "slide-left",
          },
        });
      } else {
        this.$router.push({ name: "Lista de Votos", params: { manter: false } });
      }
    },

    imprimir: function() {
      window.print();
    },
  },

  components: {},
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
