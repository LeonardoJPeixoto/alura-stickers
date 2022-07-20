import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Properties;

public class App {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BOLD = "\u001B[1m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_BRIGHT_BG_CYAN = "\u001B[105m";

    public static void main(String[] args) throws Exception {

        ClienteHttp clienteHttp = new ClienteHttp();

        // Lista de filmes Imdb
//        String url = leUrlDeProperties();
//        Extrator extrator = new ExtratorImdb();

        // Lista de imagens da Nasa
        String url = "https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY&count=5";
        Extrator extrator = new ExtratorNasa();

        String json = clienteHttp.buscaDados(url);

        List<Conteudo> listaDeConteudos = extrator.extrai(json);
        System.out.printf("Número de Itens encontrados: %s%n", listaDeConteudos.size());

        // exibir e manipular os dados
        var geradora = new GeradoraDeFigurinha();
        listaDeConteudos.stream()
                .limit(10)
                .forEach(conteudo -> {
                    try {
                        String titulo = conteudo.getTitulo();
                        String urlImagem = conteudo.getUrlImagem();
                        String avaliacao = conteudo.getAvaliacao();

                        System.out.printf(ANSI_BOLD + ANSI_YELLOW + "Título" + ANSI_RESET + ": " + ANSI_BLACK + ANSI_BRIGHT_BG_CYAN + " %s " + ANSI_RESET + "%n", titulo);
                        if (avaliacao != null && !avaliacao.isBlank()) {
                            String estrelas = converteEmEstrelas(avaliacao);
                            System.out.printf(ANSI_BOLD + ANSI_YELLOW + "Avaliação" + ANSI_RESET + ": " + ANSI_YELLOW + "%s" + ANSI_RESET + " (%s)\n", estrelas, avaliacao != null ? avaliacao : "Null");
                        }
                        System.out.println();

                        if (urlImagem != null && !urlImagem.isBlank()) {
                            InputStream input = new URL(urlImagem).openStream();
                            geradora.cria(input, titulo);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        // Desafios
        // Extrair chave do IMDB - OK
        // Trocar endpoint para MostPopularMovies - OK
        // Saída visualmente melhorada no terminal - OK
        // Usar Jackson/Gson — continuar como List de Map - OK
        // TODO: Método para usuário dar avaliação no filme

    }

    private static String converteEmEstrelas(String imDbRating) {
        if (Util.stringVazia(imDbRating)) {
            return "NA";
        }
        double avaliacao = Double.parseDouble(imDbRating);
        int numeroDeEstrelas = (int) Math.round(avaliacao / 2);
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
            throw new RuntimeException(e);
        }
    }

    private static boolean stringVazia(String s) {
        return s == null || s.isBlank();
    }

}
