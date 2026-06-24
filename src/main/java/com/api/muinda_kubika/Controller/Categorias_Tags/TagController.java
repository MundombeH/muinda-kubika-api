package com.api.muinda_kubika.Controller.Categorias_Tags;

import com.api.muinda_kubika.DTO.Tags.TagsResponseDto;
import com.api.muinda_kubika.Service.Categorias_Tags.TagService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
