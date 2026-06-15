package com.api.muinda_kubika.Controller.Files;

import com.api.muinda_kubika.DTO.Files.AnalizeIA.DocumentoIAMetadadosResponseDto;
import com.api.muinda_kubika.DTO.Files.AnalizeIA.DocumentoIAResultadoRequestDto;
import com.api.muinda_kubika.Enums.StatusDocumentoEnum;
import com.api.muinda_kubika.Repository.Files.DocumentoRepository;
import com.api.muinda_kubika.Service.Files.AnalizeIaService;
import com.api.muinda_kubika.model.Files.DocumentosModel;
import jakarta.validation.Valid;
import java.util.HashSet;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("ia")
public class DocumentoIAController {

    private final DocumentoRepository documentoRepository;
    private final AnalizeIaService analizeIaService;
    private final String workerToken;

    public DocumentoIAController(
        DocumentoRepository documentoRepository,
        AnalizeIaService analizeIaService,
        @Value("${ia.worker.token}") String workerToken
    ) {
        this.documentoRepository = documentoRepository;
        this.analizeIaService = analizeIaService;
        this.workerToken = workerToken;
    }

    @GetMapping("/documentos/{documentoId}/metadados")
    public ResponseEntity<DocumentoIAMetadadosResponseDto> buscarMetadados(
        @PathVariable UUID documentoId
    ) {
        return ResponseEntity.ok(
            analizeIaService.buscarMetadadosPorDocumentoId(documentoId)
        );
    }

    @PostMapping("/resultado")
    public ResponseEntity<String> receberResultado(
        @RequestHeader("X-IA-Worker-Token") String token,
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
            .orElseThrow(() ->
                new RuntimeException("Documento não encontrado")
            );

        if (dto.getTitulo() != null && !dto.getTitulo().isBlank()) {
            documento.setTitulo(dto.getTitulo());
        }
        if (dto.getResumo() != null && !dto.getResumo().isBlank()) {
            documento.setResumo(dto.getResumo());
        }
        if (dto.getAutores() != null && !dto.getAutores().isEmpty()) {
            documento.setAutores(new HashSet<>(dto.getAutores()));
        }

        analizeIaService.salvarAnalise(documento, dto);
        documento.setStatus(StatusDocumentoEnum.AGUARDANDO_CONFIRMACAO_USUARIO);
        documentoRepository.save(documento);

        return ResponseEntity.ok("Analise recebida");
    }
}
