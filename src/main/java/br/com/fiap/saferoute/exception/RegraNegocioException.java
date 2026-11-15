package br.com.fiap.saferoute.exception;

/** Lançada quando a operação fere uma regra de negócio, ex.: e-mail já cadastrado (HTTP 409). */
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
