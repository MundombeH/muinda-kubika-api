package com.api.muinda_kubika.Controller.Localizacao;

import java.util.List;
import java.util.UUID;
import com.api.muinda_kubika.DTO.Localizacao.PaisesRequestDTO;
import com.api.muinda_kubika.DTO.Localizacao.PaisesResponseDTO;
import com.api.muinda_kubika.Service.Localizacao.PaisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/localizacao/pais")
public class PaisesController {

    
    private final PaisService paisservice;

    public PaisesController(PaisService paisservice) {
        this.paisservice = paisservice;
    }

    @GetMapping("")
    public ResponseEntity<List<PaisesResponseDTO>> getPaises() {

        return ResponseEntity.status(200).body(paisservice.getAll());

    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getOne(@PathVariable UUID id) {
        PaisesResponseDTO paisDTO = paisservice.getOne(id);

        return ResponseEntity.status(201).body(paisDTO);

    }

    @PostMapping("")
    public ResponseEntity<Object> create(@RequestBody @Valid PaisesRequestDTO paisesRequestDTO){
        try{
            PaisesResponseDTO paisDTO = paisservice.create(paisesRequestDTO);
            return ResponseEntity.status(201).body(paisDTO);

        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable UUID id, @RequestBody @Valid PaisesRequestDTO paisesRequestDTO)
            {
       try {
           PaisesResponseDTO paisDTO = paisservice.put(id, paisesRequestDTO);
           return ResponseEntity.status(201).body(paisDTO);
       } catch (Exception e) {
           return ResponseEntity.badRequest().body(e.getMessage());
       }
    }

    @DeleteMapping("{id}")
        public ResponseEntity<Object> delete(@PathVariable UUID id) throws Exception{
        paisservice.delete(id);
        return ResponseEntity.status(200).body("País Eliminado com suucesso!");
    }  

}