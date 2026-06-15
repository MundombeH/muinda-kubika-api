package com.api.muinda_kubika.Controller.Files;

import com.api.muinda_kubika.DTO.Files.Documentos.DocumentosRequestDto;
import com.api.muinda_kubika.DTO.Files.Documentos.DocumentosResponseDto;
import com.api.muinda_kubika.Service.Files.DocumentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("documento")
public class DocumentoController {
    private final DocumentoService documentoService;

    public DocumentoController(DocumentoService documentoService) {
        this.documentoService = documentoService;
    }

    @GetMapping("")
    public ResponseEntity<List<DocumentosResponseDto>> getAll() {
        return ResponseEntity.ok(documentoService.getAllDocumentos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentosResponseDto> getOne(UUID id) {

        return ResponseEntity.ok(documentoService.getOneDocumento(id));
    }

    @PostMapping("")
    public ResponseEntity<DocumentosResponseDto> post(@RequestBody  @Valid DocumentosRequestDto dto) {
        UUID idTeste = UUID.fromString("9d80ba6c-ecd9-44f5-aac6-c86415ecd982");
        return ResponseEntity.status(HttpStatus.CREATED).body(documentoService.createDocumento(dto,idTeste));

    }
}
