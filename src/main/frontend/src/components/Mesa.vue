<template>
  <div class="container-fluid content">
    <div class="row">
      <div class="col-md-12">
        <h4 class="text-center mt-3 mb-3">Mesa Virtual</h4>
      </div>
      <div class="col col-sm-12" v-if="errormsg">
        <p class="alert alert-danger">
          <strong>Erro!</strong> {{errormsg}}
        </p>
      </div>
    </div>

    <div class="row mb-3 d-print-none">
      <div class="col-sm-auto ml-1">
        <div class="input-group">
          <div class="input-group-addon">
            <span class="fa fa-map-marker"></span>
          </div>
          <select id="mesa" class="form-control" v-model="mesa" @change="selecionarMesa" name="mesa">
            <option disabled selected hidden :value="undefined">[Selecionar]</option>
            <option v-for="i in mesas" :value="i.id">{{i.nome}}</option>
          </select>
        </div>
      </div>
      <div class="col-sm-auto ml-1">
        <div class="input-group">
          <div class="input-group-addon">
            <span class="fa fa-search"></span>
          </div>
          <input type="text" class="form-control" placeholder="Filtrar" v-model="filtro" ng-model-options="{ debounce: 200 }">
        </div>
      </div>
      <div class="col-sm-auto ml-auto">
        <button type="button" @click="criar()" class="btn btn-primary ml-1" title="">
          Assinar&nbsp;&nbsp
          <span class="badge badge-pill badge-warning">{{filtradosEMarcados.length}}</span>
        </button>
      </div>
    </div>

    <div class="row" v-if="filtrados.length == 0">
      <div class="col col-sm-12">
        <p class="alert alert-warning">
          <strong>Atenção!</strong> Nenhuma documento na mesa.
        </p>
      </div>
    </div>

    <div class="row" v-if="filtrados.length > 0">
      <div class="col-sm-12">
        <table class="table table-striped table-sm table-responsive">
          <thead class="thead-inverse">
            <tr>
              <th style="text-align: center">
                <input type="checkbox" id="progress_checkall" name="progress_checkall" v-model="todos" @change="marcarTodos()"></input>
              </th>
              <th>Documento</th>
              <th>Processo</th>
              <th>Situação</th>
              <th>Motivo</th>
              <th>Origem</th>
              <th>Data/Hora</th>
              <th style="text-align: center"></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="f in filtrados">
              <td style="text-align: center">
                <input type="checkbox" v-model="f.checked" :disabled="f.disabled"></input>
              </td>
              <td>{{f.documento}}</td>
              <td>
                <span class="unbreakable">
                  <router-link :to="{name: 'Processo', params: {numero: f.processo}}" target="_blank">{{f.processoFormatado}}</router-link>
                </span>
              </td>
              <td>{{f.situacao}}
                <span v-if="f.errormsg" :class="{red: true}">Erro {{f.errormsg}}
                </span>
              </td>
              <td>{{f.motivo}}</td>
              <td>{{f.origem}}</td>
              <td>{{f.datahora}}</td>
              <td align="right">
                <a href="" @click.prevent="acrescentar(f)">
                  <span class="fa fa-certificate icone-em-linha" title="Assinar Digitalmente"></span>
                </a>
                <a v-if="f.quantidade > 0" href="" @click.prevent="subtrair(f)">
                  <span class="fa fa-minus icone-em-linha"></span>
                </a>
                <a v-if="f.quantidade > 0" href="" @click.prevent="editar(f)">
                  <span class="fa fa-pencil icone-em-linha"></span>
                </a>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import UtilsBL from '../bl/utils.js'
import ProcessoBL from '../bl/processo.js'
// import { Bus } from '../bl/bus.js'

export default {
  components: {
  },

  mounted () {
    this.errormsg = undefined

    setTimeout(() => {
      this.carregarMesas()
    })
  },

  data () {
    return {
      mesa: undefined,
      mesas: [],
      filtro: undefined,
      lista: [],
      todos: true,
      errormsg: undefined
    }
  },

  computed: {
    filtrados: function () {
      var a = this.lista
      a = UtilsBL.filtrarPorSubstring(a, this.filtro)
      return a
    },

    filtradosEMarcados: function () {
      return this.filtrados.filter(function (item) {
        return item.checked
      })
    }
  },

  methods: {
    carregarMesas: function () {
      this.$http.get('mesa', { block: true }).then(
        response => {
          var list = response.data.list
          for (var i = 0; i < list.length; i++) {
            this.mesas.push({ id: list[1].id, nome: list[i].nome })
          }
        },
        error => {
          this.mesas.push({
            id: '1',
            nome: 'Balcão de Entrada - 12VF'
          })
          this.mesas.push({
            id: '2',
            nome: 'Gabinete - 12VF'
          })
          this.mesa = '1'
          this.selecionarMesa()
          /* UtilsBL.errormsg(error, this) */
          return error
        }
      )
    },

    selecionarMesa: function () {
      this.$http.get('mesa/' + this.mesa, { block: true }).then(
        response => {
          var list = response.data.list
          for (var i = 0; i < list.length; i++) {
            this.lista.push(this.fixItem(list[1]))
          }
        },
        error => {
          this.lista.push(this.fixItem({
            datahora: '1968-12-16T10:00:00',
            processo: '00996027720164025151',
            documento: '2017.3000.000017-1',
            motivo: 'Novo Documento Cadastrado',
            situacao: 'Aguardando',
            origem: 'jrjtah'
          }))
          // UtilsBL.errormsg(error, this)
          return error
        }
      )
    },

    fixItem: function (item) {
      UtilsBL.applyDefauts(item, {
        checked: true,
        disabled: false,
        datahora: undefined,
        processo: undefined,
        processoFormatado: undefined,
        documento: undefined,
        motivo: undefined,
        situacao: undefined,
        origem: undefined,
        errormsg: undefined
      })
      if (item.processo !== undefined) {
        item.processoFormatado = ProcessoBL.formatarProcesso(item.processo)
      }
      return item
    },

    marcarTodos: function () {
      var docs = this.filtrados
      for (var i = 0; i < docs.length; i++) {
        var doc = docs[i]
        doc.checked = this.todos
      }
    },

    editar: function () {
      this.$refs.etiqueta.show()
    },

    exibirProcessosMultiplos: function () {
      this.$refs.processosMultiplos.show()
    },

    acrescentarProcessosNaLista: function (arr) {
      if (!arr || arr.length === 0) return
      this.pasta = 'inbox'
      for (var i = 0; i < arr.length; i++) {
        if (arr[i] === '') continue
        var p = this.fixProcesso({
          numero: arr[i],
          inbox: true
        })
        this.processos.push(p)
      }
      this.validarEmLoteSilenciosamente()
    }
  }
}
</script>

<style scoped>
.destaque {
  color: red;
}
</style>
