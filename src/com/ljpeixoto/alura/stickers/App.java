package com.ljpeixoto.alura.stickers;

import com.ljpeixoto.alura.stickers.imdb.FontesImagens;
import com.ljpeixoto.alura.stickers.interfaces.FonteImagens;
import com.ljpeixoto.alura.stickers.model.Conteudo;
import com.ljpeixoto.alura.stickers.util.Util;
import com.ljpeixoto.alura.stickers.util.exception.StickerException;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class App {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BOLD = "\u001B[1m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_BRIGHT_BG_CYAN = "\u001B[105m";

    public static void main(String[] args) throws Exception {

        FonteImagens fonteImagens = FontesImagens.IMDB_MOST_POPULAR.getFonteImagens();

        List<Conteudo> listaDeConteudos = fonteImagens.buscaListaConteudo();
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
                            System.out.printf(ANSI_BOLD + ANSI_YELLOW + "Avaliação" + ANSI_RESET + ": " + ANSI_YELLOW + "%s" + ANSI_RESET + " (%s)\n", estrelas, avaliacao);
                        }
                        System.out.println();

                        if (urlImagem != null && !urlImagem.isBlank()) {
                            InputStream input = new URL(urlImagem).openStream();
                            geradora.cria(input, titulo);
                        }
                    } catch (Exception e) {
                        throw new StickerException(e);
                    }
                });

        // Desafios Aula 1
        // Extrair chave do IMDB - OK
        // Trocar endpoint para MostPopularMovies - OK
        // Saída visualmente melhorada no terminal - OK
        // Usar Jackson/Gson — continuar como List de Map - OK
        // TODO: Método para usuário dar avaliação no filme

        // Desafios Aula 2
        // TODO: usar imagem do Poster
        // TODO: centralizar texto
        // TODO: colocar texto customizado pela avaliação
        // TODO: colocar foto com joinha
        // TODO: fazer outline da imagem
        // TODO: aplicar filtros diversos (contorno do personagem?)

        // Aula 3
        // TODO: usar record
        // TODO: usar exceção customizada
        // TODO: usar outras fontes (Marvel?)

    }

    private static String converteEmEstrelas(String imDbRating) {
        if (Util.stringVazia(imDbRating)) {
            return "NA";
        }
        double avaliacao = Double.parseDouble(imDbRating);
        int numeroDeEstrelas = (int) Math.round(avaliacao / 2);
        return "\u2B50".repeat(numeroDeEstrelas);
    }

}
