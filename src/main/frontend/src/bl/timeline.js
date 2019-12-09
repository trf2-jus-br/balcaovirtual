import UtilsBL from "./utils.js";

const DIA_EM_MINUTOS = 60 * 24;

export default {
  emptyTimeline: function() {
    return JSON.parse(
      JSON.stringify({
        autuacao: {
          passou: true
        },
        distribuicao: {},
        intimacao: {},
        remessa: {},
        devolucao: {},
        juntada: {},
        audiencia: {},
        conclusao: {},
        sentenca: {
          texto: "-"
        },
        suspensao: {},
        apelacao: {
          texto: "-"
        },
        devolucaoapelacao: {},
        baixa: {},
        execucao: {},
        arquivamento: {}
      })
    );
  },

  updateTimeline: function(sistema, movs, calcularTempos, classeProcessual) {
    var timeline = this.emptyTimeline();
    timeline.sentenca.texto = sistema.includes("trf")
      ? "Inteiro Teor"
      : "Sentença";
    timeline.apelacao.texto = sistema.includes("trf") ? undefined : "TRF2";

    if (sistema.includes("eproc")) {
      this.updateTimelineEproc(
        timeline,
        movs,
        calcularTempos,
        classeProcessual
      );
    } else {
      this.updateTimelineApolo(timeline, movs, calcularTempos);
    }

    // Calcula tempo médio e cores
    if (calcularTempos) {
      var ti, tempoAcumulado, perc, k;
      tempoAcumulado = 0;
      for (k in timeline) {
        if (!timeline.hasOwnProperty(k)) continue;
        if (!timeline[k].contador) continue;
        timeline[k].tempoMedio = timeline[k].tempo / timeline[k].contador;
        tempoAcumulado += timeline[k].tempoMedio;
      }
      for (k in timeline) {
        if (!timeline.hasOwnProperty(k)) continue;
        ti = timeline[k];
        if (!ti.contador) continue;
        perc = ti.tempoMedio / tempoAcumulado;
        ti.transito = "verde";
        if (ti.tempo > 15 * DIA_EM_MINUTOS && perc > 0.3)
          ti.transito = "laranja";
        if (ti.tempo > 30 * DIA_EM_MINUTOS && perc > 0.5)
          ti.transito = "vermelho";
        if (ti.tempo > 60 * DIA_EM_MINUTOS && perc > 0.8) ti.transito = "vinho";
      }
    }
    if (timeline.execucao.contador) timeline.execucao.contador = 1;
    return timeline;
  },

  updateTimelineEproc: function(
    timeline,
    movs,
    calcularTempos,
    classeProcessual
  ) {
    var e;
    var prev;
    var fApelacao = false;
    var hora, ultHora;
    if ([157, 156, 12078, 12231, 12246, 155].includes(classeProcessual)) {
      timeline.execucao.contador = 1;
      timeline.execucao.passou = true;
      timeline.execucao.esta = true;
      prev = timeline.execucao;
    }
    for (var i = movs.length - 1; i >= 0; i--) {
      var m = movs[i].mov;
      e = undefined;
      if (m === undefined || !m.movimentoLocal) continue;
      if (m.movimentoLocal.codigoMovimento === 2147483647) {
        if (m.movimentoLocal.descricao) {
          var c = UtilsBL.slugify(m.movimentoLocal.descricao);
          if (
            UtilsBL.startsWith(
              c,
              "distribuicao-sorteio-automatico",
              "redistribuicao"
            )
          )
            e = timeline.distribuicao;
          else if (UtilsBL.startsWith(c, "intimacao")) e = timeline.intimacao;
          else if (UtilsBL.startsWith(c, "remessa-carga-trf-2-regiao")) {
            e = timeline.apelacao;
            fApelacao = true;
          } else if (UtilsBL.startsWith(c, "remessa-carga"))
            e = timeline.remessa;
          else if (UtilsBL.startsWith(c, "devolucao-de-remessa"))
            e = timeline.devolucao;
          else if (UtilsBL.startsWith(c, "juntada")) e = timeline.juntada;
          else if (UtilsBL.startsWith(c, "audiencia")) e = timeline.audiencia;
          else if (
            UtilsBL.startsWith(
              c,
              "conclusao-sentenca",
              "conclusao-para-sentenca",
              "inteiro-teor"
            )
          )
            e = timeline.sentenca;
          else if (UtilsBL.startsWith(c, "conclusao")) e = timeline.conclusao;
          else if (UtilsBL.startsWith(c, "suspensao")) e = timeline.suspensao;
          else if (
            UtilsBL.startsWith(c, "procedimento-de-execucao-de-sentenca")
          )
            e = timeline.execucao;
          else if (UtilsBL.startsWith(c, "baixa")) e = timeline.baixa;
          else continue;
          if (fApelacao && e === timeline.devolucao) {
            e = timeline.devolucaoapelacao;
            fApelacao = false;
          }
        }
      } else {
        if (m.movimentoLocal.descricao) {
          c = UtilsBL.slugify(m.movimentoLocal.descricao);
          if (
            UtilsBL.startsWith(
              c,
              "distribuido",
              "distribuicao",
              "redistribuicao"
            )
          )
            e = timeline.distribuicao;
          else if (
            UtilsBL.startsWith(
              c,
              "intimacao-em-secretaria",
              "intimacao-eletronica",
              "citacao-eletronica-expedida-certificada",
              "citacao-do-reu"
            ) &&
            !UtilsBL.startsWith(c, "intimacao-eletronica-confirmada")
          )
            e = timeline.intimacao;
          else if (
            UtilsBL.startsWith(
              c,
              "peticao-protocolada-juntada",
              "juntada",
              "lavrada-certidao",
              "juntado",
              "peticao"
            )
          )
            e = timeline.juntada;
          else if (
            UtilsBL.startsWith(
              c,
              "audiencia-designada",
              "audiencia-redesignada",
              "audiencia-prorrogada",
              "audiencia-convertida-em-diligencia",
              "audiencia-realizada",
              "audiencia-adiada"
            )
          )
            e = timeline.audiencia;
          else if (
            UtilsBL.startsWith(
              c,
              "despacho-decisao-arquivamento",
              "decisao-despacho",
              "despacho-decisao"
            )
          )
            e = timeline.conclusao;
          else if (
            UtilsBL.startsWith(
              c,
              "sentenca",
              "inteiro-teor-ementa-acordao",
              "relatorio-do-acordao"
            )
          )
            e = timeline.sentenca;
          else if (UtilsBL.startsWith(c, "suspensao-sobrestamento"))
            e = timeline.suspensao;
          else if (
            UtilsBL.startsWith(
              c,
              "baixa-definitiva",
              "baixa-processo-eletronico-baixado",
              "cancelamento-de-distribuicao"
            )
          )
            e = timeline.baixa;
          else if (
            UtilsBL.startsWith(c, "remessa-externa") &&
            c.includes("trf2")
          )
            e = timeline.apelacao;
          else if (UtilsBL.startsWith(c, "remessa-externa"))
            e = timeline.remessa;
          else if (UtilsBL.startsWith(c, "recebimento-trf2"))
            e = timeline.devolucaoapelacao;
          else if (
            UtilsBL.startsWith(c, "recebimento") &&
            !UtilsBL.startsWith(c, "recebimento-movimentado-por")
          )
            e = timeline.devolucao;
          else if (UtilsBL.startsWith(c, "execucao-cumprimento-de-sentenca"))
            e = timeline.execucao;
          else continue;
        }
        if (
          e === timeline.intimacao &&
          !UtilsBL.startsWith(c, "intimacao-em-secretaria")
        ) {
          if (!m.tipo || !m.tipo.includes("#intimacao ")) {
            if (timeline.intimacao.contador) timeline.intimacao.contador += 1;
            else timeline.intimacao.contador = 1;
            timeline.intimacao.passou = true;
            m.tipo = "#intimacao ";
            if (
              m.complemento &&
              m.complemento.length >= 3 &&
              m.complemento[2].includes("Status: FECHADO")
            ) {
              if (timeline.remessa.contador) timeline.remessa.contador += 1;
              else timeline.remessa.contador = 1;
              timeline.remessa.passou = true;
              m.tipo += " #remessa ";
              e = timeline.devolucao;
            } else {
              e = timeline.remessa;
            }
          }
        }
      }
      if (e) {
        if (calcularTempos) {
          if (!e.tempo) e.tempo = 0;
          hora = UtilsBL.convertString2DateYYYYMMDDHHMM(m.dataHora);
          if (ultHora) {
            var dif = (hora - ultHora) / 1000 / 60;
            e.tempo += dif;
          }
          ultHora = hora;
        }

        var keyFound;
        for (var key in timeline) {
          if (timeline.hasOwnProperty(key) && e === timeline[key]) {
            keyFound = key;
            break;
          }
        }
        var keyHash = "#" + keyFound + " ";
        if (!m.tipo || !m.tipo.includes(keyHash)) {
          m.tipo = m.tipo !== undefined ? m.tipo + keyHash : keyHash;
          e.passou = true;
          if (e.contador) e.contador += 1;
          else e.contador = 1;
        }
        if (prev) {
          prev.esta = false;

          // delete prev.complemento;
        }
        e.esta = true;
        e.dataHora = m.dataHora;
        if (m.complemento) {
          e.complemento = [];
          if (m.complemento && m.complemento.length > 0) {
            e.complemento[0] = UtilsBL.trunc(m.complemento[0], 30, true);
          }
          if (m.complemento && m.complemento.length > 1) {
            e.complemento[1] = UtilsBL.trunc(m.complemento[1], 30, true);
          }
          if (m.complemento && m.complemento.length > 2) {
            c = m.complemento[2];
            if (c && c.includes("Parte: "))
              e.complemento[1] = UtilsBL.trunc(
                c.substring(c.indexOf("Parte: ")),
                30,
                true
              );
          }
          if (
            m.complemento &&
            m.complemento.length > 1 &&
            e.complemento[1] === "Registro no Sistema"
          ) {
            delete e.complemento[1];
          }
        }
        prev = e;
      }
      if (
        e === timeline.conclusao ||
        e === timeline.sentenca ||
        e === timeline.apelacao ||
        e === timeline.execucao ||
        e === timeline.baixa
      ) {
        for (var k in timeline) {
          if (timeline.hasOwnProperty(k)) delete timeline[k].complemento;
        }
      }

      // if (e === timeline.devolucaoapelacao) break;
    }
  },

  updateTimelineApolo: function(timeline, movs, calcularTempos) {
    var contains = function(m, a) {
      return a.indexOf(m.movimentoLocal.codigoMovimento) !== -1;
    };

    var e;
    var prev;
    var fApelacao = false;
    var hora, ultHora;
    for (var i = movs.length - 1; i >= 0; i--) {
      var m = movs[i].mov;
      e = undefined;
      if (m === undefined || !m.movimentoLocal) continue;
      if (
        contains(m, [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 60, 61, 62, 63, 80, 81, 83])
      ) {
        e = timeline.distribuicao;
      }
      if (contains(m, [11, 76])) {
        if (m.complemento[0] === "Despacho" || m.complemento[0] === "Decisão") {
          e = timeline.conclusao;
        } else if (
          m.complemento[0] === "Sentença" ||
          m.complemento[0] === "Sentença/Julgamento"
        ) {
          e = timeline.sentenca;
        }
      }
      if (
        contains(m, [78]) // Inteiro Teor
      ) {
        e = timeline.sentenca;
      }
      if (contains(m, [12])) e = timeline.intimacao;
      if (contains(m, [14])) {
        if (m.complemento && m.complemento[0] === "TRF - 2ª Região") {
          e = timeline.apelacao;
          fApelacao = true;
        } else e = timeline.remessa;
      }
      if (contains(m, [15])) {
        if (fApelacao) {
          e = timeline.devolucaoapelacao;
          fApelacao = false;
        } else e = timeline.devolucao;
      }
      if (contains(m, [27])) e = timeline.juntada;
      if (contains(m, [101])) e = timeline.suspensao;
      if (contains(m, [19])) e = timeline.audiencia;
      if (contains(m, [18])) e = timeline.execucao;
      if (contains(m, [26])) e = timeline.baixa;
      if (e) {
        if (calcularTempos) {
          if (!e.tempo) e.tempo = 0;
          hora = UtilsBL.convertString2DateYYYYMMDDHHMM(m.dataHora);
          if (ultHora) {
            var dif = (hora - ultHora) / 1000 / 60;
            e.tempo += dif;
          }
          ultHora = hora;
        }

        for (var key in timeline) {
          if (timeline.hasOwnProperty(key) && e === timeline[key]) {
            m.tipo = "#" + key;
          }
        }
        e.passou = true;
        if (e.contador) e.contador += 1;
        else e.contador = 1;
        if (prev) {
          prev.esta = false;

          // delete prev.complemento;
        }
        e.esta = true;
        e.dataHora = m.dataHora;
        if (m.complemento) {
          e.complemento = [];
          if (m.complemento && m.complemento.length > 0) {
            e.complemento[0] = UtilsBL.trunc(m.complemento[0], 30, true);
          }
          if (m.complemento && m.complemento.length > 1) {
            e.complemento[1] = UtilsBL.trunc(m.complemento[1], 30, true);
          }
          if (
            m.complemento &&
            m.complemento.length > 1 &&
            e.complemento[1] === "Registro no Sistema"
          ) {
            delete e.complemento[1];
          }
        }
        prev = e;
      }
      if (
        e === timeline.conclusao ||
        e === timeline.sentenca ||
        e === timeline.apelacao ||
        e === timeline.execucao ||
        e === timeline.baixa
      ) {
        for (var k in timeline) {
          if (timeline.hasOwnProperty(k)) delete timeline[k].complemento;
        }
      }

      // if (e === timeline.devolucaoapelacao) break;
    }
  }
};
