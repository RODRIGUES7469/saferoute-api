package br.com.fiap.saferoute.exception;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/** Encapsula erros de JDBC lançados pelos DAOs, para que as camadas de cima não dependam de SQLException. */
public class BancoDadosException extends RuntimeException {

    public BancoDadosException(String mensagem, SQLException causa) {
        super(mensagem, causa);
    }

    /** true quando o Oracle recusou a operação por constraint (ORA-00001 unique, ORA-02291/02292 FK, ORA-02290 check). */
    public boolean isViolacaoIntegridade() {
        return getCause() instanceof SQLIntegrityConstraintViolationException;
    }
}
