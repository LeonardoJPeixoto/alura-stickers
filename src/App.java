import util.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class App {

//    private static final String URL_API = "https://imdb-api.com/en/API/Top250Movies/CHAVE_API_IMDB";
        private static final String URL_API = "https://mocki.io/v1/9a7c1ca9-29b4-4eb3-8306-1adb9d159060";

    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        // fazer uma conexão HTTP
        var endereco = URI.create(URL_API);
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(endereco).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();
        System.out.println("Body: ");
        System.out.println(body);

        // extrair só os dados que interessam (título, poster, classificação)
        List<Map<String, String>> listaDeFilmes = new JsonParser().parse(body);
        System.out.printf("Número de Filmes encontrados: %s%n", listaDeFilmes.size());

        // exibir e manipular os dados
        listaDeFilmes.stream()
                .limit(50)
                .forEach(mapaFilme -> {
                    System.out.printf("Título: %s %n", mapaFilme.get("title"));
                    System.out.printf("Poster: %s %n", mapaFilme.get("image"));
                    System.out.printf("Avaliação: %s %n", mapaFilme.get("imDbRating"));
                });

        // Desafios
        // TODO: Extrair chave do IMDB
        // TODO: Trocar endpoint para MostPopularMovies
        // TODO: Saída visualmente melhorada no terminal
        // TODO: Usar Jackson/Gson — continuar como List de Map ?
        // TODO: Método para usuário dar avaliação no filme

    }

}
