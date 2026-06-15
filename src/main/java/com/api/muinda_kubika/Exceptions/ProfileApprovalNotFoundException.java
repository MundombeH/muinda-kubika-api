package com.api.muinda_kubika.Exceptions;

import java.util.UUID;

public class ProfileApprovalNotFoundException extends RuntimeException {
    public ProfileApprovalNotFoundException(UUID id) {
        super("Pedido de aprovação não encontrado: " + id);
    }
}
