package com.example.projectApiWebflux.controller;

import com.example.projectApiWebflux.model.Pagamento;
import com.example.projectApiWebflux.service.PagamentoService;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/pagamentos")
@RequiredArgsConstructor
public class PagamentoController {
    private final PagamentoService service;

    @PostMapping
    public Mono<Pagamento> novoPagamento(@RequestBody NovoPagamentoRequest request) {
        return service.save(request);
    }

    @GetMapping
    public Mono<Pagamento> getPagamento(@RequestParam("id") String id) {
        return service.get(id);
    }

    @Data
    public static class NovoPagamentoRequest {
        @JsonProperty("usuario_id")
        private String UsuarioId;
    }

}
