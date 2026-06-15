package com.api.muinda_kubika.Exceptions;

import java.util.UUID;

public class ProfileAlreadyExistsException extends RuntimeException {
    public ProfileAlreadyExistsException(UUID userId) {
        super("Já existe um perfil para o utilizador: " + userId);
    }

    public ProfileAlreadyExistsException(String message) {
        super(message);
    }
}
