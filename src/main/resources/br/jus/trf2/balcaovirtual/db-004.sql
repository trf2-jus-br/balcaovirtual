UPDATE `tipo_marca_item` SET `TIMI_NM`='Denúncia Acostada' WHERE `TIMI_ID`='1';
UPDATE `tipo_marca_item` SET `TIMI_NM`='Recebimento Denúncia' WHERE `TIMI_ID`='2';
UPDATE `tipo_marca_item` SET `TIMI_NM`='Rejeição Denúncia' WHERE `TIMI_ID`='3';
UPDATE `tipo_marca_item` SET `TIMI_NM`='FAC' WHERE `TIMI_ID`='4';
UPDATE `tipo_marca_item` SET `TIMI_NM`='Defesa Preliminar (CPP 514)', `TIMI_NR_ORDEM`='7' WHERE `TIMI_ID`='6';
UPDATE `tipo_marca_item` SET `TIMI_NM`='Resposta  Acusação', `TIMI_NR_ORDEM`='6' WHERE `TIMI_ID`='10';
UPDATE `tipo_marca_item` SET `TIMI_NM`='Citação Negativa', `TIMI_NR_ORDEM`='9' WHERE `TIMI_ID`='8';
UPDATE `tipo_marca_item` SET `TIMI_NM`='Publicação Edital', `TIMI_NR_ORDEM`='10' WHERE `TIMI_ID`='9';
UPDATE `tipo_marca_item` SET `TIMI_NM`='Requisição preso SEAP' WHERE `TIMI_ID`='11';
UPDATE `tipo_marca_item` SET `TIMI_NM`='Requisição preso PF' WHERE `TIMI_ID`='12';
UPDATE `tipo_marca_item` SET `TIMI_NM`='Laudo pericial' WHERE `TIMI_ID`='13';
UPDATE `tipo_marca_item` SET `TIMI_NM`='Despacho Saneador' WHERE `TIMI_ID`='14';
UPDATE `tipo_marca_item` SET `TIMI_NM`='Registro de Audiência ' WHERE `TIMI_ID`='15';
UPDATE `tipo_marca_item` SET `TIMI_NM`='Intimação Positiva Testemunha' WHERE `TIMI_ID`='16';
UPDATE `tipo_marca_item` SET `TIMI_NM`='Intimação Positiva Réu' WHERE `TIMI_ID`='17';
UPDATE `tipo_marca_item` SET `TIMI_NM`='Intimação Negativa Testemunha' WHERE `TIMI_ID`='18';
UPDATE `tipo_marca_item` SET `TIMI_NM`='Intimação Negativa Réu' WHERE `TIMI_ID`='19';
UPDATE `tipo_marca_item` SET `TIMI_NM`='Alegações Finais MPF' WHERE `TIMI_ID`='20';
UPDATE `tipo_marca_item` SET `TIMI_NM`='Alegações Finais Defesa' WHERE `TIMI_ID`='21';
UPDATE `tipo_marca_item` SET `TIMI_NM`='Citação Positiva', `TIMI_NR_ORDEM`='8' WHERE `TIMI_ID`='7';
UPDATE `tipo_marca_item` SET `TIMI_NM`='Procuração', `TIMI_NR_ORDEM`='5' WHERE `TIMI_ID`='5';

INSERT INTO `tipo_marca_item` (`TIMI_ID`, `TIMA_ID`, `TIMI_NM`, `TIMI_NR_ORDEM`) VALUES ('22', '1', 'Sentença', '22');
INSERT INTO `tipo_marca_item` (`TIMI_ID`, `TIMA_ID`, `TIMI_NM`, `TIMI_NR_ORDEM`) VALUES ('23', '1', 'Embargos Declaração', '23');
INSERT INTO `tipo_marca_item` (`TIMI_ID`, `TIMA_ID`, `TIMI_NM`, `TIMI_NR_ORDEM`) VALUES ('24', '1', 'Recurso MPF', '24');
INSERT INTO `tipo_marca_item` (`TIMI_ID`, `TIMA_ID`, `TIMI_NM`, `TIMI_NR_ORDEM`) VALUES ('25', '1', 'Recurso Defesa', '25');
INSERT INTO `tipo_marca_item` (`TIMI_ID`, `TIMA_ID`, `TIMI_NM`, `TIMI_NR_ORDEM`) VALUES ('26', '1', 'Contrarrazões MPF', '26');
INSERT INTO `tipo_marca_item` (`TIMI_ID`, `TIMA_ID`, `TIMI_NM`, `TIMI_NR_ORDEM`) VALUES ('27', '1', 'Contrarrazões Defesa', '27');

