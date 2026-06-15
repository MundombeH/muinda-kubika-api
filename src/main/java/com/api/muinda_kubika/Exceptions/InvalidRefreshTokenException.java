package com.api.muinda_kubika.Exceptions;

public class InvalidRefreshTokenException extends RuntimeException {
    public InvalidRefreshTokenException() {
        super("Refresh token inválido ou expirado.");
    }
}
