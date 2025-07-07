package com.aluracursos.screenmatch.model;

public enum Categoria {
    // Atributos de la categoría
    ACCION("Action", "Acción"),
    ROMANCE("Romance", "Romance"),
    COMEDIA("Comedy", "Comedia"),
    DRAMA("Drama", "Drama"),
    CRIMEN("Crime", "Crimen");

    // Atributo para almacenar el valor de la categoría en constructor
    private final String categoriaOmdb;
    private final String categoriaOmdbEspanol;

    // Constructor para inicializar el valor de la categoría enum
    Categoria (String categoriaOmdb, String categoriaOmdbEspanol) {
        this.categoriaOmdb = categoriaOmdb;
        this.categoriaOmdbEspanol = categoriaOmdbEspanol;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Categoría no válida: " + text);
    }

    public static Categoria fromEspanol(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdbEspanol.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Categoría no válida: " + text);
    }
}
