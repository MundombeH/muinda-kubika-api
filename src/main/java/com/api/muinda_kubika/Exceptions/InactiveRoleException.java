package com.api.muinda_kubika.Exceptions;

public class InactiveRoleException extends RuntimeException {

    public InactiveRoleException() {
        super("O usuário possui roles inativas ou não tem permissão para esta ação.");
    }

    public InactiveRoleException(String message) {
        super(message);
    }

}
