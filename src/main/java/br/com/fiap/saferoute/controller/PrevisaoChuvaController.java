package br.com.fiap.saferoute.controller;

import br.com.fiap.saferoute.bo.PrevisaoChuvaBO;
import br.com.fiap.saferoute.model.PrevisaoChuva;
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

/** Endpoints de previsões de chuva (CRUD). */
@RestController
@RequestMapping("/api/previsoes")
public class PrevisaoChuvaController {

    private final PrevisaoChuvaBO bo;

    public PrevisaoChuvaController(PrevisaoChuvaBO bo) {
        this.bo = bo;
    }

    @GetMapping
    public ResponseEntity<List<PrevisaoChuva>> listar(@RequestParam(required = false) Long idBairro) {
        return ResponseEntity.ok(bo.listar(idBairro));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrevisaoChuva> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(bo.buscar(id));
    }

    @PostMapping
    public ResponseEntity<PrevisaoChuva> criar(@RequestBody PrevisaoChuva previsao) {
        PrevisaoChuva criado = bo.criar(previsao);
        return ResponseEntity.created(URI.create("/api/previsoes/" + criado.getIdPrevisao())).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrevisaoChuva> atualizar(@PathVariable Long id, @RequestBody PrevisaoChuva previsao) {
        return ResponseEntity.ok(bo.atualizar(id, previsao));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        bo.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
