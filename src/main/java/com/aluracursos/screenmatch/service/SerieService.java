package com.aluracursos.screenmatch.service;

import com.aluracursos.screenmatch.dto.EpisodioDTO;
import com.aluracursos.screenmatch.dto.SerieDTO;
import com.aluracursos.screenmatch.model.Serie;
import com.aluracursos.screenmatch.repository.ISerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SerieService {

    @Autowired // Inyección de dependencia para el repository
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

    public SerieDTO obtenerSeriePorId(Long id) {
        Optional<Serie> serieId = repository.findById(id);

        if (serieId.isPresent()) {
            Serie serie = serieId.get();
            return new SerieDTO(
                    serie.getId(),
                    serie.getTitulo(),
                    serie.getTotalTemporadas(),
                    String.valueOf(serie.getEvaluacion()),
                    serie.getPoster(),
                    serie.getGenero(),
                    serie.getActores(),
                    serie.getSinopsis());
        } else {
           return null;
        }
    }

    public List<EpisodioDTO> obtenerTodasLasTemporadas(Long id) {
        Optional<Serie> episodio = repository.findById(id);

        if (episodio.isPresent()){
            var serie = episodio.get();
            return serie.getEpisodios().stream()
                    .map(episodios -> new EpisodioDTO(
                            episodios.getTemporada(),
                            episodios.getTitulo(),
                            episodios.getNumeroEpisodio()))
                    .toList();
        }
        return null;
    }

    public List<SerieDTO> convertirDatos(List<Serie> serie) {
        return serie.stream()
                .map(serieItem -> new SerieDTO(
                        serieItem.getId(),
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
