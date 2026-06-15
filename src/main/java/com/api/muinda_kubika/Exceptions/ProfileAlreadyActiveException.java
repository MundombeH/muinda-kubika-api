package com.api.muinda_kubika.Exceptions;

import java.util.UUID;

public class ProfileAlreadyActiveException extends RuntimeException {
    public ProfileAlreadyActiveException(UUID profileId) {
        super("Perfil já se encontra activo: " + profileId);
    }
}
