package com.api.muinda_kubika.Service.Files;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DocumentoIAService {

    public IAResponse analisarDocumento(String url) {
        // 1. baixar PDF
        // 2. extrair texto (PDFBox / Tika)
        // 3. enviar para OpenAI
        // 4. retornar resultado

        return new IAResponse(
            "Título gerado",
            "Resumo gerado",
            List.of("Autor IA"),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );
    }
}
