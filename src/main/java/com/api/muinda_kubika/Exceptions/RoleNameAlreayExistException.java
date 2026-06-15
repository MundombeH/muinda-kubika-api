package com.api.muinda_kubika.Exceptions;


public class RoleNameAlreayExistException extends RuntimeException {
    public RoleNameAlreayExistException(String nome) {
        super("Já existe uma role com esse nome: "+nome);
    }

}
