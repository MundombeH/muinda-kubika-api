package com.api.muinda_kubika.Specification;

import com.api.muinda_kubika.Enums.StatusDocumentoEnum;
import com.api.muinda_kubika.Enums.TipoDocumentoEnum;
import com.api.muinda_kubika.model.Files.DocumentosModel;
import org.springframework.data.jpa.domain.Specification;
import java.util.UUID;

public class DocumentoSpecification {

    public static Specification<DocumentosModel> activo() {
        return (root, query, cb) -> cb.isTrue(root.get("isActive"));
    }

    public static Specification<DocumentosModel> porTitulo(String titulo) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("titulo")), "%" + titulo.toLowerCase() + "%");
    }

    public static Specification<DocumentosModel> porAutor(String autor) {
        return (root, query, cb) -> cb.isMember(autor, root.get("autores"));
    }

    public static Specification<DocumentosModel> porInstituicao(UUID instituicaoId) {
        return (root, query, cb) -> cb.equal(root.get("instituicao").get("id"), instituicaoId);
    }

    public static Specification<DocumentosModel> porCategoria(UUID categoriaId) {
        return (root, query, cb) -> cb.isMember(categoriaId, root.get("categorias"));
    }

    public static Specification<DocumentosModel> porCategorias(java.util.List<UUID> categoriasIds) {
        return (root, query, cb) -> root.get("categorias").get("id").in(categoriasIds);
    }

    public static Specification<DocumentosModel> porTag(UUID tagId) {
        return (root, query, cb) -> cb.isMember(tagId, root.get("tags"));
    }

    public static Specification<DocumentosModel> porTags(java.util.List<UUID> tagsIds) {
        return (root, query, cb) -> root.get("tags").get("id").in(tagsIds);
    }

    public static Specification<DocumentosModel> porStatus(StatusDocumentoEnum status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<DocumentosModel> porTipo(TipoDocumentoEnum tipo) {
        return (root, query, cb) -> cb.equal(root.get("tipoDeDocumento"), tipo);
    }

    public static Specification<DocumentosModel> porUsuario(UUID usuarioId) {
        return (root, query, cb) -> cb.equal(root.get("usuario").get("id"), usuarioId);
    }
}
