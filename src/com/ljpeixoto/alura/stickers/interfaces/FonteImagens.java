package com.ljpeixoto.alura.stickers.interfaces;

import com.ljpeixoto.alura.stickers.model.Conteudo;
import com.ljpeixoto.alura.stickers.util.ClienteHttp;

import java.io.IOException;
import java.util.List;

public interface FonteImagens {

    ClienteHttp clienteHttp = new ClienteHttp();
    String getUrl();
    Extrator getExtrator();

    default List<Conteudo> buscaListaConteudo() throws IOException {
        String json = clienteHttp.buscaDados(getUrl());
        return getExtrator().extrai(json);
    }
}
