package br.com.fiap.saferoute.model;

/**
 * Resultado do cálculo de risco de um trecho para uma chuva prevista (não é tabela: é calculado pelo RiscoBO).
 * Tabela: (calculado)
 */
public class RiscoTrecho {

    /** trecho avaliado */
    private Long idTrecho;
    /** código do trecho */
    private String codigo;
    /** logradouro */
    private String logradouro;
    /** chuva considerada na hora mais forte (mm/h) */
    private Double chuvaMmH;
    /** chuva acumulada nas 72 h anteriores (mm) */
    private Double chuvaAcum72hMm;
    /** probabilidade de a via ficar intransitável (0 a 1) */
    private Double probabilidadeIntransitavel;
    /** altura de água prevista (cm) */
    private Double laminaPrevistaCm;
    /** BAIXO, MEDIO ou ALTO */
    private NivelRisco nivelRisco;
    /** existe relato ativo de morador neste trecho */
    private Boolean relatoAtivo;
    /** mensagem para o cidadão */
    private String recomendacao;

    public RiscoTrecho() {
    }

    public Long getIdTrecho() {
        return idTrecho;
    }

    public void setIdTrecho(Long idTrecho) {
        this.idTrecho = idTrecho;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public Double getChuvaMmH() {
        return chuvaMmH;
    }

    public void setChuvaMmH(Double chuvaMmH) {
        this.chuvaMmH = chuvaMmH;
    }

    public Double getChuvaAcum72hMm() {
        return chuvaAcum72hMm;
    }

    public void setChuvaAcum72hMm(Double chuvaAcum72hMm) {
        this.chuvaAcum72hMm = chuvaAcum72hMm;
    }

    public Double getProbabilidadeIntransitavel() {
        return probabilidadeIntransitavel;
    }

    public void setProbabilidadeIntransitavel(Double probabilidadeIntransitavel) {
        this.probabilidadeIntransitavel = probabilidadeIntransitavel;
    }

    public Double getLaminaPrevistaCm() {
        return laminaPrevistaCm;
    }

    public void setLaminaPrevistaCm(Double laminaPrevistaCm) {
        this.laminaPrevistaCm = laminaPrevistaCm;
    }

    public NivelRisco getNivelRisco() {
        return nivelRisco;
    }

    public void setNivelRisco(NivelRisco nivelRisco) {
        this.nivelRisco = nivelRisco;
    }

    public Boolean getRelatoAtivo() {
        return relatoAtivo;
    }

    public void setRelatoAtivo(Boolean relatoAtivo) {
        this.relatoAtivo = relatoAtivo;
    }

    public String getRecomendacao() {
        return recomendacao;
    }

    public void setRecomendacao(String recomendacao) {
        this.recomendacao = recomendacao;
    }

}
