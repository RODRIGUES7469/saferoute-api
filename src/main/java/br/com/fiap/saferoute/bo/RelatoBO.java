package br.com.fiap.saferoute.bo;

import br.com.fiap.saferoute.dao.RelatoDAO;
import br.com.fiap.saferoute.dao.TrechoDAO;
import br.com.fiap.saferoute.dao.UsuarioDAO;
import br.com.fiap.saferoute.exception.RecursoNaoEncontradoException;
import br.com.fiap.saferoute.exception.RegraNegocioException;
import br.com.fiap.saferoute.model.Relato;
import br.com.fiap.saferoute.model.StatusRelato;
import br.com.fiap.saferoute.model.Usuario;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Regras de negócio dos relatos de alagamento:
 * só usuário ativo pode relatar; o relato nasce ATIVO e expira em 2 horas;
 * para REMOVER (moderação) é obrigatório informar o motivo.
 */
@Service
public class RelatoBO {

    static final int HORAS_VALIDADE = 2;

    private final RelatoDAO dao;
    private final TrechoDAO trechoDAO;
    private final UsuarioDAO usuarioDAO;

    public RelatoBO(RelatoDAO dao, TrechoDAO trechoDAO, UsuarioDAO usuarioDAO) {
        this.dao = dao;
        this.trechoDAO = trechoDAO;
        this.usuarioDAO = usuarioDAO;
    }

    public List<Relato> listar(StatusRelato status) {
        dao.expirarVencidos();
        return dao.listar(status);
    }

    public Relato buscar(Long id) {
        dao.expirarVencidos();
        return dao.buscarPorId(id).orElseThrow(() -> new RecursoNaoEncontradoException("Relato", id));
    }

    public Relato criar(Relato r) {
        new Validador()
                .obrigatorio(r.getIdTrecho(), "idTrecho")
                .obrigatorio(r.getIdUsuario(), "idUsuario")
                .obrigatorio(r.getNivelAgua(), "nivelAgua")
                .tamanhoMaximo(r.getUrlFoto(), 255, "urlFoto")
                .regra(r.getUrlFoto() == null || r.getUrlFoto().startsWith("http"), "O campo 'urlFoto' deve ser um link http(s).")
                .validar();
        trechoDAO.buscarPorId(r.getIdTrecho()).orElseThrow(() -> new RecursoNaoEncontradoException("Trecho", r.getIdTrecho()));
        Usuario autor = usuarioDAO.buscarPorId(r.getIdUsuario())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", r.getIdUsuario()));
        if (!Boolean.TRUE.equals(autor.getAtivo())) {
            throw new RegraNegocioException("Usuário inativo não pode enviar relatos.");
        }
        LocalDateTime agora = LocalDateTime.now().withSecond(0).withNano(0);
        r.setIdRelato(null);
        r.setDataHora(agora);
        r.setExpiracao(agora.plusHours(HORAS_VALIDADE));
        r.setStatus(StatusRelato.ATIVO);
        r.setMotivoRemocao(null);
        return dao.inserir(r);
    }

    /** Atualiza nível da água, foto, status e motivo. Trecho, autor e datas não mudam. */
    public Relato atualizar(Long id, Relato dados) {
        Relato atual = buscar(id);
        if (dados.getNivelAgua() != null) atual.setNivelAgua(dados.getNivelAgua());
        if (dados.getUrlFoto() != null) atual.setUrlFoto(dados.getUrlFoto().isBlank() ? null : dados.getUrlFoto());
        if (dados.getStatus() != null) atual.setStatus(dados.getStatus());
        if (dados.getMotivoRemocao() != null) atual.setMotivoRemocao(dados.getMotivoRemocao());
        new Validador()
                .tamanhoMaximo(atual.getUrlFoto(), 255, "urlFoto")
                .tamanhoMaximo(atual.getMotivoRemocao(), 200, "motivoRemocao")
                .regra(atual.getStatus() != StatusRelato.REMOVIDO
                                || (atual.getMotivoRemocao() != null && !atual.getMotivoRemocao().isBlank()),
                        "Para remover um relato informe o 'motivoRemocao'.")
                .regra(!(atual.getStatus() == StatusRelato.ATIVO && atual.getExpiracao().isBefore(LocalDateTime.now())),
                        "Um relato expirado não pode voltar a ficar ATIVO.")
                .validar();
        dao.atualizar(atual);
        return atual;
    }

    /** Relatos com confirmações não podem ser excluídos (FK); nesse caso use PUT com status REMOVIDO. */
    public void excluir(Long id) {
        buscar(id);
        dao.excluir(id);
    }
}
