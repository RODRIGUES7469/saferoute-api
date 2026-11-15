package br.com.fiap.saferoute.bo;

import br.com.fiap.saferoute.dao.UsuarioDAO;
import br.com.fiap.saferoute.exception.RecursoNaoEncontradoException;
import br.com.fiap.saferoute.exception.RegraNegocioException;
import br.com.fiap.saferoute.model.PerfilUsuario;
import br.com.fiap.saferoute.model.Usuario;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

/** Regras de negócio de usuários: e-mail único, senha guardada só como hash e valores padrão. */
@Service
public class UsuarioBO {

    private static final String REGEX_EMAIL = "^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$";
    private final UsuarioDAO dao;
    private final SecureRandom random = new SecureRandom();

    public UsuarioBO(UsuarioDAO dao) {
        this.dao = dao;
    }

    public List<Usuario> listar() {
        return dao.listar();
    }

    public Usuario buscar(Long id) {
        return dao.buscarPorId(id).orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", id));
    }

    public Usuario criar(Usuario u) {
        u.setIdUsuario(null);
        new Validador().obrigatorio(u.getSenha(), "senha").validar();
        validar(u);
        if (u.getPerfil() == null) u.setPerfil(PerfilUsuario.CIDADAO);
        if (u.getAtivo() == null) u.setAtivo(true);
        u.setDataCadastro(LocalDate.now());
        u.setSenhaHash(gerarHash(u.getSenha()));
        return dao.inserir(u);
    }

    /** No PUT a senha é opcional: se não vier, a senha atual é mantida. */
    public Usuario atualizar(Long id, Usuario u) {
        Usuario atual = buscar(id);
        u.setIdUsuario(id);
        validar(u);
        if (u.getPerfil() == null) u.setPerfil(atual.getPerfil());
        if (u.getAtivo() == null) u.setAtivo(atual.getAtivo());
        u.setDataCadastro(atual.getDataCadastro());
        u.setSenhaHash(u.getSenha() == null || u.getSenha().isBlank() ? atual.getSenhaHash() : gerarHash(u.getSenha()));
        dao.atualizar(u);
        return u;
    }

    public void excluir(Long id) {
        buscar(id);
        dao.excluir(id);
    }

    private void validar(Usuario u) {
        if (u.getEmail() != null) u.setEmail(u.getEmail().trim().toLowerCase());
        if (u.getTelefone() != null) u.setTelefone(u.getTelefone().replaceAll("\\D", ""));
        new Validador()
                .obrigatorio(u.getNome(), "nome").tamanhoMaximo(u.getNome(), 80, "nome")
                .obrigatorio(u.getEmail(), "email").tamanhoMaximo(u.getEmail(), 120, "email")
                .regra(u.getEmail() == null || u.getEmail().matches(REGEX_EMAIL), "O campo 'email' não é um e-mail válido.")
                .regra(u.getTelefone() == null || u.getTelefone().isEmpty() || u.getTelefone().matches("\\d{10,11}"),
                        "O campo 'telefone' deve ter DDD + número (10 ou 11 dígitos).")
                .regra(u.getSenha() == null || u.getSenha().isBlank() || u.getSenha().length() >= 6,
                        "A senha deve ter pelo menos 6 caracteres.")
                .validar();
        if (u.getTelefone() != null && u.getTelefone().isEmpty()) u.setTelefone(null);
        if (dao.existeEmail(u.getEmail(), u.getIdUsuario())) {
            throw new RegraNegocioException("O e-mail " + u.getEmail() + " já está cadastrado.");
        }
    }

    /** Hash SHA-256 com salt aleatório, no formato sha256$salt$hash. A senha em texto nunca vai para o banco. */
    private String gerarHash(String senha) {
        try {
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hash = md.digest(senha.getBytes(StandardCharsets.UTF_8));
            Base64.Encoder b64 = Base64.getEncoder();
            return "sha256$" + b64.encodeToString(salt) + "$" + b64.encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 indisponível", e);
        }
    }
}
