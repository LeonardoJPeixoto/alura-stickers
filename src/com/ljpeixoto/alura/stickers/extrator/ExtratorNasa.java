package com.ljpeixoto.alura.stickers.extrator;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ljpeixoto.alura.stickers.model.Conteudo;

import java.util.List;

public class ExtratorNasa implements Extrator {

    @Override
    public List<Conteudo> extrai(String json) throws JsonProcessingException {
        TypeReference<List<ConteudoNasa>> typeRef = new TypeReference<>() {};
        List<ConteudoNasa> conteudosNasa = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .readValue(json, typeRef);
        return conteudosNasa.stream()
                .map(ConteudoNasa::asConteudo)
                .toList();
    }

    private static class ConteudoNasa {
        @JsonAlias("title")
        private String titulo;
        @JsonAlias("url")
        private String urlImagem;

        public String getTitulo() {
            return titulo;
        }

        public String getUrlImagem() {
            return urlImagem;
        }

        public Conteudo asConteudo() {
            return new Conteudo(titulo, urlImagem);
        }

    }
}
