package br.com.fiap.saferoute.bo;

import br.com.fiap.saferoute.dao.BairroDAO;
import br.com.fiap.saferoute.exception.RecursoNaoEncontradoException;
import br.com.fiap.saferoute.exception.RegraNegocioException;
import br.com.fiap.saferoute.model.Bairro;
import org.springframework.stereotype.Service;

import java.util.List;

/** Regras de negócio de bairros. */
@Service
public class BairroBO {

    private final BairroDAO dao;

    public BairroBO(BairroDAO dao) {
        this.dao = dao;
    }

    public List<Bairro> listar() {
        return dao.listar();
    }

    public Bairro buscar(Long id) {
        return dao.buscarPorId(id).orElseThrow(() -> new RecursoNaoEncontradoException("Bairro", id));
    }

    public Bairro criar(Bairro b) {
        b.setIdBairro(null);
        validar(b);
        return dao.inserir(b);
    }

    public Bairro atualizar(Long id, Bairro b) {
        buscar(id);
        b.setIdBairro(id);
        validar(b);
        dao.atualizar(b);
        return b;
    }

    /** Bairros com trechos ou previsões não podem ser excluídos: o Oracle recusa pela FK e a API devolve 409. */
    public void excluir(Long id) {
        buscar(id);
        dao.excluir(id);
    }

    private void validar(Bairro b) {
        if (b.getUf() != null) b.setUf(b.getUf().trim().toUpperCase());
        new Validador()
                .obrigatorio(b.getNome(), "nome").tamanhoMaximo(b.getNome(), 60, "nome")
                .obrigatorio(b.getCidade(), "cidade").tamanhoMaximo(b.getCidade(), 60, "cidade")
                .obrigatorio(b.getUf(), "uf")
                .regra(b.getUf() == null || b.getUf().matches("[A-Z]{2}"), "O campo 'uf' deve ter 2 letras (ex.: SP).")
                .tamanhoMaximo(b.getRegiao(), 20, "regiao")
                .validar();
        if (dao.existeNome(b.getNome(), b.getCidade(), b.getIdBairro())) {
            throw new RegraNegocioException("Já existe o bairro '" + b.getNome() + "' em " + b.getCidade() + ".");
        }
    }
}
