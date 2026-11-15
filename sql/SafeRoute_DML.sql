-- =====================================================================
-- SafeRoute · Global Solution 2026.2 · Database Design
-- Script D.M.L. (Oracle Database 19c) · Turma 2ESA
--   Guilherme Rodrigues de Andrade - RM 565863
--   Gusthavo Costa de Melo         - RM 561330
--
-- Dados de EXEMPLO: pessoas, e-mails e telefones são fictícios, e os
-- valores de alagamento são ilustrativos (não são medições reais).
-- Execute depois do script DDL. Inserção na ordem das dependências (pais -> filhos).
-- =====================================================================

-- ---------------------------------------------------------------------
-- SR_BAIRRO (5 linhas)
-- ---------------------------------------------------------------------
INSERT INTO SR_BAIRRO (id_bairro, nm_bairro, nm_cidade, sg_uf, nm_regiao) VALUES (1, 'Vila Mariana', 'São Paulo', 'SP', 'Zona Sul');
INSERT INTO SR_BAIRRO (id_bairro, nm_bairro, nm_cidade, sg_uf, nm_regiao) VALUES (2, 'Moema',        'São Paulo', 'SP', 'Zona Sul');
INSERT INTO SR_BAIRRO (id_bairro, nm_bairro, nm_cidade, sg_uf, nm_regiao) VALUES (3, 'Saúde',        'São Paulo', 'SP', 'Zona Sul');
INSERT INTO SR_BAIRRO (id_bairro, nm_bairro, nm_cidade, sg_uf, nm_regiao) VALUES (4, 'Aclimação',    'São Paulo', 'SP', 'Centro');
INSERT INTO SR_BAIRRO (id_bairro, nm_bairro, nm_cidade, sg_uf, nm_regiao) VALUES (5, 'Ipiranga',     'São Paulo', 'SP', 'Zona Sul');

-- ---------------------------------------------------------------------
-- SR_TRECHO (8 linhas)
-- ---------------------------------------------------------------------
INSERT INTO SR_TRECHO (id_trecho, id_bairro, cd_trecho, nm_logradouro, ds_referencia, vl_cota_m, vl_declividade_pct, vl_dist_corrego_m, vl_bocas_lobo_100m, dt_ultima_limpeza, vl_latitude, vl_longitude)
VALUES (1, 1, 'T01', 'Rua Domingos de Morais', 'Entre a Estação Vila Mariana e a Rua Sena Madureira', 28.0, 2.1, 820, 2.4, TO_DATE('2025-12-10', 'YYYY-MM-DD'), -23.589200, -46.634700);
INSERT INTO SR_TRECHO (id_trecho, id_bairro, cd_trecho, nm_logradouro, ds_referencia, vl_cota_m, vl_declividade_pct, vl_dist_corrego_m, vl_bocas_lobo_100m, dt_ultima_limpeza, vl_latitude, vl_longitude)
VALUES (2, 1, 'T02', 'Rua Sena Madureira', 'Próximo à Rua Dr. Diogo de Faria', 9.5, 1.2, 210, 1.6, TO_DATE('2025-10-05', 'YYYY-MM-DD'), -23.592400, -46.641000);
INSERT INTO SR_TRECHO (id_trecho, id_bairro, cd_trecho, nm_logradouro, ds_referencia, vl_cota_m, vl_declividade_pct, vl_dist_corrego_m, vl_bocas_lobo_100m, dt_ultima_limpeza, vl_latitude, vl_longitude)
VALUES (3, 1, 'T03', 'Av. Conselheiro Rodrigues Alves', 'Cruzamento com a Rua Sena Madureira', 6.0, 0.9, 140, 1.8, TO_DATE('2025-11-20', 'YYYY-MM-DD'), -23.594100, -46.643800);
INSERT INTO SR_TRECHO (id_trecho, id_bairro, cd_trecho, nm_logradouro, ds_referencia, vl_cota_m, vl_declividade_pct, vl_dist_corrego_m, vl_bocas_lobo_100m, dt_ultima_limpeza, vl_latitude, vl_longitude)
VALUES (4, 1, 'T04', 'Rua França Pinto', 'Altura da Rua Joaquim Távora', 15.5, 3.4, 460, 2.0, TO_DATE('2025-12-01', 'YYYY-MM-DD'), -23.586900, -46.645100);
INSERT INTO SR_TRECHO (id_trecho, id_bairro, cd_trecho, nm_logradouro, ds_referencia, vl_cota_m, vl_declividade_pct, vl_dist_corrego_m, vl_bocas_lobo_100m, dt_ultima_limpeza, vl_latitude, vl_longitude)
VALUES (5, 1, 'T05', 'Rua Vergueiro', 'Em frente à Estação Ana Rosa', 33.0, 4.8, 1150, 2.9, TO_DATE('2026-01-08', 'YYYY-MM-DD'), -23.581500, -46.638200);
INSERT INTO SR_TRECHO (id_trecho, id_bairro, cd_trecho, nm_logradouro, ds_referencia, vl_cota_m, vl_declividade_pct, vl_dist_corrego_m, vl_bocas_lobo_100m, dt_ultima_limpeza, vl_latitude, vl_longitude)
VALUES (6, 1, 'T06', 'Av. Prof. Noé Azevedo', 'Próximo à Rua Rio Grande', 11.0, 1.5, 300, 1.4, NULL, -23.591000, -46.637500);
INSERT INTO SR_TRECHO (id_trecho, id_bairro, cd_trecho, nm_logradouro, ds_referencia, vl_cota_m, vl_declividade_pct, vl_dist_corrego_m, vl_bocas_lobo_100m, dt_ultima_limpeza, vl_latitude, vl_longitude)
VALUES (7, 2, 'T07', 'Av. República do Líbano', 'Cruzamento com a Av. IV Centenário', 4.5, 0.6, 90, 2.2, TO_DATE('2025-11-02', 'YYYY-MM-DD'), -23.594700, -46.659000);
INSERT INTO SR_TRECHO (id_trecho, id_bairro, cd_trecho, nm_logradouro, ds_referencia, vl_cota_m, vl_declividade_pct, vl_dist_corrego_m, vl_bocas_lobo_100m, dt_ultima_limpeza, vl_latitude, vl_longitude)
VALUES (8, 5, 'T08', 'Av. Ricardo Jafet', 'Próximo ao Viaduto Saioá', 3.0, 0.5, 60, 2.6, TO_DATE('2025-12-15', 'YYYY-MM-DD'), -23.600900, -46.620300);

-- ---------------------------------------------------------------------
-- SR_USUARIO (6 linhas) · senhas guardadas apenas como hash (exemplo fictício)
-- ---------------------------------------------------------------------
INSERT INTO SR_USUARIO (id_usuario, nm_usuario, ds_email, nr_telefone, ds_senha_hash, tp_perfil, dt_cadastro, st_ativo)
VALUES (1, 'Administrador SafeRoute', 'admin@saferoute.example', '11900000000', '$2a$10$Qm1aZk9yX2FkbWluX2hhc2hfZXhlbXBsbw', 'ADMIN', TO_DATE('2025-08-01', 'YYYY-MM-DD'), 'S');
INSERT INTO SR_USUARIO (id_usuario, nm_usuario, ds_email, nr_telefone, ds_senha_hash, tp_perfil, dt_cadastro, st_ativo)
VALUES (2, 'Juliana Ramos', 'juliana.ramos@email.example', '11911110001', '$2a$10$SnVsaWFuYV9oYXNoX2V4ZW1wbG9fZmljdGljaW8', 'CIDADAO', TO_DATE('2025-09-14', 'YYYY-MM-DD'), 'S');
INSERT INTO SR_USUARIO (id_usuario, nm_usuario, ds_email, nr_telefone, ds_senha_hash, tp_perfil, dt_cadastro, st_ativo)
VALUES (3, 'Antônio Ferreira', 'antonio.ferreira@email.example', '11911110002', '$2a$10$QW50b25pb19oYXNoX2V4ZW1wbG9fZmljdGljaW8', 'CIDADAO', TO_DATE('2025-09-20', 'YYYY-MM-DD'), 'S');
INSERT INTO SR_USUARIO (id_usuario, nm_usuario, ds_email, nr_telefone, ds_senha_hash, tp_perfil, dt_cadastro, st_ativo)
VALUES (4, 'Rafael Costa', 'rafael.costa@email.example', '11911110003', '$2a$10$UmFmYWVsX2hhc2hfZXhlbXBsb19maWN0aWNpbw', 'CIDADAO', TO_DATE('2025-10-02', 'YYYY-MM-DD'), 'S');
INSERT INTO SR_USUARIO (id_usuario, nm_usuario, ds_email, nr_telefone, ds_senha_hash, tp_perfil, dt_cadastro, st_ativo)
VALUES (5, 'Mariana Lopes', 'mariana.lopes@email.example', NULL, '$2a$10$TWFyaWFuYV9oYXNoX2V4ZW1wbG9fZmljdGljaW8', 'CIDADAO', TO_DATE('2025-11-11', 'YYYY-MM-DD'), 'S');
INSERT INTO SR_USUARIO (id_usuario, nm_usuario, ds_email, nr_telefone, ds_senha_hash, tp_perfil, dt_cadastro, st_ativo)
VALUES (6, 'Pedro Almeida', 'pedro.almeida@email.example', '11911110005', '$2a$10$UGVkcm9faGFzaF9leGVtcGxvX2ZpY3RpY2lvXw', 'CIDADAO', TO_DATE('2026-01-05', 'YYYY-MM-DD'), 'N');

-- ---------------------------------------------------------------------
-- SR_PREVISAO_CHUVA (6 linhas)
-- ---------------------------------------------------------------------
INSERT INTO SR_PREVISAO_CHUVA (id_previsao, id_bairro, dt_hr_inicio, dt_hr_fim, vl_chuva_mm_h, vl_prob_chuva_pct, nm_fonte, dt_hr_emissao)
VALUES (1, 1, TO_DATE('2026-01-14 17:00', 'YYYY-MM-DD HH24:MI'), TO_DATE('2026-01-14 19:00', 'YYYY-MM-DD HH24:MI'), 38.0, 85, 'CGE', TO_DATE('2026-01-14 12:00', 'YYYY-MM-DD HH24:MI'));
INSERT INTO SR_PREVISAO_CHUVA (id_previsao, id_bairro, dt_hr_inicio, dt_hr_fim, vl_chuva_mm_h, vl_prob_chuva_pct, nm_fonte, dt_hr_emissao)
VALUES (2, 1, TO_DATE('2026-01-20 18:00', 'YYYY-MM-DD HH24:MI'), TO_DATE('2026-01-20 20:00', 'YYYY-MM-DD HH24:MI'), 22.5, 70, 'INMET', TO_DATE('2026-01-20 13:00', 'YYYY-MM-DD HH24:MI'));
INSERT INTO SR_PREVISAO_CHUVA (id_previsao, id_bairro, dt_hr_inicio, dt_hr_fim, vl_chuva_mm_h, vl_prob_chuva_pct, nm_fonte, dt_hr_emissao)
VALUES (3, 2, TO_DATE('2026-01-14 17:00', 'YYYY-MM-DD HH24:MI'), TO_DATE('2026-01-14 19:00', 'YYYY-MM-DD HH24:MI'), 35.0, 80, 'CGE', TO_DATE('2026-01-14 12:00', 'YYYY-MM-DD HH24:MI'));
INSERT INTO SR_PREVISAO_CHUVA (id_previsao, id_bairro, dt_hr_inicio, dt_hr_fim, vl_chuva_mm_h, vl_prob_chuva_pct, nm_fonte, dt_hr_emissao)
VALUES (4, 5, TO_DATE('2026-11-05 16:00', 'YYYY-MM-DD HH24:MI'), TO_DATE('2026-11-05 18:00', 'YYYY-MM-DD HH24:MI'), 45.0, 90, 'CGE', TO_DATE('2026-11-05 11:00', 'YYYY-MM-DD HH24:MI'));
INSERT INTO SR_PREVISAO_CHUVA (id_previsao, id_bairro, dt_hr_inicio, dt_hr_fim, vl_chuva_mm_h, vl_prob_chuva_pct, nm_fonte, dt_hr_emissao)
VALUES (5, 1, TO_DATE('2026-11-05 16:00', 'YYYY-MM-DD HH24:MI'), TO_DATE('2026-11-05 18:00', 'YYYY-MM-DD HH24:MI'), 41.0, 90, 'CGE', TO_DATE('2026-11-05 11:00', 'YYYY-MM-DD HH24:MI'));
INSERT INTO SR_PREVISAO_CHUVA (id_previsao, id_bairro, dt_hr_inicio, dt_hr_fim, vl_chuva_mm_h, vl_prob_chuva_pct, nm_fonte, dt_hr_emissao)
VALUES (6, 3, TO_DATE('2026-11-10 15:00', 'YYYY-MM-DD HH24:MI'), TO_DATE('2026-11-10 16:00', 'YYYY-MM-DD HH24:MI'), 8.0, 40, 'INMET', TO_DATE('2026-11-09 21:00', 'YYYY-MM-DD HH24:MI'));

-- ---------------------------------------------------------------------
-- SR_HISTORICO_ALAGAMENTO (10 linhas)
-- ---------------------------------------------------------------------
INSERT INTO SR_HISTORICO_ALAGAMENTO (id_historico, id_trecho, dt_hr_ocorrencia, vl_lamina_cm, vl_chuva_1h_mm, st_intransitavel, nm_fonte)
VALUES (1, 3, TO_DATE('2025-01-22 18:10', 'YYYY-MM-DD HH24:MI'), 32.0, 48.0, 'S', 'CGE');
INSERT INTO SR_HISTORICO_ALAGAMENTO (id_historico, id_trecho, dt_hr_ocorrencia, vl_lamina_cm, vl_chuva_1h_mm, st_intransitavel, nm_fonte)
VALUES (2, 3, TO_DATE('2025-02-14 17:40', 'YYYY-MM-DD HH24:MI'), 24.5, 39.5, 'S', 'CGE');
INSERT INTO SR_HISTORICO_ALAGAMENTO (id_historico, id_trecho, dt_hr_ocorrencia, vl_lamina_cm, vl_chuva_1h_mm, st_intransitavel, nm_fonte)
VALUES (3, 2, TO_DATE('2025-02-14 17:50', 'YYYY-MM-DD HH24:MI'), 21.0, 39.5, 'S', 'CGE');
INSERT INTO SR_HISTORICO_ALAGAMENTO (id_historico, id_trecho, dt_hr_ocorrencia, vl_lamina_cm, vl_chuva_1h_mm, st_intransitavel, nm_fonte)
VALUES (4, 2, TO_DATE('2025-03-05 19:00', 'YYYY-MM-DD HH24:MI'), 12.0, 25.0, 'N', 'Relatos SafeRoute');
INSERT INTO SR_HISTORICO_ALAGAMENTO (id_historico, id_trecho, dt_hr_ocorrencia, vl_lamina_cm, vl_chuva_1h_mm, st_intransitavel, nm_fonte)
VALUES (5, 7, TO_DATE('2025-01-22 18:20', 'YYYY-MM-DD HH24:MI'), 35.5, 48.0, 'S', 'CGE');
INSERT INTO SR_HISTORICO_ALAGAMENTO (id_historico, id_trecho, dt_hr_ocorrencia, vl_lamina_cm, vl_chuva_1h_mm, st_intransitavel, nm_fonte)
VALUES (6, 8, TO_DATE('2025-02-14 17:30', 'YYYY-MM-DD HH24:MI'), 41.0, 39.5, 'S', 'CGE');
INSERT INTO SR_HISTORICO_ALAGAMENTO (id_historico, id_trecho, dt_hr_ocorrencia, vl_lamina_cm, vl_chuva_1h_mm, st_intransitavel, nm_fonte)
VALUES (7, 8, TO_DATE('2025-12-18 16:45', 'YYYY-MM-DD HH24:MI'), 28.0, 33.0, 'S', 'CGE');
INSERT INTO SR_HISTORICO_ALAGAMENTO (id_historico, id_trecho, dt_hr_ocorrencia, vl_lamina_cm, vl_chuva_1h_mm, st_intransitavel, nm_fonte)
VALUES (8, 6, TO_DATE('2025-12-18 16:50', 'YYYY-MM-DD HH24:MI'), 17.5, 33.0, 'N', 'Relatos SafeRoute');
INSERT INTO SR_HISTORICO_ALAGAMENTO (id_historico, id_trecho, dt_hr_ocorrencia, vl_lamina_cm, vl_chuva_1h_mm, st_intransitavel, nm_fonte)
VALUES (9, 4, TO_DATE('2025-02-14 17:45', 'YYYY-MM-DD HH24:MI'), 8.0, 39.5, 'N', 'Relatos SafeRoute');
INSERT INTO SR_HISTORICO_ALAGAMENTO (id_historico, id_trecho, dt_hr_ocorrencia, vl_lamina_cm, vl_chuva_1h_mm, st_intransitavel, nm_fonte)
VALUES (10, 3, TO_DATE('2025-12-18 16:40', 'YYYY-MM-DD HH24:MI'), 26.0, 33.0, 'S', 'CGE');

-- ---------------------------------------------------------------------
-- SR_RELATO (7 linhas) · relato expira 2 h após o envio
-- ---------------------------------------------------------------------
INSERT INTO SR_RELATO (id_relato, id_trecho, id_usuario, dt_hr_relato, tp_nivel_agua, ds_url_foto, st_relato, dt_hr_expiracao, ds_motivo_remocao)
VALUES (1, 3, 4, TO_DATE('2026-01-14 17:35', 'YYYY-MM-DD HH24:MI'), 'INTRANSITAVEL', 'https://saferoute.example/fotos/relato-1.jpg', 'EXPIRADO', TO_DATE('2026-01-14 19:35', 'YYYY-MM-DD HH24:MI'), NULL);
INSERT INTO SR_RELATO (id_relato, id_trecho, id_usuario, dt_hr_relato, tp_nivel_agua, ds_url_foto, st_relato, dt_hr_expiracao, ds_motivo_remocao)
VALUES (2, 2, 2, TO_DATE('2026-01-14 17:50', 'YYYY-MM-DD HH24:MI'), 'JOELHO', NULL, 'EXPIRADO', TO_DATE('2026-01-14 19:50', 'YYYY-MM-DD HH24:MI'), NULL);
INSERT INTO SR_RELATO (id_relato, id_trecho, id_usuario, dt_hr_relato, tp_nivel_agua, ds_url_foto, st_relato, dt_hr_expiracao, ds_motivo_remocao)
VALUES (3, 7, 5, TO_DATE('2026-01-14 18:05', 'YYYY-MM-DD HH24:MI'), 'INTRANSITAVEL', 'https://saferoute.example/fotos/relato-3.jpg', 'EXPIRADO', TO_DATE('2026-01-14 20:05', 'YYYY-MM-DD HH24:MI'), NULL);
INSERT INTO SR_RELATO (id_relato, id_trecho, id_usuario, dt_hr_relato, tp_nivel_agua, ds_url_foto, st_relato, dt_hr_expiracao, ds_motivo_remocao)
VALUES (4, 6, 3, TO_DATE('2026-01-14 18:10', 'YYYY-MM-DD HH24:MI'), 'POCAS', NULL, 'EXPIRADO', TO_DATE('2026-01-14 20:10', 'YYYY-MM-DD HH24:MI'), NULL);
INSERT INTO SR_RELATO (id_relato, id_trecho, id_usuario, dt_hr_relato, tp_nivel_agua, ds_url_foto, st_relato, dt_hr_expiracao, ds_motivo_remocao)
VALUES (5, 3, 6, TO_DATE('2026-01-14 18:20', 'YYYY-MM-DD HH24:MI'), 'INTRANSITAVEL', NULL, 'REMOVIDO', TO_DATE('2026-01-14 20:20', 'YYYY-MM-DD HH24:MI'), 'Duplicado: o relato 1 já cobria este trecho');
INSERT INTO SR_RELATO (id_relato, id_trecho, id_usuario, dt_hr_relato, tp_nivel_agua, ds_url_foto, st_relato, dt_hr_expiracao, ds_motivo_remocao)
VALUES (6, 8, 4, TO_DATE('2026-11-05 16:40', 'YYYY-MM-DD HH24:MI'), 'INTRANSITAVEL', 'https://saferoute.example/fotos/relato-6.jpg', 'ATIVO', TO_DATE('2026-11-05 18:40', 'YYYY-MM-DD HH24:MI'), NULL);
INSERT INTO SR_RELATO (id_relato, id_trecho, id_usuario, dt_hr_relato, tp_nivel_agua, ds_url_foto, st_relato, dt_hr_expiracao, ds_motivo_remocao)
VALUES (7, 3, 2, TO_DATE('2026-11-05 16:45', 'YYYY-MM-DD HH24:MI'), 'JOELHO', NULL, 'ATIVO', TO_DATE('2026-11-05 18:45', 'YYYY-MM-DD HH24:MI'), NULL);

-- ---------------------------------------------------------------------
-- SR_CONFIRMACAO_RELATO (7 linhas) · ninguém confirma o próprio relato
-- ---------------------------------------------------------------------
INSERT INTO SR_CONFIRMACAO_RELATO (id_confirmacao, id_relato, id_usuario, st_ainda_alagado, dt_hr_confirmacao)
VALUES (1, 1, 2, 'S', TO_DATE('2026-01-14 17:45', 'YYYY-MM-DD HH24:MI'));
INSERT INTO SR_CONFIRMACAO_RELATO (id_confirmacao, id_relato, id_usuario, st_ainda_alagado, dt_hr_confirmacao)
VALUES (2, 1, 5, 'S', TO_DATE('2026-01-14 17:55', 'YYYY-MM-DD HH24:MI'));
INSERT INTO SR_CONFIRMACAO_RELATO (id_confirmacao, id_relato, id_usuario, st_ainda_alagado, dt_hr_confirmacao)
VALUES (3, 1, 3, 'S', TO_DATE('2026-01-14 18:30', 'YYYY-MM-DD HH24:MI'));
INSERT INTO SR_CONFIRMACAO_RELATO (id_confirmacao, id_relato, id_usuario, st_ainda_alagado, dt_hr_confirmacao)
VALUES (4, 2, 4, 'S', TO_DATE('2026-01-14 18:00', 'YYYY-MM-DD HH24:MI'));
INSERT INTO SR_CONFIRMACAO_RELATO (id_confirmacao, id_relato, id_usuario, st_ainda_alagado, dt_hr_confirmacao)
VALUES (5, 3, 4, 'S', TO_DATE('2026-01-14 18:15', 'YYYY-MM-DD HH24:MI'));
INSERT INTO SR_CONFIRMACAO_RELATO (id_confirmacao, id_relato, id_usuario, st_ainda_alagado, dt_hr_confirmacao)
VALUES (6, 4, 2, 'N', TO_DATE('2026-01-14 18:40', 'YYYY-MM-DD HH24:MI'));
INSERT INTO SR_CONFIRMACAO_RELATO (id_confirmacao, id_relato, id_usuario, st_ainda_alagado, dt_hr_confirmacao)
VALUES (7, 6, 2, 'S', TO_DATE('2026-11-05 16:55', 'YYYY-MM-DD HH24:MI'));

-- ---------------------------------------------------------------------
-- SR_TRAJETO_FAVORITO (5 linhas)
-- ---------------------------------------------------------------------
INSERT INTO SR_TRAJETO_FAVORITO (id_trajeto, id_usuario, nm_trajeto, ds_origem, ds_destino, tp_modal, hr_saida_habitual, st_alerta_ativo)
VALUES (1, 2, 'Trabalho -> Creche', 'Rua Domingos de Morais - Vila Mariana', 'Rua França Pinto - Vila Mariana', 'A_PE', '18:00', 'S');
INSERT INTO SR_TRAJETO_FAVORITO (id_trajeto, id_usuario, nm_trajeto, ds_origem, ds_destino, tp_modal, hr_saida_habitual, st_alerta_ativo)
VALUES (2, 2, 'Creche -> Casa', 'Rua França Pinto - Vila Mariana', 'Av. Ricardo Jafet - Ipiranga', 'ONIBUS', '18:30', 'S');
INSERT INTO SR_TRAJETO_FAVORITO (id_trajeto, id_usuario, nm_trajeto, ds_origem, ds_destino, tp_modal, hr_saida_habitual, st_alerta_ativo)
VALUES (3, 3, 'Casa -> UBS', 'Rua Sena Madureira - Vila Mariana', 'Rua Vergueiro - Vila Mariana', 'A_PE', '09:00', 'S');
INSERT INTO SR_TRAJETO_FAVORITO (id_trajeto, id_usuario, nm_trajeto, ds_origem, ds_destino, tp_modal, hr_saida_habitual, st_alerta_ativo)
VALUES (4, 4, 'Casa -> Faculdade', 'Av. Conselheiro Rodrigues Alves - Vila Mariana', 'Av. Paulista - Bela Vista', 'CARRO', '18:40', 'S');
INSERT INTO SR_TRAJETO_FAVORITO (id_trajeto, id_usuario, nm_trajeto, ds_origem, ds_destino, tp_modal, hr_saida_habitual, st_alerta_ativo)
VALUES (5, 5, 'Casa -> Trabalho', 'Av. República do Líbano - Moema', 'Rua Domingos de Morais - Vila Mariana', 'ONIBUS', '07:30', 'N');

-- ---------------------------------------------------------------------
-- SR_ALERTA (6 linhas)
-- ---------------------------------------------------------------------
INSERT INTO SR_ALERTA (id_alerta, id_trajeto, id_previsao, dt_hr_envio, ds_mensagem, st_lido)
VALUES (1, 1, 1, TO_DATE('2026-01-14 16:45', 'YYYY-MM-DD HH24:MI'), 'Chuva forte prevista entre 17h e 19h (38 mm/h). A Rua Sena Madureira tem risco alto: veja a rota segura.', 'S');
INSERT INTO SR_ALERTA (id_alerta, id_trajeto, id_previsao, dt_hr_envio, ds_mensagem, st_lido)
VALUES (2, 2, 1, TO_DATE('2026-01-14 16:45', 'YYYY-MM-DD HH24:MI'), 'Chuva forte prevista entre 17h e 19h (38 mm/h). Considere sair mais cedo ou usar a rota segura.', 'S');
INSERT INTO SR_ALERTA (id_alerta, id_trajeto, id_previsao, dt_hr_envio, ds_mensagem, st_lido)
VALUES (3, 4, 1, TO_DATE('2026-01-14 17:00', 'YYYY-MM-DD HH24:MI'), 'Risco alto na Av. Conselheiro Rodrigues Alves entre 17h e 19h. Sugerimos rota alternativa.', 'S');
INSERT INTO SR_ALERTA (id_alerta, id_trajeto, id_previsao, dt_hr_envio, ds_mensagem, st_lido)
VALUES (4, 1, 5, TO_DATE('2026-11-05 15:30', 'YYYY-MM-DD HH24:MI'), 'Chuva forte prevista entre 16h e 18h (41 mm/h). 2 trechos do seu trajeto têm risco alto.', 'N');
INSERT INTO SR_ALERTA (id_alerta, id_trajeto, id_previsao, dt_hr_envio, ds_mensagem, st_lido)
VALUES (5, 2, 5, TO_DATE('2026-11-05 15:30', 'YYYY-MM-DD HH24:MI'), 'Chuva forte prevista entre 16h e 18h (41 mm/h). Confira a rota segura antes de sair.', 'N');
INSERT INTO SR_ALERTA (id_alerta, id_trajeto, id_previsao, dt_hr_envio, ds_mensagem, st_lido)
VALUES (6, 4, 5, TO_DATE('2026-11-05 15:30', 'YYYY-MM-DD HH24:MI'), 'Risco alto na Av. Conselheiro Rodrigues Alves entre 16h e 18h. Sugerimos rota alternativa.', 'S');

-- ---------------------------------------------------------------------
-- SR_CONSULTA_ROTA (7 linhas) · id_usuario NULL = visitante sem login
-- ---------------------------------------------------------------------
INSERT INTO SR_CONSULTA_ROTA (id_consulta, id_usuario, id_previsao, ds_origem, ds_destino, tp_modal, dt_hr_consulta, nr_min_rota_rapida, nr_min_rota_segura, st_rota_segura_escolhida)
VALUES (1, 2, 1, 'Rua Domingos de Morais - Vila Mariana', 'Rua França Pinto - Vila Mariana', 'A_PE', TO_DATE('2026-01-14 17:55', 'YYYY-MM-DD HH24:MI'), 22, 28, 'S');
INSERT INTO SR_CONSULTA_ROTA (id_consulta, id_usuario, id_previsao, ds_origem, ds_destino, tp_modal, dt_hr_consulta, nr_min_rota_rapida, nr_min_rota_segura, st_rota_segura_escolhida)
VALUES (2, 4, 1, 'Av. Conselheiro Rodrigues Alves - Vila Mariana', 'Av. Paulista - Bela Vista', 'CARRO', TO_DATE('2026-01-14 18:35', 'YYYY-MM-DD HH24:MI'), 18, 25, 'S');
INSERT INTO SR_CONSULTA_ROTA (id_consulta, id_usuario, id_previsao, ds_origem, ds_destino, tp_modal, dt_hr_consulta, nr_min_rota_rapida, nr_min_rota_segura, st_rota_segura_escolhida)
VALUES (3, NULL, 1, 'Estação Ana Rosa - Vila Mariana', 'Rua Domingos de Morais - Vila Mariana', 'A_PE', TO_DATE('2026-01-14 18:00', 'YYYY-MM-DD HH24:MI'), 15, 15, 'N');
INSERT INTO SR_CONSULTA_ROTA (id_consulta, id_usuario, id_previsao, ds_origem, ds_destino, tp_modal, dt_hr_consulta, nr_min_rota_rapida, nr_min_rota_segura, st_rota_segura_escolhida)
VALUES (4, 3, 2, 'Rua Sena Madureira - Vila Mariana', 'Rua Vergueiro - Vila Mariana', 'A_PE', TO_DATE('2026-01-20 18:10', 'YYYY-MM-DD HH24:MI'), 14, 19, 'N');
INSERT INTO SR_CONSULTA_ROTA (id_consulta, id_usuario, id_previsao, ds_origem, ds_destino, tp_modal, dt_hr_consulta, nr_min_rota_rapida, nr_min_rota_segura, st_rota_segura_escolhida)
VALUES (5, 2, 5, 'Rua Domingos de Morais - Vila Mariana', 'Rua França Pinto - Vila Mariana', 'A_PE', TO_DATE('2026-11-05 17:58', 'YYYY-MM-DD HH24:MI'), 22, 29, 'S');
INSERT INTO SR_CONSULTA_ROTA (id_consulta, id_usuario, id_previsao, ds_origem, ds_destino, tp_modal, dt_hr_consulta, nr_min_rota_rapida, nr_min_rota_segura, st_rota_segura_escolhida)
VALUES (6, 5, 3, 'Av. República do Líbano - Moema', 'Rua Domingos de Morais - Vila Mariana', 'ONIBUS', TO_DATE('2026-01-14 17:40', 'YYYY-MM-DD HH24:MI'), 35, 41, 'S');
INSERT INTO SR_CONSULTA_ROTA (id_consulta, id_usuario, id_previsao, ds_origem, ds_destino, tp_modal, dt_hr_consulta, nr_min_rota_rapida, nr_min_rota_segura, st_rota_segura_escolhida)
VALUES (7, NULL, 4, 'Museu do Ipiranga - Ipiranga', 'Estação Santa Cruz - Vila Mariana', 'ONIBUS', TO_DATE('2026-11-05 16:30', 'YYYY-MM-DD HH24:MI'), 30, 38, 'S');

-- ---------------------------------------------------------------------
-- SR_CONSULTA_TRECHO (16 linhas)
-- risco: ALTO >= 0,25 | MEDIO 0,08 a 0,25 | BAIXO < 0,08 (limiares do modelo de Data Science)
-- ---------------------------------------------------------------------
INSERT INTO SR_CONSULTA_TRECHO (id_consulta, id_trecho, tp_risco, vl_prob_intransitavel, vl_lamina_prevista_cm, st_evitado) VALUES (1, 1, 'BAIXO', 0.050,  6.0, 'N');
INSERT INTO SR_CONSULTA_TRECHO (id_consulta, id_trecho, tp_risco, vl_prob_intransitavel, vl_lamina_prevista_cm, st_evitado) VALUES (1, 2, 'ALTO',  0.780, 24.0, 'S');
INSERT INTO SR_CONSULTA_TRECHO (id_consulta, id_trecho, tp_risco, vl_prob_intransitavel, vl_lamina_prevista_cm, st_evitado) VALUES (1, 6, 'MEDIO', 0.210, 16.5, 'N');
INSERT INTO SR_CONSULTA_TRECHO (id_consulta, id_trecho, tp_risco, vl_prob_intransitavel, vl_lamina_prevista_cm, st_evitado) VALUES (1, 4, 'BAIXO', 0.070,  9.0, 'N');
INSERT INTO SR_CONSULTA_TRECHO (id_consulta, id_trecho, tp_risco, vl_prob_intransitavel, vl_lamina_prevista_cm, st_evitado) VALUES (2, 3, 'ALTO',  0.910, 29.5, 'S');
INSERT INTO SR_CONSULTA_TRECHO (id_consulta, id_trecho, tp_risco, vl_prob_intransitavel, vl_lamina_prevista_cm, st_evitado) VALUES (2, 1, 'BAIXO', 0.040,  5.5, 'N');
INSERT INTO SR_CONSULTA_TRECHO (id_consulta, id_trecho, tp_risco, vl_prob_intransitavel, vl_lamina_prevista_cm, st_evitado) VALUES (3, 5, 'BAIXO', 0.010,  2.0, 'N');
INSERT INTO SR_CONSULTA_TRECHO (id_consulta, id_trecho, tp_risco, vl_prob_intransitavel, vl_lamina_prevista_cm, st_evitado) VALUES (3, 1, 'BAIXO', 0.060,  6.5, 'N');
INSERT INTO SR_CONSULTA_TRECHO (id_consulta, id_trecho, tp_risco, vl_prob_intransitavel, vl_lamina_prevista_cm, st_evitado) VALUES (4, 2, 'MEDIO', 0.180, 15.0, 'N');
INSERT INTO SR_CONSULTA_TRECHO (id_consulta, id_trecho, tp_risco, vl_prob_intransitavel, vl_lamina_prevista_cm, st_evitado) VALUES (4, 5, 'BAIXO', 0.020,  3.0, 'N');
INSERT INTO SR_CONSULTA_TRECHO (id_consulta, id_trecho, tp_risco, vl_prob_intransitavel, vl_lamina_prevista_cm, st_evitado) VALUES (5, 2, 'ALTO',  0.820, 25.5, 'S');
INSERT INTO SR_CONSULTA_TRECHO (id_consulta, id_trecho, tp_risco, vl_prob_intransitavel, vl_lamina_prevista_cm, st_evitado) VALUES (5, 6, 'ALTO',  0.400, 19.5, 'S');
INSERT INTO SR_CONSULTA_TRECHO (id_consulta, id_trecho, tp_risco, vl_prob_intransitavel, vl_lamina_prevista_cm, st_evitado) VALUES (5, 4, 'BAIXO', 0.070,  8.5, 'N');
INSERT INTO SR_CONSULTA_TRECHO (id_consulta, id_trecho, tp_risco, vl_prob_intransitavel, vl_lamina_prevista_cm, st_evitado) VALUES (6, 7, 'ALTO',  0.880, 31.0, 'S');
INSERT INTO SR_CONSULTA_TRECHO (id_consulta, id_trecho, tp_risco, vl_prob_intransitavel, vl_lamina_prevista_cm, st_evitado) VALUES (6, 1, 'BAIXO', 0.050,  6.0, 'N');
INSERT INTO SR_CONSULTA_TRECHO (id_consulta, id_trecho, tp_risco, vl_prob_intransitavel, vl_lamina_prevista_cm, st_evitado) VALUES (7, 8, 'ALTO',  0.950, 36.0, 'S');

COMMIT;
