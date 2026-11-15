-- =====================================================================
-- SafeRoute · Global Solution 2026.2 · Database Design
-- Script D.D.L. (Oracle Database 19c) · Turma 2ESA
--   Guilherme Rodrigues de Andrade - RM 565863
--   Gusthavo Costa de Melo         - RM 561330
--
-- Ordem: (0) remove as tabelas e sequences, se já existirem
--        (1) CREATE TABLE com colunas, tipos, NOT NULL e DEFAULT
--        (2) ALTER TABLE com as constraints: PK, UNIQUE, CHECK e FK
--        (3) SEQUENCES usadas pela API Java para gerar os IDs novos
-- Prefixo SR_ = SafeRoute (evita conflito com outras tabelas no schema).
-- =====================================================================

-- ---------------------------------------------------------------------
-- (0) LIMPEZA: permite rodar o script mais de uma vez
-- ---------------------------------------------------------------------
BEGIN
  FOR t IN (SELECT table_name FROM user_tables
             WHERE table_name IN ('SR_CONSULTA_TRECHO','SR_CONSULTA_ROTA','SR_ALERTA',
                                  'SR_TRAJETO_FAVORITO','SR_CONFIRMACAO_RELATO','SR_RELATO',
                                  'SR_HISTORICO_ALAGAMENTO','SR_PREVISAO_CHUVA','SR_USUARIO',
                                  'SR_TRECHO','SR_BAIRRO')) LOOP
    EXECUTE IMMEDIATE 'DROP TABLE ' || t.table_name || ' CASCADE CONSTRAINTS PURGE';
  END LOOP;
  FOR s IN (SELECT sequence_name FROM user_sequences WHERE sequence_name LIKE 'SEQ_SR\_%' ESCAPE '\') LOOP
    EXECUTE IMMEDIATE 'DROP SEQUENCE ' || s.sequence_name;
  END LOOP;
END;
/

-- ---------------------------------------------------------------------
-- (1) CRIAÇÃO DAS TABELAS
-- ---------------------------------------------------------------------

-- Bairros atendidos pelo SafeRoute
CREATE TABLE SR_BAIRRO (
    id_bairro           NUMBER(5)       NOT NULL,
    nm_bairro           VARCHAR2(60)    NOT NULL,
    nm_cidade           VARCHAR2(60)    NOT NULL,
    sg_uf               CHAR(2)         NOT NULL,
    nm_regiao           VARCHAR2(20)
);

-- Trechos de rua avaliados quanto ao risco de alagamento
CREATE TABLE SR_TRECHO (
    id_trecho           NUMBER(6)       NOT NULL,
    id_bairro           NUMBER(5)       NOT NULL,
    cd_trecho           VARCHAR2(5)     NOT NULL,
    nm_logradouro       VARCHAR2(100)   NOT NULL,
    ds_referencia       VARCHAR2(120),
    vl_cota_m           NUMBER(5,1)     NOT NULL,
    vl_declividade_pct  NUMBER(4,1)     NOT NULL,
    vl_dist_corrego_m   NUMBER(6)       NOT NULL,
    vl_bocas_lobo_100m  NUMBER(3,1)     NOT NULL,
    dt_ultima_limpeza   DATE,
    vl_latitude         NUMBER(9,6)     NOT NULL,
    vl_longitude        NUMBER(9,6)     NOT NULL
);

-- Usuários: cidadãos cadastrados e administradores
CREATE TABLE SR_USUARIO (
    id_usuario          NUMBER(8)       NOT NULL,
    nm_usuario          VARCHAR2(80)    NOT NULL,
    ds_email            VARCHAR2(120)   NOT NULL,
    nr_telefone         VARCHAR2(15),
    ds_senha_hash       VARCHAR2(100)   NOT NULL,
    tp_perfil           VARCHAR2(10)    DEFAULT 'CIDADAO' NOT NULL,
    dt_cadastro         DATE            DEFAULT SYSDATE   NOT NULL,
    st_ativo            CHAR(1)         DEFAULT 'S'       NOT NULL
);

-- Previsão de chuva por bairro, obtida do serviço meteorológico
CREATE TABLE SR_PREVISAO_CHUVA (
    id_previsao         NUMBER(8)       NOT NULL,
    id_bairro           NUMBER(5)       NOT NULL,
    dt_hr_inicio        DATE            NOT NULL,
    dt_hr_fim           DATE            NOT NULL,
    vl_chuva_mm_h       NUMBER(5,1)     NOT NULL,
    vl_prob_chuva_pct   NUMBER(3)       NOT NULL,
    nm_fonte            VARCHAR2(40)    NOT NULL,
    dt_hr_emissao       DATE            NOT NULL
);

-- Histórico oficial de alagamentos por trecho (base do cálculo de risco)
CREATE TABLE SR_HISTORICO_ALAGAMENTO (
    id_historico        NUMBER(8)       NOT NULL,
    id_trecho           NUMBER(6)       NOT NULL,
    dt_hr_ocorrencia    DATE            NOT NULL,
    vl_lamina_cm        NUMBER(4,1)     NOT NULL,
    vl_chuva_1h_mm      NUMBER(5,1),
    st_intransitavel    CHAR(1)         NOT NULL,
    nm_fonte            VARCHAR2(40)    NOT NULL
);

-- Relatos de alagamento enviados pelos moradores em tempo real
CREATE TABLE SR_RELATO (
    id_relato           NUMBER(10)      NOT NULL,
    id_trecho           NUMBER(6)       NOT NULL,
    id_usuario          NUMBER(8)       NOT NULL,
    dt_hr_relato        DATE            NOT NULL,
    tp_nivel_agua       VARCHAR2(15)    NOT NULL,
    ds_url_foto         VARCHAR2(255),
    st_relato           VARCHAR2(10)    DEFAULT 'ATIVO' NOT NULL,
    dt_hr_expiracao     DATE            NOT NULL,
    ds_motivo_remocao   VARCHAR2(200)
);

-- Confirmações ("ainda alagado" / "já secou") feitas por outros usuários
CREATE TABLE SR_CONFIRMACAO_RELATO (
    id_confirmacao      NUMBER(10)      NOT NULL,
    id_relato           NUMBER(10)      NOT NULL,
    id_usuario          NUMBER(8)       NOT NULL,
    st_ainda_alagado    CHAR(1)         NOT NULL,
    dt_hr_confirmacao   DATE            NOT NULL
);

-- Trajetos salvos pelo usuário (ex.: Casa -> Trabalho)
CREATE TABLE SR_TRAJETO_FAVORITO (
    id_trajeto          NUMBER(8)       NOT NULL,
    id_usuario          NUMBER(8)       NOT NULL,
    nm_trajeto          VARCHAR2(50)    NOT NULL,
    ds_origem           VARCHAR2(150)   NOT NULL,
    ds_destino          VARCHAR2(150)   NOT NULL,
    tp_modal            VARCHAR2(10)    NOT NULL,
    hr_saida_habitual   VARCHAR2(5),
    st_alerta_ativo     CHAR(1)         DEFAULT 'S' NOT NULL
);

-- Alertas de chuva enviados para um trajeto favorito
CREATE TABLE SR_ALERTA (
    id_alerta           NUMBER(10)      NOT NULL,
    id_trajeto          NUMBER(8)       NOT NULL,
    id_previsao         NUMBER(8)       NOT NULL,
    dt_hr_envio         DATE            NOT NULL,
    ds_mensagem         VARCHAR2(250)   NOT NULL,
    st_lido             CHAR(1)         DEFAULT 'N' NOT NULL
);

-- Cada busca de rota feita no app (id_usuario nulo = visitante)
CREATE TABLE SR_CONSULTA_ROTA (
    id_consulta              NUMBER(10)     NOT NULL,
    id_usuario               NUMBER(8),
    id_previsao              NUMBER(8),
    ds_origem                VARCHAR2(150)  NOT NULL,
    ds_destino               VARCHAR2(150)  NOT NULL,
    tp_modal                 VARCHAR2(10)   NOT NULL,
    dt_hr_consulta           DATE           NOT NULL,
    nr_min_rota_rapida       NUMBER(4)      NOT NULL,
    nr_min_rota_segura       NUMBER(4)      NOT NULL,
    st_rota_segura_escolhida CHAR(1)        NOT NULL
);

-- Trechos avaliados em cada consulta (entidade associativa N:N)
-- st_evitado = 'S' -> trecho de risco que a rota segura desviou (indicador de impacto)
CREATE TABLE SR_CONSULTA_TRECHO (
    id_consulta              NUMBER(10)     NOT NULL,
    id_trecho                NUMBER(6)      NOT NULL,
    tp_risco                 VARCHAR2(5)    NOT NULL,
    vl_prob_intransitavel    NUMBER(4,3)    NOT NULL,
    vl_lamina_prevista_cm    NUMBER(4,1)    NOT NULL,
    st_evitado               CHAR(1)        DEFAULT 'N' NOT NULL
);

-- ---------------------------------------------------------------------
-- (2) CONSTRAINTS
-- ---------------------------------------------------------------------

-- Chaves primárias -----------------------------------------------------
ALTER TABLE SR_BAIRRO               ADD CONSTRAINT pk_sr_bairro      PRIMARY KEY (id_bairro);
ALTER TABLE SR_TRECHO               ADD CONSTRAINT pk_sr_trecho      PRIMARY KEY (id_trecho);
ALTER TABLE SR_USUARIO              ADD CONSTRAINT pk_sr_usuario     PRIMARY KEY (id_usuario);
ALTER TABLE SR_PREVISAO_CHUVA       ADD CONSTRAINT pk_sr_previsao    PRIMARY KEY (id_previsao);
ALTER TABLE SR_HISTORICO_ALAGAMENTO ADD CONSTRAINT pk_sr_historico   PRIMARY KEY (id_historico);
ALTER TABLE SR_RELATO               ADD CONSTRAINT pk_sr_relato      PRIMARY KEY (id_relato);
ALTER TABLE SR_CONFIRMACAO_RELATO   ADD CONSTRAINT pk_sr_confirmacao PRIMARY KEY (id_confirmacao);
ALTER TABLE SR_TRAJETO_FAVORITO     ADD CONSTRAINT pk_sr_trajeto     PRIMARY KEY (id_trajeto);
ALTER TABLE SR_ALERTA               ADD CONSTRAINT pk_sr_alerta      PRIMARY KEY (id_alerta);
ALTER TABLE SR_CONSULTA_ROTA        ADD CONSTRAINT pk_sr_consulta    PRIMARY KEY (id_consulta);
ALTER TABLE SR_CONSULTA_TRECHO      ADD CONSTRAINT pk_sr_consulta_trecho PRIMARY KEY (id_consulta, id_trecho);

-- Unicidade --------------------------------------------------------------
ALTER TABLE SR_BAIRRO             ADD CONSTRAINT uk_sr_bairro_nome    UNIQUE (nm_bairro, nm_cidade);
ALTER TABLE SR_TRECHO             ADD CONSTRAINT uk_sr_trecho_codigo  UNIQUE (cd_trecho);
ALTER TABLE SR_USUARIO            ADD CONSTRAINT uk_sr_usuario_email  UNIQUE (ds_email);
ALTER TABLE SR_CONFIRMACAO_RELATO ADD CONSTRAINT uk_sr_confirm_unica  UNIQUE (id_relato, id_usuario);

-- Regras de domínio (CHECK) ---------------------------------------------
ALTER TABLE SR_TRECHO ADD CONSTRAINT ck_sr_trecho_declividade CHECK (vl_declividade_pct >= 0);
ALTER TABLE SR_TRECHO ADD CONSTRAINT ck_sr_trecho_bocas       CHECK (vl_bocas_lobo_100m >= 0);
ALTER TABLE SR_TRECHO ADD CONSTRAINT ck_sr_trecho_dist        CHECK (vl_dist_corrego_m >= 0);

ALTER TABLE SR_USUARIO ADD CONSTRAINT ck_sr_usuario_perfil CHECK (tp_perfil IN ('CIDADAO', 'ADMIN'));
ALTER TABLE SR_USUARIO ADD CONSTRAINT ck_sr_usuario_ativo  CHECK (st_ativo IN ('S', 'N'));

ALTER TABLE SR_PREVISAO_CHUVA ADD CONSTRAINT ck_sr_previsao_periodo CHECK (dt_hr_fim > dt_hr_inicio);
ALTER TABLE SR_PREVISAO_CHUVA ADD CONSTRAINT ck_sr_previsao_chuva   CHECK (vl_chuva_mm_h >= 0);
ALTER TABLE SR_PREVISAO_CHUVA ADD CONSTRAINT ck_sr_previsao_prob    CHECK (vl_prob_chuva_pct BETWEEN 0 AND 100);

ALTER TABLE SR_HISTORICO_ALAGAMENTO ADD CONSTRAINT ck_sr_hist_lamina CHECK (vl_lamina_cm >= 0);
ALTER TABLE SR_HISTORICO_ALAGAMENTO ADD CONSTRAINT ck_sr_hist_intrans CHECK (st_intransitavel IN ('S', 'N'));

ALTER TABLE SR_RELATO ADD CONSTRAINT ck_sr_relato_nivel  CHECK (tp_nivel_agua IN ('POCAS', 'JOELHO', 'INTRANSITAVEL'));
ALTER TABLE SR_RELATO ADD CONSTRAINT ck_sr_relato_status CHECK (st_relato IN ('ATIVO', 'EXPIRADO', 'REMOVIDO'));
ALTER TABLE SR_RELATO ADD CONSTRAINT ck_sr_relato_expira CHECK (dt_hr_expiracao > dt_hr_relato);

ALTER TABLE SR_CONFIRMACAO_RELATO ADD CONSTRAINT ck_sr_confirm_alagado CHECK (st_ainda_alagado IN ('S', 'N'));

ALTER TABLE SR_TRAJETO_FAVORITO ADD CONSTRAINT ck_sr_trajeto_modal  CHECK (tp_modal IN ('A_PE', 'ONIBUS', 'CARRO'));
ALTER TABLE SR_TRAJETO_FAVORITO ADD CONSTRAINT ck_sr_trajeto_alerta CHECK (st_alerta_ativo IN ('S', 'N'));

ALTER TABLE SR_ALERTA ADD CONSTRAINT ck_sr_alerta_lido CHECK (st_lido IN ('S', 'N'));

ALTER TABLE SR_CONSULTA_ROTA ADD CONSTRAINT ck_sr_consulta_modal  CHECK (tp_modal IN ('A_PE', 'ONIBUS', 'CARRO'));
ALTER TABLE SR_CONSULTA_ROTA ADD CONSTRAINT ck_sr_consulta_segura CHECK (st_rota_segura_escolhida IN ('S', 'N'));
ALTER TABLE SR_CONSULTA_ROTA ADD CONSTRAINT ck_sr_consulta_tempos CHECK (nr_min_rota_rapida > 0 AND nr_min_rota_segura >= nr_min_rota_rapida);

ALTER TABLE SR_CONSULTA_TRECHO ADD CONSTRAINT ck_sr_ct_risco   CHECK (tp_risco IN ('BAIXO', 'MEDIO', 'ALTO'));
ALTER TABLE SR_CONSULTA_TRECHO ADD CONSTRAINT ck_sr_ct_prob    CHECK (vl_prob_intransitavel BETWEEN 0 AND 1);
ALTER TABLE SR_CONSULTA_TRECHO ADD CONSTRAINT ck_sr_ct_lamina  CHECK (vl_lamina_prevista_cm >= 0);
ALTER TABLE SR_CONSULTA_TRECHO ADD CONSTRAINT ck_sr_ct_evitado CHECK (st_evitado IN ('S', 'N'));

-- Chaves estrangeiras (relacionamentos) ---------------------------------
ALTER TABLE SR_TRECHO ADD CONSTRAINT fk_sr_trecho_bairro
    FOREIGN KEY (id_bairro) REFERENCES SR_BAIRRO (id_bairro);

ALTER TABLE SR_PREVISAO_CHUVA ADD CONSTRAINT fk_sr_previsao_bairro
    FOREIGN KEY (id_bairro) REFERENCES SR_BAIRRO (id_bairro);

ALTER TABLE SR_HISTORICO_ALAGAMENTO ADD CONSTRAINT fk_sr_hist_trecho
    FOREIGN KEY (id_trecho) REFERENCES SR_TRECHO (id_trecho);

ALTER TABLE SR_RELATO ADD CONSTRAINT fk_sr_relato_trecho
    FOREIGN KEY (id_trecho) REFERENCES SR_TRECHO (id_trecho);
ALTER TABLE SR_RELATO ADD CONSTRAINT fk_sr_relato_usuario
    FOREIGN KEY (id_usuario) REFERENCES SR_USUARIO (id_usuario);

ALTER TABLE SR_CONFIRMACAO_RELATO ADD CONSTRAINT fk_sr_confirm_relato
    FOREIGN KEY (id_relato) REFERENCES SR_RELATO (id_relato);
ALTER TABLE SR_CONFIRMACAO_RELATO ADD CONSTRAINT fk_sr_confirm_usuario
    FOREIGN KEY (id_usuario) REFERENCES SR_USUARIO (id_usuario);

ALTER TABLE SR_TRAJETO_FAVORITO ADD CONSTRAINT fk_sr_trajeto_usuario
    FOREIGN KEY (id_usuario) REFERENCES SR_USUARIO (id_usuario);

ALTER TABLE SR_ALERTA ADD CONSTRAINT fk_sr_alerta_trajeto
    FOREIGN KEY (id_trajeto) REFERENCES SR_TRAJETO_FAVORITO (id_trajeto);
ALTER TABLE SR_ALERTA ADD CONSTRAINT fk_sr_alerta_previsao
    FOREIGN KEY (id_previsao) REFERENCES SR_PREVISAO_CHUVA (id_previsao);

ALTER TABLE SR_CONSULTA_ROTA ADD CONSTRAINT fk_sr_consulta_usuario
    FOREIGN KEY (id_usuario) REFERENCES SR_USUARIO (id_usuario);
ALTER TABLE SR_CONSULTA_ROTA ADD CONSTRAINT fk_sr_consulta_previsao
    FOREIGN KEY (id_previsao) REFERENCES SR_PREVISAO_CHUVA (id_previsao);

ALTER TABLE SR_CONSULTA_TRECHO ADD CONSTRAINT fk_sr_ct_consulta
    FOREIGN KEY (id_consulta) REFERENCES SR_CONSULTA_ROTA (id_consulta);
ALTER TABLE SR_CONSULTA_TRECHO ADD CONSTRAINT fk_sr_ct_trecho
    FOREIGN KEY (id_trecho) REFERENCES SR_TRECHO (id_trecho);

-- ---------------------------------------------------------------------
-- (3) SEQUENCES · geram os IDs dos registros criados pela API Java.
-- Começam em 1000 para não colidir com os IDs dos dados de exemplo (DML).
-- ---------------------------------------------------------------------
CREATE SEQUENCE SEQ_SR_BAIRRO      START WITH 1000 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE SEQ_SR_TRECHO      START WITH 1000 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE SEQ_SR_USUARIO     START WITH 1000 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE SEQ_SR_PREVISAO    START WITH 1000 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE SEQ_SR_HISTORICO   START WITH 1000 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE SEQ_SR_RELATO      START WITH 1000 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE SEQ_SR_CONFIRMACAO START WITH 1000 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE SEQ_SR_TRAJETO     START WITH 1000 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE SEQ_SR_ALERTA      START WITH 1000 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE SEQ_SR_CONSULTA    START WITH 1000 INCREMENT BY 1 NOCACHE;
