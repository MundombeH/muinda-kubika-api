package com.api.muinda_kubika.Service.Files;

import com.api.muinda_kubika.Config.RabbitConfig;
import com.api.muinda_kubika.DTO.Cloudinary.CloudinaryUploadResponse;
import com.api.muinda_kubika.DTO.Files.Documentos.DocumentoResumoDto;
import com.api.muinda_kubika.DTO.Files.Ficheiros.FicheirosResponseDto;
import com.api.muinda_kubika.DTO.Messages.DocumentoIAMessage;
import com.api.muinda_kubika.Enums.OrigemAnaliseIAEnum;
import com.api.muinda_kubika.Exceptions.DocumentoAnalisePendenteException;
import com.api.muinda_kubika.Repository.Files.DocumentoRepository;
import com.api.muinda_kubika.Repository.Files.FichieroRepository;
import com.api.muinda_kubika.Repository.Files.RepositorioRepository;
import com.api.muinda_kubika.Service.Cloudinary.CloudinaryService;
import com.api.muinda_kubika.model.Files.DocumentosModel;
import com.api.muinda_kubika.model.Files.FicheiroModel;
import com.api.muinda_kubika.model.Files.RepositorioModel;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FicheiroService {

    private final FichieroRepository fichieroRepository;
    private final DocumentoRepository documentoRepository;
    private final RepositorioRepository repositorioRepository;
    private final AnalizeIaService analizeIaService;
    private final CloudinaryService cloudinaryService;
    private final RabbitTemplate rabbitTemplate;

    public FicheiroService(
        FichieroRepository fichieroRepository,
        DocumentoRepository documentoRepository,
        RepositorioRepository repositorioRepository,
        AnalizeIaService analizeIaService,
        CloudinaryService cloudinaryService,
        RabbitTemplate rabbitTemplate
    ) {
        this.fichieroRepository = fichieroRepository;
        this.documentoRepository = documentoRepository;
        this.repositorioRepository = repositorioRepository;
        this.analizeIaService = analizeIaService;
        this.cloudinaryService = cloudinaryService;
        this.rabbitTemplate = rabbitTemplate;
    }

    public List<FicheirosResponseDto> getAllFileS() {
        return fichieroRepository
            .findByIsActiveTrue()
            .stream()
            .map(this::mapToFicheiros)
            .collect(Collectors.toList());
    }

    public FicheirosResponseDto getOneFile(UUID id) {
        FicheiroModel ficheiro = fichieroRepository
            .findByIdAndIsActiveTrue(id)
            .orElseThrow(() ->
                new RuntimeException("Ficheiro nao encontrado ou inativo")
            );
        return mapToFicheiros(ficheiro);
    }

    public FicheirosResponseDto createFile(MultipartFile file, UUID documentoId)
        throws IOException {
        DocumentosModel documento = documentoRepository
            .findByIdAndIsActiveTrue(documentoId)
            .orElseThrow(() ->
                new RuntimeException("Documento não encontrado")
            );

        validarAnalisePendente(documentoId, OrigemAnaliseIAEnum.FICHEIRO);

        System.out.println(
            "Tipo de documento:" + documento.getTipoDeDocumento()
        );
        System.out.println("nome do :" + file.getOriginalFilename());
        // 1. Upload para Cloudinary
        CloudinaryUploadResponse upload = cloudinaryService.uploadFile(
            file,
            documento.getTipoDeDocumento()
        );

        rabbitTemplate.convertAndSend(
            RabbitConfig.EXCHANGE,
            RabbitConfig.ROUTING_KEY,
            new DocumentoIAMessage(
                documento.getId(),
                upload.url(),
                documento.getTipoDeDocumento().name(),
                file.getOriginalFilename(),
                file.getContentType(),
                upload.bytes(),
                upload.format(),
                OrigemAnaliseIAEnum.FICHEIRO.name()
            )
        );

        // 2. Criar entidade
        FicheiroModel ficheiro = new FicheiroModel();

        ficheiro.setDocumento(documento);
        ficheiro.setNome(file.getOriginalFilename());

        ficheiro.setUrl(upload.url());
        ficheiro.setFormato(upload.format());
        ficheiro.setTamanho(upload.bytes());
        ficheiro.setChecksum(generateChecksum(file));
        ficheiro.setPublicId(upload.publicId());
        ficheiro.setResourceType(upload.resourceType());

        ficheiro.setMimeType(file.getContentType());

        fichieroRepository.save(ficheiro);

        return mapToFicheiros(ficheiro);
    }

    public boolean documentoTemArquivoZip(UUID documentoId) {
        return fichieroRepository.existsByDocumentoIdAndIsActiveTrueAndFormatoIgnoreCase(
            documentoId,
            "zip"
        );
    }

    public void submeterRepositorioGithub(String gitUrl, Set<String> tecnologiasUsadas, UUID documentoId) {
        DocumentosModel documento = documentoRepository
            .findByIdAndIsActiveTrue(documentoId)
            .orElseThrow(() ->
                new RuntimeException("Documento não encontrado")
            );

        validarAnalisePendente(documentoId, OrigemAnaliseIAEnum.REPOSITORIO);

        String normalizedUrl = gitUrl != null ? gitUrl.trim() : "";
        if (!isGithubUrl(normalizedUrl)) {
            throw new IllegalArgumentException(
                "A URL informada deve ser de um repositório público do GitHub"
            );
        }

        RepositorioModel repositorio = repositorioRepository
            .findByDocumentoId(documentoId)
            .orElseGet(RepositorioModel::new);
        repositorio.setDocumento(documento);
        repositorio.setUrlGithub(normalizedUrl);
        if (tecnologiasUsadas != null) {
            repositorio.setTecnologiasUsadas(tecnologiasUsadas);
        }
        repositorioRepository.save(repositorio);

        rabbitTemplate.convertAndSend(
            RabbitConfig.EXCHANGE,
            RabbitConfig.ROUTING_KEY,
            new DocumentoIAMessage(
                documento.getId(),
                normalizedUrl,
                documento.getTipoDeDocumento().name(),
                extractRepoName(normalizedUrl),
                "text/plain",
                0L,
                "git",
                OrigemAnaliseIAEnum.REPOSITORIO.name()
            )
        );
    }

    private void validarAnalisePendente(
        UUID documentoId,
        OrigemAnaliseIAEnum origemAnalise
    ) {
        if (
            analizeIaService.existeAnalisePendente(documentoId, origemAnalise)
        ) {
            throw new DocumentoAnalisePendenteException(
                "Já existe uma análise pendente para esta origem neste documento"
            );
        }
    }

    private FicheirosResponseDto mapToFicheiros(FicheiroModel ficheiros) {
        FicheirosResponseDto dto = new FicheirosResponseDto();

        dto.setId(ficheiros.getId());
        dto.setNome(ficheiros.getNome());
        dto.setUrl(ficheiros.getUrl());
        dto.setFormato(ficheiros.getFormato());
        dto.setIsActive(ficheiros.getIsActive());
        dto.setChecksum(ficheiros.getChecksum());
        dto.setUpdatedAt(ficheiros.getUpdatedAt());
        dto.setCreatedAt(ficheiros.getCreatedAt());
        dto.setTamanho(ficheiros.getTamanho());
        dto.setMimeType(ficheiros.getMimeType());

        if (ficheiros.getDocumento() != null) {
            dto.setDocumento(mapToDocumento(ficheiros.getDocumento()));
        }

        return dto;
    }

    private DocumentoResumoDto mapToDocumento(DocumentosModel documentosModel) {
        DocumentoResumoDto dto = new DocumentoResumoDto();
        dto.setAutores(documentosModel.getAutores());
        dto.setResumo(documentosModel.getResumo());
        dto.setVersao(documentosModel.getVersao());
        dto.setTitulo(documentosModel.getTitulo());
        dto.setTipoDocumento(documentosModel.getTipoDeDocumento());
        dto.setStatus(documentosModel.getStatus());

        return dto;
    }

    private boolean isGithubUrl(String url) {
        try {
            URI uri = URI.create(url);
            String host = uri.getHost();
            String path = uri.getPath();
            return (
                host != null &&
                host.equalsIgnoreCase("github.com") &&
                path != null &&
                path.matches("^/[^/]+/[^/]+/?$")
            );
        } catch (Exception e) {
            return false;
        }
    }

    private String extractRepoName(String url) {
        try {
            String path = URI.create(url).getPath();
            if (path == null || path.isBlank()) {
                return "repositorio-github";
            }
            String[] parts = path.replaceAll("/$", "").split("/");
            return parts.length > 0
                ? parts[parts.length - 1]
                : "repositorio-github";
        } catch (Exception e) {
            return "repositorio-github";
        }
    }

    public String uploadCoverImage(MultipartFile file) throws IOException {
        return cloudinaryService.uploadCoverImage(file);
    }

    private String generateChecksum(MultipartFile file) {
        try {
            java.security.MessageDigest digest =
                java.security.MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(file.getBytes());

            return java.util.Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular checksum");
        }
    }
}
