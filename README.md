# Balcão Virtual

Balcão Virtual é um site que permite acessar sistemas processuais compatíveis com o Modelo Nacional de Interoperabilidade - MNI.

As operações disponíveis são:
- Consulta processual;
- Peticionamento inicial;
- Peticionamento intercorrente;
- Consulta à listagem de intimações e citações;
- Recebimento de intimações e citações.

Além disso, o Balcão Virtual disponibiliza:
- Visualização gráfica dos principais eventos ocorridos no processo - Timeline Processual;
- Lista de processos recentes e favoritos;
- Marcações em peças processuais que podem ser de texto livre ou seguindo um padrão que pode ser definido para cada classe processual;
- Anotações relacionadas ao processo, pessoais ou da unidade.

## Arquitetura

Completamente baseado em micro-serviços, o Balcão Virtual é composto dos seguintes componentes:
- Site do Balcão Virtual: desenvolvido em VueJS e Java.
- Webservice REST para obter informações dos sistemas processuais: desenvolvido em C#.
- Webservices SOAP no padrão MNI para envio de petições, um para cada órgão conectado.

O Balcão Virtual utiliza

## Ambiente

Para executar o Balcão Virtual, é necessário que algumas propriedades sejam definidas.

O Balcão Virtual se comunica com um Webservice REST para obter informações dos sistemas processuais e também com Webservices SOAP em conformidade com o MNI, um para cada órgão conectado.

```xml
<property name="balcaovirtual.ws.processual.url" value="http://host/ApoloWS/api"/>
<property name="balcaovirtual.orgaos" value="TRF2,JFRJ,JFES"/>
<property name="balcaovirtual.mni.trf2.url" value="http://host/servico-intercomunicacao-2.2.2-mtom/trf2/?wsdl"/>
<property name="balcaovirtual.mni.jfrj.url" value="http://host/servico-intercomunicacao-2.2.2-mtom/jfrj/?wsdl"/>
<property name="balcaovirtual.mni.jfes.url" value="http://host/servico-intercomunicacao-2.2.2-mtom/jfes/?wsdl"/>
 ```

Utiliza JWT como mecanismo de autenticação e autorização.

```xml
<property name="balcaovirtual.jwt.issuer" value="trf2-balcao-virtual"/>
<property name="balcaovirtual.jwt.secret" value="senha_secreta_preferencialmente_guid"/>
 ```

Armazena arquivos temporários e PDFs em diretórios no FileSystem. É interessante usar um *crontab* para apagar os arquivos que já estejam nestes diretórios há mais de 24 horas.

```xml
<property name="balcaovirtual.upload.dir.final" value="~/tmp"/>
<property name="balcaovirtual.upload.dir.temp" value="~/tmp"/>
 ```

Requer um banco de dados para armazenar marcas, notas e sinais. Em um servidor de aplicação como o JBoss, pode ser configurado um _pool_ chamado BalcaoVirtualDS.

Precisa de um servidor SMTP para o envio de emails de sugestões.

```xml
<property name="balcaovirtual.smtp.remetente" value="balcaovirtual@trf2.jus.br"/>
<property name="balcaovirtual.smtp.host" value="smtp.trf2.jus.br"/>
<property name="balcaovirtual.smtp.host.alt" value="smtp2.trf2.jus.br"/>
<property name="balcaovirtual.smtp.auth" value="true"/>
<property name="balcaovirtual.smtp.auth.usuario" value="intelijus"/>
<property name="balcaovirtual.smtp.auth.senha" value="senha_secreta"/>
<property name="balcaovirtual.smtp.porta" value="25"/>
<property name="balcaovirtual.smtp.destinatario" value="equipe_responsavel@trf2.jus.br"/>
<property name="balcaovirtual.smtp.assunto" value="Balcão Virtual: Sugestão"/>
```

Por fim, deve ser informado qual o ambiente, "desenv", "homolo", "prod".

```xml
<property name="balcaovirtual.env" value="desenv"/>
```
