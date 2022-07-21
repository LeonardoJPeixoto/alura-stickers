package com.ljpeixoto.alura.stickers.util;

import com.ljpeixoto.alura.stickers.util.exception.StickerException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ClienteHttp {
    public String buscaDados(String url) {
        try {

            // fazer uma conexão HTTP
            var endereco = URI.create(url);
            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder(endereco).build();
            HttpResponse<String> response;
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } catch (Exception e) {
            throw new StickerException(e);
        }

    }

}
