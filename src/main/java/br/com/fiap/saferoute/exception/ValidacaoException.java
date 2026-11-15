package br.com.fiap.saferoute.exception;

import java.util.List;

/** Lançada pelos BOs quando os dados enviados são inválidos (HTTP 400). Traz a lista de erros. */
public class ValidacaoException extends RuntimeException {

    private final List<String> erros;

    public ValidacaoException(List<String> erros) {
        super("Dados inválidos: " + String.join("; ", erros));
        this.erros = List.copyOf(erros);
    }

    public List<String> getErros() {
        return erros;
    }
}
