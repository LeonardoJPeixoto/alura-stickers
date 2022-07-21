package com.ljpeixoto.alura.stickers.imdb;

import com.ljpeixoto.alura.stickers.interfaces.FonteImagens;
import com.ljpeixoto.alura.stickers.nasa.FonteImagensNasa;

public enum FontesImagens {
    IMDB_TOP250(new FonteImagensImdbTop250()),
    IMDB_MOST_POPULAR(new FonteImagensImdbMostPopular()),
    NASA(new FonteImagensNasa());

    private final FonteImagens fonteImagens;

    FontesImagens(FonteImagens fonteImagens) {
        this.fonteImagens = fonteImagens;
    }

    public FonteImagens getFonteImagens() {
        return fonteImagens;
    }
}
