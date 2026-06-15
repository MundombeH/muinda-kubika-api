package com.api.muinda_kubika.Service.Localizacao;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import com.api.muinda_kubika.DTO.Localizacao.MunicipiosRequestDTO;
import com.api.muinda_kubika.DTO.Localizacao.MunicipiosResponseDTO;
import com.api.muinda_kubika.DTO.Localizacao.PaisResumoDTO;
import com.api.muinda_kubika.DTO.Localizacao.ProvinciasResumoDTO;
import com.api.muinda_kubika.Repository.Localizacao.MunicipiosRepository;
import com.api.muinda_kubika.Repository.Localizacao.ProvinciasRepository;
import com.api.muinda_kubika.model.Localizacao.MunicipioModel;
import com.api.muinda_kubika.model.Localizacao.PaisModel;
import com.api.muinda_kubika.model.Localizacao.ProvinciasModel;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class MunicipioService {

    private final MunicipiosRepository municipiosRepository;
    private final ProvinciasRepository provinciasRepository;

    public MunicipioService(MunicipiosRepository municipiosRepository, ProvinciasRepository provinciasRepository) {
        this.municipiosRepository = municipiosRepository;
        this.provinciasRepository = provinciasRepository;
    }

    public List<MunicipiosResponseDTO> getAllMunicipios() {
        return municipiosRepository.findAll().stream().map(this::mapToMunicipioDTO).collect(Collectors.toList());
    }

    public MunicipiosResponseDTO getOneMunicipio(UUID id) {
        MunicipioModel municipio = municipiosRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Município não encontrado ou inativo"));

        return mapToMunicipioDTO(municipio);
    }

    public List<MunicipiosResponseDTO> getMunicipioByProvincia(UUID id){
       provinciasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Província não encontrada ou inativa"));
        return municipiosRepository.findByProvinciaId(id).stream().map(this::mapToMunicipioDTO).collect(Collectors.toList());
    }

    @Transactional
    public MunicipiosResponseDTO createMunicipio(MunicipiosRequestDTO municipioRequestDto) throws Exception {
        ProvinciasModel findProvincia = provinciasRepository.findByIdAndIsActiveTrue(municipioRequestDto.getProvinciaId())
                .orElseThrow(() -> new RuntimeException("Provincia não encontrada ou inativa"));
        if (municipioRequestDto.getDescricao().startsWith(" ")) {
            throw new Exception("O Nome do Municipio não deve começar com espaço em branco");
        }
        MunicipioModel municipio = new MunicipioModel();
        municipio.setDescricao(municipioRequestDto.getDescricao());
        municipio.setProvincia(findProvincia);

        MunicipioModel saved = municipiosRepository.save(municipio);
        return mapToMunicipioDTO(saved);
    }

    @Transactional
    public MunicipiosResponseDTO updateMunicipio(UUID id, MunicipiosRequestDTO municipioRequestDto) {
        MunicipioModel municipio = municipiosRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Município não encontrado ou inativo"));

        if (municipioRequestDto.getDescricao() == null || municipioRequestDto.getDescricao().isBlank()) {
            throw new IllegalArgumentException("O nome do município não pode ser vazio ou em branco!");
        }

        ProvinciasModel provincia = provinciasRepository.findByIdAndIsActiveTrue(municipioRequestDto.getProvinciaId())
                .orElseThrow(() -> new RuntimeException("Província não encontrada ou inativa"));

        municipio.setDescricao(municipioRequestDto.getDescricao());
        municipio.setProvincia(provincia);
        MunicipioModel updated = municipiosRepository.save(municipio);

        return mapToMunicipioDTO(updated);
    }

    @Transactional
    public void deleteMunicipio(UUID id) throws Exception {
        MunicipioModel municipio = municipiosRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Município não encontrado"));

        municipio.setIsActive(false);
        municipiosRepository.delete(municipio);
    }

    private MunicipiosResponseDTO mapToMunicipioDTO(MunicipioModel municipio) {
        MunicipiosResponseDTO dtoMunicipio = new MunicipiosResponseDTO();

        var provinciaDto = getProvinciasResumoDTO(municipio);

        dtoMunicipio.setId(municipio.getId());
        dtoMunicipio.setDescricao(municipio.getDescricao());
        dtoMunicipio.setProvinciaId(provinciaDto);
        dtoMunicipio.setCreatedAt(municipio.getCreatedAt());
        dtoMunicipio.setUpdatedAt(municipio.getUpdatedAt());
        dtoMunicipio.setIsActive(municipio.getIsActive());

        return dtoMunicipio;
    }

    private ProvinciasResumoDTO getProvinciasResumoDTO(MunicipioModel municipio) {
        ProvinciasModel provincia = municipio.getProvincia();
        ProvinciasResumoDTO provinciaDto = new ProvinciasResumoDTO();

        PaisModel pais = municipio.getProvincia().getPais();
        PaisResumoDTO paisDto = new PaisResumoDTO();

        paisDto.setId(pais.getId());
        paisDto.setDescricao(pais.getDescricao());
        pais.setIsActive(pais.getIsActive());
        provinciaDto.setId(provincia.getId());
        provinciaDto.setDescricao(provincia.getDescricao());
        provinciaDto.setActive(provincia.getIsActive());
        provinciaDto.setPais(paisDto);
        return provinciaDto;
    }

}
