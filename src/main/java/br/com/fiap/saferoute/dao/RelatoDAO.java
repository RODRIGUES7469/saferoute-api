package br.com.fiap.saferoute.dao;

import br.com.fiap.saferoute.exception.BancoDadosException;
import br.com.fiap.saferoute.model.NivelAgua;
import br.com.fiap.saferoute.model.Relato;
import br.com.fiap.saferoute.model.StatusRelato;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Acesso à tabela SR_RELATO via JDBC. */
@Repository
public class RelatoDAO extends DAOBase {

    private static final String COLUNAS = "id_relato, id_trecho, id_usuario, dt_hr_relato, tp_nivel_agua, ds_url_foto, "
            + "st_relato, dt_hr_expiracao, ds_motivo_remocao";

    public RelatoDAO(DataSource dataSource) {
        super(dataSource);
    }

    /** Lista os relatos (mais recentes primeiro), opcionalmente filtrando por status. */
    public List<Relato> listar(StatusRelato status) {
        String sql = "SELECT " + COLUNAS + " FROM SR_RELATO"
                + (status != null ? " WHERE st_relato = ?" : "") + " ORDER BY dt_hr_relato DESC";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            if (status != null) ps.setString(1, status.name());
            try (ResultSet rs = ps.executeQuery()) {
                List<Relato> lista = new ArrayList<>();
                while (rs.next()) lista.add(mapear(rs));
                return lista;
            }
        } catch (SQLException e) {
            throw new BancoDadosException("Erro ao listar relatos", e);
        }
    }

    public Optional<Relato> buscarPorId(Long id) {
        String sql = "SELECT " + COLUNAS + " FROM SR_RELATO WHERE id_relato = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new BancoDadosException("Erro ao buscar relato", e);
        }
    }

    /** true se o trecho tem relato ATIVO ainda dentro do prazo de validade e com nível JOELHO ou INTRANSITAVEL. */
    public boolean existeRelatoGraveAtivo(Long idTrecho) {
        String sql = "SELECT COUNT(*) FROM SR_RELATO WHERE id_trecho = ? AND st_relato = 'ATIVO' "
                + "AND dt_hr_expiracao > SYSDATE AND tp_nivel_agua IN ('JOELHO', 'INTRANSITAVEL')";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, idTrecho);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new BancoDadosException("Erro ao verificar relatos ativos", e);
        }
    }

    /** Regra de expiração: relatos ATIVOS com prazo vencido passam para EXPIRADO. Retorna quantos mudaram. */
    public int expirarVencidos() {
        String sql = "UPDATE SR_RELATO SET st_relato = 'EXPIRADO' WHERE st_relato = 'ATIVO' AND dt_hr_expiracao <= SYSDATE";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new BancoDadosException("Erro ao expirar relatos vencidos", e);
        }
    }

    public Relato inserir(Relato r) {
        String sql = "INSERT INTO SR_RELATO (" + COLUNAS + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = conectar()) {
            r.setIdRelato(proximoId(con, "SEQ_SR_RELATO"));
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setLong(1, r.getIdRelato());
                ps.setLong(2, r.getIdTrecho());
                ps.setLong(3, r.getIdUsuario());
                setDataHora(ps, 4, r.getDataHora());
                ps.setString(5, r.getNivelAgua().name());
                ps.setString(6, r.getUrlFoto());
                ps.setString(7, r.getStatus().name());
                setDataHora(ps, 8, r.getExpiracao());
                ps.setString(9, r.getMotivoRemocao());
                ps.executeUpdate();
            }
            return r;
        } catch (SQLException e) {
            throw new BancoDadosException("Erro ao inserir relato", e);
        }
    }

    /** Atualiza o que pode mudar em um relato: nível da água, foto, status e motivo de remoção. */
    public boolean atualizar(Relato r) {
        String sql = "UPDATE SR_RELATO SET tp_nivel_agua = ?, ds_url_foto = ?, st_relato = ?, ds_motivo_remocao = ? "
                + "WHERE id_relato = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, r.getNivelAgua().name());
            ps.setString(2, r.getUrlFoto());
            ps.setString(3, r.getStatus().name());
            ps.setString(4, r.getMotivoRemocao());
            ps.setLong(5, r.getIdRelato());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new BancoDadosException("Erro ao atualizar relato", e);
        }
    }

    public boolean excluir(Long id) {
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement("DELETE FROM SR_RELATO WHERE id_relato = ?")) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new BancoDadosException("Erro ao excluir relato", e);
        }
    }

    private Relato mapear(ResultSet rs) throws SQLException {
        Relato r = new Relato();
        r.setIdRelato(rs.getLong("id_relato"));
        r.setIdTrecho(rs.getLong("id_trecho"));
        r.setIdUsuario(rs.getLong("id_usuario"));
        r.setDataHora(lerDataHora(rs, "dt_hr_relato"));
        r.setNivelAgua(NivelAgua.valueOf(rs.getString("tp_nivel_agua")));
        r.setUrlFoto(rs.getString("ds_url_foto"));
        r.setStatus(StatusRelato.valueOf(rs.getString("st_relato")));
        r.setExpiracao(lerDataHora(rs, "dt_hr_expiracao"));
        r.setMotivoRemocao(rs.getString("ds_motivo_remocao"));
        return r;
    }
}
