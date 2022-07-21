package com.ljpeixoto.alura.stickers.imdb;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ljpeixoto.alura.stickers.imdb.models.Poster;
import com.ljpeixoto.alura.stickers.imdb.service.ImdbService;
import com.ljpeixoto.alura.stickers.interfaces.Extrator;
import com.ljpeixoto.alura.stickers.model.Conteudo;
import com.ljpeixoto.alura.stickers.util.exception.StickerException;

import java.util.List;

public class ExtratorImdb implements Extrator {

    private final ImdbService imdbService = new ImdbService();

    @Override
    public List<Conteudo> extrai(String json) throws JsonProcessingException {
        TypeReference<Resposta> typeRef = new TypeReference<>() {};
        Resposta resposta = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .readValue(json, typeRef);
        return resposta.getConteudosImdb().stream()
                .map(conteudoImdb -> {
                    String id = conteudoImdb.getId();
                    try {
                        Poster poster = imdbService.obtemListaPosteres(id).stream().findFirst().orElse(null);
                        if (poster != null) {
                            return new Resposta.ConteudoImdb(
                                    conteudoImdb.getId(),
                                    conteudoImdb.getTitulo(),
                                    poster.getLink(),
                                    conteudoImdb.getAvaliacao()
                            );
                        }
                    } catch (Exception e) {
                        throw new StickerException(e);
                    }
                    return conteudoImdb;
                })
                .map(Resposta.ConteudoImdb::asConteudo)
                .toList();
    }

    private static class Resposta {
        @JsonAlias("items")
        private List<ConteudoImdb> conteudos;
        private String errorMessage;

        public List<ConteudoImdb> getConteudosImdb() {
            return conteudos;
        }
        public List<Conteudo> getConteudos() {
            return conteudos.stream().map(ConteudoImdb::asConteudo).toList();
        }

        public void setConteudos(List<ConteudoImdb> conteudos) {
            this.conteudos = conteudos;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        private static class ConteudoImdb {

            public ConteudoImdb() {}

            public ConteudoImdb(String id, String titulo, String urlImagem, String avaliacao) {
                this.id = id;
                this.titulo = titulo;
                this.urlImagem = urlImagem;
                this.avaliacao = avaliacao;
            }

            private String id;
            @JsonAlias("title")
            private String titulo;
            @JsonAlias("image")
            private String urlImagem;
            @JsonAlias("imDbRating")
            private String avaliacao;

            public String getTitulo() {
                return titulo;
            }

            public String getUrlImagem() {
                return urlImagem;
            }

            public String getAvaliacao() {
                return avaliacao;
            }

            public Conteudo asConteudo() {
                return new Conteudo(titulo, urlImagem, avaliacao);
            }

            public String getId() {
                return id;
            }

            public void setUrlImagem(String urlImagem) {
                this.urlImagem = urlImagem;
            }
        }
    }

}
