package com.ljpeixoto.alura.stickers.imdb.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ljpeixoto.alura.stickers.imdb.models.Filme;
import com.ljpeixoto.alura.stickers.imdb.models.Poster;
import com.ljpeixoto.alura.stickers.util.ClienteHttp;
import com.ljpeixoto.alura.stickers.util.exception.StickerException;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static com.ljpeixoto.alura.stickers.util.Util.stringVazia;
import static java.util.Arrays.asList;

public class ImdbService {

    private final ClienteHttp clienteHttp = new ClienteHttp();
    private static final String URL_BASE;
    private static final String CHAVE;

    static {
        try (InputStream input = ImdbService.class.getResourceAsStream("/resources/config.properties")) {
            if (input == null) {
                throw new StickerException("Arquivo de config.properties não encontrado.");
            }

            Properties prop = new Properties();
            prop.load(input);

            String urlBase = prop.getProperty("IMDB_URL");
            String chave = prop.getProperty("IMDB_CHAVE_API");

            if (stringVazia(urlBase) || stringVazia(chave)) {
                throw new StickerException("Url e/ou chave do IMDB não configurados corretamente em config.properties.");
            }

            if (urlBase.contains("mock")) {
                System.out.println("AVISO: usando URL mockada.");
            }

            URL_BASE = urlBase;
            CHAVE = chave;
        } catch (Exception e) {
            throw new StickerException(e);
        }

    }


    public List<Filme> obtemTop250Movies() throws IOException {
        return this.obtemListaFilmes("Top250Movies");
    }

    public List<Filme> obtemMostPopularMovies() throws IOException {
        return this.obtemListaFilmes("MostPopularMovies");
    }

    private List<Filme> obtemListaFilmes(String servico) throws IOException {
        String body = chamaServico(servico);
        TypeReference<RespostaComListaFilmes> typeRef = new TypeReference<>() {};
        RespostaComListaFilmes resposta = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .readValue(body, typeRef);
        return resposta.getItems();
    }

    public List<Poster> obtemListaPosteres(String id) throws IOException {
        String body = chamaServico("Posters", id);
        TypeReference<RespostaComListaPosteres> typeRef = new TypeReference<>() {};
        RespostaComListaPosteres resposta = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .readValue(body, typeRef);
        return resposta.getPosters();
    }

    private String chamaServico(String servico) {
        return this.chamaServico(servico, (String) null);
    }

    private String chamaServico(String servico, String ...restParams) {

        List<String> listaComponentes = URL_BASE.contains("mock") ?
                asList(URL_BASE, CHAVE)
                :
                asList(URL_BASE, servico, CHAVE);
        listaComponentes = new ArrayList<>(listaComponentes);
        var parametros = Arrays.stream(restParams).filter(Objects::nonNull).filter(s -> !s.isBlank()).toList();
        if (!parametros.isEmpty()) {
            listaComponentes.addAll(asList(restParams));
        }
        return clienteHttp.buscaDados(String.join("/", listaComponentes));
    }

    public String getUrl() {
        return URL_BASE + "/" + CHAVE;
    }

    private static class RespostaComListaFilmes {
        private List<Filme> items;
        private String errorMessage;

        public List<Filme> getItems() {
            return items;
        }

        public void setItems(List<Filme> items) {
            this.items = items;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }

    private static class RespostaComListaPosteres {
        private List<Poster> posters;

        public List<Poster> getPosters() {
            return posters;
        }

        public void setPosters(List<Poster> posters) {
            this.posters = posters;
        }
    }

    public boolean isMockado() {
        return URL_BASE.contains("mock");
    }
}
