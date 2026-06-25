package com.api.muinda_kubika.Controller.Files;

import com.api.muinda_kubika.DTO.Files.Documentos.DocumentoStatusUpdateDto;
import com.api.muinda_kubika.DTO.Files.Documentos.DocumentoUpdateRequestDto;
import com.api.muinda_kubika.DTO.Files.Documentos.DocumentosRequestDto;
import com.api.muinda_kubika.DTO.Files.Documentos.DocumentosResponseDto;
import com.api.muinda_kubika.Enums.StatusDocumentoEnum;
import com.api.muinda_kubika.Enums.TipoDocumentoEnum;
import com.api.muinda_kubika.Service.Files.DocumentoService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("documento")
public class DocumentoController {
    private final DocumentoService documentoService;

    public DocumentoController(DocumentoService documentoService) {
        this.documentoService = documentoService;
    }

    @GetMapping("")
    public ResponseEntity<List<DocumentosResponseDto>> getAll(
        @RequestParam(required = false) String titulo,
        @RequestParam(required = false) String autor,
        @RequestParam(required = false) UUID instituicaoId,
        @RequestParam(required = false) List<UUID> categoriaId,
        @RequestParam(required = false) List<UUID> tagId,
        @RequestParam(required = false) StatusDocumentoEnum status,
        @RequestParam(required = false) TipoDocumentoEnum tipo,
        @RequestParam(required = false) UUID usuarioId
    ) {
        boolean hasFilter = Stream.of(titulo, autor, instituicaoId, categoriaId, tagId, status, tipo, usuarioId)
            .anyMatch(v -> v != null && !v.toString().isBlank());
        if (!hasFilter) {
            return ResponseEntity.ok(documentoService.getAllDocumentos());
        }
        return ResponseEntity.ok(documentoService.buscarDocumentos(titulo, autor, instituicaoId, categoriaId, tagId, status, tipo, usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentosResponseDto> getOne(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(documentoService.getOneDocumento(id));
    }

    @PostMapping("")
    public ResponseEntity<DocumentosResponseDto> post(
        @RequestBody @Valid DocumentosRequestDto dto,
        Authentication auth
    ) {
        UUID userId = UUID.fromString(auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(documentoService.createDocumento(dto, userId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DocumentosResponseDto> update(
        @PathVariable UUID id,
        @RequestBody @Valid DocumentoUpdateRequestDto dto
    ) {
        return ResponseEntity.ok(documentoService.updateDocumento(id, dto));
    }

    @PatchMapping("/{id}/aprovar")
    public ResponseEntity<DocumentosResponseDto> approve(
        @PathVariable UUID id,
        Authentication auth
    ) {
        UUID adminId = UUID.fromString(auth.getName());
        return ResponseEntity.ok(documentoService.approveDocumento(id, adminId));
    }

    @PatchMapping("/{id}/rejeitar")
    public ResponseEntity<DocumentosResponseDto> reject(
        @PathVariable UUID id,
        @RequestBody(required = false) DocumentoStatusUpdateDto dto,
        Authentication auth
    ) {
        UUID adminId = UUID.fromString(auth.getName());
        String motivo = dto != null ? dto.getMotivoRejeicao() : null;
        return ResponseEntity.ok(documentoService.rejectDocumento(id, adminId, motivo));
    }

    @PatchMapping("/{id}/publicar")
    public ResponseEntity<DocumentosResponseDto> publish(@PathVariable UUID id) {
        return ResponseEntity.ok(documentoService.publishDocumento(id));
    }
}
