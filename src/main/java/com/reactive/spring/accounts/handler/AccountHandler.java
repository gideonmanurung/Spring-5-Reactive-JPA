package com.reactive.spring.accounts.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface AccountHandler {
    Mono<ServerResponse> handleGetAccounts(ServerRequest serverRequest);
    Mono<ServerResponse> handleGetAccountById(ServerRequest serverRequest);
    Mono<ServerResponse> handleSaveAccount(ServerRequest serverRequest);
    Mono<ServerResponse> handleUpdateAccount(ServerRequest serverRequest);
    Mono<ServerResponse> handleDeleteAccount(ServerRequest serverRequest);
}
