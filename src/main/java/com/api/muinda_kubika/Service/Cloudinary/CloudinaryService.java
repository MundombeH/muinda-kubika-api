package com.api.muinda_kubika.Service.Cloudinary;

import com.api.muinda_kubika.DTO.Cloudinary.CloudinaryUploadResponse;
import com.api.muinda_kubika.Enums.TipoDocumentoEnum;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }
    public CloudinaryUploadResponse uploadFile(MultipartFile file, TipoDocumentoEnum tipoDocumento) throws IOException {

        System.out.println("=== INSPEÇÃO CLOUDINARY UPLOAD ===");
        System.out.println("Tipo de Documento: " + tipoDocumento);

        if (file != null) {
            System.out.println("Nome do Ficheiro: " + file.getOriginalFilename());
            System.out.println("Tamanho do Ficheiro (bytes): " + file.getSize());
            System.out.println("Content Type: " + file.getContentType());
        } else {
            System.out.println("AVISO: O objeto 'file' chegou NULO ao CloudinaryService!");
        }
        System.out.println("==================================");
        String folder = "repositorio/" + tipoDocumento.name().toLowerCase();

        System.out.println("Folder:"+folder);
        Map<String, Object> params = ObjectUtils.asMap(
                "resource_type", "raw",
                "folder", folder
        );

        Map<?, ?> result = cloudinary.uploader().upload(
                file.getBytes(),
                params
        );

        return new CloudinaryUploadResponse(
                (String) result.get("public_id"),
                (String) result.get("secure_url"),
                (String) result.get("format"),
                ((Number) result.get("bytes")).longValue(),
                (String) result.get("resource_type")
        );
    }

    public void deleteFile(String publicId) throws IOException {

        cloudinary.uploader().destroy(
                publicId,
                ObjectUtils.asMap(
                        "resource_type",
                        "raw"
                )
        );
    }
}
