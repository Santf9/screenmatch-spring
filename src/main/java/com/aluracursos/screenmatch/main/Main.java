package com.aluracursos.screenmatch.main;

import com.aluracursos.screenmatch.modelo.DatosEpisodio;
import com.aluracursos.screenmatch.modelo.DatosSerie;
import com.aluracursos.screenmatch.modelo.DatosTemporada;
import com.aluracursos.screenmatch.modelo.Episodio;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvertirDatos;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    private Scanner scanner = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private Dotenv dotenv = Dotenv.load();
    private ConvertirDatos conversor = new ConvertirDatos();

    String apiKey = dotenv.get("API_KEY");

    public void mostrarMenu() {
        System.out.println("Por favor, ingrese el nombre de la serie que desea buscar: ");
        // Busca los datos generales de la serie
        var nombreSerie = scanner.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ","+") + "&apikey="+apiKey);
        var datos = conversor.obtenerDatos(json, DatosSerie.class);
        System.out.println(datos);

        // Busca los datos de todas las temporadas de la serie
        List<DatosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i <= datos.totalTemporadas(); i++) {
            json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ","+") +"&Season="+i+"&apikey="+apiKey);
            var datosTemporada = conversor.obtenerDatos(json, DatosTemporada.class);
            temporadas.add(datosTemporada);
        }
        temporadas.forEach(System.out::println);

        // Mostrar solo el título para los episodios de cada temporada
//        for (int i = 0; i < datos.totalTemporadas(); i++) { // Itera la lista de temporadas para conseguir traer todos los episodios
//            List<DatosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//            for (int j = 0; j < episodiosTemporada.size(); j++) { // Itera la lista de episodios para conseguir traer el título de cada episodio
//                System.out.println(episodiosTemporada.get(j).tituloEpisodio());
//            }
//        }

        // Simplificar código anterior en Lambda
        //temporadas.forEach(temporada -> temporada.episodios().forEach(episodio -> System.out.println(episodio.tituloEpisodio())));

        // Convertir todas las informaciones a una lista del tipo DatosEpisodio
        List<DatosEpisodio> datosEpisodios = temporadas.stream()
                .flatMap(temporada -> temporada.episodios().stream())
                .collect(Collectors.toList()); // Almacena la lista de episodios y es mutable

        // Top 5 episodios
        System.out.println("Top 5 episodios con mejor evaluación:");
        datosEpisodios.stream()
                .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A")) // Filtra episodios con evaluación e ignora "N/A"
                .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed()) // Ordena los episodios por evaluación de forma descendente
                .limit(5) // Limita a los 5 primeros episodios
                .forEach(System.out::println);

        // Convirtiendo los datos a una lista del tipo Episodios
        List<Episodio> listaEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                .map(d -> new Episodio(t.numeroTemporada(), d)))
                .collect(Collectors.toList());

        listaEpisodios.forEach(System.out::println);

    }
}
