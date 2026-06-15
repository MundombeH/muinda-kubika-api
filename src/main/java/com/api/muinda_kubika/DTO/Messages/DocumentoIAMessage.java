package com.api.muinda_kubika.DTO.Messages;

import java.util.UUID;

public record DocumentoIAMessage(
    UUID documentoId,
    String fileUrl,
    String tipoDocumento,
    String fileName,
    String mimeType,
    Long tamanhoBytes,
    String formato
) {}
