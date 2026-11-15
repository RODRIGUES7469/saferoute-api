package br.com.fiap.saferoute.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Camada de tratamento de exceções: converte qualquer exceção lançada pelos
 * controllers, BOs ou DAOs em uma resposta HTTP com status correto e corpo {@link ErroResposta}.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResposta> naoEncontrado(RecursoNaoEncontradoException ex, HttpServletRequest req) {
        return resposta(HttpStatus.NOT_FOUND, ex.getMessage(), req, List.of());
    }

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<ErroResposta> validacao(ValidacaoException ex, HttpServletRequest req) {
        return resposta(HttpStatus.BAD_REQUEST, "Os dados enviados são inválidos.", req, ex.getErros());
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResposta> regraNegocio(RegraNegocioException ex, HttpServletRequest req) {
        return resposta(HttpStatus.CONFLICT, ex.getMessage(), req, List.of());
    }

    @ExceptionHandler(BancoDadosException.class)
    public ResponseEntity<ErroResposta> bancoDados(BancoDadosException ex, HttpServletRequest req) {
        if (ex.isViolacaoIntegridade()) {
            return resposta(HttpStatus.CONFLICT,
                    "Operação recusada pelo banco: o registro está relacionado a outros dados ou duplica um valor único.",
                    req, List.of(ex.getCause().getMessage().trim()));
        }
        log.error("Erro de banco de dados em {}", req.getRequestURI(), ex);
        return resposta(HttpStatus.SERVICE_UNAVAILABLE,
                "Não foi possível acessar o banco de dados. Tente novamente em instantes.", req, List.of());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResposta> jsonInvalido(HttpMessageNotReadableException ex, HttpServletRequest req) {
        return resposta(HttpStatus.BAD_REQUEST,
                "JSON inválido ou com valor fora do permitido (confira datas, números e campos de lista fixa).", req, List.of());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResposta> tipoInvalido(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        return resposta(HttpStatus.BAD_REQUEST,
                "O parâmetro '" + ex.getName() + "' recebeu um valor inválido: " + ex.getValue(), req, List.of());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErroResposta> parametroFaltando(MissingServletRequestParameterException ex, HttpServletRequest req) {
        return resposta(HttpStatus.BAD_REQUEST,
                "O parâmetro obrigatório '" + ex.getParameterName() + "' não foi informado.", req, List.of());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErroResposta> rotaInexistente(NoResourceFoundException ex, HttpServletRequest req) {
        return resposta(HttpStatus.NOT_FOUND, "Endpoint não encontrado.", req, List.of());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErroResposta> metodoNaoSuportado(HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        return resposta(HttpStatus.METHOD_NOT_ALLOWED,
                "Método " + ex.getMethod() + " não é suportado neste endpoint.", req, List.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResposta> erroInesperado(Exception ex, HttpServletRequest req) {
        log.error("Erro inesperado em {}", req.getRequestURI(), ex);
        return resposta(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno inesperado.", req, List.of());
    }

    private ResponseEntity<ErroResposta> resposta(HttpStatus status, String mensagem, HttpServletRequest req, List<String> detalhes) {
        ErroResposta corpo = new ErroResposta(LocalDateTime.now(), status.value(), status.getReasonPhrase(),
                mensagem, req.getRequestURI(), detalhes);
        return ResponseEntity.status(status).body(corpo);
    }
}
