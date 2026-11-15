package br.com.fiap.saferoute.controller;

import br.com.fiap.saferoute.bo.RelatoBO;
import br.com.fiap.saferoute.model.Relato;
import br.com.fiap.saferoute.model.StatusRelato;
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

/** Endpoints de relatos de alagamento dos moradores (CRUD). */
@RestController
@RequestMapping("/api/relatos")
public class RelatoController {

    private final RelatoBO bo;

    public RelatoController(RelatoBO bo) {
        this.bo = bo;
    }

    @GetMapping
    public ResponseEntity<List<Relato>> listar(@RequestParam(required = false) StatusRelato status) {
        return ResponseEntity.ok(bo.listar(status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Relato> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(bo.buscar(id));
    }

    @PostMapping
    public ResponseEntity<Relato> criar(@RequestBody Relato relato) {
        Relato criado = bo.criar(relato);
        return ResponseEntity.created(URI.create("/api/relatos/" + criado.getIdRelato())).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Relato> atualizar(@PathVariable Long id, @RequestBody Relato relato) {
        return ResponseEntity.ok(bo.atualizar(id, relato));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        bo.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
