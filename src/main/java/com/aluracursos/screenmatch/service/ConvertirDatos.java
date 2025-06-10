package com.aluracursos.screenmatch.service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvertirDatos implements IConvierteDatos {
    // Instancia de ObjectMapper para convertir JSON a objetos Java
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Metodo genérico de la interfaz IConvierteDatos
    @Override
    public <T> T obtenerDatos(String json, Class<T> clase) {
        try {
            return objectMapper.readValue(json, clase);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}


