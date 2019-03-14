# Balcão Virtual

Balcão Virtual (BV) é um site que permite acessar sistemas processuais compatíveis com o Modelo Nacional de Interoperabilidade - MNI. Clique [aqui](https://docs.google.com/presentation/d/11qFey-qboRy3G0X4thrT3370daoDSTCRetbv_LfDOSc/edit?usp=sharing) para ver a apresentação do sistema.

![balcao-virtual-consulta-processual](https://user-images.githubusercontent.com/4137623/54284473-bec09a00-457e-11e9-9864-c3baf411792d.png)

#### As operações disponíveis são:
- Consulta processual;
- Peticionamento inicial;
- Peticionamento intercorrente em lote;
- Recebimento de intimações e citações.

#### Além disso, o Balcão Virtual disponibiliza:
- Visualização gráfica dos principais eventos ocorridos no processo - Timeline Processual;
- Lista de processos recentes e favoritos;
- Marcações em peças processuais que podem ser de texto livre ou seguindo um padrão que pode ser definido para cada classe processual;
- Anotações relacionadas ao processo, pessoais ou da unidade;
- Inclusão de cotas em processos;
- Download do PDF completo do processo;
- Impressão em lote dos PDFs completos.

## Arquitetura

Completamente baseado em micro-serviços, o Balcão Virtual é composto dos seguintes componentes:
- Site do Balcão Virtual: desenvolvido em VueJS e Java
- Para cada sistema conectado:
  - Webservice SOAP no padrão MNI para envio de petições
  - Webservice REST para obter informações adicionais: desenvolvido em Java

O Balcão Virtual se vale do estado da arte em tecnologia para propriciar a melhor experiência de uso possível.

## Ambiente

Para executar o Balcão Virtual, é necessário que algumas propriedades sejam definidas.

O Balcão Virtual se comunica com um Webservice REST para obter informações dos sistemas processuais e também com Webservices SOAP em conformidade com o MNI, um para cada órgão conectado.

```xml
<property name="balcaovirtual.systems" value="br.jus.trf2.apolo,br.jus.trf2.eproc"/>
<property name="balcaovirtual.br.jus.trf2.apolo.api.url" value="http://host/ApoloWS/api"/>
<property name="balcaovirtual.br.jus.trf2.apolo.mni.url" value="http://host/servico-intercomunicacao-2.2.2-mtom/trf2/?wsdl"/>
<property name="balcaovirtual.br.jus.trf2.eproc.api.url" value="https://host/eproc-api/api/v1"/>
<property name="balcaovirtual.br.jus.trf2.eproc.mni.url" value="https://host/eproc/wsdl.php?srv=intercomunicacao2.2"/>
<property name="balcaovirtual.br.jus.trf2.eproc.mni.endpoint" value="https://host/eproc/ws/controlador_ws.php?srv=intercomunicacao2.2"/>
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

Envia cotas para processos, portanto deve ser configurado o identificador do tipo documental da cota.

```xml
<property name="balcaovirtual.br.jus.trf2.eproc.cota.tipo" value="1"/>
```

Requer um banco de dados para armazenar marcas, notas e sinais. Em um servidor de aplicação como o JBoss, pode ser configurado um _pool_ chamado BalcaoVirtualDS. 

```xml
<property name="balcaovirtual.datasource.name" value="balcaovirtualds"/>
```

Alternativamente, podem ser informados dados de conexão direta ao banco de dados MySQL.

```xml
<property name="balcaovirtual.datasource.url" value="jdbc:mysql://host/balcaovirtual"/>
<property name="balcaovirtual.datasource.username" value="usuario"/>
<property name="balcaovirtual.datasource.password" value="senha"/>
```

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
