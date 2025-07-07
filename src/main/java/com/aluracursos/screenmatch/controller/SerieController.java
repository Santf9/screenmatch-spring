package com.aluracursos.screenmatch.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SerieController {

    // Ejemplo de un endpoint para obtener todas las series
    @GetMapping("/series")
    public String mostrarMensaje() {
        return "Este es un mensaje de API"; // Este es un ejemplo, deberías devolver una lista de series
    }
}
