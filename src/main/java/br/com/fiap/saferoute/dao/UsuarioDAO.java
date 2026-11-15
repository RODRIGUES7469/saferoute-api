package br.com.fiap.saferoute.dao;

import br.com.fiap.saferoute.exception.BancoDadosException;
import br.com.fiap.saferoute.model.PerfilUsuario;
import br.com.fiap.saferoute.model.Usuario;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Acesso à tabela SR_USUARIO via JDBC. */
@Repository
public class UsuarioDAO extends DAOBase {

    private static final String COLUNAS = "id_usuario, nm_usuario, ds_email, nr_telefone, ds_senha_hash, tp_perfil, dt_cadastro, st_ativo";

    public UsuarioDAO(DataSource dataSource) {
        super(dataSource);
    }

    public List<Usuario> listar() {
        String sql = "SELECT " + COLUNAS + " FROM SR_USUARIO ORDER BY nm_usuario";
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Usuario> lista = new ArrayList<>();
            while (rs.next()) lista.add(mapear(rs));
            return lista;
        } catch (SQLException e) {
            throw new BancoDadosException("Erro ao listar usuários", e);
        }
    }

    public Optional<Usuario> buscarPorId(Long id) {
        String sql = "SELECT " + COLUNAS + " FROM SR_USUARIO WHERE id_usuario = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new BancoDadosException("Erro ao buscar usuário", e);
        }
    }

    public boolean existeEmail(String email, Long ignorarId) {
        String sql = "SELECT COUNT(*) FROM SR_USUARIO WHERE LOWER(ds_email) = LOWER(?) AND id_usuario <> ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setLong(2, ignorarId == null ? -1 : ignorarId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new BancoDadosException("Erro ao verificar e-mail", e);
        }
    }

    public Usuario inserir(Usuario u) {
        String sql = "INSERT INTO SR_USUARIO (" + COLUNAS + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = conectar()) {
            u.setIdUsuario(proximoId(con, "SEQ_SR_USUARIO"));
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setLong(1, u.getIdUsuario());
                ps.setString(2, u.getNome());
                ps.setString(3, u.getEmail());
                ps.setString(4, u.getTelefone());
                ps.setString(5, u.getSenhaHash());
                ps.setString(6, u.getPerfil().name());
                setData(ps, 7, u.getDataCadastro());
                ps.setString(8, simNao(u.getAtivo()));
                ps.executeUpdate();
            }
            return u;
        } catch (SQLException e) {
            throw new BancoDadosException("Erro ao inserir usuário", e);
        }
    }

    public boolean atualizar(Usuario u) {
        String sql = "UPDATE SR_USUARIO SET nm_usuario = ?, ds_email = ?, nr_telefone = ?, ds_senha_hash = ?, "
                + "tp_perfil = ?, st_ativo = ? WHERE id_usuario = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getNome());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getTelefone());
            ps.setString(4, u.getSenhaHash());
            ps.setString(5, u.getPerfil().name());
            ps.setString(6, simNao(u.getAtivo()));
            ps.setLong(7, u.getIdUsuario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new BancoDadosException("Erro ao atualizar usuário", e);
        }
    }

    public boolean excluir(Long id) {
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement("DELETE FROM SR_USUARIO WHERE id_usuario = ?")) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new BancoDadosException("Erro ao excluir usuário", e);
        }
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setIdUsuario(rs.getLong("id_usuario"));
        u.setNome(rs.getString("nm_usuario"));
        u.setEmail(rs.getString("ds_email"));
        u.setTelefone(rs.getString("nr_telefone"));
        u.setSenhaHash(rs.getString("ds_senha_hash"));
        u.setPerfil(PerfilUsuario.valueOf(rs.getString("tp_perfil")));
        u.setDataCadastro(lerData(rs, "dt_cadastro"));
        u.setAtivo("S".equals(rs.getString("st_ativo")));
        return u;
    }
}
