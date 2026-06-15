package com.api.muinda_kubika.Exceptions;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID id) {
        super("Usuário com o id: "+id+" não encontrado.");
    }

}
