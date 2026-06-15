package com.api.muinda_kubika.Service.Files;

import com.api.muinda_kubika.Config.RabbitConfig;
import com.api.muinda_kubika.DTO.Messages.DocumentoIAMessage;
import com.api.muinda_kubika.Enums.StatusDocumentoEnum;
import com.api.muinda_kubika.Repository.Files.DocumentoRepository;
import com.api.muinda_kubika.model.Files.DocumentosModel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(
        name = "ia.consumer.enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class DocumentoIAConsumer {

    private final DocumentoRepository documentoRepository;
    private final DocumentoIAService iaService;
    private final AnalizeIaService analizeIaService;

    public DocumentoIAConsumer(
            DocumentoRepository documentoRepository,
            DocumentoIAService iaService,
            AnalizeIaService analizeIaService
    ) {
        this.documentoRepository = documentoRepository;
        this.iaService = iaService;
        this.analizeIaService = analizeIaService;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void processarIA(DocumentoIAMessage message) {
        System.out.println(
                "Processando IA do documento: " + message.documentoId()
        );

        DocumentosModel documento = documentoRepository
                .findById(message.documentoId())
                .orElseThrow();

        try {
            documento.setStatus(StatusDocumentoEnum.PROCESSANDO_IA);
            documentoRepository.save(documento);

            IAResponse response = iaService.analisarDocumento(
                    message.fileUrl()
            );

            documento.setTitulo(response.titulo());
            documento.setResumo(response.resumo());
            documento.setAutores(new java.util.HashSet<>(response.autores()));

            analizeIaService.salvarAnalise(documento, response);

            documento.setStatus(
                    StatusDocumentoEnum.AGUARDANDO_CONFIRMACAO_USUARIO
            );
        } catch (Exception e) {
            documento.setStatus(StatusDocumentoEnum.REJEITADO);
        }

        documentoRepository.save(documento);
    }
}
