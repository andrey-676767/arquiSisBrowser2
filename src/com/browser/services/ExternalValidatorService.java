package com.browser.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ExternalValidatorService {

    public String obtenerInfoServidor(String url) {
        // Limpiamos la URL para obtener solo el dominio
        String dominio = url.replace("https://", "").replace("http://", "").split("/")[0];

        // Usamos una API real y gratuita (ip-api.com) que no pide API Key para pruebas
        String apiUrl = "http://ip-api.com/json/" + dominio + "?fields=status,country,isp";

        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(5))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .GET()
                    .build();

            // Enviamos la petición real a internet
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body(); // Retorna el JSON real de la API
            } else {
                return "{\"error\": \"No se pudo conectar con el servicio externo\"}";
            }
        } catch (Exception e) {
            return "{\"error\": \"Fallo de red: " + e.getMessage() + "\"}";
        }
    }
}