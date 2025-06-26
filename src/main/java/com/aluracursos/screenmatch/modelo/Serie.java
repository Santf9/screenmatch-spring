package com.aluracursos.screenmatch.modelo;

import com.aluracursos.screenmatch.service.ConsultaGemini;
import jakarta.persistence.*;
import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name = "series")
public class Serie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generación automática del ID
    private Long Id; // Asumiendo que la clase tiene un campo id para la persistencia a la base de datos

    @Column(unique = true) // Asegurando que el título sea único
    private String titulo;

    private Integer totalTemporadas;

    private Double evaluacion;

    private String poster;

    @Enumerated(EnumType.STRING) // Usando EnumType.STRING para almacenar el nombre del género como cadena
    private Categoria genero;

    private String actores;

    private String sinopsis;

    // Relación uno a muchos con Episodio, donde Episodio tiene una referencia a Serie
    // Cascade ALL significa que las operaciones de persistencia, actualización y eliminación en Serie se propagan a Episodio
    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER) // FetchType.EAGER para cargar episodios junto con la serie y mostrarlos inmediatamente
    private List<Episodio> episodios;

    public Serie() {
        // Constructor vacío por defecto necesario para JPA - declararlo manualmente
    }

    public Serie(DatosSerie datosSerie) {
        this.titulo = datosSerie.titulo();
        this.totalTemporadas = datosSerie.totalTemporadas();
        this.evaluacion = OptionalDouble.of(Double.valueOf(datosSerie.evaluacion())).orElse(0); // Valor por defecto si la evaluación no es un número válido
        this.poster = datosSerie.poster();
        this.genero = Categoria.fromString(datosSerie.genero().split(",")[0].trim()); // Asumiendo que el género es una cadena separada por comas
        this.actores = datosSerie.actores();
        this.sinopsis = ConsultaGemini.obtenerTraduccion(datosSerie.sinopsis());
    }

    @Override
    public String toString() {
        return  "genero='" + genero + '\'' +
                ", titulo='" + titulo + '\'' +
                ", totalTemporadas=" + totalTemporadas +
                ", evaluacion=" + evaluacion +
                ", poster='" + poster + '\'' +
                ", actores='" + actores + '\'' +
                ", sinopsis='" + sinopsis + '\'' +
                ", episodios='" + episodios + '\'';
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }

    public Double getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Double evaluacion) {
        this.evaluacion = evaluacion;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getActores() {
        return actores;
    }

    public void setActores(String actores) {
        this.actores = actores;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public List<Episodio> getEpisodios() {
        return episodios;
    }

    public void setEpisodios(List<Episodio> episodios) {
        episodios.forEach(episodio -> episodio.setSerie(this)); // Establecer la referencia a la serie en cada episodio
        this.episodios = episodios;
    }
}
