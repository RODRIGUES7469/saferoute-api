package br.com.fiap.saferoute.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Base dos DAOs: obtém conexões do pool (DataSource configurado pelo Spring a partir do
 * application.properties) e concentra conversões entre tipos Java e tipos do Oracle.
 */
public abstract class DAOBase {

    private final DataSource dataSource;

    protected DAOBase(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected Connection conectar() throws SQLException {
        return dataSource.getConnection();
    }

    /** Busca o próximo valor de uma sequence do Oracle (ex.: SEQ_SR_BAIRRO). */
    protected long proximoId(Connection con, String sequence) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT " + sequence + ".NEXTVAL FROM DUAL");
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getLong(1);
        }
    }

    // ---------- leitura de colunas que podem ser nulas ----------

    protected static Double lerDouble(ResultSet rs, String coluna) throws SQLException {
        double v = rs.getDouble(coluna);
        return rs.wasNull() ? null : v;
    }

    protected static Integer lerInteger(ResultSet rs, String coluna) throws SQLException {
        int v = rs.getInt(coluna);
        return rs.wasNull() ? null : v;
    }

    protected static Long lerLong(ResultSet rs, String coluna) throws SQLException {
        long v = rs.getLong(coluna);
        return rs.wasNull() ? null : v;
    }

    protected static LocalDate lerData(ResultSet rs, String coluna) throws SQLException {
        Timestamp ts = rs.getTimestamp(coluna);
        return ts == null ? null : ts.toLocalDateTime().toLocalDate();
    }

    protected static LocalDateTime lerDataHora(ResultSet rs, String coluna) throws SQLException {
        Timestamp ts = rs.getTimestamp(coluna);
        return ts == null ? null : ts.toLocalDateTime();
    }

    // ---------- escrita de parâmetros que podem ser nulos ----------

    protected static void setDouble(PreparedStatement ps, int i, Double v) throws SQLException {
        if (v == null) ps.setNull(i, Types.NUMERIC); else ps.setDouble(i, v);
    }

    protected static void setInteger(PreparedStatement ps, int i, Integer v) throws SQLException {
        if (v == null) ps.setNull(i, Types.NUMERIC); else ps.setInt(i, v);
    }

    protected static void setLong(PreparedStatement ps, int i, Long v) throws SQLException {
        if (v == null) ps.setNull(i, Types.NUMERIC); else ps.setLong(i, v);
    }

    protected static void setData(PreparedStatement ps, int i, LocalDate v) throws SQLException {
        if (v == null) ps.setNull(i, Types.DATE); else ps.setDate(i, Date.valueOf(v));
    }

    protected static void setDataHora(PreparedStatement ps, int i, LocalDateTime v) throws SQLException {
        if (v == null) ps.setNull(i, Types.TIMESTAMP); else ps.setTimestamp(i, Timestamp.valueOf(v));
    }

    protected static String simNao(Boolean b) {
        return Boolean.TRUE.equals(b) ? "S" : "N";
    }
}
