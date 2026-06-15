package com.api.muinda_kubika.Service.Files;

import com.api.muinda_kubika.Config.RabbitConfig;
import com.api.muinda_kubika.DTO.Cloudinary.CloudinaryUploadResponse;
import com.api.muinda_kubika.DTO.Files.Documentos.DocumentoResumoDto;
import com.api.muinda_kubika.DTO.Files.Ficheiros.FicheirosRequestDto;
import com.api.muinda_kubika.DTO.Files.Ficheiros.FicheirosResponseDto;
import com.api.muinda_kubika.DTO.Messages.DocumentoIAMessage;
import com.api.muinda_kubika.Repository.Files.DocumentoRepository;
import com.api.muinda_kubika.Repository.Files.FichieroRepository;
import com.api.muinda_kubika.Service.Cloudinary.CloudinaryService;
import com.api.muinda_kubika.model.Files.DocumentosModel;
import com.api.muinda_kubika.model.Files.FicheiroModel;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FicheiroService {

    private final FichieroRepository fichieroRepository;
    private final DocumentoRepository documentoRepository;
    private final CloudinaryService cloudinaryService;
    private final RabbitTemplate rabbitTemplate;

    public FicheiroService(
        FichieroRepository fichieroRepository,
        DocumentoRepository documentoRepository,
        CloudinaryService cloudinaryService,
        RabbitTemplate rabbitTemplate
    ) {
        this.fichieroRepository = fichieroRepository;
        this.documentoRepository = documentoRepository;
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
                upload.format()
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

        return dto;
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
