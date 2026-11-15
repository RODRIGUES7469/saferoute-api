package br.com.fiap.saferoute.bo;

import br.com.fiap.saferoute.exception.ValidacaoException;

import java.util.ArrayList;
import java.util.List;

/** Acumula os erros de validação de um objeto e lança {@link ValidacaoException} com todos de uma vez. */
class Validador {

    private final List<String> erros = new ArrayList<>();

    Validador obrigatorio(Object valor, String campo) {
        if (valor == null || (valor instanceof String s && s.isBlank())) {
            erros.add("O campo '" + campo + "' é obrigatório.");
        }
        return this;
    }

    Validador tamanhoMaximo(String valor, int max, String campo) {
        if (valor != null && valor.length() > max) {
            erros.add("O campo '" + campo + "' aceita no máximo " + max + " caracteres.");
        }
        return this;
    }

    Validador faixa(Number valor, double min, double max, String campo) {
        if (valor != null && (valor.doubleValue() < min || valor.doubleValue() > max)) {
            erros.add("O campo '" + campo + "' deve estar entre " + formatar(min) + " e " + formatar(max) + ".");
        }
        return this;
    }

    Validador regra(boolean condicaoValida, String mensagem) {
        if (!condicaoValida) erros.add(mensagem);
        return this;
    }

    void validar() {
        if (!erros.isEmpty()) throw new ValidacaoException(erros);
    }

    private static String formatar(double v) {
        return v == Math.rint(v) ? String.valueOf((long) v) : String.valueOf(v);
    }
}
