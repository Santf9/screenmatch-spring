package com.aluracursos.screenmatch.repository;

import com.aluracursos.screenmatch.modelo.Categoria;
import com.aluracursos.screenmatch.modelo.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {

    // Por ejemplo, para buscar series por título:
    Optional<Serie> findByTituloContainsIgnoreCase(String nombreSerie);

    List<Serie> findTop5ByOrderByEvaluacionDesc(); // Metodo para obtener las 5 series mejor valoradas

    List<Serie> findByGenero(Categoria categoria);

    // Búsqueda personalizada: series con máximo número de temporadas y evaluación mínima
    List<Serie> findByTotalTemporadasLessThanEqualAndEvaluacionGreaterThanEqual(Integer totalTemporadas, Double Evaluacion);

}
