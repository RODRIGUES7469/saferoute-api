package br.com.fiap.saferoute.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

/**
 * Usuário do app: cidadão ou administrador.
 * Tabela: SR_USUARIO
 */
public class Usuario {

    /** id_usuario (PK) */
    private Long idUsuario;
    /** nm_usuario */
    private String nome;
    /** ds_email (único) */
    private String email;
    /** nr_telefone */
    private String telefone;
    /** senha em texto: só é recebida no POST/PUT, nunca devolvida no JSON */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;
    /** ds_senha_hash: guardado no banco, nunca exposto */
    @JsonIgnore
    private String senhaHash;
    /** tp_perfil */
    private PerfilUsuario perfil;
    /** dt_cadastro */
    private LocalDate dataCadastro;
    /** st_ativo (S/N) */
    private Boolean ativo;

    public Usuario() {
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public PerfilUsuario getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilUsuario perfil) {
        this.perfil = perfil;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

}
