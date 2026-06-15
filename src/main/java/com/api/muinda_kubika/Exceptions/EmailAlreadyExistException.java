package com.api.muinda_kubika.Exceptions;

public class EmailAlreadyExistException extends RuntimeException {
    public EmailAlreadyExistException(String email) {
        super("Este Email: "+email+" já existe no sistema.");
    }

}
