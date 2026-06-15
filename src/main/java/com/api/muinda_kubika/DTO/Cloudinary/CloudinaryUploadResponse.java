package com.api.muinda_kubika.DTO.Cloudinary;


public record CloudinaryUploadResponse(
    String publicId,
    String url,
    String format,
    Long bytes,
    String resourceType
) {}

