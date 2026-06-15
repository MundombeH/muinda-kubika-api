package com.api.muinda_kubika.Enums;

public enum StatusDocumentoEnum {
    DRAFT("Draft"),              // upload feito
    PROCESSANDO_IA("Prosseçando utilizando IA"),     // IA a trabalhar
    AGUARDANDO_CONFIRMACAO_USUARIO("Aguardando aprovação do usuario"), // IA terminou
    PENDENTE_REVISAO_ADMIN("Pendente em revisão pelo admin"),         // usuário confirmou
    APROVADO("Aprivado"),
    REJEITADO("Rejeitado"),
    PUBLICADO("Publiacdo");

    private final  String status;
    StatusDocumentoEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
