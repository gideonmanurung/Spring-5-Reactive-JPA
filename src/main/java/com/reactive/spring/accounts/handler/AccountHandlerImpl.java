package com.reactive.spring.accounts.handler;

import com.reactive.spring.accounts.models.Account;
import com.reactive.spring.accounts.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Component
public class AccountHandlerImpl implements AccountHandler{
    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    @Override
    public Mono<ServerResponse> handleGetAccounts(ServerRequest serverRequest) {
        List<Account> accountList = accountRepository.findAll();
        Flux<Account> accountFlux = Flux.fromIterable(accountList);
        Mono<ServerResponse> badRequest = ServerResponse.badRequest().build();
        return ServerResponse.ok().body(accountFlux, Account.class).switchIfEmpty(badRequest).publishOn(Schedulers.elastic());
    }

    @Override
    public Mono<ServerResponse> handleGetAccountById(ServerRequest serverRequest) {
        int idAccount = Integer.valueOf(serverRequest.pathVariable("id"));
        Account accountFind = accountRepository.getOne(idAccount);
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        Mono<Account> accountMonoFind = Mono.just(accountFind);
        return ServerResponse.ok().body(accountMonoFind,Account.class).switchIfEmpty(notFound).publishOn(Schedulers.elastic());
    }

    @Override
    public Mono<ServerResponse> handleSaveAccount(ServerRequest serverRequest) {
        Mono<Account> accountSave = serverRequest.bodyToMono(Account.class);
        return ServerResponse.ok().build(accountSave.doOnNext(account -> accountRepository.save(account)).thenEmpty(Mono.empty()));
    }

    @Override
    public Mono<ServerResponse> handleUpdateAccount(ServerRequest serverRequest) {
        Mono<Account> accountUpdate = serverRequest.bodyToMono(Account.class);
        return ServerResponse.ok().build(accountUpdate.doOnNext(account -> accountRepository.save(account)).thenEmpty(Mono.empty()));
    }

    @Override
    public Mono<ServerResponse> handleDeleteAccount(ServerRequest serverRequest) {
        int idAccount = Integer.valueOf(serverRequest.pathVariable("id"));
        Account accountDelete = accountRepository.getOne(idAccount);
        Mono<Account> accountMonoDelete = Mono.just(accountDelete);
        return ServerResponse.ok().build(accountMonoDelete.doOnNext(account -> accountRepository.delete(account)).then());
    }
}
