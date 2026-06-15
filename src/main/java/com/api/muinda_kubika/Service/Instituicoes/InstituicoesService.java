package com.api.muinda_kubika.Service.Instituicoes;

import com.api.muinda_kubika.DTO.Instituicoes.InstituicaoPathDto;
import com.api.muinda_kubika.DTO.Instituicoes.InstituicoesRequestDto;
import com.api.muinda_kubika.DTO.Instituicoes.InstituicoesResponseDto;
import com.api.muinda_kubika.DTO.Localizacao.BairroResumoDto;
import com.api.muinda_kubika.Enums.TipoInstituicaoEnum;
import com.api.muinda_kubika.Repository.Instituicoes.InstituicoesRepository;
import com.api.muinda_kubika.Repository.Localizacao.BairrosRepository;
import com.api.muinda_kubika.Specification.InstituicaoSpecification;
import com.api.muinda_kubika.model.Instituicao.InstituicaoModel;
import com.api.muinda_kubika.model.Localizacao.BairroModel;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class InstituicoesService {
    private final InstituicoesRepository instituicoesRepository;
    private final BairrosRepository bairrosRepository;

    public InstituicoesService(InstituicoesRepository instituicoesRepository, BairrosRepository bairrosRepository) {
        this.instituicoesRepository = instituicoesRepository;
        this.bairrosRepository = bairrosRepository;
    }

    public List<InstituicoesResponseDto> getAllInstituicoes(UUID id, UUID bairroId,UUID municipioId,UUID provinciaId, UUID paisId, TipoInstituicaoEnum tipo){
        Specification<InstituicaoModel> spec = InstituicaoSpecification.activo();

        if(id != null){
            spec = spec.and(InstituicaoSpecification.porId(id));

        }
        if(bairroId != null){
            spec = spec.and(InstituicaoSpecification.porBairro(bairroId));

        }
        if(tipo != null){
            spec = spec.and(InstituicaoSpecification.porTipo(tipo));

        }
        if(municipioId != null){
            spec = spec.and(InstituicaoSpecification.porMunicipio(municipioId));

        }
        if(provinciaId != null){
            spec = spec.and(InstituicaoSpecification.porProvincia(provinciaId));

        }
        if(paisId != null){
            spec = spec.and(InstituicaoSpecification.porPais(paisId));

        }

        return instituicoesRepository.findAll(spec).stream().map(this::mapToDto).toList();
    }


    @Transactional
    public InstituicoesResponseDto createInstituicao(InstituicoesRequestDto dto){
        InstituicaoModel instituicao = new InstituicaoModel();

        BairroModel bairro = bairrosRepository.findByIdAndIsActiveTrue(dto.getBairro()).orElseThrow(() -> new RuntimeException("Bairro não encontrado"));

        Set<TipoInstituicaoEnum> tipos = new HashSet<>(dto.getTipoInstituicao());
        instituicao.setTipoInstituicao(tipos);

        instituicao.setBairro(bairro);
        instituicao.setEmail(dto.getEmail());
        instituicao.setDescricao(dto.getDescricao());
        instituicao.setAnoDeFundacao(dto.getAnoDeFundacao());
        instituicao.setHoraioDeFuncionamento(dto.getHoraioDeFuncionamento());
        instituicao.setNumeroDeTelefone(dto.getNumeroDeTelefone());
        instituicao.setLatitude(dto.getLatitude());
        instituicao.setLongitude(dto.getLongitude());

        InstituicaoModel saved = instituicoesRepository.save(instituicao);

        return mapToDto(saved);
    }

    @Transactional
    public InstituicoesResponseDto updateInstituicao(UUID instituicaoId, InstituicaoPathDto dto){
        InstituicaoModel instituicao = instituicoesRepository.findByIdAndIsActiveTrue(instituicaoId).orElseThrow(() -> new RuntimeException("Instituição não encontada"));
        BairroModel bairro = bairrosRepository.findByIdAndIsActiveTrue(dto.getBairro()).orElseThrow(() -> new RuntimeException("Bairro não encontrado"));

        if(dto.getTipoInstituicao() != null){
        instituicao.setTipoInstituicao(dto.getTipoInstituicao());

        }
        if(dto.getBairro() != null){
        instituicao.setBairro(bairro);

        }
        if(dto.getEmail() !=null ){
        instituicao.setEmail(dto.getEmail());

        }
        if(dto.getDescricao() != null){
        instituicao.setDescricao(dto.getDescricao());

        }
        if(dto.getAnoDeFundacao() != null){
        instituicao.setAnoDeFundacao(dto.getAnoDeFundacao());

        }
        if(dto.getHoraioDeFuncionamento() != null){
            instituicao.setHoraioDeFuncionamento(dto.getHoraioDeFuncionamento());

        }
        if(dto.getNumeroDeTelefone() != null){
        instituicao.setNumeroDeTelefone(dto.getNumeroDeTelefone());

        }

        if((dto.getLatitude()!=null)){
        instituicao.setLatitude(dto.getLatitude());

        }

        if(dto.getLongitude() != null){
        instituicao.setLongitude(dto.getLongitude());

        }

        InstituicaoModel saved = instituicoesRepository.save(instituicao);

        return mapToDto(saved);
    }


    public void deleteInstituicao(UUID id){
        InstituicaoModel instituicao = instituicoesRepository.findByIdAndIsActiveTrue(id).orElseThrow(() -> new RuntimeException("Instituição não encontada"));
        instituicao.setIsActive(false);
        instituicoesRepository.save(instituicao);

    }

    private InstituicoesResponseDto mapToDto(InstituicaoModel instituicao) {
        InstituicoesResponseDto dto = new InstituicoesResponseDto();
        dto.setId(instituicao.getId());
        dto.setDescricao(instituicao.getDescricao());
        dto.setTipoInstituicao(instituicao.getTipoInstituicao());
        dto.setNumeroDeTelefone(instituicao.getNumeroDeTelefone());
        dto.setEmail(instituicao.getEmail());
        dto.setIsActive(instituicao.getIsActive());
        dto.setLatitude(instituicao.getLatitude());
        dto.setLongitude(instituicao.getLongitude());
        dto.setAnoDeFundacao(instituicao.getAnoDeFundacao());
        dto.setHoraioDeFuncionamento(instituicao.getHoraioDeFuncionamento());
        dto.setUpdatedAt(instituicao.getUpdatedAt());
        dto.setCreatedAt(instituicao.getCreatedAt());

        if (instituicao.getBairro() != null) {
            dto.setBairro(mapToBairro(instituicao.getBairro()));
        }
        return dto;
    }

    private BairroResumoDto mapToBairro(BairroModel bairro) {
        BairroResumoDto dto = new BairroResumoDto();
        dto.setDescricao(bairro.getDescricao());
        dto.setId(bairro.getId());
        dto.setIsActive(bairro.getIsActive());
        dto.setMunicipioId(bairro.getMunicipio().getId());
        return dto;
    }
}
