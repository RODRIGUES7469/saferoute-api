package br.com.fiap.saferoute.dao;

import br.com.fiap.saferoute.exception.BancoDadosException;
import br.com.fiap.saferoute.model.Trecho;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Acesso à tabela SR_TRECHO via JDBC. */
@Repository
public class TrechoDAO extends DAOBase {

    private static final String COLUNAS = "id_trecho, id_bairro, cd_trecho, nm_logradouro, ds_referencia, vl_cota_m, "
            + "vl_declividade_pct, vl_dist_corrego_m, vl_bocas_lobo_100m, dt_ultima_limpeza, vl_latitude, vl_longitude";

    public TrechoDAO(DataSource dataSource) {
        super(dataSource);
    }

    /** Lista todos os trechos ou, se idBairro for informado, só os do bairro. */
    public List<Trecho> listar(Long idBairro) {
        String sql = "SELECT " + COLUNAS + " FROM SR_TRECHO"
                + (idBairro != null ? " WHERE id_bairro = ?" : "") + " ORDER BY cd_trecho";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            if (idBairro != null) ps.setLong(1, idBairro);
            try (ResultSet rs = ps.executeQuery()) {
                List<Trecho> lista = new ArrayList<>();
                while (rs.next()) lista.add(mapear(rs));
                return lista;
            }
        } catch (SQLException e) {
            throw new BancoDadosException("Erro ao listar trechos", e);
        }
    }

    public Optional<Trecho> buscarPorId(Long id) {
        String sql = "SELECT " + COLUNAS + " FROM SR_TRECHO WHERE id_trecho = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new BancoDadosException("Erro ao buscar trecho", e);
        }
    }

    public boolean existeCodigo(String codigo, Long ignorarId) {
        String sql = "SELECT COUNT(*) FROM SR_TRECHO WHERE UPPER(cd_trecho) = UPPER(?) AND id_trecho <> ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigo);
            ps.setLong(2, ignorarId == null ? -1 : ignorarId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new BancoDadosException("Erro ao verificar código do trecho", e);
        }
    }

    public Trecho inserir(Trecho t) {
        String sql = "INSERT INTO SR_TRECHO (" + COLUNAS + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = conectar()) {
            t.setIdTrecho(proximoId(con, "SEQ_SR_TRECHO"));
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setLong(1, t.getIdTrecho());
                preencherDados(ps, 2, t);
                ps.executeUpdate();
            }
            return t;
        } catch (SQLException e) {
            throw new BancoDadosException("Erro ao inserir trecho", e);
        }
    }

    public boolean atualizar(Trecho t) {
        String sql = "UPDATE SR_TRECHO SET id_bairro = ?, cd_trecho = ?, nm_logradouro = ?, ds_referencia = ?, vl_cota_m = ?, "
                + "vl_declividade_pct = ?, vl_dist_corrego_m = ?, vl_bocas_lobo_100m = ?, dt_ultima_limpeza = ?, "
                + "vl_latitude = ?, vl_longitude = ? WHERE id_trecho = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            preencherDados(ps, 1, t);
            ps.setLong(12, t.getIdTrecho());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new BancoDadosException("Erro ao atualizar trecho", e);
        }
    }

    public boolean excluir(Long id) {
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement("DELETE FROM SR_TRECHO WHERE id_trecho = ?")) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new BancoDadosException("Erro ao excluir trecho", e);
        }
    }

    /** Preenche os 11 campos de dados a partir da posição informada (usado no INSERT e no UPDATE). */
    private void preencherDados(PreparedStatement ps, int i, Trecho t) throws SQLException {
        ps.setLong(i, t.getIdBairro());
        ps.setString(i + 1, t.getCodigo());
        ps.setString(i + 2, t.getLogradouro());
        ps.setString(i + 3, t.getReferencia());
        setDouble(ps, i + 4, t.getCotaM());
        setDouble(ps, i + 5, t.getDeclividadePct());
        setInteger(ps, i + 6, t.getDistCorregoM());
        setDouble(ps, i + 7, t.getBocasLobo100m());
        setData(ps, i + 8, t.getDataUltimaLimpeza());
        setDouble(ps, i + 9, t.getLatitude());
        setDouble(ps, i + 10, t.getLongitude());
    }

    private Trecho mapear(ResultSet rs) throws SQLException {
        Trecho t = new Trecho();
        t.setIdTrecho(rs.getLong("id_trecho"));
        t.setIdBairro(rs.getLong("id_bairro"));
        t.setCodigo(rs.getString("cd_trecho"));
        t.setLogradouro(rs.getString("nm_logradouro"));
        t.setReferencia(rs.getString("ds_referencia"));
        t.setCotaM(lerDouble(rs, "vl_cota_m"));
        t.setDeclividadePct(lerDouble(rs, "vl_declividade_pct"));
        t.setDistCorregoM(lerInteger(rs, "vl_dist_corrego_m"));
        t.setBocasLobo100m(lerDouble(rs, "vl_bocas_lobo_100m"));
        t.setDataUltimaLimpeza(lerData(rs, "dt_ultima_limpeza"));
        t.setLatitude(lerDouble(rs, "vl_latitude"));
        t.setLongitude(lerDouble(rs, "vl_longitude"));
        return t;
    }
}
