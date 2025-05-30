package com.aluracursos.screenmatch.main;

import java.util.Arrays;
import java.util.List;


public class EjemploStream {
    public void ejemploStream() {
        List<String> nombres = Arrays.asList("Juan", "Ana", "Pedro", "Maria", "Luis");

        nombres.stream()
                .sorted()
                .limit(3)
                .forEach(System.out::println);

    }
}
