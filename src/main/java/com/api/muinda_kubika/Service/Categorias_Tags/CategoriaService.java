package com.api.muinda_kubika.Service.Categorias_Tags;

import com.api.muinda_kubika.DTO.Categorias.CategroiaRepsonseDto;
import com.api.muinda_kubika.Repository.Categorias_Tags.CategoriasRepository;
import com.api.muinda_kubika.model.Categorias_Tags.CategoriasModel;
import java.util.List;
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
