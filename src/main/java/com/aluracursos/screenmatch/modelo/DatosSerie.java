package com.aluracursos.screenmatch.modelo;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Anotación para ignorar propiedades desconocidas en la deserialización
@JsonIgnoreProperties(ignoreUnknown = true)

// Datos Serializados de la API
public record DatosSerie(
        @JsonAlias("Title") String titulo,
        @JsonAlias("totalSeasons") Integer totalTemporadas,
        @JsonAlias("imdbRating") String evaluacion) {
}
