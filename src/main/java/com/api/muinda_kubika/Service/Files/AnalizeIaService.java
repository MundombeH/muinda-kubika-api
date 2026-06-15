package com.api.muinda_kubika.Service.Files;

import com.api.muinda_kubika.DTO.Files.AnalizeIA.DocumentoIAMetadadosResponseDto;
import com.api.muinda_kubika.DTO.Files.AnalizeIA.DocumentoIAResultadoRequestDto;
import com.api.muinda_kubika.DTO.Files.AnalizeIA.SugestaoConfiancaDto;
import com.api.muinda_kubika.Repository.Files.DocumentoAnaliseRepository;
import com.api.muinda_kubika.model.Files.DocumentoAnaliseModel;
import com.api.muinda_kubika.model.Files.DocumentosModel;
import com.api.muinda_kubika.model.Files.SugestaoConfiancaModel;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class AnalizeIaService {

    private final DocumentoAnaliseRepository documentoAnaliseRepository;

    public AnalizeIaService(
        DocumentoAnaliseRepository documentoAnaliseRepository
    ) {
        this.documentoAnaliseRepository = documentoAnaliseRepository;
    }

    public DocumentoAnaliseModel salvarAnalise(
        DocumentosModel documento,
        IAResponse response
    ) {
        DocumentoAnaliseModel analise = documentoAnaliseRepository
            .findByDocumentoId(documento.getId())
            .orElseGet(DocumentoAnaliseModel::new);

        analise.setDocumento(documento);
        analise.setResumoGeradoIA(response.resumo());
        analise.setTituloSugerido(response.titulo());
        analise.setCategoriaSugerida(response.categoriaSugerida());
        analise.setCategoriaConfianca(response.categoriaConfianca());
        analise.setSubcategoriaSugerida(response.subcategoriaSugerida());
        analise.setSubcategoriaConfianca(response.subcategoriaConfianca());
        analise.setPalavrasChaveIA(
            response.palavrasChaveIA() != null
                ? response.palavrasChaveIA()
                : new HashSet<>()
        );
        analise.setTagsSugeridas(
            response.tagsSugeridas() != null
                ? response.tagsSugeridas()
                : new HashSet<>()
        );
        analise.setTecnologiasSugeridas(
            response.tecnologiasSugeridas() != null
                ? response.tecnologiasSugeridas()
                : new HashSet<>()
        );
        analise.setFrameworksSugeridos(
            response.frameworksSugeridos() != null
                ? response.frameworksSugeridos()
                : new HashSet<>()
        );
        analise.setConflitosDetectados(
            response.conflitosDetectados() != null
                ? response.conflitosDetectados()
                : new HashSet<>()
        );
        analise.setDataProcessamento(LocalDateTime.now());
        analise.setVersao(documento.getVersao());

        return documentoAnaliseRepository.save(analise);
    }

    public DocumentoAnaliseModel salvarAnalise(
        DocumentosModel documento,
        DocumentoIAResultadoRequestDto dto
    ) {
        DocumentoAnaliseModel analise = documentoAnaliseRepository
            .findByDocumentoId(documento.getId())
            .orElseGet(DocumentoAnaliseModel::new);

        analise.setDocumento(documento);
        analise.setResumoGeradoIA(dto.getResumo());
        analise.setTituloSugerido(dto.getTitulo());
        analise.setCategoriaSugerida(dto.getCategoriaSugerida());
        analise.setCategoriaConfianca(dto.getCategoriaConfianca());
        analise.setSubcategoriaSugerida(dto.getSubcategoriaSugerida());
        analise.setSubcategoriaConfianca(dto.getSubcategoriaConfianca());
        analise.setPalavrasChaveIA(mapSugestoes(dto.getPalavrasChaveIA()));
        analise.setTagsSugeridas(mapSugestoes(dto.getTagsSugeridas()));
        analise.setTecnologiasSugeridas(
            mapSugestoes(dto.getTecnologiasSugeridas())
        );
        analise.setFrameworksSugeridos(
            mapSugestoes(dto.getFrameworksSugeridos())
        );
        analise.setConflitosDetectados(
            dto.getConflitosDetectados() != null
                ? dto.getConflitosDetectados()
                : new HashSet<>()
        );
        analise.setDataProcessamento(LocalDateTime.now());
        analise.setVersao(documento.getVersao());

        return documentoAnaliseRepository.save(analise);
    }

    public DocumentoIAMetadadosResponseDto buscarMetadadosPorDocumentoId(
        UUID documentoId
    ) {
        DocumentoAnaliseModel analise = documentoAnaliseRepository
            .findByDocumentoId(documentoId)
            .orElseThrow(() ->
                new RuntimeException("Metadados da IA não encontrados")
            );

        DocumentoIAMetadadosResponseDto dto =
            new DocumentoIAMetadadosResponseDto();
        dto.setId(analise.getId());
        dto.setCreatedAt(analise.getCreatedAt());
        dto.setUpdatedAt(analise.getUpdatedAt());
        dto.setIsActive(analise.getIsActive());
        dto.setDocumentoId(
            analise.getDocumento() != null
                ? analise.getDocumento().getId()
                : null
        );
        dto.setResumoGeradoIA(analise.getResumoGeradoIA());
        dto.setTituloSugerido(analise.getTituloSugerido());
        dto.setTituloConfianca(analise.getTituloConfianca());
        dto.setCategoriaSugerida(analise.getCategoriaSugerida());
        dto.setCategoriaConfianca(analise.getCategoriaConfianca());
        dto.setSubcategoriaSugerida(analise.getSubcategoriaSugerida());
        dto.setSubcategoriaConfianca(analise.getSubcategoriaConfianca());
        dto.setPalavrasChaveIA(mapSugestoesModel(analise.getPalavrasChaveIA()));
        dto.setTagsSugeridas(mapSugestoesModel(analise.getTagsSugeridas()));
        dto.setTecnologiasSugeridas(
            mapSugestoesModel(analise.getTecnologiasSugeridas())
        );
        dto.setFrameworksSugeridos(
            mapSugestoesModel(analise.getFrameworksSugeridos())
        );
        dto.setConflitosDetectados(analise.getConflitosDetectados());
        dto.setMotivoRejeicao(analise.getMotivoRejeicao());
        dto.setObservacaoAdmin(analise.getObservacaoAdmin());
        dto.setDataProcessamento(analise.getDataProcessamento());
        dto.setVersao(analise.getVersao());
        return dto;
    }

    private Set<SugestaoConfiancaModel> mapSugestoes(
        Set<SugestaoConfiancaDto> sugestoes
    ) {
        if (sugestoes == null) {
            return new HashSet<>();
        }
        return sugestoes
            .stream()
            .map(this::mapSugestao)
            .collect(Collectors.toSet());
    }

    private SugestaoConfiancaModel mapSugestao(SugestaoConfiancaDto dto) {
        SugestaoConfiancaModel model = new SugestaoConfiancaModel();
        model.setValor(dto.getValor());
        model.setConfianca(dto.getConfianca());
        return model;
    }

    private Set<SugestaoConfiancaDto> mapSugestoesModel(
        Set<SugestaoConfiancaModel> sugestoes
    ) {
        if (sugestoes == null) {
            return new HashSet<>();
        }
        return sugestoes
            .stream()
            .map(this::mapSugestaoDto)
            .collect(Collectors.toSet());
    }

    private SugestaoConfiancaDto mapSugestaoDto(SugestaoConfiancaModel model) {
        SugestaoConfiancaDto dto = new SugestaoConfiancaDto();
        dto.setValor(model.getValor());
        dto.setConfianca(model.getConfianca());
        return dto;
    }
}
