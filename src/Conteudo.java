import com.fasterxml.jackson.annotation.JsonAlias;

public class Conteudo {

    @JsonAlias("title")
    private final String titulo;
    @JsonAlias("image")
    private final String urlImagem;
    @JsonAlias("imDbRating")
    private final String avaliacao;

    public Conteudo(String titulo, String urlImagem) {
        this.titulo = titulo;
        this.urlImagem = urlImagem;
        this.avaliacao = null;
    }

    public Conteudo(String titulo, String urlImagem, String avaliacao) {
        this.titulo = titulo;
        this.urlImagem = urlImagem;
        this.avaliacao = avaliacao;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public String getAvaliacao() {
        return this.avaliacao;
    }
}
