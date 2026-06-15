package com.api.muinda_kubika.Repository.Localizacao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.api.muinda_kubika.model.Localizacao.ProvinciasModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ProvinciasRepository extends JpaRepository<ProvinciasModel, UUID> {

    Optional<ProvinciasModel> findByIdAndIsActiveTrue(UUID id);
    Optional<ProvinciasModel> findByDescricao(String descricao);
    List<ProvinciasModel> findByPaisId(UUID paisId);

}
