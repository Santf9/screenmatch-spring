package com.aluracursos.screenmatch.main;
import com.aluracursos.screenmatch.modelo.DatosSerie;
import com.aluracursos.screenmatch.modelo.DatosTemporada;
import com.aluracursos.screenmatch.modelo.Episodio;
import com.aluracursos.screenmatch.modelo.Serie;
import com.aluracursos.screenmatch.repository.SerieRepository;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvertirDatos;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.*;


public class Main {

    private final Scanner scanner = new Scanner(System.in);
    private final ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final Dotenv dotenv = Dotenv.load();
    private final ConvertirDatos conversor = new ConvertirDatos();
    private final List<DatosSerie> datosSeries = new ArrayList<>();
    private final SerieRepository repositorio;
    private List<Serie> series;

    String apiKey = dotenv.get("API_KEY");

    public Main(SerieRepository repository) {
        this.repositorio = repository;
    }

    public void mostrarMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1) - Buscar series
                    2) - Buscar episodios
                    3) - Mostrar series buscadas
                  
                    0) - Salir
                    """;

            System.out.println(menu);
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    mostrarSeriesBuscadas();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private DatosSerie getDatosSerie() {
        System.out.println("Escribe el nombre de la serie que deseas buscar: ");
        var nombreSerie = scanner.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + "&apikey=" + apiKey);
        System.out.println(json);
        return conversor.obtenerDatos(json, DatosSerie.class);
    }

    private void buscarEpisodioPorSerie() {
        mostrarSeriesBuscadas();
        System.out.println("Escribe el nombre de la serie para buscar sus episodios: ");
        var nombreSerie = scanner.nextLine();

        // Optional para buscar la serie por nombre
        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
                .findFirst(); // Buscar la serie por nombre

        if (serie.isPresent()) {
            var serieEncontrada = serie.get();

            List<DatosTemporada> temporadas = new ArrayList<>(); // Lista para almacenar los episodios de la serie

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumoApi.obtenerDatos(URL_BASE + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + "&apikey=" + apiKey);
                DatosTemporada datosTemporada = conversor.obtenerDatos(json, DatosTemporada.class);
                temporadas.add(datosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numeroTemporada(), e))) // Convertir cada DatosEpisodio a Episodio
                    .toList(); // Convertir los datos de episodios a objetos Episodio

            serieEncontrada.setEpisodios(episodios); // Asignar los episodios a la serie
            repositorio.save(serieEncontrada); // Guardar la serie con sus episodios en la base de datos
        }

    }

    private void buscarSerieWeb() {
        DatosSerie datos = getDatosSerie();
        System.out.println(datos);

        Serie serie = new Serie(datos);
        repositorio.save(serie); // Guardar la serie en la base de datos

        //datosSeries.add(datos); // Agregar la serie a la lista de series buscadas
        //System.out.println(datos);
    }

    private void mostrarSeriesBuscadas() {
        series = repositorio.findAll(); // Obtener todas las series de la base de datos

        if (series.isEmpty()) {
            System.out.println("No hay series buscadas.");
            return;
        }

//        List<Serie> series = new ArrayList<>();
//        series = datosSeries.stream()
//                .map(dato -> new Serie(dato))
//                .toList();

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }
}

