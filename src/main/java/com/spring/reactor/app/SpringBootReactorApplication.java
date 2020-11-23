package com.spring.reactor.app;

import com.spring.reactor.app.models.Usuario;
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
        Flux<Usuario> nombres = Flux.just("Jose Usuga", "Luis Perez", "Carlos Guerra",
                "Juana Herrera", "Raul Duque", "Andres Gonzales")
                .map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(),
                        nombre.split(" ")[1].toUpperCase()))
                .filter(usuario ->
                        usuario.getNombre().toLowerCase().equals("juana"))
                /*
                 * Lo que se aplica antes del doOnNext, son filtros, los cuales sólo entraran o seran
                 * tomados en las posteriores ejecuciones
                 * */
                .doOnNext(usuario -> {
                    if (usuario == null)
                        throw new RuntimeException("Los nombres no pueden estar vacios");

                    System.out.println(usuario.getNombre().concat(" ")
                            .concat(usuario.getApellido()));
                }).map(usuario -> {
                    String nombre = usuario.getNombre().toLowerCase();
                    usuario.setNombre(nombre);
                    return usuario;
                });

        nombres.subscribe(e -> LOGGER.info(e.toString()), error -> LOGGER.error(error.getMessage()), new Runnable() {
            @Override
            public void run() {
                LOGGER.info("Ha finalizado con éxito la ejecución");
            }
        });
    }
}
