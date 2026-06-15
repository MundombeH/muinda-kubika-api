package com.api.muinda_kubika.Exceptions;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException() {
        super("Role não encontrada.");
    }

}
