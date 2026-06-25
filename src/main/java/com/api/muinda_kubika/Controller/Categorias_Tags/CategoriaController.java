package com.api.muinda_kubika.Controller.Categorias_Tags;

import com.api.muinda_kubika.DTO.Categorias.CategoriaRequestDto;
import com.api.muinda_kubika.DTO.Categorias.CategroiaRepsonseDto;
import com.api.muinda_kubika.Service.Categorias_Tags.CategoriaService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("categoria")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping("")
    public ResponseEntity<List<CategroiaRepsonseDto>> getAll() {
        return ResponseEntity.ok(categoriaService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategroiaRepsonseDto> getOne(@PathVariable UUID id) {
        return ResponseEntity.ok(categoriaService.getOne(id));
    }

    @PreAuthorize(
        "@roleChecker.hasActiveRole(authentication, 'ROLE_ADMIN') and @roleChecker.hasAuthority(authentication, 'USUARIO_GERIR')"
    )
    @PostMapping("")
    public ResponseEntity<CategroiaRepsonseDto> post(
        @RequestBody @Valid CategoriaRequestDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.create(dto));
    }

    @PreAuthorize(
        "@roleChecker.hasActiveRole(authentication, 'ROLE_ADMIN') and @roleChecker.hasAuthority(authentication, 'USUARIO_GERIR')"
    )
    @PutMapping("/{id}")
    public ResponseEntity<CategroiaRepsonseDto> put(
        @PathVariable UUID id,
        @RequestBody @Valid CategoriaRequestDto dto
    ) {
        return ResponseEntity.ok(categoriaService.update(id, dto));
    }

    @PreAuthorize(
        "@roleChecker.hasActiveRole(authentication, 'ROLE_ADMIN') and @roleChecker.hasAuthority(authentication, 'USUARIO_GERIR')"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        categoriaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
