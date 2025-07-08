package com.aluracursos.screenmatch.repository;

import com.aluracursos.screenmatch.model.Categoria;
import com.aluracursos.screenmatch.model.Episodio;
import com.aluracursos.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface ISerieRepository extends JpaRepository<Serie, Long> {

    // Por ejemplo, para buscar series por título: [DERIVED QUERY]
    Optional<Serie> findByTituloContainsIgnoreCase(String nombreSerie);

    // Metodo para obtener las 5 series mejor valoradas [DERIVED QUERY]
    List<Serie> findTop5ByOrderByEvaluacionDesc();

    List<Serie> findByGenero(Categoria categoria);

    // Búsqueda personalizada: series con máximo número de temporadas y evaluación mínima
    List<Serie> findByTotalTemporadasLessThanEqualAndEvaluacionGreaterThanEqual(Integer totalTemporadas, Double Evaluacion);

    // Consulta personalizada utilizando (NATIVE QUERY) - Trabajamos con SQL nativo
    @Query(value = "SELECT * FROM serie WHERE serie.total_temporadas <= 6 AND serie.evaluacion >= 7.5", nativeQuery = true)
    List<Serie> seriesPorTemporadasYEvaluacion();

    // Misma consulta personalizada utilizando (JPQL) - Trabajamos con Clases donde Persistimos los datos
    @Query("SELECT s FROM Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.evaluacion >= :evaluacion")
    List<Serie> seriesPorTemporadasYEvaluacionJPQL(Integer totalTemporadas, Double Evaluacion);

    // Buscar episodios por nombre
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:nombreEpisodio%")
    List<Episodio> episodiosPorNombre(String nombreEpisodio);

    // Top 5 mejores episodios por serie
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.titulo = :titulo ORDER BY e.evaluacion DESC LIMIT 5")
    List<Episodio> top5EpisodiosPorSerie(Serie titulo);

}
