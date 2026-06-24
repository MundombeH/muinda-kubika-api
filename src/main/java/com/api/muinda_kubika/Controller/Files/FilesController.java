package com.api.muinda_kubika.Controller.Files;

import com.api.muinda_kubika.DTO.Files.Ficheiros.FicheirosResponseDto;
import com.api.muinda_kubika.Service.Files.FicheiroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("upload")
public class FilesController {
    private final FicheiroService ficheiroService;

    public FilesController(FicheiroService ficheiroService) {
        this.ficheiroService = ficheiroService;
    }
    @PreAuthorize(
            "@roleChecker.hasActiveRole(authentication, 'ROLE_ADMIN') and @roleChecker.hasAuthority(authentication, 'USUARIO_GERIR')"
    )
    @GetMapping("")
    public ResponseEntity<List<FicheirosResponseDto>> getAll() {
        return ResponseEntity.ok(ficheiroService.getAllFileS());
    }


    @GetMapping("/{id}")
    public ResponseEntity<FicheirosResponseDto> getOne(@PathVariable  UUID id) {

        return ResponseEntity.ok(ficheiroService.getOneFile(id));
    }


    @PostMapping("")
    public ResponseEntity<FicheirosResponseDto> post(
            @RequestParam("file") MultipartFile file,
            @RequestParam("documento") UUID documento) throws IOException {

        return ResponseEntity.status(HttpStatus.CREATED).body(ficheiroService.createFile(file,documento));
    }

    @PostMapping("/capa")
    public ResponseEntity<Map<String, String>> uploadCover(
            @RequestParam("file") MultipartFile file) throws IOException {

        String url = ficheiroService.uploadCoverImage(file);
        return ResponseEntity.ok(Map.of("url", url));
    }


}
