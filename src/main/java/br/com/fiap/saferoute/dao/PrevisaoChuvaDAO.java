package br.com.fiap.saferoute.dao;

import br.com.fiap.saferoute.exception.BancoDadosException;
import br.com.fiap.saferoute.model.PrevisaoChuva;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Acesso à tabela SR_PREVISAO_CHUVA via JDBC. */
@Repository
public class PrevisaoChuvaDAO extends DAOBase {

    private static final String COLUNAS = "id_previsao, id_bairro, dt_hr_inicio, dt_hr_fim, vl_chuva_mm_h, "
            + "vl_prob_chuva_pct, nm_fonte, dt_hr_emissao";

    public PrevisaoChuvaDAO(DataSource dataSource) {
        super(dataSource);
    }

    /** Lista as previsões (mais recentes primeiro), opcionalmente filtrando por bairro. */
    public List<PrevisaoChuva> listar(Long idBairro) {
        String sql = "SELECT " + COLUNAS + " FROM SR_PREVISAO_CHUVA"
                + (idBairro != null ? " WHERE id_bairro = ?" : "") + " ORDER BY dt_hr_inicio DESC";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            if (idBairro != null) ps.setLong(1, idBairro);
            try (ResultSet rs = ps.executeQuery()) {
                List<PrevisaoChuva> lista = new ArrayList<>();
                while (rs.next()) lista.add(mapear(rs));
                return lista;
            }
        } catch (SQLException e) {
            throw new BancoDadosException("Erro ao listar previsões", e);
        }
    }

    public Optional<PrevisaoChuva> buscarPorId(Long id) {
        String sql = "SELECT " + COLUNAS + " FROM SR_PREVISAO_CHUVA WHERE id_previsao = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new BancoDadosException("Erro ao buscar previsão", e);
        }
    }

    public PrevisaoChuva inserir(PrevisaoChuva p) {
        String sql = "INSERT INTO SR_PREVISAO_CHUVA (" + COLUNAS + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = conectar()) {
            p.setIdPrevisao(proximoId(con, "SEQ_SR_PREVISAO"));
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setLong(1, p.getIdPrevisao());
                preencherDados(ps, 2, p);
                ps.executeUpdate();
            }
            return p;
        } catch (SQLException e) {
            throw new BancoDadosException("Erro ao inserir previsão", e);
        }
    }

    public boolean atualizar(PrevisaoChuva p) {
        String sql = "UPDATE SR_PREVISAO_CHUVA SET id_bairro = ?, dt_hr_inicio = ?, dt_hr_fim = ?, vl_chuva_mm_h = ?, "
                + "vl_prob_chuva_pct = ?, nm_fonte = ?, dt_hr_emissao = ? WHERE id_previsao = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            preencherDados(ps, 1, p);
            ps.setLong(8, p.getIdPrevisao());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new BancoDadosException("Erro ao atualizar previsão", e);
        }
    }

    public boolean excluir(Long id) {
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement("DELETE FROM SR_PREVISAO_CHUVA WHERE id_previsao = ?")) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new BancoDadosException("Erro ao excluir previsão", e);
        }
    }

    private void preencherDados(PreparedStatement ps, int i, PrevisaoChuva p) throws SQLException {
        ps.setLong(i, p.getIdBairro());
        setDataHora(ps, i + 1, p.getInicio());
        setDataHora(ps, i + 2, p.getFim());
        setDouble(ps, i + 3, p.getChuvaMmH());
        setInteger(ps, i + 4, p.getProbabilidadePct());
        ps.setString(i + 5, p.getFonte());
        setDataHora(ps, i + 6, p.getEmissao());
    }

    private PrevisaoChuva mapear(ResultSet rs) throws SQLException {
        PrevisaoChuva p = new PrevisaoChuva();
        p.setIdPrevisao(rs.getLong("id_previsao"));
        p.setIdBairro(rs.getLong("id_bairro"));
        p.setInicio(lerDataHora(rs, "dt_hr_inicio"));
        p.setFim(lerDataHora(rs, "dt_hr_fim"));
        p.setChuvaMmH(lerDouble(rs, "vl_chuva_mm_h"));
        p.setProbabilidadePct(lerInteger(rs, "vl_prob_chuva_pct"));
        p.setFonte(rs.getString("nm_fonte"));
        p.setEmissao(lerDataHora(rs, "dt_hr_emissao"));
        return p;
    }
}
