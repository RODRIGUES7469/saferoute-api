package br.com.fiap.saferoute.dao;

import br.com.fiap.saferoute.exception.BancoDadosException;
import br.com.fiap.saferoute.model.Bairro;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Acesso à tabela SR_BAIRRO via JDBC. */
@Repository
public class BairroDAO extends DAOBase {

    private static final String COLUNAS = "id_bairro, nm_bairro, nm_cidade, sg_uf, nm_regiao";

    public BairroDAO(DataSource dataSource) {
        super(dataSource);
    }

    public List<Bairro> listar() {
        String sql = "SELECT " + COLUNAS + " FROM SR_BAIRRO ORDER BY nm_bairro";
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Bairro> lista = new ArrayList<>();
            while (rs.next()) lista.add(mapear(rs));
            return lista;
        } catch (SQLException e) {
            throw new BancoDadosException("Erro ao listar bairros", e);
        }
    }

    public Optional<Bairro> buscarPorId(Long id) {
        String sql = "SELECT " + COLUNAS + " FROM SR_BAIRRO WHERE id_bairro = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new BancoDadosException("Erro ao buscar bairro", e);
        }
    }

    public boolean existeNome(String nome, String cidade, Long ignorarId) {
        String sql = "SELECT COUNT(*) FROM SR_BAIRRO WHERE UPPER(nm_bairro) = UPPER(?) AND UPPER(nm_cidade) = UPPER(?) AND id_bairro <> ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setString(2, cidade);
            ps.setLong(3, ignorarId == null ? -1 : ignorarId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new BancoDadosException("Erro ao verificar bairro", e);
        }
    }

    public Bairro inserir(Bairro b) {
        String sql = "INSERT INTO SR_BAIRRO (" + COLUNAS + ") VALUES (?, ?, ?, ?, ?)";
        try (Connection con = conectar()) {
            b.setIdBairro(proximoId(con, "SEQ_SR_BAIRRO"));
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                preencher(ps, b);
                ps.executeUpdate();
            }
            return b;
        } catch (SQLException e) {
            throw new BancoDadosException("Erro ao inserir bairro", e);
        }
    }

    public boolean atualizar(Bairro b) {
        String sql = "UPDATE SR_BAIRRO SET nm_bairro = ?, nm_cidade = ?, sg_uf = ?, nm_regiao = ? WHERE id_bairro = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, b.getNome());
            ps.setString(2, b.getCidade());
            ps.setString(3, b.getUf());
            ps.setString(4, b.getRegiao());
            ps.setLong(5, b.getIdBairro());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new BancoDadosException("Erro ao atualizar bairro", e);
        }
    }

    public boolean excluir(Long id) {
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement("DELETE FROM SR_BAIRRO WHERE id_bairro = ?")) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new BancoDadosException("Erro ao excluir bairro", e);
        }
    }

    private void preencher(PreparedStatement ps, Bairro b) throws SQLException {
        ps.setLong(1, b.getIdBairro());
        ps.setString(2, b.getNome());
        ps.setString(3, b.getCidade());
        ps.setString(4, b.getUf());
        ps.setString(5, b.getRegiao());
    }

    private Bairro mapear(ResultSet rs) throws SQLException {
        Bairro b = new Bairro();
        b.setIdBairro(rs.getLong("id_bairro"));
        b.setNome(rs.getString("nm_bairro"));
        b.setCidade(rs.getString("nm_cidade"));
        b.setUf(rs.getString("sg_uf"));
        b.setRegiao(rs.getString("nm_regiao"));
        return b;
    }
}
