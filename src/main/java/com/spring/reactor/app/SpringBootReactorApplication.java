package com.spring.reactor.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

/**
 * CommandLineRunner para que nuestra app sea por liena de comandos
 */
@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(SpringBootReactorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringBootReactorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Flux<String> nombres = Flux.just("Jose", "Luis", "Carlos", "Juana")
                .doOnNext(e -> {
                    if (e.isEmpty())
                        throw new RuntimeException("Los nombres no pueden estar vacios");

                    System.out.println(e);
                });

        nombres.subscribe(e -> LOGGER.info(e), error -> LOGGER.error(error.getMessage()), new Runnable() {
            @Override
            public void run() {
                LOGGER.info("Ha finalizado con éxito la ejecución");
            }
        });
    }
}
