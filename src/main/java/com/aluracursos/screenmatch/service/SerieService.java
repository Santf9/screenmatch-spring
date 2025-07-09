package com.aluracursos.screenmatch.service;

import com.aluracursos.screenmatch.dto.SerieDTO;
import com.aluracursos.screenmatch.repository.ISerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SerieService {

    @Autowired
    private ISerieRepository repository;

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
