package com.api.muinda_kubika.Controller.Categorias_Tags;

import com.api.muinda_kubika.DTO.Tags.TagsRequestTo;
import com.api.muinda_kubika.DTO.Tags.TagsResponseDto;
import com.api.muinda_kubika.Service.Categorias_Tags.TagService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("tag")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("")
    public ResponseEntity<List<TagsResponseDto>> getAll() {
        return ResponseEntity.ok(tagService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagsResponseDto> getOne(@PathVariable UUID id) {
        return ResponseEntity.ok(tagService.getOne(id));
    }

    @PreAuthorize(
        "@roleChecker.hasActiveRole(authentication, 'ROLE_ADMIN') and @roleChecker.hasAuthority(authentication, 'USUARIO_GERIR')"
    )
    @PostMapping("")
    public ResponseEntity<TagsResponseDto> post(
        @RequestBody @Valid TagsRequestTo dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tagService.create(dto));
    }

    @PreAuthorize(
        "@roleChecker.hasActiveRole(authentication, 'ROLE_ADMIN') and @roleChecker.hasAuthority(authentication, 'USUARIO_GERIR')"
    )
    @PutMapping("/{id}")
    public ResponseEntity<TagsResponseDto> put(
        @PathVariable UUID id,
        @RequestBody @Valid TagsRequestTo dto
    ) {
        return ResponseEntity.ok(tagService.update(id, dto));
    }

    @PreAuthorize(
        "@roleChecker.hasActiveRole(authentication, 'ROLE_ADMIN') and @roleChecker.hasAuthority(authentication, 'USUARIO_GERIR')"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        tagService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
