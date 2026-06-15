package com.api.muinda_kubika.Exceptions;

import java.util.UUID;

public class ProfileAlreadyRejectedException extends RuntimeException {
    public ProfileAlreadyRejectedException(UUID approvalId) {
        super("Pedido de aprovação já foi rejeitado: " + approvalId);
    }
}
