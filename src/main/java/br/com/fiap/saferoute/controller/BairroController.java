package br.com.fiap.saferoute.controller;

import br.com.fiap.saferoute.bo.BairroBO;
import br.com.fiap.saferoute.model.Bairro;
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

/** Endpoints de bairros (CRUD) e do mapa de risco do bairro. */
@RestController
@RequestMapping("/api/bairros")
public class BairroController {

    private final BairroBO bo;
    private final RiscoBO riscoBO;

    public BairroController(BairroBO bo, RiscoBO riscoBO) {
        this.bo = bo;
        this.riscoBO = riscoBO;
    }

    @GetMapping
    public ResponseEntity<List<Bairro>> listar() {
        return ResponseEntity.ok(bo.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bairro> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(bo.buscar(id));
    }

    @PostMapping
    public ResponseEntity<Bairro> criar(@RequestBody Bairro bairro) {
        Bairro criado = bo.criar(bairro);
        return ResponseEntity.created(URI.create("/api/bairros/" + criado.getIdBairro())).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bairro> atualizar(@PathVariable Long id, @RequestBody Bairro bairro) {
        return ResponseEntity.ok(bo.atualizar(id, bairro));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        bo.excluir(id);
        return ResponseEntity.noContent().build();
    }

    /** Mapa de risco do bairro para uma chuva prevista: todos os trechos, do maior para o menor risco. */
    @GetMapping("/{id}/mapa-risco")
    public ResponseEntity<List<RiscoTrecho>> mapaRisco(@PathVariable Long id,
                                                       @RequestParam Double chuvaMmH,
                                                       @RequestParam(required = false) Double chuvaAcum72hMm) {
        return ResponseEntity.ok(riscoBO.mapaDeRisco(id, chuvaMmH, chuvaAcum72hMm));
    }
}
