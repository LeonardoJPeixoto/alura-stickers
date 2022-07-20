package com.ljpeixoto.alura.stickers.extrator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ljpeixoto.alura.stickers.model.Conteudo;

import java.util.List;

public interface Extrator {
    List<Conteudo> extrai(String json) throws JsonProcessingException;
}
