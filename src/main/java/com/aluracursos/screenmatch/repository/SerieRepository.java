package com.aluracursos.screenmatch.repository;

import com.aluracursos.screenmatch.modelo.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    // Aquí puedes agregar métodos personalizados si es necesario
    // Por ejemplo, para buscar series por título:
    Optional<Serie> findByTituloContainsIgnoreCase(String nombreSerie);


}
