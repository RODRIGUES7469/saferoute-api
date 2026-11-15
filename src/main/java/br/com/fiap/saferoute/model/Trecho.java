package br.com.fiap.saferoute.model;

import java.time.LocalDate;

/**
 * Trecho de rua avaliado quanto ao risco de alagamento.
 * Tabela: SR_TRECHO
 */
public class Trecho {

    /** id_trecho (PK) */
    private Long idTrecho;
    /** id_bairro (FK -> SR_BAIRRO) */
    private Long idBairro;
    /** cd_trecho (único, ex.: T01) */
    private String codigo;
    /** nm_logradouro */
    private String logradouro;
    /** ds_referencia */
    private String referencia;
    /** vl_cota_m: altura acima do ponto mais baixo do bairro (m) */
    private Double cotaM;
    /** vl_declividade_pct */
    private Double declividadePct;
    /** vl_dist_corrego_m */
    private Integer distCorregoM;
    /** vl_bocas_lobo_100m */
    private Double bocasLobo100m;
    /** dt_ultima_limpeza */
    private LocalDate dataUltimaLimpeza;
    /** vl_latitude */
    private Double latitude;
    /** vl_longitude */
    private Double longitude;

    public Trecho() {
    }

    public Long getIdTrecho() {
        return idTrecho;
    }

    public void setIdTrecho(Long idTrecho) {
        this.idTrecho = idTrecho;
    }

    public Long getIdBairro() {
        return idBairro;
    }

    public void setIdBairro(Long idBairro) {
        this.idBairro = idBairro;
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

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Double getCotaM() {
        return cotaM;
    }

    public void setCotaM(Double cotaM) {
        this.cotaM = cotaM;
    }

    public Double getDeclividadePct() {
        return declividadePct;
    }

    public void setDeclividadePct(Double declividadePct) {
        this.declividadePct = declividadePct;
    }

    public Integer getDistCorregoM() {
        return distCorregoM;
    }

    public void setDistCorregoM(Integer distCorregoM) {
        this.distCorregoM = distCorregoM;
    }

    public Double getBocasLobo100m() {
        return bocasLobo100m;
    }

    public void setBocasLobo100m(Double bocasLobo100m) {
        this.bocasLobo100m = bocasLobo100m;
    }

    public LocalDate getDataUltimaLimpeza() {
        return dataUltimaLimpeza;
    }

    public void setDataUltimaLimpeza(LocalDate dataUltimaLimpeza) {
        this.dataUltimaLimpeza = dataUltimaLimpeza;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

}
