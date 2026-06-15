package com.api.muinda_kubika.Specification;

import com.api.muinda_kubika.model.Usuarios.AdminModel;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class AdminSpeficitaion {
    public static Specification<AdminModel> porId(UUID id){
        return((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"),id));

    }

    public static Specification<AdminModel> activo(){
        return((root, query, criteriaBuilder) -> criteriaBuilder.isTrue(root.get("isActive")));

    }

    public static Specification<AdminModel> porUsuario(UUID usuario){
        return((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("usuario").get("id"),usuario));

    }
}
