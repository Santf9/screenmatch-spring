package com.aluracursos.screenmatch.main;

import com.aluracursos.screenmatch.modelo.DatosEpisodio;
import com.aluracursos.screenmatch.modelo.DatosSerie;
import com.aluracursos.screenmatch.modelo.DatosTemporada;
import com.aluracursos.screenmatch.modelo.Episodio;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvertirDatos;
import io.github.cdimascio.dotenv.Dotenv;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + "&apikey=" + apiKey);
        var datos = conversor.obtenerDatos(json, DatosSerie.class);
        System.out.println(datos);

        // Busca los datos de todas las temporadas de la serie
        List<DatosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i <= datos.totalTemporadas(); i++) {
            json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + "&Season=" + i + "&apikey=" + apiKey);
            var datosTemporada = conversor.obtenerDatos(json, DatosTemporada.class);
            temporadas.add(datosTemporada);
        }
       // temporadas.forEach(System.out::println);

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
//        System.out.println("Top 5 episodios con mejor evaluación:");
//        datosEpisodios.stream()
//                .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A"))// Filtra episodios con evaluación e ignora "N/A"
////                .peek(e -> System.out.println("Primer filtro N/A: " + e)) // Observa los episodios que pasan el filtro
//                .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed()) // Ordena los episodios por evaluación de forma descendente
////                .peek(e -> System.out.println("Segundo filtro (Mayor a menor): " + e))
////                .map(e -> e.tituloEpisodio().toUpperCase())
////                .peek(e -> System.out.println("Tercer filtro MAYÚSCULAS (menor a Mayor): " + e))
//                .limit(5) // Limita a los 5 primeros episodios
//                .forEach(System.out::println);

        // Convirtiendo los datos a una lista del tipo Episodios
        List<Episodio> listaEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numeroTemporada(), d)))
                .collect(Collectors.toList()); // Almacena la lista de episodios en una lista

        //listaEpisodios.forEach(System.out::println);

        // Busqueda de episodios a partir de X año
//      System.out.println("Ingrese el año a partir del cual desea buscar los episodios: ");
//      var fecha = scanner.nextInt();

        // Crea una fecha de búsqueda con el primer día del año
//      LocalDate fechaBusqueda = LocalDate.of(fecha, 1, 1);

        // Formateador para mostrar la fecha en el formato dd/MM/yyyy
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Filtra los episodios que se lanzaron a partir de la fecha indicada
//        listaEpisodios.stream()
//                .filter(e -> e.getFechaLanzamiento() != null && e.getFechaLanzamiento().isAfter(fechaBusqueda))
//                .forEach(e -> System.out.println(
//                        "Temporada: " + e.getTemporada() +
//                                ", Episodio: " + e.getTitulo() +
//                                ", Fecha de lanzamiento: " + e.getFechaLanzamiento().format(dtf) // Se pasa el formateador
//                ));

        // Buscar episodios por un pedazo del título
        System.out.println("Ingrese un pedazo del título de los episodios que desea buscar: ");
        var pedazoTitulo = scanner.nextLine();

        // Utiliza Optional para manejar el caso en que no se encuentre ningún episodio | Puede que contenga o no contenga
        Optional<Episodio> episodioBuscado = listaEpisodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(pedazoTitulo.toUpperCase()))
                .findFirst();// Operación terminal

        if (episodioBuscado.isPresent()) {
            Episodio episodio = episodioBuscado.get();
            System.out.println("Episodio encontrado: " + episodio.getTitulo() +
                    ", Temporada: " + episodio.getTemporada() +
                    ", Número de episodio: " + episodio.getNumeroEpisodio() +
                    ", Evaluación: " + episodio.getEvaluacion() +
                    ", Fecha de lanzamiento: " + (episodio.getFechaLanzamiento() != null ? episodio.getFechaLanzamiento().format(dtf) : "N/A"));
        } else {
            System.out.println("No se encontró ningún episodio con el título proporcionado.");
        }

        // Calcular la evaluación promedio por temporada
        Map<Integer, Double> evaluacionesPorTemporada = listaEpisodios.stream()
                .filter(e -> e.getEvaluacion() > 0.0) // Filtra episodios con evaluación válida
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getEvaluacion)));
        System.out.println("Evaluación promedio por temporada: " + evaluacionesPorTemporada);

        // Estadísticas con DoubleSummaryStatistics para promedio máximo y mínimo de las evaluaciones
        DoubleSummaryStatistics est = listaEpisodios.stream()
                .filter(e -> e.getEvaluacion() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getEvaluacion));
        System.out.println("La media de las evaluaciones es: " + est.getAverage() +
                ",\n El episodio mejor evaluado es: " + est.getMax() +
                ",\n El episodio peor evaluado es: " + est.getMin());
    }
}
