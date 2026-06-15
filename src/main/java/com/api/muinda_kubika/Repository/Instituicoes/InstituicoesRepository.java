package com.api.muinda_kubika.Repository.Instituicoes;

import com.api.muinda_kubika.model.Instituicao.InstituicaoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InstituicoesRepository extends JpaRepository<InstituicaoModel, UUID>, JpaSpecificationExecutor<InstituicaoModel> {

    List<InstituicaoModel> findByIsActiveTrue();

    Optional<InstituicaoModel> findByIdAndIsActiveTrue(UUID id);

}
