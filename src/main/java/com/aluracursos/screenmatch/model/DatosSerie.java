package com.aluracursos.screenmatch.model;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Anotación para ignorar propiedades desconocidas en la deserialización
@JsonIgnoreProperties(ignoreUnknown = true)

// Datos Serializados de la API
public record DatosSerie(

        @JsonAlias("Title") String titulo,
        @JsonAlias("totalSeasons") Integer totalTemporadas,
        @JsonAlias("imdbRating") String evaluacion,
        @JsonAlias("Poster") String poster,
        @JsonAlias("Genre") String genero,
        @JsonAlias("Actors") String actores,
        @JsonAlias("Plot") String sinopsis) {
}
