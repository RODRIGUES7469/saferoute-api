package br.com.fiap.saferoute.bo;

import br.com.fiap.saferoute.dao.BairroDAO;
import br.com.fiap.saferoute.dao.TrechoDAO;
import br.com.fiap.saferoute.exception.RecursoNaoEncontradoException;
import br.com.fiap.saferoute.exception.RegraNegocioException;
import br.com.fiap.saferoute.model.Trecho;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/** Regras de negócio de trechos de rua. */
@Service
public class TrechoBO {

    private final TrechoDAO dao;
    private final BairroDAO bairroDAO;

    public TrechoBO(TrechoDAO dao, BairroDAO bairroDAO) {
        this.dao = dao;
        this.bairroDAO = bairroDAO;
    }

    public List<Trecho> listar(Long idBairro) {
        return dao.listar(idBairro);
    }

    public Trecho buscar(Long id) {
        return dao.buscarPorId(id).orElseThrow(() -> new RecursoNaoEncontradoException("Trecho", id));
    }

    public Trecho criar(Trecho t) {
        t.setIdTrecho(null);
        validar(t);
        return dao.inserir(t);
    }

    public Trecho atualizar(Long id, Trecho t) {
        buscar(id);
        t.setIdTrecho(id);
        validar(t);
        dao.atualizar(t);
        return t;
    }

    public void excluir(Long id) {
        buscar(id);
        dao.excluir(id);
    }

    private void validar(Trecho t) {
        if (t.getCodigo() != null) t.setCodigo(t.getCodigo().trim().toUpperCase());
        new Validador()
                .obrigatorio(t.getIdBairro(), "idBairro")
                .obrigatorio(t.getCodigo(), "codigo").tamanhoMaximo(t.getCodigo(), 5, "codigo")
                .obrigatorio(t.getLogradouro(), "logradouro").tamanhoMaximo(t.getLogradouro(), 100, "logradouro")
                .tamanhoMaximo(t.getReferencia(), 120, "referencia")
                .obrigatorio(t.getCotaM(), "cotaM").faixa(t.getCotaM(), 0, 9999.9, "cotaM")
                .obrigatorio(t.getDeclividadePct(), "declividadePct").faixa(t.getDeclividadePct(), 0, 100, "declividadePct")
                .obrigatorio(t.getDistCorregoM(), "distCorregoM").faixa(t.getDistCorregoM(), 0, 999999, "distCorregoM")
                .obrigatorio(t.getBocasLobo100m(), "bocasLobo100m").faixa(t.getBocasLobo100m(), 0, 99.9, "bocasLobo100m")
                .obrigatorio(t.getLatitude(), "latitude").faixa(t.getLatitude(), -90, 90, "latitude")
                .obrigatorio(t.getLongitude(), "longitude").faixa(t.getLongitude(), -180, 180, "longitude")
                .regra(t.getDataUltimaLimpeza() == null || !t.getDataUltimaLimpeza().isAfter(LocalDate.now()),
                        "O campo 'dataUltimaLimpeza' não pode estar no futuro.")
                .validar();
        if (bairroDAO.buscarPorId(t.getIdBairro()).isEmpty()) {
            throw new RecursoNaoEncontradoException("Bairro", t.getIdBairro());
        }
        if (dao.existeCodigo(t.getCodigo(), t.getIdTrecho())) {
            throw new RegraNegocioException("Já existe um trecho com o código " + t.getCodigo() + ".");
        }
    }
}
