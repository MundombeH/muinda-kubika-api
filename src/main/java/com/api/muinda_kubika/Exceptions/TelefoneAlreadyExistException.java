package com.api.muinda_kubika.Exceptions;

public class TelefoneAlreadyExistException extends RuntimeException {
    public TelefoneAlreadyExistException(String telefone) {
        super(" este telefone"+telefone+" Telefone já existe no sistema.");
    }

}
