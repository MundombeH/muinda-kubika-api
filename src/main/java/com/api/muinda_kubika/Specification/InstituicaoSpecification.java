package com.api.muinda_kubika.Specification;

import com.api.muinda_kubika.Enums.TipoInstituicaoEnum;
import com.api.muinda_kubika.model.Instituicao.InstituicaoModel;
import org.springframework.data.jpa.domain.Specification;
import java.util.UUID;

public class InstituicaoSpecification {
    public static Specification<InstituicaoModel> porId(UUID id){
        return((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"),id));

    }

    public static Specification<InstituicaoModel> activo(){
        return((root, query, criteriaBuilder) -> criteriaBuilder.isTrue(root.get("isActive")));

    }

    public static Specification<InstituicaoModel> porTipo(TipoInstituicaoEnum tipo){
        return((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("TipoInstituicaoEnum"),tipo));

    }

    public static Specification<InstituicaoModel> porBairro(UUID id){
        return((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("bairro").get("id"),id));

    }

    public static Specification<InstituicaoModel> porMunicipio(UUID id){
        return((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("bairro").get("municipio").get("id"),id));

    }
    public static Specification<InstituicaoModel> porProvincia(UUID id){
        return((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("bairro").get("municipio").get("provincia").get("id"),id));

    }
    public static Specification<InstituicaoModel> porPais(UUID id){
        return((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("bairro").get("municipio").get("provincia").get("pais").get("id"),id));

    }


}
