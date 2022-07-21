package com.ljpeixoto.alura.stickers.nasa;

import com.ljpeixoto.alura.stickers.interfaces.Extrator;
import com.ljpeixoto.alura.stickers.interfaces.FonteImagens;

public class FonteImagensNasa implements FonteImagens {
    @Override
    public String getUrl() {
        return "https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY&count=5";
    }

    @Override
    public Extrator getExtrator() {
        return new ExtratorNasa();
    }
}
