package br.com.fiap.saferoute.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Página inicial da API: confirma que o deploy está no ar e indica onde estão os endpoints. */
@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> inicio() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("aplicacao", "SafeRoute API");
        info.put("descricao", "Rotas seguras em dias de chuva forte - Vila Mariana, São Paulo");
        info.put("status", "online");
        info.put("documentacao", "/swagger-ui.html");
        info.put("recursos", List.of("/api/bairros", "/api/trechos", "/api/usuarios", "/api/previsoes", "/api/relatos"));
        info.put("exemploRisco", "/api/trechos/3/risco?chuvaMmH=38&chuvaAcum72hMm=20");
        return ResponseEntity.ok(info);
    }
}
