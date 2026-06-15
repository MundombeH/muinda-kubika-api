package com.api.muinda_kubika.Exceptions;

public class InsufficientApprovalPermissionException extends RuntimeException {
    public InsufficientApprovalPermissionException() {
        super("Sem permissão para aprovar ou rejeitar este perfil");
    }

    public InsufficientApprovalPermissionException(String message) {
        super(message);
    }
}
