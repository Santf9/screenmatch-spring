package com.aluracursos.screenmatch.controller;

import com.aluracursos.screenmatch.dto.SerieDTO;
import com.aluracursos.screenmatch.repository.ISerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class SerieController {

    @Autowired
    private ISerieRepository repository;

    // Ejemplo de un endpoint para obtener todas las series
    @GetMapping("/series")
    public List<SerieDTO> obtenerTodasLasSeries() {
        return repository.findAll().stream()
                .map(serie -> new SerieDTO(
                        serie.getTitulo(),
                        serie.getTotalTemporadas(),
                        String.valueOf(serie.getEvaluacion()),
                        serie.getPoster(),
                        serie.getGenero(),
                        serie.getActores(),
                        serie.getSinopsis()))
                .toList();
    }
}
