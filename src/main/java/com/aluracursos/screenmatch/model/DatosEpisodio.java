package com.aluracursos.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Anotación para ignorar propiedades desconocidas en la deserialización
@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosEpisodio(

        @JsonAlias("Title") String tituloEpisodio,
        @JsonAlias("Episode") Integer numeroEpisodio,
        @JsonAlias("imdbRating") String evaluacion,
        @JsonAlias("Released") String fechaLanzamiento) {
}
