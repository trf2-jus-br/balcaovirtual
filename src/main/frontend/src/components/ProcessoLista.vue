<template>
  <div class="container-fluid content">
    <div class="row">
      <div class="col-md-12">
        <h4 class="text-center mt-3 mb-3">Lista de Processos</h4>
      </div>
      <div class="col col-sm-12" v-if="tentouBaixar">
        <p class="alert alert-danger">
          <strong>Atenção!</strong> Não foi possível detectar a extensão do
          Chrome que é utilizada para realizar downloads em lote. Ela pode ser
          instalada acessando a página da
          <a
            href="https://chrome.google.com/webstore/detail/trf2-download-manager/komegelldppbjndifhabfpjpddjaocfa"
          >
            <u>Chrome Web Store</u> </a
          >. Também verifique na Lista de Extensões (Menu/Mais
          ferramentas/Extensões) se 'TRF2 Download Manager' está presente e
          ativa.
        </p>
      </div>
    </div>

    <div class="row d-print-none">
      <div class="col-sm-auto mb-3">
        <div class="btn-group">
          <label
            class="btn"
            :class="{
              'active btn-primary': pasta === 'inbox',
              'btn-outline-primary': pasta !== 'inbox'
            }"
          >
            <input
              v-show="false"
              type="radio"
              v-model="pasta"
              value="inbox"
              autocomplete="off"
            />
            <span class="fa fa-inbox"></span> Caixa de Entrada
          </label>
          <label
            class="btn btn-outline-primary"
            :class="{
              'active btn-primary': pasta === 'recente',
              'btn-outline-primary': pasta !== 'recente'
            }"
          >
            <input
              v-show="false"
              type="radio"
              v-model="pasta"
              value="recente"
              autocomplete="off"
            />
            <span class="fa fa-history"></span> Recentes
          </label>
          <label
            class="btn btn-outline-primary"
            :class="{
              'active btn-primary': pasta === 'favorito',
              'btn-outline-primary': pasta !== 'favorito'
            }"
          >
            <input
              v-show="false"
              type="radio"
              v-model="pasta"
              value="favorito"
              autocomplete="off"
            />
            <span class="fa fa-star"></span> Favoritos
          </label>
        </div>
      </div>

      <div class="col-sm-auto mr-sm-auto mb-3">
        <div class="input-group">
          <div class="input-group-addon">&#128269;</div>
          <input
            type="text"
            class="form-control"
            placeholder="Filtrar"
            v-model="filtro"
            ng-model-options="{ debounce: 200 }"
          />
        </div>
      </div>
      <div class="col-sm-auto mb-3" v-if="false">
        <div class="btn-group btn-block" role="group">
          <button
            id="btnGroupDrop1"
            type="button"
            class="btn btn-secondary dropdown-toggle btn-block"
            data-toggle="dropdown"
            aria-haspopup="true"
            aria-expanded="false"
          >
            Avançado
          </button>
          <div class="dropdown-menu" aria-labelledby="btnGroupDrop1">
            <a class="dropdown-item" @click="exibirProcessosMultiplos"
              >Inserir Múltiplos Processos</a
            >
          </div>
        </div>
      </div>
      <div class="col-sm-auto mb-3">
        <button
          type="button"
          @click="baixarEmLote()"
          class="btn btn-primary"
          title="Inserir este PDF em múltiplos processos"
        >
          Baixar Completo&nbsp;&nbsp;
          <span class="badge badge-pill badge-warning">{{
            filtradosMarcadosDigitaisEAcessiveis.length
          }}</span>
        </button>
      </div>
    </div>

    <div class="row" v-if="filtrados.length == 0">
      <div class="col col-sm-12">
        <p class="alert alert-warning">
          <strong>Atenção!</strong> Nenhum processo na lista.
        </p>
      </div>
    </div>

    <div class="row" v-if="filtrados.length > 0">
      <div class="col-sm-12">
        <div class="table-responsive">
          <table class="table table-striped table-sm">
            <thead class="thead-dark">
              <tr>
                <th style="text-align: center">
                  <input
                    type="checkbox"
                    id="progress_checkall"
                    name="progress_checkall"
                    v-model="todos"
                    @change="marcarTodos()"
                  />
                </th>
                <th>Processo</th>
                <th>Autor</th>
                <th>Réu</th>
                <th>Último Movimento</th>
                <th>Sistema/Órgão</th>
                <th>Unidade</th>
                <th>Suporte</th>
                <th>Acesso</th>
                <th>Status</th>
                <th style="text-align: center"></th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="p in filtrados" :key="p.numero">
                <td style="text-align: center">
                  <input
                    type="checkbox"
                    v-model="p.checked"
                    :disabled="p.disabled"
                  />
                </td>
                <td>
                  <span class=" unbreakable">
                    <router-link
                      :to="{
                        name: 'Processo',
                        params: { numero: p.numero },
                        query: { avisos: $parent.cAvisos }
                      }"
                      target="_blank"
                      >{{ p.numeroFormatado }}</router-link
                    >
                  </span>
                </td>
                <td>{{ p.autor }}</td>
                <td>{{ p.reu }}</td>
                <td>
                  <span
                    :class="{
                      destaque:
                        p.recente === undefined ||
                        (p.dataultimomovimento !== undefined &&
                          p.recente < p.dataultimomovimento)
                    }"
                    v-html="p.dataultimomovimentoFormatada"
                  ></span>
                </td>
                <td>
                  <span :title="'Sigla do Sistema: ' + p.sistema">{{
                    $parent.test.properties[
                      "balcaovirtual." + p.sistema + ".name"
                    ]
                  }}</span>
                </td>
                <td>{{ p.unidade }}</td>
                <td>{{ p.digitalFormatado }}</td>
                <td>{{ p.acesso }}</td>
                <td class="status-td">
                  <span v-if="p.state == 'building'">
                    {{ p.status }}
                    <span v-if="p.bytes">- {{ p.bytes }}</span>
                  </span>
                  <span v-if="p.state == 'ready'">Preparado</span>
                  <span v-if="p.state == 'set'">Solicitado</span>
                  <span v-if="p.state == 'go'">Iniciado</span>
                  <span
                    v-if="p.state=='in_progress' &amp;&amp; p.perc===undefined"
                    >Aguardando...</span
                  >
                  <span
                    v-if="p.state=='in_progress' &amp;&amp; p.perc !==undefined"
                    >{{ p.perc }}%</span
                  >
                  <span class="green" v-if="p.state == 'complete'"
                    >Baixado</span
                  >
                  <span v-if="p.errormsg" :class="{ red: true }"
                    >{{ p.errormsg }}
                  </span>
                </td>

                <td align="right">
                  <a
                    v-if="!p.favorito"
                    href=""
                    @click.prevent="sinalizar(p, { favorito: true })"
                  >
                    <span class="fa fa-star-o icone-em-linha"></span>
                  </a>
                  <a
                    v-if="p.favorito"
                    href=""
                    @click.prevent="sinalizar(p, { favorito: false })"
                  >
                    <span class="fa fa-star icone-em-linha"></span>
                  </a>
                  <template v-if="pasta !== 'favorito'">
                    <a href="" @click.prevent="remover(p)">
                      <span class="fa fa-remove icone-em-linha"></span>
                    </a>
                  </template>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-sm-12" style="padding-top: 1em;">
        <button
          v-if="pasta === 'inbox'"
          type="button "
          @click="exibirProcessosMultiplos"
          class="btn btn-primary float-right d-print-none mt-3 "
        >
          Inserir Múltiplos Processos
        </button>
        <button
          v-if="pasta === 'recente'"
          type="button "
          @click="sinalizarEmLoteSilenciosamente({ recente: false })"
          id="prosseguir"
          :disabled="sinalizandoEmLote"
          class="btn btn-primary float-right d-print-none mt-3 "
        >
          Remover da Lista&nbsp;&nbsp;
          <span class="badge badge-pill badge-warning">{{
            filtradosEMarcados.length
          }}</span>
        </button>
      </div>
    </div>
    <processo-multiplos
      ref="processosMultiplos"
      :show.sync="exibirProcessoMultiplos"
      @ok="acrescentarProcessosNaLista"
    ></processo-multiplos>
  </div>
</template>

<script>
import ProcessoBL from "../bl/processo.js";
import UtilsBL from "../bl/utils.js";
import ProcessoMultiplos from "./ProcessoMultiplos";
import { Bus } from "../bl/bus.js";

/* global chrome */
export default {
  components: {
    ProcessoMultiplos
  },

  mounted() {
    var i;

    setTimeout(() => {
      this.versionTRF2DownloadChromeExtension = document.getElementById(
        "trf2-download-chrome-extension-active"
      ).value;
    }, 500);

    this.errormsg = undefined;

    setTimeout(() => {
      if (this.$route.params && this.$route.params.processos) {
        this.pasta = "inbox";
        for (i = 0; i < this.$route.params.processos.length; i++) {
          var p = this.fixProcesso(this.$route.params.processos[i]);
          p.inbox = true;
          this.processos.push(p);
        }
      }
      this.$http.get("processo/listar-sinais", { block: true }).then(
        response => {
          var favorito, recente;
          var list = response.data.list;
          for (var i = 0; i < list.length; i++) {
            var s = list[i];
            var p;
            for (var j = 0; j < this.processos.length; j++) {
              if (s.numero === this.processos[j].numero) {
                p = this.processos[j];
                break;
              }
            }
            if (p) {
              this.processos[j].recente = s.recente;
              this.processos[j].favorito = s.favorito;
            } else {
              this.processos.push(this.fixProcesso(s));
            }
            if (s.favorito) favorito = true;
            if (s.recente) recente = true;
          }
          if (this.pasta === undefined) {
            if (favorito) this.pasta = "favorito";
            else if (recente) this.pasta = "recente";
            else this.pasta = "inbox";
          }
          this.validarEmLoteSilenciosamente();
        },
        error => UtilsBL.errormsg(error, this)
      );
    });
  },

  data() {
    return {
      pasta: undefined,
      filtro: undefined,
      todos: true,
      processos: [],
      versionTRF2DownloadChromeExtension: undefined,
      downloadExtensionId: "komegelldppbjndifhabfpjpddjaocfa",
      map: {},
      exibirProcessoMultiplos: false,
      tentouBaixar: false,
      sinalizandoEmLote: false
    };
  },

  computed: {
    filtrados: function() {
      var a = this.processos;
      a = UtilsBL.filtrarPorSubstring(a, this.filtro);
      a = a.filter(o => !!o[this.pasta]);
      return a;
    },

    filtradosEMarcados: function() {
      return this.filtrados.filter(function(item) {
        return item.checked;
      });
    },
    filtradosMarcadosDigitaisEAcessiveis: function() {
      return this.filtradosEMarcados.filter(function(item) {
        return item.digital === true && item.usuarioautorizado === true;
      });
    }
  },
  methods: {
    fixProcesso: function(p) {
      UtilsBL.applyDefauts(p, {
        dataultimomovimento: undefined,
        dataultimomovimentoFormatada: undefined,
        numero: undefined,
        numeroFormatado: undefined,
        sistema: undefined,
        unidade: undefined,
        usuarioautorizado: undefined,
        segredodejustica: undefined,
        segredodejusticadesistema: undefined,
        segredodejusticaabsoluto: undefined,
        acesso: undefined,
        digital: undefined,
        digitalFormatado: undefined,
        validade: undefined,
        checked: true,
        disabled: false,
        state: undefined,
        errormsg: undefined,
        perc: undefined,
        bytes: undefined,
        status: undefined,
        uuid: undefined,
        inbox: undefined,
        favorito: undefined,
        recente: undefined
      });
      if (p.dataultimomovimento !== undefined) {
        p.dataultimomovimentoFormatada = UtilsBL.formatJSDDMMYYYYHHMM(
          p.dataultimomovimento
        );
      }
      if (p.numero !== undefined) {
        p.numero = ProcessoBL.somenteNumeros(p.numero);
        p.numeroFormatado = ProcessoBL.formatarProcesso(p.numero);
      }
      if (p.validado) {
        if (p.digital !== undefined)
          p.digitalFormatado = p.digital ? "Digital" : "Físico";
        if (
          (p.segredodejusticaabsoluto || p.segredodejusticadesistema) &&
          p.usuarioautorizado
        )
          p.acesso = "Autorizado";
        else if (p.segredodejusticaabsoluto) p.acesso = "Segredo Absoluto";
        else if (p.segredodejusticadesistema) p.acesso = "Segredo de Sistema";
        else p.acesso = "Público";
      }
      return p;
    },

    validarEmLoteSilenciosamente2: function() {},

    validarEmLoteSilenciosamente: function() {
      var a = this.processos.filter(function(item) {
        return !item.validado;
      });
      // Prepara um mapa para aplicar os resultados
      var map = {};
      for (var i = 0; i < a.length; i++) {
        map[a[i].numero] = a[i];
      }
      // Prepara um array onde cada elemento tem no máximo 50 números separados por vírgulas
      var b = [];
      while (a.length > 0) {
        console.log(a.length);
        var l = a.splice(0, a.length > 50 ? 50 : a.length);
        var numeros = [];
        for (var j = 0; j < l.length; j++) {
          numeros.push(l[j].numero);
        }
        b.push(numeros.join(","));
      }
      UtilsBL.quietBatch(b, (processos, cont) => {
        this.$http.get("processo/" + processos + "/validar").then(
          response => {
            // Prepara um outro mapa com a lista resultante
            var map2 = {};
            for (i = 0; i < response.data.list.length; i++) {
              map2[response.data.list[i].numero] = response.data.list[i];
            }
            var l2 = processos.split(",");
            for (i = 0; i < l2.length; i++) {
              var processo = map[l2[i]];
              if (map2[l2[i]]) {
                UtilsBL.overrideProperties(processo, map2[l2[i]]);
                processo.validado = true;
                this.fixProcesso(processo);
              } else {
                processo.checked = false;
                processo.disabled = true;
                processo.errormsg = "Processo não encontrado";
              }
            }
            cont();
          },
          error => {
            var l3 = processos.split(",");
            for (i = 0; i < l3.length; i++) {
              var processo = map[l3[i]];
              processo.checked = false;
              processo.disabled = true;
              processo.errormsg = error.data.errormsg;
            }
            cont();
          }
        );
      });
    },

    baixarEmLote: function() {
      if (this.versionTRF2DownloadChromeExtension === "0") {
        this.tentouBaixar = true;
        return;
      }
      var processos = this.filtradosMarcadosDigitaisEAcessiveis;
      if (processos.length > 0) {
        this.errormsg = undefined;
        this.obterJwt(processos, 0);
      }
    },

    obterJwt: function(processos, i) {
      processos[i].jwt = undefined;
      processos[i].state = undefined;
      processos[i].perc = undefined;
      this.$http
        .get(
          "processo/" +
            processos[i].numero +
            "/pdf?sistema=" +
            processos[i].sistema
        )
        .then(
          response => {
            if (i + 1 < processos.length)
              this.$nextTick(() => this.obterJwt(processos, i + 1));
            else this.$nextTick(this.continuarBaixando);
            processos[i].jwt = response.data.jwt;
            processos[i].uuid = response.data.uuid;
            processos[i].state = "building";
            processos[i].errormsg = undefined;
          },
          error => {
            if (i + 1 < processos.length) this.obterJwt(processos, i + 1);
            processos[i].errormsg = error.data.errormsg;
          }
        );
    },

    continuarBaixando: function() {
      if (this.baixando() === 0) return;
      this.baixarProximo();
      this.atualizar();
      this.atualizarStatusDeConstrucao();
      setTimeout(this.continuarBaixando, 1000);
    },

    baixando: function() {
      var c = 0;
      var processos = this.filtradosEMarcados;
      for (var i = 0; i < processos.length; i++) {
        if (
          processos[i].jwt &&
          ["building", "ready", "set", "go", "in_progress"].indexOf(
            processos[i].state
          ) >= 0
        )
          c++;
      }
      return c;
    },

    atualizarStatusDeConstrucao: function() {
      var processos = this.filtradosEMarcados;
      for (var i = 0; i < processos.length; i++) {
        if (processos[i].jwt && processos[i].state === "building") {
          this.atualizarStatusDeConstrucaoDeProcesso(processos[i]);
        }
      }
    },

    atualizarStatusDeConstrucaoDeProcesso: function(processo) {
      this.$http.get("status/" + processo.uuid).then(
        response => {
          var r = response.data;
          processo.status = r.mensagem;
          processo.progressbarWidth = 100 * (r.indice / r.contador);
          processo.bytes = r.bytes ? UtilsBL.formatBytes(r.bytes) : undefined;
          if (r.indice === r.contador) {
            processo.state = "ready";
          }
        },
        error => {
          processo.state = "error";
          processo.errormsg =
            error.data.errormsg || "Erro obtendo informações de status";
        }
      );
    },

    atualizar: function() {
      chrome.runtime.sendMessage(
        this.downloadExtensionId,
        {
          command: "updates"
        },
        response => {
          if (!response) return;
          if (response.success) {
            var updates = response.data;
            for (var id in updates) {
              if (!updates.hasOwnProperty(id)) continue;
              this.atualizarProcesso(updates[id]);
            }
          } else {
            console.log("error updating");
          }
        }
      );
    },

    atualizarProcesso: function(update) {
      console.log("AtualizarProcessoL update");
      console.log(update);
      console.log("AtualizarProcesso: map");
      console.log(this.map);
      var processo = this.map[update.id];
      console.log("AtualizarProcessoL processo");
      console.log(processo);
      if (update.state && update.state.current)
        processo.state = update.state.current;
      if (update.error) processo.errormsg = update.error.current;
      if (update.received && update.total)
        processo.perc = Math.round((update.received / update.total) * 100);
      if (update.state && update.state.current !== "in_progress")
        delete processo.perc;
      if (update.state && update.state.current === "complete") {
        processo.checked = false;
        processo.state = "complete";
      }
    },

    baixarProximo: function() {
      var processos = this.filtradosEMarcados;
      for (var i = 0; i < processos.length; i++) {
        if (processos[i].jwt && processos[i].state === "ready") {
          processos[i].state = "set";
          this.baixar(processos[i]);
          return true;
        }
      }
      return false;
    },

    baixar: function(processo) {
      var url =
        this.$http.options.root +
        "/download/" +
        processo.jwt +
        "/" +
        processo.numeroFormatado +
        ".pdf?disposition=attachment";
      if (url.substring(0, 4) !== "http") {
        var host =
          window.location.protocol +
          "//" +
          window.location.hostname +
          (window.location.port && window.location.port !== ""
            ? ":" + window.location.port
            : "");
        url = host + url;
      }
      chrome.runtime.sendMessage(
        this.downloadExtensionId,
        {
          command: "download",
          url: url,
          filename: processo.numeroFormatado + ".pdf"
        },
        response => {
          if (!response.success) {
            processo.erromsg = response.data.erromsg;
            console.log("error downloading: ", response.data.erromsg);
          }

          processo.state = "go";
          processo.downloadId = response.data.id;
          this.$set(this.map, processo.downloadId, processo);
        }
      );
      UtilsBL.logEvento(
        "consulta-processual",
        "mostrar pdf completo",
        "em lote"
      );
    },

    remover: function(p) {
      if (this.pasta === "inbox") {
        p.inbox = undefined;
        this.removerProcessoDesnecessario(p);
      }
      if (this.pasta === "recente") this.sinalizar(p, { recente: false });
      if (this.pasta === "favorito") this.sinalizar(p, { favorito: false });
    },

    removerProcessoDesnecessario: function(p) {
      if (!p.inbox && !p.recente && !p.favorito) {
        for (var i = 0; i < this.processos.length; i++) {
          if (p === this.processos[i]) this.processos.splice(i, 1);
        }
      }
    },

    exibirProcessosMultiplos: function() {
      this.$refs.processosMultiplos.show();
    },

    acrescentarProcessosNaLista: function(arr) {
      console.log(arr);
      if (!arr || arr.length === 0) return;
      this.pasta = "inbox";
      for (var i = 0; i < arr.length; i++) {
        if (arr[i] === "") continue;
        var p = this.fixProcesso({
          numero: arr[i],
          inbox: true
        });
        this.processos.push(p);
      }
      this.validarEmLoteSilenciosamente();
    },

    marcarTodos: function() {
      var docs = this.filtrados;
      for (var i = 0; i < docs.length; i++) {
        var doc = docs[i];
        doc.checked = this.todos;
      }
    },

    sinalizar: function(p, sinais, lote) {
      this.errormsg = undefined;
      if (sinais.favorito !== undefined) p.favorito = sinais.favorito;
      if (lote) Bus.$emit("prgCaption", "Sinalizando " + p.numeroFormatado);

      this.$http
        .post("processo/" + p.numero + "/sinalizar", sinais, { block: !lote })
        .then(
          response => {
            var d = response.data;
            p.favorito = d.processo.favorito;
            p.recente = d.processo.recente;
            this.removerProcessoDesnecessario(p);
            UtilsBL.logEvento("processo", "sinalizar");
            if (lote) Bus.$emit("prgNext");
          },
          error => {
            p.errormsg = error.data.errormsg;
            if (lote) Bus.$emit("prgNext");
          }
        );
    },

    sinalizarEmLoteSilenciosamente: function(sinais) {
      var a = this.filtradosEMarcados.filter(function() {
        return true;
      });
      this.sinalizandoEmLote = true;
      UtilsBL.quietBatch(
        a,
        (p, cont) => {
          this.$http.post("processo/" + p.numero + "/sinalizar", sinais).then(
            response => {
              var d = response.data;
              p.favorito = d.processo.favorito;
              p.recente = d.processo.recente;
              this.removerProcessoDesnecessario(p);
              UtilsBL.logEvento("processo", "sinalizar");
              cont();
            },
            error => {
              p.errormsg = error.data.errormsg;
              cont();
            }
          );
        },
        () => {
          this.sinalizandoEmLote = false;
        }
      );
    }

    // confirmarEmLote: function () {
    //   var a = this.filtradosEMarcados
    //   Bus.$emit('prgStart', 'Confirmando Intimações/Citações', a.length, (i) => this.confirmarAviso(a[i], ))
    // }
  }
};
</script>

<style scoped>
.destaque {
  color: red;
}
</style>
