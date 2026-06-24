package com.api.muinda_kubika.Controller.Categorias_Tags;

import com.api.muinda_kubika.DTO.Categorias.CategroiaRepsonseDto;
import com.api.muinda_kubika.Service.Categorias_Tags.CategoriaService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
