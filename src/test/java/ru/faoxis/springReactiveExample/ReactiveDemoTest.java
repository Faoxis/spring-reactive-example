package ru.faoxis.springReactiveExample;

import org.junit.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

import static ru.faoxis.springReactiveExample.ReactiveDemo.userRouter;
import static ru.faoxis.springReactiveExample.ReactiveDemo.User;

public class ReactiveDemoTest {
    @Test
    public void userHandlerTest() {
        WebTestClient client = WebTestClient
                .bindToRouterFunction(userRouter).build();
        client.get().uri("/users").exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class);
    }
}