package com.example.teste.service;

import com.example.teste.controller.PagamentoController;
import com.example.teste.model.Pagamento;
import com.example.teste.repository.InMemoryPagamentoDB;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PagamentoService {
    private final InMemoryPagamentoDB repository;

    public Mono<Pagamento> save(PagamentoController.NovoPagamentoRequest request) {
        log.info("Iniciando pagamento - {}", request);
        final var novoPagamento = Pagamento.builder()
                .id(UUID.randomUUID().toString())
                .usuarioId(request.getUsuarioId())
                .status(Pagamento.Status.PENDENTE)
                .dataCriacao(Instant.now())
                .build();

        return Mono.fromCallable(() -> {
                    log.info("Persistindo novo pagamento - {}", request.getUsuarioId());
                    return repository.salvar(request.getUsuarioId(), novoPagamento);
                })
                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(evento -> log.info("Pagamento persistido - {}", evento));

    }

    public Mono<Pagamento> get(String id) {
        return Mono.defer(() -> {
                    log.info("Buscando pagamento - {}", id);
                    return Mono.justOrEmpty(repository.get(id));
                }).subscribeOn(Schedulers.parallel())
                .doOnNext(pagamento -> log.info("Pagamento encontrado - {}", pagamento));
    }
}
