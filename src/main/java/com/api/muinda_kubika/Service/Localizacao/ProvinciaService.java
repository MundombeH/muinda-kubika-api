package com.api.muinda_kubika.Service.Localizacao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import com.api.muinda_kubika.DTO.Localizacao.PaisResumoDTO;
import com.api.muinda_kubika.DTO.Localizacao.ProvinciasRequestDTO;
import com.api.muinda_kubika.DTO.Localizacao.ProvinciasResponseDTO;
import com.api.muinda_kubika.Repository.Localizacao.PaisesRepository;
import com.api.muinda_kubika.Repository.Localizacao.ProvinciasRepository;
import com.api.muinda_kubika.model.Localizacao.PaisModel;
import com.api.muinda_kubika.model.Localizacao.ProvinciasModel;
import org.springframework.stereotype.Service;


@Service
public class ProvinciaService {

    private final ProvinciasRepository provinciasRepository;
    private final PaisesRepository paisesRepository;

    ProvinciaService(ProvinciasRepository provinciasRepository, PaisesRepository paisesRepository) {
        this.provinciasRepository = provinciasRepository;
        this.paisesRepository = paisesRepository;
    }

    public List<ProvinciasResponseDTO> getAllProvincias() {
        List<ProvinciasModel> provincias = provinciasRepository.findAll();
        return provincias.stream().map(this::mapToProvinciaDTO).collect(Collectors.toList());
    }

    public ProvinciasResponseDTO getOneProvincia(UUID id) {
        ProvinciasModel provinciasModel = provinciasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Província não encontrada ou inativa"));

        return mapToProvinciaDTO(provinciasModel);
    }

    public List<ProvinciasResponseDTO> getProvinciaByPais(UUID id) {
        paisesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pais não encontrado ou inativo"));
        return provinciasRepository.findByPaisId(id).stream().map(this::mapToProvinciaDTO)
                .collect(Collectors.toList());
    }

    public ProvinciasResponseDTO createProvincia(ProvinciasRequestDTO provinciasRequestDTO) throws Exception {

        Optional<ProvinciasModel> provincia = provinciasRepository.findByDescricao(provinciasRequestDTO.getDescricao());
        if (provincia.isPresent()) {
            throw new Exception("Erro esta provincia já existe no sistema!");
        }
        PaisModel findPais = paisesRepository.findById(provinciasRequestDTO.getPaisId())
                .orElseThrow(() -> new RuntimeException("Pais não encontrado ou inativo"));

        if (provinciasRequestDTO.getDescricao() == null) {
            throw new Exception("O Nome da  provincia não deve ser nulo");

        }
        if (provinciasRequestDTO.getDescricao().isEmpty()) {
            throw new Exception("O Nome da provincia não deve ser vazio");

        }
        if (provinciasRequestDTO.getDescricao().startsWith(" ")) {
            throw new Exception("O Nome da provincia não deve começar com espaço em branco");
        }

        ProvinciasModel provinciasModel = new ProvinciasModel();
        provinciasModel.setDescricao(provinciasRequestDTO.getDescricao());
        provinciasModel.setPais(findPais);

        ProvinciasModel provinciasModelSave = provinciasRepository.save(provinciasModel);

        return mapToProvinciaDTO(provinciasModelSave);
    }

    public ProvinciasResponseDTO UpdateProvincia(UUID id, ProvinciasRequestDTO provinciasRequestDTO) throws Exception {
        PaisModel findPais = paisesRepository.findById(provinciasRequestDTO.getPaisId())
                .orElseThrow(() -> new RuntimeException("País não encontrado ou inativo"));
        Optional<ProvinciasModel> provincia = provinciasRepository.findByDescricao(provinciasRequestDTO.getDescricao());
        if (provincia.isPresent()) {
            throw new Exception("Erro esta provincia já existe no sistema!");
        }

        if (provinciasRequestDTO.getDescricao() == null) {
            throw new Exception("O Nome da  provincia não deve ser nulo");

        }
        if (provinciasRequestDTO.getDescricao().isEmpty()) {
            throw new Exception("O Nome da provincia não deve ser vazio");

        }
        if (provinciasRequestDTO.getDescricao().startsWith(" ")) {
            throw new Exception("O Nome da provincia não deve começar com espaço em branco");
        }
        ProvinciasModel findProvincia = provinciasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("País não encontrado ou inativo"));

        findProvincia.setDescricao(provinciasRequestDTO.getDescricao());
        findProvincia.setPais(findPais);

        ProvinciasModel provinciasModelSave = provinciasRepository.save(findProvincia);

        return mapToProvinciaDTO(provinciasModelSave);

    }

    public void deleteProvincia(UUID id) {
        ProvinciasModel findProvincia = provinciasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("País não encontrado ou inativo"));

        findProvincia.setIsActive(false);
        findProvincia.setUpdatedAt(LocalDateTime.now());
        provinciasRepository.delete(findProvincia);
    }

    private ProvinciasResponseDTO mapToProvinciaDTO(ProvinciasModel provincia) {
        ProvinciasResponseDTO responseDto = new ProvinciasResponseDTO();
        responseDto.setId(provincia.getId());
        responseDto.setDescricao(provincia.getDescricao());

        PaisModel pais = provincia.getPais();
        if (pais != null) {
            PaisResumoDTO paisDto = new PaisResumoDTO();
            paisDto.setId(pais.getId());
            paisDto.setDescricao(pais.getDescricao());
            paisDto.setActive(pais.getIsActive());
            responseDto.setPais(paisDto);
        }
        responseDto.setUpdatedAt(provincia.getUpdatedAt());
        responseDto.setCreatedAt(provincia.getCreatedAt());
        responseDto.setIsActive(provincia.getIsActive());
        return responseDto;
    }

}
