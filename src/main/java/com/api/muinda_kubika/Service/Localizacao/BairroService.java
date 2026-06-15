package com.api.muinda_kubika.Service.Localizacao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.api.muinda_kubika.DTO.Localizacao.*;
import com.api.muinda_kubika.Repository.Localizacao.BairrosRepository;
import com.api.muinda_kubika.Repository.Localizacao.MunicipiosRepository;
import com.api.muinda_kubika.model.Localizacao.BairroModel;
import com.api.muinda_kubika.model.Localizacao.MunicipioModel;
import com.api.muinda_kubika.model.Localizacao.PaisModel;
import com.api.muinda_kubika.model.Localizacao.ProvinciasModel;
import org.springframework.stereotype.Service;


import jakarta.transaction.Transactional;

@Service
public class BairroService {

    private final MunicipiosRepository municipiosRepository;
    private final BairrosRepository bairrosRepository;

    public BairroService(MunicipiosRepository municipiosRepository, BairrosRepository bairrosRepository) {
        this.municipiosRepository = municipiosRepository;
        this.bairrosRepository = bairrosRepository;
    }

    public List<BairroResponseDTO> getAllcidades() {
        return bairrosRepository.findAll().stream().map(this::mapToCidadesDTO).collect(Collectors.toList());
    }

    public List<BairroResponseDTO> getCidadeByMunicipio(UUID id) {
        municipiosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cidade não encontrada ou inativa"));

        return bairrosRepository.findByMunicipioId(id).stream().map(this::mapToCidadesDTO).collect(Collectors.toList());

    }

    public BairroResponseDTO getOneCidade(UUID id) {
        BairroModel cidadesModel = bairrosRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Cidade não encontrada ou inativa"));
        return mapToCidadesDTO(cidadesModel);
    }

    @Transactional
    public BairroResponseDTO createCidade(BairroRequestDTO bairroReuestDto) throws Exception {
        Optional<BairroModel> findCidades = bairrosRepository.findByDescricaoAndIsActiveTrue(bairroReuestDto.getDescricao());

        if (findCidades.isPresent()) {
            throw new Exception("Esta cidade ja existe no sistema");

        }
        MunicipioModel findMunicipios = municipiosRepository.findById(bairroReuestDto.getMunicipioId())
                .orElseThrow(() -> new RuntimeException("Municipio não encontrada ou inativa"));

        if (bairroReuestDto.getDescricao().isEmpty()) {
            throw new Exception("A descrição da cidade não pode ser vazia");
        }
        if (bairroReuestDto.getDescricao().isBlank()) {
            throw new Exception("A descrição da cidade não pode ser nula");
        }
        if (bairroReuestDto.getDescricao().startsWith(" ")) {
            throw new Exception("A descrição da cidade não pode começar com espaços em branco!");
        }
        BairroModel cidadesModel = new BairroModel();

        cidadesModel.setDescricao(bairroReuestDto.getDescricao());
        cidadesModel.setMunicipio(findMunicipios);

        BairroModel save = bairrosRepository.save(cidadesModel);

        return mapToCidadesDTO(save);

    }

    @Transactional
    public BairroResponseDTO updateCidade(UUID id, BairroRequestDTO bairroReuestDto) throws Exception {
        BairroModel findCidades = bairrosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cidade não Encontrada!"));
        MunicipioModel findMunicipios = municipiosRepository.findById(bairroReuestDto.getMunicipioId())
                .orElseThrow(() -> new RuntimeException("Província não encontrada ou inativa"));

        if (bairroReuestDto.getDescricao().isEmpty()) {
            throw new Exception("A descrição da cidade não pode ser vazia");
        }
        if (bairroReuestDto.getDescricao().isBlank()) {
            throw new Exception("A descrição da cidade não pode ser nula");
        }
        if (bairroReuestDto.getDescricao().startsWith(" ")) {
            throw new Exception("A descrição da cidade não pode começar com espaços em branco!");
        }

        findCidades.setDescricao(bairroReuestDto.getDescricao());
        findCidades.setMunicipio(findMunicipios);

        BairroModel save = bairrosRepository.save(findCidades);

        return mapToCidadesDTO(save);

    }

    public void deleteCidade(UUID id) {
        BairroModel findCidades = bairrosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cidade não Encontrada!"));

        findCidades.setIsActive(false);
        bairrosRepository.delete(findCidades);
    }

    private BairroResponseDTO mapToCidadesDTO(BairroModel cidade) {
        BairroResponseDTO cidadeDTO = new BairroResponseDTO();

        MunicipioModel municipios = cidade.getMunicipio();
        MunicipiosResumoDTO municipiosResumoDTO = new MunicipiosResumoDTO();

        ProvinciasResumoDTO provinciasResumoDTO = getProvinciasResumoDTO(cidade);
        municipiosResumoDTO.setActive(municipios.getIsActive());
        municipiosResumoDTO.setDescricao(municipios.getDescricao());
        municipiosResumoDTO.setId(municipios.getId());
        municipiosResumoDTO.setProvincia(provinciasResumoDTO);

        cidadeDTO.setIsActive(cidade.getIsActive());
        cidadeDTO.setId(cidade.getId());
        cidadeDTO.setMunicipio(municipiosResumoDTO);
        cidadeDTO.setDescricao(cidade.getDescricao());
        cidadeDTO.setCreatedAt(cidade.getCreatedAt());
        cidadeDTO.setUpdatedAt(cidade.getUpdatedAt());

        return cidadeDTO;

    }

    private ProvinciasResumoDTO getProvinciasResumoDTO(BairroModel cidade) {
        ProvinciasModel provincias = cidade.getMunicipio().getProvincia();

        ProvinciasResumoDTO provinciasResumoDTO = new ProvinciasResumoDTO();

        PaisModel paises = cidade.getMunicipio().getProvincia().getPais();
        PaisResumoDTO paisResumoDTO = new PaisResumoDTO();

        paisResumoDTO.setId(paises.getId());
        paisResumoDTO.setDescricao(paises.getDescricao());
        paisResumoDTO.setActive(paises.getIsActive());

        provinciasResumoDTO.setId(provincias.getId());
        provinciasResumoDTO.setDescricao(provincias.getDescricao());
        provinciasResumoDTO.setActive(provincias.getIsActive());
        provinciasResumoDTO.setPais(paisResumoDTO);

        return provinciasResumoDTO;
    }
}