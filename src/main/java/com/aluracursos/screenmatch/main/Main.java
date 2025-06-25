package com.aluracursos.screenmatch.main;
import com.aluracursos.screenmatch.modelo.DatosSerie;
import com.aluracursos.screenmatch.modelo.DatosTemporada;
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
    private SerieRepository repositorio;

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
        DatosSerie datosSerie = getDatosSerie();
        List<DatosTemporada> temporadas = new ArrayList<>(); // Lista para almacenar los episodios de la serie

        for (int i = 1; i <= datosSerie.totalTemporadas(); i++) {
            var json = consumoApi.obtenerDatos(URL_BASE + datosSerie.titulo().replace(" ", "+") + "&season=" + i + "&apikey=" + apiKey);
            DatosTemporada datosTemporada = conversor.obtenerDatos(json, DatosTemporada.class);
            temporadas.add(datosTemporada);
        }
        temporadas.forEach(System.out::println);
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
        List<Serie> series = new ArrayList<>();
        series = datosSeries.stream()
                .map(dato -> new Serie(dato))
                .toList();

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }
}

