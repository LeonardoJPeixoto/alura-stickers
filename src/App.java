import util.JsonParser;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class App {

    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        // fazer uma conexão HTTP
        var endereco = URI.create(leUrlDeProperties());
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
        // Extrair chave do IMDB - OK
        // TODO: Trocar endpoint para MostPopularMovies
        // TODO: Saída visualmente melhorada no terminal
        // TODO: Usar Jackson/Gson — continuar como List de Map ?
        // TODO: Método para usuário dar avaliação no filme

    }

    private static String leUrlDeProperties() {
        try (InputStream input = App.class.getResourceAsStream("resources/config.properties")) {
            if (input == null) {
                throw new RuntimeException("Arquivo de config.properties não encontrado.");
            }

            Properties prop = new Properties();
            prop.load(input);

            String url = prop.getProperty("IMDB_URL");
            String chave = prop.getProperty("IMDB_CHAVE_API");

            if (stringVazia(url) || stringVazia(chave)) {
                throw new RuntimeException("Url e/ou chave do IMDB não configurados corretamente em config.properties.");
            }

            if (url.contains("mock")) {
                System.out.println("AVISO: usando URL mockada.");
            }

            return url + chave;
        } catch (IOError | IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static boolean stringVazia(String s) {
        if (s == null) {
            return true;
        }
        return "".equals(s.trim());
    }
}
