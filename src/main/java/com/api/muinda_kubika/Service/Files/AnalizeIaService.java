package com.api.muinda_kubika.Service.Files;

import com.api.muinda_kubika.DTO.Files.AnalizeIA.DocumentoIAMetadadosResponseDto;
import com.api.muinda_kubika.DTO.Files.AnalizeIA.DocumentoIAResultadoRequestDto;
import com.api.muinda_kubika.DTO.Files.AnalizeIA.SugestaoConfiancaDto;
import com.api.muinda_kubika.Enums.OrigemAnaliseIAEnum;
import com.api.muinda_kubika.Repository.Files.DocumentoAnaliseRepository;
import com.api.muinda_kubika.Repository.Files.RepositorioRepository;
import com.api.muinda_kubika.model.Files.DocumentoAnaliseModel;
import com.api.muinda_kubika.model.Files.DocumentosModel;
import com.api.muinda_kubika.model.Files.SugestaoConfiancaModel;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class AnalizeIaService {

    private final DocumentoAnaliseRepository documentoAnaliseRepository;
    private final RepositorioRepository repositorioRepository;

    public AnalizeIaService(
        DocumentoAnaliseRepository documentoAnaliseRepository,
        RepositorioRepository repositorioRepository
    ) {
        this.documentoAnaliseRepository = documentoAnaliseRepository;
        this.repositorioRepository = repositorioRepository;
    }

    public boolean existeAnalisePendente(
        UUID documentoId,
        OrigemAnaliseIAEnum origemAnalise
    ) {
        return documentoAnaliseRepository.existsByDocumentoIdAndOrigemAnaliseAndPendenteConfirmacaoTrue(
            documentoId,
            origemAnalise
        );
    }

    public void confirmarAnalisePendente(UUID documentoId) {
        documentoAnaliseRepository
            .findByDocumentoIdOrderByCreatedAtDesc(documentoId)
            .stream()
            .filter(DocumentoAnaliseModel::getPendenteConfirmacao)
            .forEach(analise -> {
                analise.setPendenteConfirmacao(false);
                documentoAnaliseRepository.save(analise);
            });
    }

    public DocumentoAnaliseModel salvarAnalise(
        DocumentosModel documento,
        DocumentoIAResultadoRequestDto dto
    ) {
        OrigemAnaliseIAEnum origemAnalise = dto.getOrigemAnalise();
        int proximaVersao =
            documentoAnaliseRepository
                .findTopByDocumentoIdAndOrigemAnaliseOrderByVersaoAnaliseDescCreatedAtDesc(
                    documento.getId(),
                    origemAnalise
                )
                .map(DocumentoAnaliseModel::getVersaoAnalise)
                .orElse(0) + 1;

        DocumentoAnaliseModel analise = new DocumentoAnaliseModel();
        analise.setDocumento(documento);
        analise.setOrigemAnalise(origemAnalise);
        analise.setPendenteConfirmacao(true);
        analise.setVersaoAnalise(proximaVersao);
        analise.setResumoGeradoIA(dto.getResumo());
        analise.setTituloSugerido(dto.getTitulo());
        analise.setTituloConfianca(dto.getTituloConfianca());
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
        Set<String> conflitos = new HashSet<>(
            dto.getConflitosDetectados() != null
                ? dto.getConflitosDetectados()
                : new HashSet<>()
        );

        String tituloUser = documento.getTitulo();
        String tituloIa = dto.getTitulo();
        if (
            tituloUser != null && !tituloUser.isBlank() &&
            tituloIa != null && !tituloIa.isBlank() &&
            !tituloUser.equalsIgnoreCase(tituloIa.strip())
        ) {
            conflitos.add(
                "O título fornecido (" + tituloUser + ") difere do título sugerido pela IA (" + tituloIa.strip() + ")"
            );
        }

        String resumoUser = documento.getResumo();
        String resumoIa = dto.getResumo();
        if (
            resumoUser != null && !resumoUser.isBlank() &&
            resumoIa != null && !resumoIa.isBlank() &&
            !resumoUser.equalsIgnoreCase(resumoIa.strip())
        ) {
            conflitos.add("O resumo fornecido difere do resumo gerado pela IA");
        }

        analise.setConflitosDetectados(conflitos);
        analise.setDataProcessamento(LocalDateTime.now());
        analise.setVersao(documento.getVersao());

        DocumentoAnaliseModel saved = documentoAnaliseRepository.save(analise);
        sincronizarRepositorio(documento.getId(), origemAnalise, dto);
        return saved;
    }

    public List<DocumentoIAMetadadosResponseDto> buscarMetadadosPorDocumentoId(
        UUID documentoId
    ) {
        return documentoAnaliseRepository
            .findByDocumentoIdOrderByCreatedAtDesc(documentoId)
            .stream()
            .map(this::mapToMetadadosDto)
            .collect(Collectors.toList());
    }

    public Optional<
        DocumentoIAMetadadosResponseDto> buscarUltimoMetadadoPorOrigem( UUID documentoId,
        OrigemAnaliseIAEnum origemAnalise
    ) {
        return documentoAnaliseRepository
            .findTopByDocumentoIdAndOrigemAnaliseOrderByVersaoAnaliseDescCreatedAtDesc(
                documentoId,
                origemAnalise
            )
            .map(this::mapToMetadadosDto);
    }

    private void sincronizarRepositorio( UUID documentoId, OrigemAnaliseIAEnum origemAnalise,
        DocumentoIAResultadoRequestDto dto
    ) {
        if (origemAnalise != OrigemAnaliseIAEnum.REPOSITORIO) {
            return;
        }

        repositorioRepository
            .findByDocumentoId(documentoId)
            .ifPresent(repositorio -> {
                repositorio.setTecnologiasUsadas(
                    dto.getTecnologiasSugeridas() != null
                        ? dto
                              .getTecnologiasSugeridas()
                              .stream()
                              .map(SugestaoConfiancaDto::getValor)
                              .filter(
                                  valor -> valor != null && !valor.isBlank()
                              )
                              .collect(Collectors.toSet())
                        : new HashSet<>()
                );
                repositorioRepository.save(repositorio);
            });
    }

    private DocumentoIAMetadadosResponseDto mapToMetadadosDto(
        DocumentoAnaliseModel analise
    ) {
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
        dto.setOrigemAnalise(analise.getOrigemAnalise());
        dto.setPendenteConfirmacao(analise.getPendenteConfirmacao());
        dto.setVersaoAnalise(analise.getVersaoAnalise());
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
        dto.setFrameworksSugeridos(mapSugestoesModel(analise.getFrameworksSugeridos()));
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
