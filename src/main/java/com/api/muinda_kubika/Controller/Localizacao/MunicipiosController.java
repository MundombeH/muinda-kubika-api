package com.api.muinda_kubika.Controller.Localizacao;

import java.util.List;
import java.util.UUID;
import com.api.muinda_kubika.DTO.Localizacao.MunicipiosRequestDTO;
import com.api.muinda_kubika.DTO.Localizacao.MunicipiosResponseDTO;
import com.api.muinda_kubika.Service.Localizacao.MunicipioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("localizacao/municipio")
public class MunicipiosController {

    private final MunicipioService municipiosService;

    public MunicipiosController(MunicipioService municipiosService) {
        this.municipiosService = municipiosService;
    }

    @GetMapping("")
    public ResponseEntity<List<MunicipiosResponseDTO>> get(
            @RequestParam(value = "provinciaId", required = false) UUID provinciaId) {

        // Se o ID foi passado, filtra. Se não, traz tudo.
        if (provinciaId != null) {
            return ResponseEntity.status(200).body(municipiosService.getMunicipioByProvincia(provinciaId));
        }

        return ResponseEntity.ok(municipiosService.getAllMunicipios());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getOne(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(municipiosService.getOneMunicipio(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("")
    public ResponseEntity<Object> post(@RequestBody @Valid MunicipiosRequestDTO municipiosRequestDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(municipiosService.createMunicipio(municipiosRequestDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> put(@PathVariable UUID id, @RequestBody MunicipiosRequestDTO dto) {
        try {
            return ResponseEntity.ok(municipiosService.updateMunicipio(id, dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable UUID id) {
        try {
            municipiosService.deleteMunicipio(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
