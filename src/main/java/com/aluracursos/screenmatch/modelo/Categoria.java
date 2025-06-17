package com.aluracursos.screenmatch.modelo;

public enum Categoria {
    // Atributos de la categoría
    ACCION("Action"),
    ROMANCE("Romance"),
    COMEDIA("Comedy"),
    DRAMA("Drama"),
    CRIMEN("Crime");

    // Atributo para almacenar el valor de la categoría en constructor
    private String categoriaOmdb;

    // Constructor para inicializar el valor de la categoría enum
    Categoria (String categoriaOmdb) {
        this.categoriaOmdb = categoriaOmdb;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Categoría no válida: " + text);
    }
}
