package com.api.muinda_kubika.Controller.Localizacao;

import java.util.List;
import java.util.UUID;
import com.api.muinda_kubika.DTO.Localizacao.BairroRequestDTO;
import com.api.muinda_kubika.DTO.Localizacao.BairroResponseDTO;
import com.api.muinda_kubika.Service.Localizacao.BairroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("localizacao/bairro")
public class BairrosController {

    
    private final BairroService bairroService;

    public BairrosController(BairroService cidadesService) {
        this.bairroService = cidadesService;
    }

    @GetMapping("")
    public ResponseEntity<List<BairroResponseDTO>> get(
            @RequestParam(value = "municipioId", required = false) UUID municipioId) {

        // Se o ID foi passado, filtra. Se não, traz tudo.
        if (municipioId != null) {
            return ResponseEntity.ok(bairroService.getCidadeByMunicipio(municipioId));
        }

        return ResponseEntity.ok(bairroService.getAllcidades());
    }

    @PostMapping("")
    public ResponseEntity<Object> post(@RequestBody @Valid BairroRequestDTO cidadesRequestDTO) {
        try {
            return ResponseEntity.status(201).body(bairroService.createCidade(cidadesRequestDTO));
        } catch (Exception e) {
                        return ResponseEntity.badRequest().body(e.getMessage());

        }
        
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> put(@PathVariable UUID municipioId,
            @RequestBody @Valid BairroRequestDTO cidadesRequestDTO) {

        try {
            return ResponseEntity.status(201).body(bairroService.updateCidade(municipioId, cidadesRequestDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }
        @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable UUID municipioId) {
        try {
            bairroService.deleteCidade(municipioId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
