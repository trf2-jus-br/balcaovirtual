# balcaovirtual

Balcão Virtual é um site que permite o envio de petições intercorrentes para sistemas processuais compatíveis com o Modelo Nacional de Interoperabilidade - MNI.

## Arquitetura

Completamente baseado em micro-serviços, o Balcão Virtual é composto dos seguintes componentes:
- Site do Balcão Virtual: desenvolvido em AngularJS e Java.
- Webservice REST para obter informações dos sistemas processuais: desenvolvido em C#.
- Webservices SOAP no padrão MNI para envio de petições, um para cada órgão conectado.

O Balcão Virtual utiliza

## Ambiente

Para executar o Balcão Virtual, é necessário que algumas propriedades sejam definidas.

O Balcão Virtual se comunica com um Webservice REST para obter informações dos sistemas processuais e também com Webservices SOAP em conformidade com o MNI, um para cada órgão conectado.

```xml
<property name="balcaovirtual.ws.processual.url" value="http://host/ApoloWS/api"/>
<property name="balcaovirtual.mni.trf2.url" value="http://host/servico-intercomunicacao-2.2.2-mtom/trf2/?wsdl"/>
<property name="balcaovirtual.mni.jfrj.url" value="http://host/servico-intercomunicacao-2.2.2-mtom/jfrj/?wsdl"/>
<property name="balcaovirtual.mni.jfes.url" value="http://host/servico-intercomunicacao-2.2.2-mtom/jfes/?wsdl"/>
 ```

Utiliza JWT como mecanismo de autenticação e autorização.

```xml
<property name="balcaovirtual.jwt.issuer" value="trf2-balcao-virtual"/>
<property name="balcaovirtual.jwt.secret" value="senha_secreta_preferencialmente_guid"/>
 ```

Não requer um servidor de banco de dados, mas armazena arquivos temporários e PDFs em diretórios no FileSystem. É interessante usar um *crontab* para apagar os arquivos que já estejam nestes diretórios há mais de 24 horas.

```xml
<property name="balcaovirtual.upload.dir.final" value="~/tmp"/>
<property name="balcaovirtual.upload.dir.temp" value="~/tmp"/>
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
