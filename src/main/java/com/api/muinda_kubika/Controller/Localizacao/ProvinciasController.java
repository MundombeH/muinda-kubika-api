package com.api.muinda_kubika.Controller.Localizacao;

import java.util.List;
import java.util.UUID;
import com.api.muinda_kubika.DTO.Localizacao.ProvinciasRequestDTO;
import com.api.muinda_kubika.DTO.Localizacao.ProvinciasResponseDTO;
import com.api.muinda_kubika.Service.Localizacao.ProvinciaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("localizacao/provincia")
public class ProvinciasController {

    private final ProvinciaService provinciasService;

    public ProvinciasController(ProvinciaService provinciasService) {
        this.provinciasService = provinciasService;
    }

     @GetMapping("")
     public ResponseEntity<List<ProvinciasResponseDTO>> get(
             @RequestParam(value = "paisId", required = false) UUID paisId) {

         // Se o ID foi passado, filtra. Se não, traz tudo.
         if (paisId != null) {
             return ResponseEntity.status(200).body(provinciasService.getProvinciaByPais(paisId));
         }

         return ResponseEntity.status(200).body(provinciasService.getAllProvincias());
     }


    @GetMapping("{id}")
    public ResponseEntity<Object> getOne(@PathVariable UUID paisId) {
        ProvinciasResponseDTO provinciaDto = provinciasService.getOneProvincia(paisId);

        return ResponseEntity.status(200).body(provinciaDto);

    }
    @PostMapping("")
    public ResponseEntity<Object> create(@RequestBody @Valid ProvinciasRequestDTO provinciasRequestDTO) throws Exception {
       
       try {
           ProvinciasResponseDTO provinciaDto = provinciasService.createProvincia(provinciasRequestDTO);
           return ResponseEntity.status(201).body(provinciaDto);
       } catch (Exception e) {
           return ResponseEntity.badRequest().body(e.getMessage());
       }
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@PathVariable UUID id, @RequestBody @Valid ProvinciasRequestDTO provinciasRequestDTO)
            throws Exception {

       try {
           ProvinciasResponseDTO provinciaDto = provinciasService.UpdateProvincia(id, provinciasRequestDTO);
           return ResponseEntity.status(201).body(provinciaDto);

       }catch (Exception e) {
           return ResponseEntity.badRequest().body(e.getMessage());
       }
    }

    @DeleteMapping("{id}")
        public ResponseEntity<Object> delete(@PathVariable UUID id) throws Exception{
        try {
            provinciasService.deleteProvincia(id);
            return ResponseEntity.status(200).body("Provincia Eliminada com suucesso!");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
