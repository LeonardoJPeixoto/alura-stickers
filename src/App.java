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

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BOLD = "\u001B[1m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLACK  = "\u001B[30m";
    public static final String ANSI_BRIGHT_BG_CYAN   = "\u001B[105m";

    public static void main(String[] args) throws Exception {

        System.out.println(ANSI_BOLD + "Hello" + ANSI_RESET + ", World!");

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
                .limit(250)
                .forEach(mapaFilme -> {
                    System.out.println();
                    System.out.printf(ANSI_BOLD + ANSI_YELLOW + "Título" + ANSI_RESET + ": " + ANSI_BLACK + ANSI_BRIGHT_BG_CYAN + " %s " + ANSI_RESET + "%n", mapaFilme.get("title"));
                    System.out.printf(ANSI_BOLD + ANSI_YELLOW + "Poster" + ANSI_RESET + ": %s %n", mapaFilme.get("image"));
                    String avaliacao = mapaFilme.get("imDbRating");
                    String estrelas = converteEmEstrelas(avaliacao);
                    System.out.printf(ANSI_BOLD + ANSI_YELLOW + "Avaliação" + ANSI_RESET + ": " + ANSI_YELLOW +  "%s" + ANSI_RESET + " (%s)\n", estrelas, avaliacao != null ? avaliacao : "Null");
                    System.out.println();
                });

        // Desafios
        // Extrair chave do IMDB - OK
        // Trocar endpoint para MostPopularMovies - OK
        // Saída visualmente melhorada no terminal - OK
        // TODO: Usar Jackson/Gson — continuar como List de Map ?
        // TODO: Método para usuário dar avaliação no filme

    }

    private static String converteEmEstrelas(String imDbRating) {
        if (stringVazia(imDbRating)) {
            return "NA";
        }
        double avaliacao = Double.parseDouble(imDbRating);
        int numeroDeEstrelas = (int) Math.round(avaliacao/2);
        return "\u2B50".repeat(numeroDeEstrelas);
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
