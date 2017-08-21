package com.reactive.spring;

import com.reactive.spring.accounts.handler.AccountHandlerImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.ipc.netty.http.server.HttpServer;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RouterFunctions.toHttpHandler;

@SpringBootApplication
public class Application {
	@Bean
	public RouterFunction<?> router(AccountHandlerImpl accountHandler	) {
		return route(GET("/api/account/all").and(accept(MediaType.APPLICATION_JSON)),accountHandler::handleGetAccounts)
				.and(route(GET("/api/account/find/{id}").and(accept(MediaType.APPLICATION_JSON)),accountHandler::handleGetAccountById)
						.and(route(POST("/api/account/new").and(accept(MediaType.APPLICATION_JSON)),accountHandler::handleSaveAccount))
						.and(route(PUT("/api/account/update").and(accept(MediaType.APPLICATION_JSON)),accountHandler::handleUpdateAccount))
						.and(route(DELETE("/api/account/delete/{id}").and(accept(MediaType.APPLICATION_JSON)),accountHandler::handleDeleteAccount)));
	}

	@Bean
	public HttpServer server(RouterFunction<?> router) {
		HttpHandler handler = toHttpHandler(router);
		HttpServer httpServer = HttpServer.create(9000);
		httpServer.start(new ReactorHttpHandlerAdapter(handler));
		return httpServer;
	}
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
