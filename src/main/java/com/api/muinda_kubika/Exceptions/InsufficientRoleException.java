package com.api.muinda_kubika.Exceptions;

public class InsufficientRoleException extends RuntimeException{

        public InsufficientRoleException() {
        super("O usuário não possui permissões suficientes para realizar esta ação.");
    }

    public InsufficientRoleException(String message) {
        super(message);
    }
}
