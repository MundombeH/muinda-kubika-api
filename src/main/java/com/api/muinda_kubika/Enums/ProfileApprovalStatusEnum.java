package com.api.muinda_kubika.Enums;

public enum ProfileApprovalStatusEnum {
    PENDING("Pendente"),
    ACTIVE("Activo"),
    REJECTED("Rejeitado");

    private final String status;

    ProfileApprovalStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
