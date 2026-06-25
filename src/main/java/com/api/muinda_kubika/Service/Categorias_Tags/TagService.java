package com.api.muinda_kubika.Service.Categorias_Tags;

import com.api.muinda_kubika.DTO.Tags.TagsRequestTo;
import com.api.muinda_kubika.DTO.Tags.TagsResponseDto;
import com.api.muinda_kubika.Repository.Categorias_Tags.TagsRepository;
import com.api.muinda_kubika.model.Categorias_Tags.TagsModel;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class TagService {

    private final TagsRepository tagsRepository;

    public TagService(TagsRepository tagsRepository) {
        this.tagsRepository = tagsRepository;
    }

    public List<TagsResponseDto> getAll() {
        return tagsRepository.findByIsActiveTrue()
            .stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    public TagsResponseDto getOne(UUID id) {
        TagsModel model = tagsRepository.findByIdAndIsActiveTrue(id)
            .orElseThrow(() -> new RuntimeException("Tag nao encontrada"));
        return mapToDto(model);
    }

    public TagsResponseDto create(TagsRequestTo dto) {
        TagsModel model = new TagsModel();
        model.setDescricao(dto.getDescricao());
        return mapToDto(tagsRepository.save(model));
    }

    public TagsResponseDto update(UUID id, TagsRequestTo dto) {
        TagsModel model = tagsRepository.findByIdAndIsActiveTrue(id)
            .orElseThrow(() -> new RuntimeException("Tag nao encontrada"));
        model.setDescricao(dto.getDescricao());
        return mapToDto(tagsRepository.save(model));
    }

    public void delete(UUID id) {
        TagsModel model = tagsRepository.findByIdAndIsActiveTrue(id)
            .orElseThrow(() -> new RuntimeException("Tag nao encontrada"));
        model.setIsActive(false);
        tagsRepository.save(model);
    }

    private TagsResponseDto mapToDto(TagsModel model) {
        TagsResponseDto dto = new TagsResponseDto();
        dto.setId(model.getId());
        dto.setDescricao(model.getDescricao());
        dto.setCreatedAt(model.getCreatedAt());
        dto.setUpdatedAt(model.getUpdatedAt());
        dto.setIsActive(model.getIsActive());
        return dto;
    }
}
