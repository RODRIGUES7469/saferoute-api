package br.com.fiap.saferoute.dao;

import br.com.fiap.saferoute.exception.BancoDadosException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Consultas à tabela SR_HISTORICO_ALAGAMENTO usadas no cálculo de risco. */
@Repository
public class HistoricoAlagamentoDAO extends DAOBase {

    public HistoricoAlagamentoDAO(DataSource dataSource) {
        super(dataSource);
    }

    /** Quantas vezes o trecho ficou intransitável nos últimos 365 dias. */
    public int contarIntransitaveisUltimoAno(Long idTrecho) {
        String sql = "SELECT COUNT(*) FROM SR_HISTORICO_ALAGAMENTO "
                + "WHERE id_trecho = ? AND st_intransitavel = 'S' AND dt_hr_ocorrencia >= SYSDATE - 365";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, idTrecho);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new BancoDadosException("Erro ao consultar histórico de alagamentos", e);
        }
    }
}
