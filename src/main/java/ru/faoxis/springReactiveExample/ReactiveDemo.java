package ru.faoxis.springReactiveExample;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import reactor.core.publisher.Flux;
import reactor.ipc.netty.http.server.HttpServer;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

public class ReactiveDemo {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User { private String name; private int age; }

    private static HandlerFunction userHandler = request -> ok().body(Flux.just(
            new User("Peter", 44),
            new User("Brain", 8)
    ), User.class);

    public static RouterFunction userRouter = RouterFunctions.route(GET("/users"), userHandler);

    public static void main(String[] args) throws InterruptedException {
        HttpHandler httpHandler = RouterFunctions.toHttpHandler(userRouter);
        HttpServer.create(8081)
                .newHandler(new ReactorHttpHandlerAdapter(httpHandler))
                .block();

        while (true) { callAsyncService(); Thread.sleep(10 * 1000);}
    }

    private static void callAsyncService() {
        WebClient webClient = WebClient.create("http://localhost:8081");
        Flux<User> users = webClient.get()
                .uri("/users")
                .retrieve()
                .bodyToFlux(User.class);
        users.subscribe(System.out::println);
    }
}
