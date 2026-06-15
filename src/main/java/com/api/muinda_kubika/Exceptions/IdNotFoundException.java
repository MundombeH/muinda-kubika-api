package com.api.muinda_kubika.Exceptions;

public class IdNotFoundException  extends RuntimeException {
    public IdNotFoundException() {
        super("ID não encontrado.");
    }

}
