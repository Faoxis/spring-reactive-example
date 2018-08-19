package ru.faoxis.springReactiveExample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringReactiveExampleApplication {
    public static void main(String[] args) {
        ApplicationContext context =
                SpringApplication.run(SpringReactiveExampleApplication.class);
        testController(context);
    }

    private static void testController(ApplicationContext context) {
        String port = context.getEnvironment().getProperty("server.port");
        WebClient webClient = WebClient.create("http://localhost:" + port);

        Flux<String> characters = webClient
                .get()
                .uri("/rick-and-morty/characters")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class);

        Flux<String> updatedCharacters = webClient
                .post()
                .uri("/rick-and-morty/characters/add-forgotten")
                .body(characters, String.class)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class);

        assert updatedCharacters
                .filter(s -> s.equals("Summer Smith"))
                .count()
                .block() == 1;
    }
}
