package br.com.fiap.saferoute.model;

import java.time.LocalDateTime;

/**
 * Previsão de chuva para um bairro em um período.
 * Tabela: SR_PREVISAO_CHUVA
 */
public class PrevisaoChuva {

    /** id_previsao (PK) */
    private Long idPrevisao;
    /** id_bairro (FK -> SR_BAIRRO) */
    private Long idBairro;
    /** dt_hr_inicio */
    private LocalDateTime inicio;
    /** dt_hr_fim */
    private LocalDateTime fim;
    /** vl_chuva_mm_h: chuva prevista na hora mais forte */
    private Double chuvaMmH;
    /** vl_prob_chuva_pct */
    private Integer probabilidadePct;
    /** nm_fonte (CGE, INMET...) */
    private String fonte;
    /** dt_hr_emissao */
    private LocalDateTime emissao;

    public PrevisaoChuva() {
    }

    public Long getIdPrevisao() {
        return idPrevisao;
    }

    public void setIdPrevisao(Long idPrevisao) {
        this.idPrevisao = idPrevisao;
    }

    public Long getIdBairro() {
        return idBairro;
    }

    public void setIdBairro(Long idBairro) {
        this.idBairro = idBairro;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public LocalDateTime getFim() {
        return fim;
    }

    public void setFim(LocalDateTime fim) {
        this.fim = fim;
    }

    public Double getChuvaMmH() {
        return chuvaMmH;
    }

    public void setChuvaMmH(Double chuvaMmH) {
        this.chuvaMmH = chuvaMmH;
    }

    public Integer getProbabilidadePct() {
        return probabilidadePct;
    }

    public void setProbabilidadePct(Integer probabilidadePct) {
        this.probabilidadePct = probabilidadePct;
    }

    public String getFonte() {
        return fonte;
    }

    public void setFonte(String fonte) {
        this.fonte = fonte;
    }

    public LocalDateTime getEmissao() {
        return emissao;
    }

    public void setEmissao(LocalDateTime emissao) {
        this.emissao = emissao;
    }

}
