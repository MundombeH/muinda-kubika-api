package com.api.muinda_kubika.Controller.Instituicao;

import com.api.muinda_kubika.DTO.Instituicoes.InstituicaoPathDto;
import com.api.muinda_kubika.DTO.Instituicoes.InstituicoesRequestDto;
import com.api.muinda_kubika.DTO.Instituicoes.InstituicoesResponseDto;
import com.api.muinda_kubika.Enums.TipoInstituicaoEnum;
import com.api.muinda_kubika.Service.Instituicoes.InstituicoesService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("instituicao")
public class InstituicaoController {

    private final InstituicoesService instituicoesService;

    public InstituicaoController(InstituicoesService instituicoesService) {
        this.instituicoesService = instituicoesService;
    }

//       @PreAuthorize("@roleChecker.hasActiveRole(authentication, 'ROLE_USUARIO')")
    @GetMapping("")
    public ResponseEntity<List<InstituicoesResponseDto>> getAll(
        @RequestParam(value = "id", required = false) UUID id,
        @RequestParam(required = false) UUID bairroId,
        @RequestParam(required = false) UUID municipioId,
        @RequestParam(required = false) UUID provinciaId,
        @RequestParam(required = false) UUID paisId,
        @RequestParam(required = false) TipoInstituicaoEnum tipo
    ) {
        return ResponseEntity.ok().body(
            instituicoesService.getAllInstituicoes(
                id,
                bairroId,
                municipioId,
                provinciaId,
                paisId,
                tipo
            )
        );
    }

//    @PreAuthorize(
//        "@roleChecker.hasAnyActiveRole(authentication, 'ROLE_ADMIN', 'ROLE_ADMIN_INSTITUICAO') and @roleChecker.hasAuthority(authentication, 'INSTITUICAO_GERIR')"
//    )
    @PostMapping("")
    public ResponseEntity<InstituicoesResponseDto> post(
        @RequestBody @Valid InstituicoesRequestDto dto
    ) {
        return ResponseEntity.ok().body(
            instituicoesService.createInstituicao(dto)
        );
    }

    @PreAuthorize(
        "@roleChecker.hasAnyActiveRole(authentication, 'ROLE_ADMIN_INSTITUICAO', 'ROLE_ADMIN') and @roleChecker.hasAuthority(authentication, 'INSTITUICAO_GERIR')"
    )
    @PatchMapping("/{id}")
    public ResponseEntity<InstituicoesResponseDto> path(
        @RequestBody @Valid InstituicaoPathDto dto,
        @PathVariable UUID id
    ) {
        return ResponseEntity.ok().body(
            instituicoesService.updateInstituicao(id, dto)
        );
    }

    @PreAuthorize(
        "@roleChecker.hasActiveRole(authentication, 'ROLE_ADMIN') and @roleChecker.hasAuthority(authentication, 'INSTITUICAO_GERIR')"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        instituicoesService.deleteInstituicao(id);
        return ResponseEntity.noContent().build();
    }
}
