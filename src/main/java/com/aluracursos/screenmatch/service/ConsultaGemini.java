package com.aluracursos.screenmatch.service;


import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import io.github.cdimascio.dotenv.Dotenv;

public class ConsultaGemini {

        public static String obtenerTraduccion(String texto) {
            String modelo = "gemini-2.0-flash-lite";
            String prompt = "Traduce el siguiente texto al español: " + texto;

            // Cargar las variables de entorno desde el archivo .env
            Dotenv dotenv = Dotenv.load();
            String apiKey = dotenv.get("GOOGLE_API_KEY");

            Client cliente = new Client.Builder()
                    .apiKey(apiKey)
                    .build();

            try {
                GenerateContentResponse respuesta = cliente.models.generateContent(
                        modelo,
                        prompt,
                        null // Parámetro para configuraciones adicionales
                );

                if (!respuesta.text().isEmpty()) {
                    return respuesta.text();
                }
            } catch (Exception e) {
                System.err.println("Error al llamar a la API de Gemini para traducción: " + e.getMessage());
            }

            return null;
        }
}
