package com.api.muinda_kubika.Specification;

import com.api.muinda_kubika.model.Usuarios.DefaultUserModel;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class UserSpecification {

    public static Specification<DefaultUserModel> porId(UUID id){
        return((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"),id));

    }

    public static Specification<DefaultUserModel> activo(){
        return((root, query, criteriaBuilder) -> criteriaBuilder.isTrue(root.get("isActive")));

    }

}
