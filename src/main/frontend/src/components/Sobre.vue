<template>
  <div class="container content pt-5">
    <div class="row">
      <div class="col-md-12 sobre">
        <div class="headline pb-1">
          <h2>Balcão Virtual</h2>
        </div>

        <p>
          O Balcão Virtual é um aplicativo que se vale do estado da arte em
          tecnologia para propiciar a melhor experiência de uso possível ao
          consultar processos judiciais e realizar outras operações correlatas.
        </p>

        <p>As principais funcionalidades são:</p>
        <ul>
          <li>Consulta processual;</li>
          <li>Peticionamento inicial;</li>
          <li>Peticionamento intercorrente em lote;</li>
          <li>Recebimento de intimações e citações;</li>
          <li>Diagrama dos principais eventos ocorridos no processo;</li>
          <li>Lista de processos recentes e favoritos;</li>
          <li>Marcações em peças processuais;</li>
          <li>Anotações relacionadas ao processo, pessoais ou da unidade;</li>
          <li>Inclusão de cotas;</li>
          <li>Download do PDF completo;</li>
          <li>Impressão em lote;</li>
          <li>Visualização, edição e assinatura digital de minutas.</li>
        </ul>

        <p>
          Atenção, o Balcão Virtual mantém o usuário autenticado por 18h após o
          <i>login</i>. Fechar o navegador não causa automaticamente um
          <i>logout</i>. Para forçar a desconexão do usuário, é necessário
          clicar na opção "Logout" do menu principal.
        </p>

        <div class="headline pb-1 pt-3">
          <h2>Enviando Petições Intercorrentes</h2>
        </div>

        <p>
          Para protocolar petições intercorrentes em lote, siga o seguinte
          procedimento:
        </p>
        <ol>
          <li>
            Renomeie os arquivos PDF que contém as petições intercorrentes de
            modo que ele iniciem com o número do processo ao qual a petição se
            refere. Depois do número do processo, pode vir qualquer outra
            indicação do conteúdo do arquivo. Exemplos de nomes de arquivo para
            petições intercorrentes: 0527484-17.2007.4.02.5101.pdf,
            01004835520144020000.pdf
          </li>
          <li>
            Caso deseje incluir anexos, após o número do processo, insira a
            informação do número do anexo, na forma "-01", "-02", etc. Exemplos
            de nome de arquivo anexo à petição intercorrente:
            0527484-17.2007.4.02.5101-01.pdf
          </li>
          <li>Selcione a opção de menu "Petição Intercorrente"</li>
          <li>
            Clique na área marcada com "Arraste suas petições intercorrentes em
            formato PDF e solte elas nesta área, ou clique aqui para
            selecioná-las" e escolha os arquivos que deseja enviar.
          </li>
          <li>
            Selecione o tipo da petição e diga se há segredo de justiça para
            cada um dos arquivos.
          </li>
          <li>
            Se desejar enviar uma mesma petição para mais de um processo, use um
            nome de arquivo que não contenha nenhum número de processo. Na
            interface, ao lado do arquivo aparecerá um botão com símbolo "+".
            Clique nesse botão e forneça um lista dos números de todos os
            processos onde quer protocolar esta petição e clique em
            "Prosseguir".
          </li>
          <li>
            Clique no botão "Protocolar" e observe a coluna "Status" para saber
            se as petições foram protocoladas ou se ocorreu algum erro.
          </li>
        </ol>

        <div class="headline pb-1 pt-3">
          <h2>A Linha do Tempo</h2>
        </div>
        <p>
          <strong>Linha do Tempo do Processo</strong> é um diagrama que resume
          as ocorrências observadas em um processo judicial. Situações que ainda
          não ocorreram permanecem em cinza. Eventos detectados no histórico de
          movimentos são apresentados como círculos pretos e o caminho até eles
          é marcado de azul. Quando um mesmo evento ocorre mais de uma vez, o
          número total de ocorrências é apresentado dentro do respectivo
          círculo. O último evento detectado é marcado com um círculo laranja e
          ao seu lado podem ser apresentados alguns detalhes referentes, apenas,
          a esta última ocorrência.
        </p>


        <div class="headline pb-1 pt-3" name="faq">
          <h2>Perguntas Frequentes</h2>
        </div>

        <ol>
          <li>
            <div class="pergunta">
              Posso consultar processos do Apolo e do eproc pelo sistema do Balcão Virtual?
            </div>
            <div class="resposta">
              <p>Sim. A consulta às peças públicas e não-públicas dos processos de ambos os sistemas processuais pode ser realizada efetuando o login no sistema do Balcão Virtual  com o seu usuário e senha de cada sistema processual.</p>
              <p>Clique no canto superior direito no link “Login” para informar o seu usuário e senha de cada sistema. Para consultar processos do Apolo entre no sistema do Balcão com o usuário e senha do Apolo, e para o eproc proceda da mesma forma.</p>
            </div>
          </li>

          <li>
            <div class="pergunta">
              Como posso verificar em qual sistema o meu usuário está autenticado?
            </div>
            <div class="resposta">
              <p>Após efetuar o login no sistema, clique na seta que aparece ao lado do nome do seu login e selecione a opção “Status”. 
Na opção <b>“Identificação do Usuário nos Sistemas”</b> ele mostrará os sistemas processuais e órgãos que o seu usuário e senha acessaram com sucesso. 
Como o Balcão Virtual está conectado a até seis (06) sistemas processuais, Apolo (TRF2/SJRJ/SJES) e eproc (TRF2/SJRJ/SJES), o Balcão Virtual mostrará nessa parte todos os sistemas processuais que o seu usuário e senha fornecidos no login conseguiu autenticar com sucesso.</p>
            </div>
          </li>          

          <li>
            <div class="pergunta">
              Como posso saber em qual sistema o processo está tramitando?
            </div>
            <div class="resposta">
              <p>Após consultar um processo com sucesso, antes do número do seu número, na tela de resultado da consulta será mostrado o sistema processual e a Seção/Tribunal em que o mesmo está tramitando. (Ex: Processo Digital <b>Apolo/JFRJ</b> 0001807-40.2012.4.02.5101 ou Processo Digital <b>Eproc/JFRJ</b> 0001907-89.2015.4.02.5101)</p>
            </div>
          </li>          

          <li>
            <div class="pergunta">
              O que representa o gráfico da linha do tempo e os números dentro dos círculos após a consulta de um processo?
            </div>
            <div class="resposta">
              <p>O gráfico representa as principais fases do processo em que o mesmo pode ter tramitado. A linha em azul representa onde o processo já tramitou e a cinza seria as fases que o processo não passou ainda.</p>
              <p>Os números dentro dos círculos representam a quantidade de ocorrências que o processo possui desta fase até o momento da realização da consulta . (Ex: Conclusão (27)). Isto significa que o processo possui até o momento da consulta vine e sete (27) conclusões.</p>

            </div>
          </li>          

          <li>
            <div class="pergunta">
              Como posso filtrar os movimentos de um processo para ver somente as decisões do Juiz, por exemplo?
            </div>
            <div class="resposta">
              <p>Após o sistema exibir o resultado da consulta, clique no círculo de nome “Conclusão” ou “Sentença” da linha do tempo para filtrar somente os movimentos de Conclusão ou Sentença do processo.</p>
              <p>Você pode filtrar por mais de um tipo de movimento ao mesmo tempo. Para limpar o filtro e voltar a ver todos os movimentos do processo apague o conteúdo do campo que possui um filtro da Lupa, logo abaixo do painel dos dados do “Autor” de cor azul.</p>
            </div>
          </li>          

          <li>
            <div class="pergunta">
              Na linha do tempo do processo observei que no círculo da sentença está na cor preta e não possui o número dentro. O que significa?
            </div>
            <div class="resposta">
              <p>Neste caso, sempre que existir apenas uma ocorrência da fase o sistema do Balcão Virtual não mostrará a quantidade e significa que existe apenas uma ocorrência. Quando a cor do círculo estiver “laranja”, significa que este é o último movimento mais recente do processo.</p>
            </div>
          </li>          

          <li>
            <div class="pergunta">
              Todas as fases do processo estão representadas no gráfico da linha do tempo?
            </div>
            <div class="resposta">
              <p>Não. Somente as fases mais representativas foram representadas em forma de gráfico na linha do tempo. Caso queira ver todos os movimentos do processo, desça a barra de rolagem do navegador para ver todas as fases do processo e acessar as peças associadas a cada fase.</p>
            </div>
          </li>          

          <li>
            <div class="pergunta">
              Onde estão as informações da capa do processo (Autor, Réu, dados da Distribuição, Classe, Partes e etc?)
            </div>
            <div class="resposta">
              <p>Ao consultar um processo, serão exibidos três painéis nas cores AZUL, VERDE e AMARELO. 
As informações das partes, dados da Distribuição, Juízo, Classe, dentre outras, estão agrupadas nesses painéis. 
Para ver os dados complementares do processo como Classe e Assunto, clique na opção “Ver mais…” existente no painel AMARELO.</p>
            </div>
          </li>          

          <li>
            <div class="pergunta">
              O que é o botão “Filtrar Marcas”?
            </div>
            <div class="resposta">
              <p>Este é um recurso muito interessante existente no sistema do Balcão Virtual e muito útil. Esse recurso está disponível apenas para os usuários que acessam o sistema com o seu login do Apolo ou eproc.</p>
              <p>Na lista de movimentos do processo, aparece, quando existente ou quando está disponível, a peça associada ao respectivo movimento do processo. Ao lado dessa peça existe um ícone representado por uma “canetinha”.</p>
              <p>Ao clicar nessa canetinha, você pode colocar uma marcação (texto livre) para marcar essa peça.</p>
              <p>Após cadastrar uma marcação para essa peça,ela será armazenada para o seu processo e o seu login, para sempre que você acessar esse mesmo processo você possa filtrar todas as peças que você tenha feito essa marcação (anotação).</p>
              <p>Isso facilita para buscar informações nos andamentos do processo sempre que você queira acessá-lo novamente.</p>
              <p>Atenção, na consulta pública do processo (sem usar o login de acesso) este recurso não está disponível.</p>
            </div>
          </li>          

          <li>
            <div class="pergunta">
              Eu posso fazer o download de todas as peças do processo ?
            </div>
            <div class="resposta">
              <p>Sim. No sistema do Balcão Virtual, após consultar um processo, clique no botão “PDF Completo” para o sistema baixar as peças e gerar um único arquivo PDF para download.</p>
              <p>Para processos grandes com muitas peças, esta operação poderá levar um pouco mais de tempo para ser concluída.</p>
            </div>
          </li>          

          <li>
            <div class="pergunta">
              Eu posso guardar no sistema do Balcão Virtual os números dos processos que mais acesso para não ter que informar novamente o número na hora de fazer uma nova consulta processual?
            </div>
            <div class="resposta">
              <p>Sim. Claro! Sempre que você acessar o Balcão Virtual com o seu login (usuário e senha), após consultar o processo, existe um ícone de uma “estrela” ao lado do número do processo. Clicando neste ícone você pode incluir o número do processo na área de “Favoritos” do sistema.</p>
              <p>Para acessar a lista dos processos que você incluiu nos favoritos, clique no menu “Processos” e eles estão lá, todos que você clicou como favorito.</p>
            </div>
          </li>          

          <li>
            <div class="pergunta">
              Posso excluir um processo da minha lista de Favoritos?
            </div>
            <div class="resposta">
              <p>Sim. Acessado o menu processos, você pode clicar na estrela na coluna do lado direito da tela e desmarcar o processo. 
Pronto, ele será removido da lista de Favoritos.</p>
            </div>
          </li>          

          <li>
            <div class="pergunta">
              Eu posso ver todos os processos que acessei recentemente com o meu login de acesso ao sistema do Balcão Virtual?
            </div>
            <div class="resposta">
              <p>Sim. Após efetuar o login, clique no menu “Processos” e em seguida no botão “Recentes” no canto superior esquerdo da tela.</p>
              <p>A partir da relação de processos listadas ali, clicando sobre o número do processo o sistema efetuará a consulta do mesmo, sem precisar informar o seu número.</p>
            </div>
          </li>          

          <li>
            <div class="pergunta">
              Fiz uma consulta ao processo e nem todas as peças aparecem para mim. Isso é um problema do sistema?
            </div>
            <div class="resposta">
              <p>Não. Primeiro verifique se você acesso o sistema com o seu login e senha do respectivo sistema processual (Apolo ou eproc).</p>
              <p>As peças públicas e não públicas somente estão disponíveis na consulta do processo para usuários que acessaram o sistema do Balcão Virtual através da opção de “Login”.</p>
              <p>É possível acessar as informações públicas do processo pela tela principal do sistema do Balcão Virtual sem informar o seu login de acesso. Neste caso, somente as peças públicas (Despachos, Decisões, Sentenças, Certidões e Atos Ordinatórios) estarão disponíveis para acesso.</p>
              <p>Desta forma, recomendamos sempre acessar o sistema do Balcão Virtual com o seu login e senha usados nos sistemas processuais do Apolo ou eproc.</p>
              <p>Atenção! Mesmo acessando o sistema com o seu login, algumas peças poderão não estar visíveis. Isto pode ocorrer por estarem com sigilo indicado pela Vara do processo ou porque de fato não existe peça ainda para o respectivo movimento.</p>
            </div>
          </li>          

          <li>
            <div class="pergunta">
              Consultei um processo e apesar de informar o número correto o sistema do Balcão Virtual informou “Processo não encontrado”
            </div>
            <div class="resposta">
              <p>Neste caso essa mensagem pode ter sido exibida por duas situações:</p>
              <ol>
                <li>o número do processo foi digitado incorretamente e por isso ele não encontrou em qualquer uma das bases de dados processuais.</li>
                <li>o número do processo é do eproc e está tramitando com segredo de justiça. Neste caso você deve solicitar o acesso à Vara onde tramita, se for o caso.</li>
              </ol>
            </div>
          </li>          

          <li>
            <div class="pergunta">
              Como faço para enviar uma petição intercorrente para um processo do eproc ou Apolo pelo Balcão Virtual?
            </div>
            <div class="resposta">
              <p>Primeiro, efetue o login no sistema usando o respectivo usuário e senha do sistema processual onde o processo está tramitando.</p>
              <p>Após isso, entre no menu “Petição Intercorrente”. Arraste o arquivo PDF para a área destacada na tela ou então clique sobre a mesma com o mouse para abrir a janela do Windows de pesquisa de arquivos. Selecione o arquivo PDF para sele ser carregado para o sistema.</p>
              <p>Caso o seu arquivo PDF da petição já esteja nomeado com o número do processo que deseja enviar a sua petição intercorrente, o sistema fará a busca do processo e mostrará a sua localização (sistema/Vara) automaticamente.</p>
              <p>Caso ele não esteja, não tem problema. Ao arrastar ou selecionar o arquivo PDF, o sistema exibirá uma caixa para você informar o número do processo que deseja peticionar. Informe o número nessa caixa de texto e aguarde o sistema localizar as suas informações.</p>
              <p>Você pode peticionar para vários processos ao mesmo tempo. Não limite da quantidade de processos que você deseja peticionar nesta tela. Observe apenas o limite do tamanho dos anexos da sua petição, que possuem limites pré-definidos.</p>
            </div>
          </li>          

          <li>
            <div class="pergunta">
              Posso enviar petições iniciais pelo Balcão Virtual?
            </div>
            <div class="resposta">
              <p>Sim. Após acessar o sistema com o seu login e senha do órgão que deseja peticionar (TRF2, SJRJ ou SJES) do eproc ou Apolo, vá para o menu Petição Inicial e preencha todos os dados solicitados.</p>
            </div>
          </li>          

        </ol>



        <div class="headline pb-1 pt-3">
          <h2>Sobre o Balcão Virtual</h2>
        </div>

        <p>
          O Balcão Virtual foi desenvolvido em 2017 pela Coordenadoria de
          Inovação (CINOVA), parte da Secretaria de Tecnologia da Informação do
          Tribunal Regional Federal da 2ª Região, com a colaboração do
          Procurador Regional Substituto da Procuradoria Regional da Fazenda
          Nacional na 2ª Região, Dr. João Henrique Chauffaille Grognet.
        </p>
        <p>
          Contamos com o patrocínio e o apoio do Dr. Manuel Rolim Campbell
          Penna, Juiz Federal Titular da 6ª Vara Federal de Execução Fiscal da
          Justiça Federal de Primeira Instância no Rio de Janeiro - JFRJ.
        </p>
        <p>
          Operações específicas para magistrados, como a edição e assinatura de
          minutas, foram acrescentadas com a colaboração do Juiz Federal Dr.
          Mauro Lopes da JFRJ.
        </p>
        <p>
          Atualmente é mantido pelas Coordenadorias de Sistemas Administrativos
          e Processuais (COSADM e COINTP).
        </p>

        <p>Equipe técnica:</p>
        <ul>
          <li>Rogério Meceni - especificação, análise e validação</li>
          <li>
            Renato Crivano - arquitetura e desenvolvimento do front-end em VueJS
            e Webpack e do back-end em Java
          </li>
          <li>
            Alberto Paulino - desenvolvimento do webservice de integração com o
            Eproc, SQLs de consulta
          </li>
          <li>
            Giovanni Souza e André Luís Silva - desenvolvimento do webservice de
            integração com o Apolo
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "sobre"
};
</script>

<!-- Add 'scoped' attribute to limit CSS to this component only -->
<style scoped>
.pergunta {
  font-weight: bold;
  margin-bottom: 0.5em;
}

.resposta p {
  font-size: 100%;
}

.resposta ol li {
  font-size: 100%;
}
</style>
