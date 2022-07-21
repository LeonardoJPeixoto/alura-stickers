package com.ljpeixoto.alura.stickers.imdb;

import com.ljpeixoto.alura.stickers.imdb.models.Filme;
import com.ljpeixoto.alura.stickers.imdb.service.ImdbService;
import com.ljpeixoto.alura.stickers.interfaces.Extrator;

import java.io.IOException;
import java.util.List;

public class FonteImagensImdbMostPopular extends FonteImagensImdb {

    private final ImdbService imdbService = new ImdbService();
    @Override
    public String getUrl() {
        return imdbService.getUrl();
    }

    @Override
    public ImdbService getImdbService() {
        return imdbService;
    }

    @Override
    public Extrator getExtrator() {
        return new ExtratorImdb();
    }

    @Override
    protected List<Filme> getListaFilmes() throws IOException {
        return imdbService.obtemMostPopularMovies();
    }

}
