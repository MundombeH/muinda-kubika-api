package com.api.muinda_kubika.Service.Categorias_Tags;

import com.api.muinda_kubika.DTO.Categorias.CategoriaRequestDto;
import com.api.muinda_kubika.DTO.Categorias.CategroiaRepsonseDto;
import com.api.muinda_kubika.Repository.Categorias_Tags.CategoriasRepository;
import com.api.muinda_kubika.model.Categorias_Tags.CategoriasModel;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {

    private final CategoriasRepository categoriasRepository;

    public CategoriaService(CategoriasRepository categoriasRepository) {
        this.categoriasRepository = categoriasRepository;
    }

    public List<CategroiaRepsonseDto> getAll() {
        return categoriasRepository.findByIsActiveTrue()
            .stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    public CategroiaRepsonseDto getOne(UUID id) {
        CategoriasModel model = categoriasRepository.findByIdAndIsActiveTrue(id)
            .orElseThrow(() -> new RuntimeException("Categoria nao encontrada"));
        return mapToDto(model);
    }

    public CategroiaRepsonseDto create(CategoriaRequestDto dto) {
        CategoriasModel model = new CategoriasModel();
        model.setDescricao(dto.getDescricao());
        return mapToDto(categoriasRepository.save(model));
    }

    public CategroiaRepsonseDto update(UUID id, CategoriaRequestDto dto) {
        CategoriasModel model = categoriasRepository.findByIdAndIsActiveTrue(id)
            .orElseThrow(() -> new RuntimeException("Categoria nao encontrada"));
        model.setDescricao(dto.getDescricao());
        return mapToDto(categoriasRepository.save(model));
    }

    public void delete(UUID id) {
        CategoriasModel model = categoriasRepository.findByIdAndIsActiveTrue(id)
            .orElseThrow(() -> new RuntimeException("Categoria nao encontrada"));
        model.setIsActive(false);
        categoriasRepository.save(model);
    }

    private CategroiaRepsonseDto mapToDto(CategoriasModel model) {
        CategroiaRepsonseDto dto = new CategroiaRepsonseDto();
        dto.setId(model.getId());
        dto.setDescricao(model.getDescricao());
        dto.setCreatedAt(model.getCreatedAt());
        dto.setUpdatedAt(model.getUpdatedAt());
        dto.setIsActive(model.getIsActive());
        return dto;
    }
}
