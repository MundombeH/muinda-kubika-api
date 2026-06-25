package com.api.muinda_kubika.Service.Files;

import com.api.muinda_kubika.DTO.Categorias.CategroiaRepsonseDto;
import com.api.muinda_kubika.DTO.Files.Documentos.DocumentoUpdateRequestDto;
import com.api.muinda_kubika.DTO.Files.Documentos.DocumentosRequestDto;
import com.api.muinda_kubika.DTO.Files.Documentos.DocumentosResponseDto;
import com.api.muinda_kubika.DTO.Files.Ficheiros.FicheiroResumoDto;
import com.api.muinda_kubika.DTO.Instituicoes.InstituicoesResumoDto;
import com.api.muinda_kubika.DTO.Tags.TagsResponseDto;
import com.api.muinda_kubika.DTO.Usuarios.DefaultUser.DefaultUserResumoDto;
import com.api.muinda_kubika.Enums.StatusDocumentoEnum;
import com.api.muinda_kubika.Exceptions.UserNotFoundException;
import com.api.muinda_kubika.Repository.Categorias_Tags.CategoriasRepository;
import com.api.muinda_kubika.Repository.Categorias_Tags.TagsRepository;
import com.api.muinda_kubika.Repository.Files.DocumentoRepository;
import com.api.muinda_kubika.Repository.Files.RepositorioRepository;
import com.api.muinda_kubika.Repository.Instituicoes.InstituicoesRepository;
import com.api.muinda_kubika.Repository.Usuarios.DefaultUserRepository;
import com.api.muinda_kubika.model.Categorias_Tags.CategoriasModel;
import com.api.muinda_kubika.model.Categorias_Tags.TagsModel;
import com.api.muinda_kubika.model.Files.DocumentosModel;
import com.api.muinda_kubika.model.Files.FicheiroModel;
import com.api.muinda_kubika.model.Instituicao.InstituicaoModel;
import com.api.muinda_kubika.model.Usuarios.DefaultUserModel;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DocumentoService {

    private final DocumentoRepository documentoRepository;
    private final DefaultUserRepository userRepository;
    private final InstituicoesRepository instituicoesRepository;
    private final CategoriasRepository categoriasRepository;
    private final TagsRepository tagsRepository;
    private final RepositorioRepository repositorioRepository;
    private final AnalizeIaService analizeIaService;

    public DocumentoService(
        DocumentoRepository documentoRepository,
        DefaultUserRepository userRepository,
        InstituicoesRepository instituicoesRepository,
        CategoriasRepository categoriasRepository,
        TagsRepository tagsRepository,
        RepositorioRepository repositorioRepository,
        AnalizeIaService analizeIaService
    ) {
        this.documentoRepository = documentoRepository;
        this.userRepository = userRepository;
        this.instituicoesRepository = instituicoesRepository;
        this.categoriasRepository = categoriasRepository;
        this.tagsRepository = tagsRepository;
        this.repositorioRepository = repositorioRepository;
        this.analizeIaService = analizeIaService;
    }

    public List<DocumentosResponseDto> getAllDocumentos() {
        return documentoRepository
            .findByIsActiveTrue()
            .stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    public DocumentosResponseDto getOneDocumento(UUID id) {
        DocumentosModel documento = documentoRepository
            .findByIdAndIsActiveTrue(id)
            .orElseThrow(() ->
                new RuntimeException("Documento nao encontrado ou inativo")
            );
        return mapToDto(documento);
    }

    @Transactional
    public DocumentosResponseDto createDocumento(
        DocumentosRequestDto dto,
        UUID userId
    ) {
        DefaultUserModel user = userRepository
            .findByIdAndIsActiveTrue(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));
        InstituicaoModel instituicao = instituicoesRepository
            .findByIdAndIsActiveTrue(dto.getInstituicao())
            .orElseThrow(() ->
                new RuntimeException("Instituicao nao encontrada ou inativa")
            );

        DocumentosModel documento = new DocumentosModel();
        documento.setTitulo(dto.getTitulo());
        documento.setResumo(dto.getResumo());
        documento.setAutores(dto.getAutores());
        documento.setTipoDeDocumento(dto.getTipoDeDocumento());
        documento.setVersao(dto.getVersao() != null ? dto.getVersao() : 1);

        documento.setUsuario(user);
        documento.setInstituicao(instituicao);

        documento.setStatus(StatusDocumentoEnum.DRAFT);

        if (dto.getCapaUrl() != null) {
            documento.setCapaUrl(dto.getCapaUrl());
        }

        documentoRepository.save(documento);

        return mapToDto(documento);
    }

    @Transactional
    public void deleteDocumento(UUID id) {
        DocumentosModel documento = documentoRepository
            .findByIdAndIsActiveTrue(id)
            .orElseThrow(() ->
                new RuntimeException("Documento nao encontrado ou inativo")
            );
        documento.setIsActive(false);
        documentoRepository.save(documento);
    }

    @Transactional
    public DocumentosResponseDto updateDocumento(UUID id, DocumentoUpdateRequestDto dto) {
        DocumentosModel documento = documentoRepository
            .findByIdAndIsActiveTrue(id)
            .orElseThrow(() -> new RuntimeException("Documento nao encontrado ou inativo"));

        if (dto.getTitulo() != null) {
            documento.setTitulo(dto.getTitulo());
        }
        if (dto.getResumo() != null) {
            documento.setResumo(dto.getResumo());
        }
        if (dto.getAutores() != null) {
            documento.setAutores(dto.getAutores());
        }
        if (dto.getTipoDeDocumento() != null) {
            documento.setTipoDeDocumento(dto.getTipoDeDocumento());
        }
        if (dto.getStatus() != null) {
            documento.setStatus(dto.getStatus());
            if (dto.getStatus() == StatusDocumentoEnum.PENDENTE_REVISAO_ADMIN) {
                analizeIaService.confirmarAnalisePendente(documento.getId());
            }
        }
        if (dto.getCapaUrl() != null) {
            documento.setCapaUrl(dto.getCapaUrl());
        }
        if (dto.getCategorias() != null && !dto.getCategorias().isEmpty()) {
            Set<CategoriasModel> categorias = dto.getCategorias()
                .stream()
                .map(desc -> categoriasRepository
                    .findByDescricaoAndIsActiveTrue(desc)
                    .orElseGet(() -> {
                        CategoriasModel nova = new CategoriasModel();
                        nova.setDescricao(desc);
                        return categoriasRepository.save(nova);
                    }))
                .collect(Collectors.toSet());
            documento.setCategorias(categorias);
        }
        if (dto.getTags() != null && !dto.getTags().isEmpty()) {
            Set<TagsModel> tags = dto.getTags()
                .stream()
                .map(desc -> tagsRepository
                    .findByDescricaoAndIsActiveTrue(desc)
                    .orElseGet(() -> {
                        TagsModel nova = new TagsModel();
                        nova.setDescricao(desc);
                        return tagsRepository.save(nova);
                    }))
                .collect(Collectors.toSet());
            documento.setTags(tags);
        }

        return mapToDto(documentoRepository.save(documento));
    }

    @Transactional
    public DocumentosResponseDto approveDocumento(UUID id, UUID adminId) {
        DocumentosModel documento = documentoRepository
            .findByIdAndIsActiveTrue(id)
            .orElseThrow(() -> new RuntimeException("Documento nao encontrado ou inativo"));

        DefaultUserModel admin = userRepository
            .findByIdAndIsActiveTrue(adminId)
            .orElseThrow(() -> new UserNotFoundException(adminId));

        documento.setStatus(StatusDocumentoEnum.APROVADO);
        documento.setAprovadoPor(admin);
        documento.setDataAprovacao(LocalDateTime.now());

        return mapToDto(documentoRepository.save(documento));
    }

    @Transactional
    public DocumentosResponseDto rejectDocumento(UUID id, UUID adminId, String motivo) {
        DocumentosModel documento = documentoRepository
            .findByIdAndIsActiveTrue(id)
            .orElseThrow(() -> new RuntimeException("Documento nao encontrado ou inativo"));

        documento.setStatus(StatusDocumentoEnum.REJEITADO);

        return mapToDto(documentoRepository.save(documento));
    }

    @Transactional
    public DocumentosResponseDto publishDocumento(UUID id) {
        DocumentosModel documento = documentoRepository
            .findByIdAndIsActiveTrue(id)
            .orElseThrow(() -> new RuntimeException("Documento nao encontrado ou inativo"));

        documento.setStatus(StatusDocumentoEnum.PUBLICADO);

        return mapToDto(documentoRepository.save(documento));
    }

    private DocumentosResponseDto mapToDto(DocumentosModel documentosModel) {
        DocumentosResponseDto dto = new DocumentosResponseDto();
        dto.setAutores(documentosModel.getAutores());
        dto.setResumo(documentosModel.getResumo());
        dto.setVersao(documentosModel.getVersao());
        dto.setTitulo(documentosModel.getTitulo());
        dto.setTipoDeDocumento(documentosModel.getTipoDeDocumento());
        dto.setStatus(documentosModel.getStatus());
        dto.setCapaUrl(documentosModel.getCapaUrl());
        dto.setId(documentosModel.getId());
        dto.setCreatedAt(documentosModel.getCreatedAt());
        dto.setUpdatedAt(documentosModel.getUpdatedAt());

        if (documentosModel.getUsuario() != null) {
            dto.setUsuario(mapToUsuario(documentosModel.getUsuario()));
        }

        if (documentosModel.getInstituicao() != null) {
            dto.setInstituicao(
                mapToInstituicao(documentosModel.getInstituicao())
            );
        }

        if (documentosModel.getFicheiros() != null) {
            dto.setFicheiros(
                documentosModel
                    .getFicheiros()
                    .stream()
                    .map(this::mapToFicheiros)
                    .collect(Collectors.toSet())
            );
        }

        if (documentosModel.getTags() != null) {
            dto.setTags(
                documentosModel
                    .getTags()
                    .stream()
                    .map(this::mapToTags)
                    .collect(Collectors.toSet())
            );
        }

        if (documentosModel.getCategorias() != null) {
            dto.setCategorias(
                documentosModel
                    .getCategorias()
                    .stream()
                    .map(this::mapToCategorias)
                    .collect(Collectors.toSet())
            );
        }

        if (documentosModel.getAprovadoPor() != null) {
            dto.setAprovadoPor(mapToUsuario(documentosModel.getAprovadoPor()));
        }

        repositorioRepository
            .findByDocumentoId(documentosModel.getId())
            .ifPresent(repositorio -> {
                dto.setUrlGithub(repositorio.getUrlGithub());
                dto.setTecnologiasUsadas(repositorio.getTecnologiasUsadas());
            });

        return dto;
    }

    private CategroiaRepsonseDto mapToCategorias(CategoriasModel categorias) {
        CategroiaRepsonseDto dto = new CategroiaRepsonseDto();
        dto.setDescricao(categorias.getDescricao());
        dto.setId(categorias.getId());
        dto.setCreatedAt(categorias.getCreatedAt());
        dto.setIsActive(categorias.getIsActive());
        dto.setUpdatedAt(categorias.getUpdatedAt());

        return dto;
    }

    private TagsResponseDto mapToTags(TagsModel tags) {
        TagsResponseDto dto = new TagsResponseDto();
        dto.setDescricao(tags.getDescricao());
        dto.setId(tags.getId());
        dto.setCreatedAt(tags.getCreatedAt());
        dto.setIsActive(tags.getIsActive());
        dto.setUpdatedAt(tags.getUpdatedAt());

        return dto;
    }

    private FicheiroResumoDto mapToFicheiros(FicheiroModel ficheiros) {
        FicheiroResumoDto dto = new FicheiroResumoDto();
        dto.setId(ficheiros.getId());
        dto.setNome(ficheiros.getNome());
        dto.setUrl(ficheiros.getUrl());
        dto.setFormato(ficheiros.getFormato());
        return dto;
    }

    private InstituicoesResumoDto mapToInstituicao(
        InstituicaoModel instituicao
    ) {
        InstituicoesResumoDto dto = new InstituicoesResumoDto();
        dto.setDescricao(instituicao.getDescricao());
        dto.setTipoInstituicao(instituicao.getTipoInstituicao());
        dto.setId(instituicao.getId());
        dto.setEmail(instituicao.getEmail());
        dto.setNumeroDeTelefone(instituicao.getNumeroDeTelefone());
        return dto;
    }

    private DefaultUserResumoDto mapToUsuario(DefaultUserModel usuario) {
        DefaultUserResumoDto dto = new DefaultUserResumoDto();
        dto.setNumeroDeTelefone(usuario.getNumeroDeTelefone());
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setNumeroDeTelefone(usuario.getNumeroDeTelefone());
        return dto;
    }
}
