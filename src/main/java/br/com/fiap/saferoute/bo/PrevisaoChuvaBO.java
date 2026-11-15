package br.com.fiap.saferoute.bo;

import br.com.fiap.saferoute.dao.BairroDAO;
import br.com.fiap.saferoute.dao.PrevisaoChuvaDAO;
import br.com.fiap.saferoute.exception.RecursoNaoEncontradoException;
import br.com.fiap.saferoute.model.PrevisaoChuva;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/** Regras de negócio das previsões de chuva. */
@Service
public class PrevisaoChuvaBO {

    private final PrevisaoChuvaDAO dao;
    private final BairroDAO bairroDAO;

    public PrevisaoChuvaBO(PrevisaoChuvaDAO dao, BairroDAO bairroDAO) {
        this.dao = dao;
        this.bairroDAO = bairroDAO;
    }

    public List<PrevisaoChuva> listar(Long idBairro) {
        return dao.listar(idBairro);
    }

    public PrevisaoChuva buscar(Long id) {
        return dao.buscarPorId(id).orElseThrow(() -> new RecursoNaoEncontradoException("Previsão de chuva", id));
    }

    public PrevisaoChuva criar(PrevisaoChuva p) {
        p.setIdPrevisao(null);
        if (p.getEmissao() == null) p.setEmissao(LocalDateTime.now().withSecond(0).withNano(0));
        validar(p);
        return dao.inserir(p);
    }

    public PrevisaoChuva atualizar(Long id, PrevisaoChuva p) {
        PrevisaoChuva atual = buscar(id);
        p.setIdPrevisao(id);
        if (p.getEmissao() == null) p.setEmissao(atual.getEmissao());
        validar(p);
        dao.atualizar(p);
        return p;
    }

    /** Previsões já usadas em alertas ou consultas não podem ser excluídas (FK) e a API devolve 409. */
    public void excluir(Long id) {
        buscar(id);
        dao.excluir(id);
    }

    private void validar(PrevisaoChuva p) {
        new Validador()
                .obrigatorio(p.getIdBairro(), "idBairro")
                .obrigatorio(p.getInicio(), "inicio")
                .obrigatorio(p.getFim(), "fim")
                .regra(p.getInicio() == null || p.getFim() == null || p.getFim().isAfter(p.getInicio()),
                        "O campo 'fim' deve ser depois de 'inicio'.")
                .obrigatorio(p.getChuvaMmH(), "chuvaMmH").faixa(p.getChuvaMmH(), 0, 300, "chuvaMmH")
                .obrigatorio(p.getProbabilidadePct(), "probabilidadePct").faixa(p.getProbabilidadePct(), 0, 100, "probabilidadePct")
                .obrigatorio(p.getFonte(), "fonte").tamanhoMaximo(p.getFonte(), 40, "fonte")
                .validar();
        if (bairroDAO.buscarPorId(p.getIdBairro()).isEmpty()) {
            throw new RecursoNaoEncontradoException("Bairro", p.getIdBairro());
        }
    }
}
