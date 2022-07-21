package com.ljpeixoto.alura.stickers.imdb;

import com.ljpeixoto.alura.stickers.imdb.models.Filme;
import com.ljpeixoto.alura.stickers.imdb.service.ImdbService;
import com.ljpeixoto.alura.stickers.interfaces.FonteImagens;
import com.ljpeixoto.alura.stickers.model.Conteudo;
import com.ljpeixoto.alura.stickers.util.exception.StickerException;

import java.io.IOException;
import java.util.List;

public abstract class FonteImagensImdb implements FonteImagens {

    FonteImagensImdb() {}

    protected abstract ImdbService getImdbService();

    protected abstract List<Filme> getListaFilmes() throws IOException;

    @Override
    public List<Conteudo> buscaListaConteudo() throws IOException {

        List<Filme> filmes = this.getListaFilmes();

        if (!getImdbService().isMockado()) {
            filmes.forEach(filme -> {
                try {
                    String id = filme.getId();
                    getImdbService().obtemListaPosteres(id).stream()
                            .findFirst()
                            .ifPresent(poster -> filme.setImage(poster.getLink()));
                } catch (Exception e) {
                    throw new StickerException(e);
                }
            });
        }

        return this.getListaFilmes().stream()
                .map(this::converteFileEmConteudo)
                .toList();
    }

    private Conteudo converteFileEmConteudo(Filme filme) {
        return new Conteudo(filme.getTitle(), filme.getImage(), filme.getImDbRating());
    }

}
