package com.api.muinda_kubika.Controller.Files;

import com.api.muinda_kubika.DTO.Files.AnalizeIA.ConfirmarAnaliseRequestDto;
import com.api.muinda_kubika.DTO.Files.AnalizeIA.DocumentoIAGitUrlRequestDto;
import com.api.muinda_kubika.DTO.Files.AnalizeIA.DocumentoIAMetadadosResponseDto;
import com.api.muinda_kubika.DTO.Files.AnalizeIA.DocumentoIAResultadoRequestDto;
import com.api.muinda_kubika.Enums.OrigemAnaliseIAEnum;
import com.api.muinda_kubika.Enums.StatusDocumentoEnum;
import com.api.muinda_kubika.Repository.Files.DocumentoRepository;
import com.api.muinda_kubika.Service.Files.AnalizeIaService;
import com.api.muinda_kubika.Service.Files.FicheiroService;
import com.api.muinda_kubika.model.Files.DocumentosModel;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("ia")
public class DocumentoIAController {

    private static final Logger log = LoggerFactory.getLogger(DocumentoIAController.class);

    private final DocumentoRepository documentoRepository;
    private final AnalizeIaService analizeIaService;
    private final FicheiroService ficheiroService;
    private final String workerToken;

    public DocumentoIAController(
        DocumentoRepository documentoRepository,
        AnalizeIaService analizeIaService,
        FicheiroService ficheiroService,
        @Value("${ia.worker.token}") String workerToken
    ) {
        this.documentoRepository = documentoRepository;
        this.analizeIaService = analizeIaService;
        this.ficheiroService = ficheiroService;
        this.workerToken = workerToken;
    }

    @GetMapping("/documentos/{documentoId}/metadados")
    public ResponseEntity<
        List<DocumentoIAMetadadosResponseDto>
    > buscarMetadados(@PathVariable UUID documentoId) {
        return ResponseEntity.ok(
            analizeIaService.buscarMetadadosPorDocumentoId(documentoId)
        );
    }

    @GetMapping("/documentos/{documentoId}/metadados/repositorio")
    public ResponseEntity<
        DocumentoIAMetadadosResponseDto
    > buscarMetadadosRepositorio(@PathVariable UUID documentoId) {
        return analizeIaService
            .buscarUltimoMetadadoPorOrigem(
                documentoId,
                OrigemAnaliseIAEnum.REPOSITORIO
            )
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/documentos/{documentoId}/metadados/zip")
    public ResponseEntity<DocumentoIAMetadadosResponseDto> buscarMetadadosZip(
        @PathVariable UUID documentoId
    ) {
        if (!ficheiroService.documentoTemArquivoZip(documentoId)) {
            return ResponseEntity.notFound().build();
        }

        return analizeIaService
            .buscarUltimoMetadadoPorOrigem(
                documentoId,
                OrigemAnaliseIAEnum.FICHEIRO
            )
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/documentos/{documentoId}/git-url")
    public ResponseEntity<String> submeterRepositorioGithub(
        @PathVariable UUID documentoId,
        @RequestBody @Valid DocumentoIAGitUrlRequestDto dto
    ) {
        ficheiroService.submeterRepositorioGithub(dto.getGitUrl(), dto.getTecnologiasUsadas(), documentoId);
        return ResponseEntity.ok("Repositório enviado para análise");
    }

    @PostMapping("/resultado")
    public ResponseEntity<String> receberResultado(@RequestHeader("X-IA-Worker-Token") String token,
        @RequestBody @Valid DocumentoIAResultadoRequestDto dto
    ) {
        if (
            workerToken == null ||
            workerToken.isBlank() ||
            !workerToken.equals(token)
        ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                "Token inválido"
            );
        }

        UUID documentoId = dto.getDocumentoId();
        DocumentosModel documento = documentoRepository
            .findById(documentoId)
            .orElse(null);

        if (documento == null) {
            log.warn("Documento nao encontrado para id={}", documentoId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                "Documento não encontrado: " + documentoId
            );
        }

        try {
            analizeIaService.salvarAnalise(documento, dto);
            documento.setStatus(StatusDocumentoEnum.AGUARDANDO_CONFIRMACAO_USUARIO);
            documentoRepository.save(documento);
        } catch (Exception ex) {
            log.error("Erro ao salvar analise IA para documento {}: {}", documentoId, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                "Erro ao salvar análise: " + ex.getMessage()
            );
        }

        return ResponseEntity.ok("Analise recebida");
    }

    @PostMapping("/documentos/{documentoId}/confirmar")
    public ResponseEntity<String> confirmarAnalise(
        @PathVariable UUID documentoId,
        @RequestBody(required = false) ConfirmarAnaliseRequestDto request
    ) {
        DocumentosModel documento = documentoRepository
            .findById(documentoId)
            .orElse(null);

        if (documento == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                "Documento não encontrado: " + documentoId
            );
        }

        analizeIaService.confirmarAnalisePendente(documentoId, request);
        return ResponseEntity.ok("Analise confirmada");
    }
}
