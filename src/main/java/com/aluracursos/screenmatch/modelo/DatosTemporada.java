package com.aluracursos.screenmatch.modelo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

// Anotación para ignorar propiedades desconocidas en la deserialización
@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosTemporada (

        @JsonAlias("Season") Integer numeroTemporada,
        @JsonAlias("Episodes") List<DatosEpisodio> episodios) {

}
