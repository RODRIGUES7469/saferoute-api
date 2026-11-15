package br.com.fiap.saferoute.controller;

import br.com.fiap.saferoute.bo.TrechoBO;
import br.com.fiap.saferoute.model.Trecho;
import br.com.fiap.saferoute.bo.RiscoBO;
import br.com.fiap.saferoute.model.RiscoTrecho;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

/** Endpoints de trechos de rua (CRUD) e do cálculo de risco. */
@RestController
@RequestMapping("/api/trechos")
public class TrechoController {

    private final TrechoBO bo;
    private final RiscoBO riscoBO;

    public TrechoController(TrechoBO bo, RiscoBO riscoBO) {
        this.bo = bo;
        this.riscoBO = riscoBO;
    }

    @GetMapping
    public ResponseEntity<List<Trecho>> listar(@RequestParam(required = false) Long idBairro) {
        return ResponseEntity.ok(bo.listar(idBairro));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trecho> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(bo.buscar(id));
    }

    @PostMapping
    public ResponseEntity<Trecho> criar(@RequestBody Trecho trecho) {
        Trecho criado = bo.criar(trecho);
        return ResponseEntity.created(URI.create("/api/trechos/" + criado.getIdTrecho())).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trecho> atualizar(@PathVariable Long id, @RequestBody Trecho trecho) {
        return ResponseEntity.ok(bo.atualizar(id, trecho));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        bo.excluir(id);
        return ResponseEntity.noContent().build();
    }

    /** Capacidade central: risco de alagamento do trecho para a chuva informada. */
    @GetMapping("/{id}/risco")
    public ResponseEntity<RiscoTrecho> risco(@PathVariable Long id,
                                             @RequestParam Double chuvaMmH,
                                             @RequestParam(required = false) Double chuvaAcum72hMm) {
        return ResponseEntity.ok(riscoBO.calcularRisco(id, chuvaMmH, chuvaAcum72hMm));
    }
}
