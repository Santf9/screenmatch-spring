package com.aluracursos.screenmatch.modelo;

import java.time.LocalDate;

public class Episodio {

    private Integer temporada;
    private String titulo;
    private Integer numeroEpisodio;
    private Double evaluacion;
    private LocalDate fechaLanzamiento;

    public Episodio(Integer numero, DatosEpisodio d) {
        this.temporada = numero;
        this.titulo = d.tituloEpisodio();
        this.numeroEpisodio = d.numeroEpisodio();
        this.evaluacion = Double.valueOf(d.evaluacion());
        this.fechaLanzamiento = LocalDate.parse(d.fechaLanzamiento());
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
