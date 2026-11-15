package br.com.fiap.saferoute.model;

/**
 * Bairro atendido pelo SafeRoute.
 * Tabela: SR_BAIRRO
 */
public class Bairro {

    /** id_bairro (PK) */
    private Long idBairro;
    /** nm_bairro */
    private String nome;
    /** nm_cidade */
    private String cidade;
    /** sg_uf */
    private String uf;
    /** nm_regiao */
    private String regiao;

    public Bairro() {
    }

    public Long getIdBairro() {
        return idBairro;
    }

    public void setIdBairro(Long idBairro) {
        this.idBairro = idBairro;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getRegiao() {
        return regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

}
