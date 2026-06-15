package com.api.muinda_kubika.Specification;

import com.api.muinda_kubika.model.Usuarios.DocenteModel;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class docenteSpecification {
    public static Specification<DocenteModel> activo(){
        return((root, query, criteriaBuilder) -> criteriaBuilder.isTrue(root.get("isActive")));

    }

    public static Specification<DocenteModel> porUsuario(UUID usuario){
        return((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("usuario").get("id"),usuario));

    }


    public static Specification<DocenteModel> porInstituicao(UUID instituicao){
        return((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("instituicao").get("id"),instituicao));

    }


    public static Specification<DocenteModel> porBairro(UUID id){
        return((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("bairro").get("id"),id));

    }

    public static Specification<DocenteModel> porMunicipio(UUID id){
        return((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("bairro").get("municipio").get("id"),id));

    }
    public static Specification<DocenteModel> porProvincia(UUID id){
        return((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("bairro").get("municipio").get("provincia").get("id"),id));

    }
    public static Specification<DocenteModel> porPais(UUID id){
        return((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("bairro").get("municipio").get("provincia").get("pais").get("id"),id));

    }

}
