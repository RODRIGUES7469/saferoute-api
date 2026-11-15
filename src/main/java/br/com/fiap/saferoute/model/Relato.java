package br.com.fiap.saferoute.model;

import java.time.LocalDateTime;

/**
 * Relato de alagamento enviado por um morador.
 * Tabela: SR_RELATO
 */
public class Relato {

    /** id_relato (PK) */
    private Long idRelato;
    /** id_trecho (FK -> SR_TRECHO) */
    private Long idTrecho;
    /** id_usuario (FK -> SR_USUARIO) */
    private Long idUsuario;
    /** dt_hr_relato */
    private LocalDateTime dataHora;
    /** tp_nivel_agua */
    private NivelAgua nivelAgua;
    /** ds_url_foto */
    private String urlFoto;
    /** st_relato */
    private StatusRelato status;
    /** dt_hr_expiracao (relato vale por 2 horas) */
    private LocalDateTime expiracao;
    /** ds_motivo_remocao */
    private String motivoRemocao;

    public Relato() {
    }

    public Long getIdRelato() {
        return idRelato;
    }

    public void setIdRelato(Long idRelato) {
        this.idRelato = idRelato;
    }

    public Long getIdTrecho() {
        return idTrecho;
    }

    public void setIdTrecho(Long idTrecho) {
        this.idTrecho = idTrecho;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public NivelAgua getNivelAgua() {
        return nivelAgua;
    }

    public void setNivelAgua(NivelAgua nivelAgua) {
        this.nivelAgua = nivelAgua;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public StatusRelato getStatus() {
        return status;
    }

    public void setStatus(StatusRelato status) {
        this.status = status;
    }

    public LocalDateTime getExpiracao() {
        return expiracao;
    }

    public void setExpiracao(LocalDateTime expiracao) {
        this.expiracao = expiracao;
    }

    public String getMotivoRemocao() {
        return motivoRemocao;
    }

    public void setMotivoRemocao(String motivoRemocao) {
        this.motivoRemocao = motivoRemocao;
    }

}
