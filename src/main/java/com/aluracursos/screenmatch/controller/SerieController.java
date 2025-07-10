package com.aluracursos.screenmatch.controller;
import com.aluracursos.screenmatch.dto.EpisodioDTO;
import com.aluracursos.screenmatch.dto.SerieDTO;
import com.aluracursos.screenmatch.model.Episodio;
import com.aluracursos.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController // Anotación que indica que esta clase es un controlador REST
@RequestMapping("/series") // Definimos el prefijo de la URL para los endpoints de esta clase
public class SerieController {

    @Autowired // Inyección de dependencia para el service
    private SerieService servicio;

    // Ejemplo de un endpoint para obtener todas las series devolviendo una lista de SerieDTO
    @GetMapping()
    public List<SerieDTO> obtenerTodasLasSeries() {
        return servicio.obtenerTodasLasSeries();
    }

    @GetMapping("/top5")
        public List<SerieDTO> obtenerTop5Series() {
            return servicio.obtenerTop5Series();
    }

    @GetMapping("/lanzamientos") // El endpoint debe llamarse igual que en el manejo de rutas del Frontend
    public List<SerieDTO> obtenerLanzamientoMasReciente() {
        return servicio.obtenerLanzamientoMasReciente();
    }

    @GetMapping("/{id}") // Parámetro dinámico para retornar los datos de una serie por su ID
    public SerieDTO obtenerSeriePorId(@PathVariable Long id) {
        return servicio.obtenerSeriePorId(id);

    }

    @GetMapping("{id}/temporadas/todas")
    public List<EpisodioDTO> obtenerTodasLasTemporadas(@PathVariable Long id) {
        return servicio.obtenerTodasLasTemporadas(id);
    }

}