package ru.faoxis.springReactiveExample;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ExampleController {

    @GetMapping("/hello")
    public Mono<String> sayHello() {
        return Mono.just("hello!");
    }

    @GetMapping("/rick-and-morty/characters")
    public Flux<String> getRickAndMortyCharacters() {
        return Flux.just("Rick Sanchez", "Morty Smith",
                "Beth Smith", "Jerry Smith");
    }

    @PostMapping("/rick-and-morty/characters/add-forgotten")
    public Flux<String> addForgotten(@RequestBody Flux<String> characters) {
        return characters.concatWithValues("Summer Smith");
    }
}
