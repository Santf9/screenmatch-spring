package com.aluracursos.screenmatch.service;

import com.aluracursos.screenmatch.dto.SerieDTO;
import com.aluracursos.screenmatch.model.Serie;
import com.aluracursos.screenmatch.repository.ISerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SerieService {

    @Autowired
    private ISerieRepository repository;

    public List<SerieDTO> obtenerTodasLasSeries() {
        return convertirDatos(repository.findAll());
    }

    public List<SerieDTO> obtenerTop5Series() {
        return convertirDatos(repository.findTop5ByOrderByEvaluacionDesc());
    }

    public List<SerieDTO> obtenerLanzamientoMasReciente() {
        return convertirDatos(repository.lanzamientosMasReciente());
    }

    public List<SerieDTO> convertirDatos(List<Serie> serie) {
        return serie.stream()
                .map(serieItem -> new SerieDTO(
                        serieItem.getTitulo(),
                        serieItem.getTotalTemporadas(),
                        String.valueOf(serieItem.getEvaluacion()),
                        serieItem.getPoster(),
                        serieItem.getGenero(),
                        serieItem.getActores(),
                        serieItem.getSinopsis()))
                .toList();
    }
}
