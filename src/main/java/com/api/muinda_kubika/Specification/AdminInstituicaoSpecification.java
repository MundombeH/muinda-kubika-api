package com.api.muinda_kubika.Specification;

import com.api.muinda_kubika.model.Usuarios.AdminInstituicaoModel;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class AdminInstituicaoSpecification {
    public static Specification<AdminInstituicaoModel> porId(UUID id){
        return((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"),id));

    }

    public static Specification<AdminInstituicaoModel> activo(){
        return((root, query, criteriaBuilder) -> criteriaBuilder.isTrue(root.get("isActive")));

    }

    public static Specification<AdminInstituicaoModel> porUsuario(UUID usuario){
        return((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("usuario").get("id"),usuario));

    }

    public static Specification<AdminInstituicaoModel> porInstituicao(UUID instituicao){
        return((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("instituicao").get("id"),instituicao));

    }

}
