-- MySQL dump 10.13  Distrib 5.7.18, for Win64 (x86_64)
--
-- Host: localhost    Database: balcao_virtual
-- ------------------------------------------------------
-- Server version	5.7.18-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `cnj_classe`
--

LOCK TABLES `cnj_classe` WRITE;
/*!40000 ALTER TABLE `cnj_classe` DISABLE KEYS */;
INSERT INTO `cnj_classe` VALUES (7,'Procedimento Comum'),(22,'Procedimento Sumário'),(28,'Anulação e Substituição de Títulos ao Portador'),(29,'Apreensão e Depósito de Coisa Vendida com Reserva de Domínio'),(32,'Consignação em Pagamento'),(34,'Demarcação / Divisão'),(35,'Depósito'),(37,'Embargos de Terceiro'),(38,'Habilitação'),(40,'Monitória'),(41,'Nunciação de Obra Nova'),(44,'Prestação de Contas - Oferecidas'),(45,'Ação de Exigir Contas'),(46,'Restauração de Autos'),(47,'Ação Rescisória'),(49,'Usucapião'),(52,'Alienação Judicial de Bens'),(55,'Declaração de Ausência'),(56,'Especialização de Hipoteca Legal'),(59,'Organização e Fiscalização de Fundação'),(63,'Ação Civil Coletiva'),(64,'Ação Civil de Improbidade Administrativa'),(65,'Ação Civil Pública'),(66,'Ação Popular'),(69,'Alimentos - Lei Especial Nº 5.478/68'),(76,'Apreensão de Embarcações'),(77,'Arribadas Forçadas'),(79,'Regulação de Avaria Grossa'),(80,'Avarias'),(81,'Busca e Apreensão em Alienação Fiduciária'),(82,'Cancelamento de Naturalização'),(83,'Cautelar Fiscal'),(84,'Cobrança de Cédula de Crédito Industrial'),(85,'Compromisso Arbitral'),(86,'Consignatória de Aluguéis'),(88,'Correição Parcial ou Reclamação Correicional'),(89,'Depósito da Lei 8. 866/94'),(90,'Desapropriação'),(91,'Desapropriação Imóvel Rural por Interesse Social'),(92,'Despejo'),(93,'Despejo por Falta de Pagamento'),(94,'Despejo por Falta de Pagamento Cumulado Com Cobrança'),(96,'Discriminatória'),(97,'Dissolução e Liquidação de Sociedade'),(100,'Dúvida'),(107,'Expropriação da Lei 8.257/91'),(110,'Habeas Data'),(112,'Homologação de Transação Extrajudicial'),(113,'Imissão na Posse'),(119,'Mandado de Segurança Coletivo'),(120,'Mandado de Segurança'),(121,'Naturalização'),(122,'Opção de Nacionalidade'),(124,'Pedido de Resposta ou Retificação da Lei de Imprensa'),(127,'Protesto Formado a Bordo'),(136,'Remição do Imóvel Hipotecado'),(137,'Renovatória de Locação'),(140,'Revisional de Aluguel'),(144,'Suspensão de Liminar ou Antecipação de Tutela'),(145,'Suspensão de Execução de Sentença'),(151,'Liquidação por Arbitramento'),(152,'Liquidação de Sentença pelo Procedimento Comum'),(153,'Liquidação Provisória por Arbitramento'),(154,'Liquidação Provisória de Sentença pelo Procedimento Comum'),(156,'Cumprimento de sentença'),(157,'Cumprimento Provisório de Sentença'),(159,'Execução de Título Extrajudicial'),(170,'Embargos à Adjudicação'),(171,'Embargos à Arrematação'),(172,'Embargos à Execução'),(173,'Embargos de Retenção por Benfeitorias'),(176,'Alimentos - Provisionais'),(177,'Apreensão de  Títulos'),(178,'Arresto '),(179,'Arrolamento de Bens'),(180,'Atentado'),(181,'Busca e Apreensão'),(182,'Caução'),(183,'Cautelar Inominada'),(186,'Exibição'),(188,'Homologação do Penhor Legal'),(190,'Justificação'),(191,'Protesto'),(192,'Posse em Nome do Nascituro'),(193,'Produção Antecipada da Prova'),(196,'Seqüestro'),(198,'Apelação'),(199,'Remessa Necessária'),(202,'Agravo de Instrumento'),(204,'Agravo de Instrumento em Recurso Extraordinário'),(206,'Agravo Regimental'),(208,'Embargos Infringentes'),(210,'Embargos Infringentes na Execução Fiscal'),(215,'Incidentes'),(216,'Incidente De Arguição de Inconstitucionalidade'),(218,'Assistência Judiciária'),(221,'Conflito de competência'),(228,'Exibição de Documento ou Coisa'),(229,'Impugnação ao Cumprimento de Sentença'),(230,'Impugnação ao Pedido de Assistência Litisconsorcial ou Simples'),(231,'Impugnação ao Valor da Causa'),(232,'Incidente de Falsidade'),(233,'Incidente de Uniformização de Jurisprudência'),(236,'Oposição'),(238,'Avocatória'),(241,'Petição'),(242,'Comunicação'),(244,'Reclamação'),(256,'Representação por Excesso de Prazo'),(258,'Carta de Ordem Cível'),(261,'Carta Precatória Cível'),(264,'Carta Rogatória Cível'),(270,'Notícia-Crime'),(272,'Representação Criminal/Notícia de Crime'),(273,'Pedido de Arquivamento em Representação Criminal'),(275,'Notificação para Explicações'),(276,'Notificação para Explicações (Lei de Imprensa)'),(278,'Termo Circunstanciado'),(279,'Inquérito Policial'),(280,'Auto de Prisão em Flagrante'),(282,'Ação Penal de Competência do Júri'),(283,'Ação Penal - Procedimento Ordinário'),(287,'Crimes de Responsabilidade dos Funcionários Públicos'),(288,'Crimes de Calúnia, Injúria e Difamação de Competência do Juiz Singular'),(289,'Crimes Contra a Propriedade Imaterial'),(290,'Processo Sumário (Detenção)'),(291,'Restauração de Autos'),(293,'Crimes Ambientais'),(297,'Crimes de Imprensa'),(299,'Procedimento do Juizado Especial Criminal - Sumariíssimo'),(300,'Procedimento Especial da Lei Antitóxicos'),(302,'Procedimento Especial dos Crimes de Abuso de Autoridade'),(305,'Liberdade Provisória com ou sem fiança'),(307,'Habeas Corpus'),(309,'Pedido de Busca e Apreensão Criminal'),(310,'Pedido de Quebra de Sigilo de Dados e/ou Telefônico'),(311,'Medidas Investigatórias Sobre Organizações Criminosas'),(313,'Pedido de Prisão Preventiva'),(314,'Pedido de Prisão Temporária'),(315,'Pedido de Prisão/ Liberdade Vigiada para Fins de Expulsão'),(318,'Exceção de Suspeição'),(319,'Exceção de Incompetência de Juízo'),(320,'Exceção de Litispendência'),(321,'Exceção de Ilegitimidade de Parte'),(322,'Exceção de Coisa Julgada'),(323,'Exceção de Impedimento'),(324,'Exceção da Verdade'),(325,'Conflito de Jurisdição'),(326,'Restituição de Coisas Apreendidas'),(327,'Embargos de Terceiro'),(328,'Medidas Assecuratórias'),(329,'Seqüestro'),(330,'Arresto / Hipoteca Legal'),(331,'Incidentes'),(332,'Incidente de Falsidade'),(333,'Insanidade Mental do Acusado'),(335,'Carta de Ordem Criminal'),(355,'Carta Precatória Criminal'),(375,'Carta Rogatória Criminal'),(386,'Execução da Pena'),(407,'Conversão de Pena'),(408,'Excesso ou Desvio'),(409,'Anistia'),(410,'Indulto'),(411,'Comutação de Pena'),(413,'Agravo de Execução Penal'),(416,'Apelação em Mandado de Segurança'),(417,'Apelação'),(418,'Carta Testemunhável'),(419,'Correição Parcial'),(421,'Embargos Infringentes e de Nulidade'),(424,'Recurso de Medida Cautelar'),(425,'Recurso de Sentença Criminal'),(426,'Recurso em Sentido Estrito'),(427,'Remessa Necessária'),(428,'Revisão Criminal'),(432,'Desaforamento de Julgamento'),(433,'Incidente de Uniformização de Jurisprudência'),(436,'Procedimento do Juizado Especial Cível'),(457,'Pedido de Uniformização de Interpretação de Lei'),(460,'Recurso Inominado'),(988,'Dissídio Coletivo de Greve'),(1001,'Agravo de Instrumento em Agravo de Petição'),(1004,'Agravo de Petição'),(1111,'Execução de Título Judicial'),(1114,'Execução Contra a Fazenda Pública'),(1116,'Execução Fiscal'),(1117,'Execução Hipotecária do Sistema Financeiro da Habitação'),(1118,'Embargos à Execução Fiscal'),(1124,'Justificação de Dinheiro a Risco'),(1178,'Incidente de Arguição de Inconstitucionalidade'),(1199,'Pedido de Providências'),(1208,'Agravo Interno'),(1230,'Exceção de Impedimento'),(1231,'Exceção de Suspeição'),(1232,'Exceção de Incompetência'),(1262,'Processo Administrativo Disciplinar em face de Servidor'),(1264,'Processo Administrativo Disciplinar em face de Magistrado'),(1265,'Precatório'),(1266,'Requisição de Pequeno Valor'),(1269,'Habeas Corpus'),(1271,'Recurso de Medida Cautelar'),(1283,'Superveniência de doença mental'),(1284,'Unificação de penas'),(1288,'Transferência entre estabelecimentos penais'),(1289,'Outras medidas provisionais'),(1291,'Reabilitação'),(1294,'Outros procedimentos de jurisdição voluntária'),(1295,'Alvará Judicial'),(1298,'Processo Administrativo'),(1299,'Recurso Administrativo'),(1301,'Reclamação Disciplinar'),(1303,'Correição Extraordinária'),(1304,'Inspeção'),(1306,'Recurso em Processo Administrativo Disciplinar em face de Servidor'),(1307,'Correição Ordinária'),(1308,'Sindicância'),(1683,'Retificação de Registro de Imóvel'),(1689,'Embargos de Declaração'),(1701,'Nomeação de Advogado'),(1702,'Impugnação de Assistência Judiciária'),(1707,'Reintegração / Manutenção de Posse'),(1709,'Interdito Proibitório'),(1710,'Mandado de Segurança'),(1711,'Agravo de Instrumento em Recurso Especial'),(1712,'Agravo de Instrumento em Recurso Extraordinário'),(1714,'Execução Provisória'),(1715,'Embargos do Acusado'),(1717,'Alienação de Bens do Acusado'),(1719,'Avaliação para atestar dependência de drogas'),(1723,'Contraprotesto Judicial'),(1725,'Notificação'),(1726,'Interpelação'),(1727,'Petição'),(1728,'Apelação / Remessa Necesária'),(1729,'Agravo Regimental'),(1730,'Recurso Ordinário'),(1731,'Investigação contra magistrado'),(1733,'Procedimento Investigatório Criminal (PIC-MP) '),(10942,'Correição Parcial'),(10943,'Ação Penal - Procedimento Sumário'),(10944,'Ação Penal - Procedimento Sumaríssimo'),(10980,'Cumprimento Provisório de Decisão'),(10981,'Impugnação ao Cumprimento  de Decisão'),(11398,'Recurso em sentido estrito/Recurso ex officio'),(11399,'Execução de Medida de Segurança'),(11787,'Assistência Judiciária'),(11788,'Exibição de Documento ou Coisa '),(11789,'Impugnação ao Valor da Causa'),(11790,'Impugnação de Assistência Judiciária'),(11791,'Pedido de Uniformização de Interpretação de Lei'),(11793,'Justificação Criminal'),(11798,'Processo de Aplicação de Medida de Segurança Por Fato Não Criminoso'),(11875,'Reclamação Pré-processual'),(11955,'Cautelar Inominada Criminal'),(11957,'Remição de Pena'),(12070,'Pedido de Medida de Proteção'),(12071,'Procedimentos Cautelares'),(12072,'Busca e Apreensão'),(12073,'Internação Provisória'),(12074,'Pedido De Desinternação/Reavaliação/Substituição/Suspensão da Medida'),(12075,'Procedimento Conciliatório'),(12076,'Restabelecimento do Poder Familiar'),(12077,'Homologação em Acordo de Colaboração Premiada'),(12078,'Cumprimento de Sentença contra a Fazenda Pública'),(12079,'Execução de Título Extajudicial contra a Fazenda Pública'),(12080,'Incidente de Impedimento'),(12081,'Incidente de Suspeição'),(12082,'Carta Arbitral'),(12083,'Tutela Antecipada Antecedente'),(12084,'Tutela Cautelar Antecedente'),(12085,'Incidente de Resolução de Demandas Repetitivas'),(12086,'Dissolução Parcial de Sociedade'),(12087,'Incidente de Assunção de Competência'),(12088,'Liquidação Provisória de Sentença pelo Procedimento Comum'),(12089,'Renovação de Permanência em Estabelecimento Penal Federal'),(12119,'Incidente de Desconsideração de Personalidade Jurídica'),(12121,'Auto de Prisão'),(12122,'Reclamação Criminal'),(12123,'Roteiro de Pena'),(12125,'Progressão de Regime'),(12126,'Regressão de Regime'),(12127,'Remição'),(12128,'Livramento Condicional'),(12129,'Outros Incidentes de Execução Iniciados de Ofício'),(12133,'Tutela Provisória'),(12134,'Tutela Cautelar Antecedente'),(12135,'Tutela Antecipada Antecedente'),(12136,'Pedido de Mediação Pré-Processual'),(12139,'Contestação em Foro Diverso');
/*!40000 ALTER TABLE `cnj_classe` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `estilo`
--

LOCK TABLES `estilo` WRITE;
/*!40000 ALTER TABLE `estilo` DISABLE KEYS */;
INSERT INTO `estilo` VALUES (1,'Pessoal - Interno','blue',1,1,1),(2,'Unidade - Interno','yellow',1,0,2),(3,'Pessoal - Externo','pink',0,1,3),(4,'Grupo - Externo','green',0,0,4);
/*!40000 ALTER TABLE `estilo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `orgao`
--

LOCK TABLES `orgao` WRITE;
/*!40000 ALTER TABLE `orgao` DISABLE KEYS */;
INSERT INTO `orgao` VALUES (1,'TRF2'),(2,'JFRJ'),(3,'JFES');
/*!40000 ALTER TABLE `orgao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `tipo_marca`
--

LOCK TABLES `tipo_marca` WRITE;
/*!40000 ALTER TABLE `tipo_marca` DISABLE KEYS */;
INSERT INTO `tipo_marca` VALUES (1,'Criminal');
/*!40000 ALTER TABLE `tipo_marca` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `tipo_marca_item`
--

LOCK TABLES `tipo_marca_item` WRITE;
/*!40000 ALTER TABLE `tipo_marca_item` DISABLE KEYS */;
INSERT INTO `tipo_marca_item` VALUES (1,1,'Denúncia: Aconstada',1),(2,1,'Denúncia: Recebida',2),(3,1,'Denúncia: Rejeitada',3),(4,1,'Denúncia: Diligência',4),(5,1,'Denúncia: Facs Juntadas',5),(6,1,'Resposta à Acusação: Procuração',6),(7,1,'Resposta à Acusação: Citação',7),(8,1,'Resposta à Acusação: Requisição SEAP',8),(9,1,'Resposta à Acusação: Requisição PF',9),(10,1,'Resposta à Acusação: Resposta',10),(11,1,'Resposta à Acusação: Diligência',11),(12,1,'Despacho Saneador: Decisão',12),(13,1,'Audiência de Instrução e Julgamento: Testemunha de Acusação',13),(14,1,'Audiência de Instrução e Julgamento: Testemunha de Defesa',14),(15,1,'Audiência de Instrução e Julgamento: Interrogatório',15),(16,1,'Audiência de Instrução e Julgamento: Defesa Intimada',16),(17,1,'Audiência de Instrução e Julgamento: MPF Intimado',17),(18,1,'Diligências: MPF',18),(19,1,'Diligências: Defesa',19),(20,1,'Alegações Finais: MPF',20),(21,1,'Alegações Finais: Defesa',21);
/*!40000 ALTER TABLE `tipo_marca_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `tx_tipo_marca_cnj_classe`
--

LOCK TABLES `tx_tipo_marca_cnj_classe` WRITE;
/*!40000 ALTER TABLE `tx_tipo_marca_cnj_classe` DISABLE KEYS */;
INSERT INTO `tx_tipo_marca_cnj_classe` VALUES (1,202),(1,436);
/*!40000 ALTER TABLE `tx_tipo_marca_cnj_classe` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-08-17 13:19:31
