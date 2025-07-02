package com.aluracursos.screenmatch.main;
import com.aluracursos.screenmatch.modelo.*;
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
                    1) - Buscar Series
                    2) - Buscar Episodios
                    3) - Mostrar Series buscadas
                    4) - Buscar Serie por Titulo
                    5) - Top 5 mejores Series
                    6) - Buscar Series por Categoría
                    7) - Búsqueda Personalizada (Temporadas y Evaluación)
                    8) - Buscar Episodios por Nombre
                  
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
                case 4:
                    buscarSeriesPorTitulo();
                    break;
                case 5:
                    buscarTop5MejoresSeries();
                    break;
                case 6:
                    buscarSeriesPorCategoria();
                    break;
                case 7:
                    buscarSeriesPorTemporadaYEvaluacion();
                    break;
                case 8:
                    buscarEpisodioPorTitulo();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    // OBTENER DATOS DE UNA SERIE DESDE LA API
    private DatosSerie getDatosSerie() {
        System.out.println("Escribe el nombre de la serie que deseas buscar: ");
        var nombreSerie = scanner.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + "&apikey=" + apiKey);
        System.out.println(json);
        return conversor.obtenerDatos(json, DatosSerie.class);
    }

    // BUSCAR EPISODIOS DE UNA SERIE Y GUARDARLOS EN LA BASE DE DATOS
    private void buscarEpisodioPorSerie() {
        mostrarSeriesBuscadas();
        System.out.println("Escribe el nombre de la serie para buscar sus episodios: ");
        var nombreSerie = scanner.nextLine();

        // Optional para buscar la serie por nombre
        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
                .findFirst(); // Buscar la primera serie por nombre

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

    // BUSCAR CUALQUIER SERIE Y GUARDARLA EN LA BASE DE DATOS
    private void buscarSerieWeb() {
        DatosSerie datos = getDatosSerie();
        System.out.println(datos);

        Serie serie = new Serie(datos);
        repositorio.save(serie); // Guardar la serie en la base de datos
    }

    // MOSTRAR LAS SERIES BUSCADAS Y ORDENADAS POR GÉNERO
    private void mostrarSeriesBuscadas() {
        series = repositorio.findAll(); // Obtener todas las series de la base de datos

        if (series.isEmpty()) {
            System.out.println("No hay series buscadas.");
            return;
        }

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    // BUSCAR SERIES POR TITULO GUARDADAS EN LA BASE DE DATOS CON "DERIVED QUERY METHOD"
    private void buscarSeriesPorTitulo() {
        System.out.println("Escribe el título de la serie que deseas buscar: ");
        String nombreSerie = scanner.nextLine();

        // Siempre cuando trabajas con Optional se utiliza condicional con metodo isPresent()
        Optional<Serie> serieBuscada = repositorio.findByTituloContainsIgnoreCase(nombreSerie);

        if (serieBuscada.isPresent()) {
            System.out.println("La serie encontrada es: " + serieBuscada.get());
        } else {
            System.out.println("No se encontraron series con el título: " + serieBuscada);
        }
    }

    private void buscarTop5MejoresSeries() {
        // Obtener las 5 series mejor valoradas
        List<Serie> top5Series = repositorio.findTop5ByOrderByEvaluacionDesc();

        if (top5Series.isEmpty()) {
            System.out.println("No hay series en la base de datos.");
            return;
        }

        // Mostrar el título y la evaluación de cada serie
        top5Series.forEach(serie -> System.out.println("Las 5 mejores series son: \n"
                + "Serie: " + serie.getTitulo() + ", Evaluación: " + serie.getEvaluacion()));
    }

    private void buscarSeriesPorCategoria() {
        System.out.println("Escribe el Genero/Categoría de la serie que deseas buscar: ");
        String genero = scanner.nextLine();
        var categoria = Categoria.fromEspanol(genero);

        // Buscar series por género guardadas en la base de datos con "Derived Query Method":
        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);

        if (seriesPorCategoria.isEmpty()) {
            System.out.println("No se encontraron series con la categoría: " + genero);
            return;
        }

        System.out.println("Las series encontradas en la categoría " + genero + " son:");
            seriesPorCategoria.forEach(System.out::println);
    }

    // BÚSQUEDA PERSONALIZADA: Series con máximo número de temporadas y evaluación mínima
    private void buscarSeriesPorTemporadaYEvaluacion() {
        System.out.println("=== BÚSQUEDA PERSONALIZADA DE SERIES ===");

        System.out.print("Ingresa el número máximo de temporadas: ");
        var maxTemporadas = scanner.nextInt();

        System.out.print("Ingresa la evaluación mínima: ");
        var evaluacion = scanner.nextDouble();

        // Realizar la búsqueda usando la consulta derivada [DERIVED QUERY]
        List<Serie> filtroSeries = repositorio.seriesPorTemporadasYEvaluacion();

        if (filtroSeries.isEmpty()) {
            System.out.println("\n❌ No se encontraron series con los criterios especificados:");
            System.out.println(" • Máximo " + maxTemporadas + " temporadas");
            System.out.println(" • Evaluación mínima: " + evaluacion);
            return;
        }

        System.out.println("\n✅ Se encontraron " + filtroSeries.size() + " serie(s) que cumplen los criterios:");
        System.out.println(" • Máximo " + maxTemporadas + " temporadas");
        System.out.println(" • Evaluación mínima: " + evaluacion);
        System.out.println("\n=== RESULTADOS ===");

        filtroSeries.forEach(serie ->
            System.out.println("📺 " + serie.getTitulo() + " - Evaluacion: " + serie.getEvaluacion()));

        // Ejemplo específico mencionado en el requerimiento
        System.out.println("\n💡Ejemplo: Para buscar series con máximo 3 temporadas y evaluación ≥ 7.8");
        System.out.println("Ingresa: 3 para temporadas y 7.8 para evaluación");
    }

    // Buscar episodios por nombre
    private void buscarEpisodioPorTitulo() {
        System.out.println("Escribe el nombre del episodio que deseas buscar: ");
        String nombreEpisodio = scanner.nextLine();
        List<Episodio> filtroEpisodio = repositorio.episodiosPorNombre(nombreEpisodio);

        if (filtroEpisodio.isEmpty()) {
            System.out.println("No se encontraron episodios con el nombre: " + nombreEpisodio);
            return;
        }

        filtroEpisodio.forEach(episodio ->
            System.out.printf("Serie: %s, Temp orada: %s, Episodio: %s, Título: %s, Evaluación: %s\n",
                    episodio.getSerie(), episodio.getTemporada(), episodio.getNumeroEpisodio(), episodio.getTitulo(),
                    episodio.getEvaluacion()));
    }
}

