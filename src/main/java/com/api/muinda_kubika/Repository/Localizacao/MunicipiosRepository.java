package com.api.muinda_kubika.Repository.Localizacao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.api.muinda_kubika.model.Localizacao.MunicipioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MunicipiosRepository extends JpaRepository<MunicipioModel, UUID> {
    Optional<MunicipioModel> findByIdAndIsActiveTrue(UUID id);
    List<MunicipioModel> findByProvinciaId(UUID provinciaId);
    Optional<MunicipioModel> findByDescricaoAndIsActiveTrue(String desc);

}
