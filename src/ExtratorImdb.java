import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ExtratorImdb implements Extrator {

    @Override
    public List<Conteudo> extrai(String json) throws JsonProcessingException {
        TypeReference<Resposta> typeRef = new TypeReference<>() {};
        Resposta resposta = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .readValue(json, typeRef);
        return resposta.getConteudos();
    }

    private static class Resposta {
        @JsonAlias("items")
        private List<ConteudoImdb> conteudos;
        private String errorMessage;

        public List<Conteudo> getConteudos() {
            return conteudos.stream().map(ConteudoImdb::asConteudo).collect(Collectors.toList());
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

        }
    }

}
