package com.aluracursos.screenmatch.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Entity
@Table(name = "episodios")
public class Episodio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generación automática del ID
    private Long Id; // Asumiendo que la clase tiene un campo id para la persistencia a la base de datos

    private Integer temporada;

    private String titulo;

    private Integer numeroEpisodio;

    private Double evaluacion;

    private LocalDate fechaLanzamiento;

    @ManyToOne
    private Serie serie;

    public Episodio() { // Constructor vacío por defecto necesario para JPA - declararlo manualmente
    }

    public Episodio(Integer numero, DatosEpisodio d) {
        this.temporada = numero;
        this.titulo = d.tituloEpisodio();
        this.numeroEpisodio = d.numeroEpisodio();
        try {
            this.evaluacion = Double.valueOf(d.evaluacion());
        } catch (NumberFormatException e) {
            this.evaluacion = 0.0; // Valor por defecto si la evaluación no es un número válido
        }
        try {
            this.fechaLanzamiento = LocalDate.parse(d.fechaLanzamiento());
        } catch (DateTimeParseException e){
            this.fechaLanzamiento = null;// Valor por defecto si la fecha no es válida
        }

    }

    public Integer getTemporada() {
        return temporada;
    }

    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getNumeroEpisodio() {
        return numeroEpisodio;
    }

    public void setNumeroEpisodio(Integer numeroEpisodio) {
        this.numeroEpisodio = numeroEpisodio;
    }

    public Double getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Double evaluacion) {
        this.evaluacion = evaluacion;
    }

    public LocalDate getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public void setFechaLanzamiento(LocalDate fechaLanzamiento) {
        this.fechaLanzamiento = fechaLanzamiento;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    @Override
    public String toString() {
        return
                "Temporada=" + temporada +
                ", Titulo='" + titulo + '\'' +
                ", NumeroEpisodio=" + numeroEpisodio +
                ", Evaluacion=" + evaluacion +
                ", FechaLanzamiento=" + fechaLanzamiento;
    }
}
