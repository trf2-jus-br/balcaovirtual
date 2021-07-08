<template>
  <div class="container-fluid content profile" v-if="voto">
    <div class="row mt-3" v-if="errormsg !== undefined">
      <div class="col col-sm-12">
        <p class="alert alert-danger">
          {{ errormsg }}
        </p>
      </div>
    </div>

    <div class="row mt-3 mb-3">
      <div class="col-md-12">
        <h4 class="text-center mb-0" v-if="voto">{{ voto.tipoDoVoto }} {{ voto.numeroDoVoto }}</h4>
      </div>
    </div>

      <div class="row">
        <div class="col col-lg-8">
          <div class="row no-gutters mt-2">
            <div class="col col-auto mb-3">
              <router-link :to="{ name: 'Lista de Votos', params: { manter: false } }" tag="button" class="btn btn-light d-print-none"
                ><span class="fa fa-times"></span> Voltar</router-link
              >
            </div>
            <div class="col col-auto mb-3">
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
                ><span class="fa fa-arrow-left"></span> Anterior</router-link
              >
            </div>
            <div class="col col-auto mb-3">
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
                ><span class="fa fa-arrow-right"></span> Próximo</router-link
              >
            </div>
          </div>
          <div v-if="diferencas && exibirDiferencas">
            <div class="card mb-3">
              <div
                :class="{
                  'card-body': true,
                  'alert-success': voto.similaridade === 1.0,
                  'alert-primary': voto.similaridade < 1.0,
                }"
              >
                <p class="card-text" v-html="diferencas"></p>
              </div>
            </div>
            <p class="text-muted" style="font-size: 80%;" v-if="diferencas && voto.similaridade < 1.0">
              Em relação ao <router-link :to="{ name: 'Padrao', params: { numero: voto.idPadrao } }">padrão</router-link>, palavas
              incluídas aparecem em <span class="editNewInline">verde</span>, excluídas em <span class="editOldInline">vermelho</span> e
              alteradas em <span class="replaceInline">roxo</span>.
            </p>
            <p class="text-muted" style="font-size: 80%;" v-if="diferencas && voto.similaridade === 1.0">
              O fundo verde indica que o conteúdo do voto é exatamente igual ao
              <router-link :to="{ name: 'Padrao', params: { numero: voto.idPadrao } }">padrão</router-link>.
            </p>
          </div>
          <div class="card mb-3" v-show="!diferencas || !exibirDiferencas">
            <div class="card-body alert-warning">
              <p ref="conteudo" class="card-text" v-html="conteudo"></p>
            </div>
          </div>
          <b-form-checkbox v-show="diferencas && voto.similaridade < 1.0" v-model="exibirDiferencas" name="check-button" switch>
            Exibir diferenças em relação ao padrão
          </b-form-checkbox>
        </div>
        <div class="col col-lg-4">
          <div class="row no-gutters mt-2">
            <div class="col col-auto ml-auto mb-3 d-none d-lg-block"></div>
            <div class="col col-auto ml-1 mb-3" v-if="!voto.disabled">
              <button @click.prevent="acompanhar()" type="button" class="btn btn-primary d-print-none">
                <span class="fa fa-check"></span> Acompanhar
              </button>
            </div>
            <div class="col col-auto ml-1 mb-3" v-if="!voto.disabled">
              <button @click.prevent="pedirVista()" type="button" class="btn btn-info d-print-none">
                <span class="fa fa-eye"></span> Pedir Vista
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
                <br />Responsável: {{ voto.nomeDoUsuarioQueIncluiu }} <br />Status: {{ voto.descricaoDoStatus }}
              </p>
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
          if (this.voto === this.lista[i]) return i;
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
    }
  },
  methods: {
    preprocess: function(s) {
      if (!s) return;
      return s
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
      Bus.$emit("votar", [this.voto], this.exibeProximoVoto);
      // Bus.$emit('assinarComSenha', [this.doc], () => this.$router.go(-1))
    },

    pedirVista: function() {
      Bus.$emit("pedirVista", [this.voto], this.exibeProximoVoto);
      // Bus.$emit('assinarComSenha', [this.doc], () => this.$router.go(-1))
    },

    exibeProximoVoto: function() {
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

  components: {
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
