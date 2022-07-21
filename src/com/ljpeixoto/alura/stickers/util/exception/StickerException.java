package com.ljpeixoto.alura.stickers.util.exception;

public class StickerException extends RuntimeException {
    public StickerException() {
        super();
    }
    public StickerException(Exception e) {
        super(e);
    }

    public StickerException(String mensagem) {
        super(mensagem);
    }
}
