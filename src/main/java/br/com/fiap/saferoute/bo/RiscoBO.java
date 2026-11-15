package br.com.fiap.saferoute.bo;

import br.com.fiap.saferoute.dao.BairroDAO;
import br.com.fiap.saferoute.dao.HistoricoAlagamentoDAO;
import br.com.fiap.saferoute.dao.RelatoDAO;
import br.com.fiap.saferoute.dao.TrechoDAO;
import br.com.fiap.saferoute.exception.RecursoNaoEncontradoException;
import br.com.fiap.saferoute.model.NivelRisco;
import br.com.fiap.saferoute.model.RiscoTrecho;
import br.com.fiap.saferoute.model.Trecho;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

/**
 * Capacidade central do SafeRoute: calcula o risco de alagamento de um trecho para uma chuva prevista.
 *
 * Usa os coeficientes dos modelos treinados na disciplina de Data Science
 * (regressão logística para a probabilidade de a via ficar intransitável e regressão linear para a
 * altura da água), aplicados às características do trecho guardadas no banco.
 * Regra extra: um relato ativo de morador (água no joelho ou intransitável) eleva o trecho para ALTO.
 */
@Service
public class RiscoBO {

    /** Limiares de classificação (definidos na validação do modelo de Data Science). */
    static final double LIMIAR_ALTO = 0.25;
    static final double LIMIAR_MEDIO = 0.08;
    /** Impermeabilização média do bairro (%); o dado ainda não existe por trecho no banco. */
    static final double IMPERMEABILIZACAO_MEDIA = 85.3;
    /** Quando não há registro de limpeza, assume-se um valor conservador (dias). */
    static final int DIAS_SEM_LIMPEZA_PADRAO = 120;

    // Ordem: intercepto, chuva 1h, acumulada 72h, cota, declividade, dist. córrego, impermeabilização,
    //        bocas de lobo, dias desde a limpeza, alagamentos nos últimos 12 meses
    private static final double[] LOGISTICA = {-12.588523, 0.364754, 0.040355, -0.150460, -0.427869,
            -0.002292, 0.079686, -1.201795, 0.025823, 0.015176};
    private static final double[] LINEAR = {-1.266678, 0.590798, 0.072072, -0.244102, -0.668969,
            -0.002756, 0.123793, -1.945793, 0.033754, 0.126890};

    private final TrechoDAO trechoDAO;
    private final BairroDAO bairroDAO;
    private final HistoricoAlagamentoDAO historicoDAO;
    private final RelatoDAO relatoDAO;

    public RiscoBO(TrechoDAO trechoDAO, BairroDAO bairroDAO, HistoricoAlagamentoDAO historicoDAO, RelatoDAO relatoDAO) {
        this.trechoDAO = trechoDAO;
        this.bairroDAO = bairroDAO;
        this.historicoDAO = historicoDAO;
        this.relatoDAO = relatoDAO;
    }

    public RiscoTrecho calcularRisco(Long idTrecho, Double chuvaMmH, Double chuvaAcum72hMm) {
        validarChuva(chuvaMmH, chuvaAcum72hMm);
        Trecho t = trechoDAO.buscarPorId(idTrecho).orElseThrow(() -> new RecursoNaoEncontradoException("Trecho", idTrecho));
        relatoDAO.expirarVencidos();
        return calcular(t, chuvaMmH, chuvaAcum72hMm == null ? 0.0 : chuvaAcum72hMm);
    }

    /** Mapa de risco do bairro: todos os trechos, do maior para o menor risco (nível e depois probabilidade). */
    public List<RiscoTrecho> mapaDeRisco(Long idBairro, Double chuvaMmH, Double chuvaAcum72hMm) {
        validarChuva(chuvaMmH, chuvaAcum72hMm);
        bairroDAO.buscarPorId(idBairro).orElseThrow(() -> new RecursoNaoEncontradoException("Bairro", idBairro));
        relatoDAO.expirarVencidos();
        double acum = chuvaAcum72hMm == null ? 0.0 : chuvaAcum72hMm;
        return trechoDAO.listar(idBairro).stream()
                .map(t -> calcular(t, chuvaMmH, acum))
                .sorted(Comparator.comparing(RiscoTrecho::getNivelRisco)
                        .thenComparing(RiscoTrecho::getProbabilidadeIntransitavel).reversed())
                .toList();
    }

    private RiscoTrecho calcular(Trecho t, double chuva, double acum) {
        int diasSemLimpeza = t.getDataUltimaLimpeza() == null ? DIAS_SEM_LIMPEZA_PADRAO
                : (int) Math.max(0, ChronoUnit.DAYS.between(t.getDataUltimaLimpeza(), LocalDate.now()));
        int alagamentos12m = historicoDAO.contarIntransitaveisUltimoAno(t.getIdTrecho());
        double[] x = {1, chuva, acum, t.getCotaM(), t.getDeclividadePct(), t.getDistCorregoM(),
                IMPERMEABILIZACAO_MEDIA, t.getBocasLobo100m(), diasSemLimpeza, alagamentos12m};

        double probabilidade = 1.0 / (1.0 + Math.exp(-produto(LOGISTICA, x)));
        double lamina = Math.max(0, produto(LINEAR, x));
        boolean relatoAtivo = relatoDAO.existeRelatoGraveAtivo(t.getIdTrecho());

        NivelRisco nivel = relatoAtivo || probabilidade >= LIMIAR_ALTO ? NivelRisco.ALTO
                : probabilidade >= LIMIAR_MEDIO ? NivelRisco.MEDIO : NivelRisco.BAIXO;

        RiscoTrecho r = new RiscoTrecho();
        r.setIdTrecho(t.getIdTrecho());
        r.setCodigo(t.getCodigo());
        r.setLogradouro(t.getLogradouro());
        r.setChuvaMmH(chuva);
        r.setChuvaAcum72hMm(acum);
        r.setProbabilidadeIntransitavel(Math.round(probabilidade * 1000) / 1000.0);
        r.setLaminaPrevistaCm(Math.round(lamina * 10) / 10.0);
        r.setNivelRisco(nivel);
        r.setRelatoAtivo(relatoAtivo);
        r.setRecomendacao(recomendacao(nivel, relatoAtivo, r.getLaminaPrevistaCm()));
        return r;
    }

    private static String recomendacao(NivelRisco nivel, boolean relatoAtivo, double lamina) {
        return switch (nivel) {
            case ALTO -> relatoAtivo
                    ? "Moradores relataram alagamento agora. Evite este trecho e use a rota segura."
                    : String.format("Risco alto de alagamento (~%.0f cm de água). Evite este trecho e use a rota segura.", lamina);
            case MEDIO -> String.format("Atenção: pode acumular ~%.0f cm de água. Prefira outro caminho se puder.", lamina);
            case BAIXO -> "Trecho com baixo risco de alagamento para esta chuva.";
        };
    }

    private static double produto(double[] coeficientes, double[] x) {
        double soma = 0;
        for (int i = 0; i < x.length; i++) soma += coeficientes[i] * x[i];
        return soma;
    }

    private static void validarChuva(Double chuvaMmH, Double chuvaAcum72hMm) {
        new Validador()
                .obrigatorio(chuvaMmH, "chuvaMmH").faixa(chuvaMmH, 0, 300, "chuvaMmH")
                .faixa(chuvaAcum72hMm, 0, 1000, "chuvaAcum72hMm")
                .validar();
    }
}
