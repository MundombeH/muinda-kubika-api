package com.api.muinda_kubika.Repository.Files;

import com.api.muinda_kubika.Enums.OrigemAnaliseIAEnum;
import com.api.muinda_kubika.model.Files.DocumentoAnaliseModel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentoAnaliseRepository
    extends JpaRepository<DocumentoAnaliseModel, UUID>
{
    List<DocumentoAnaliseModel> findByDocumentoIdOrderByCreatedAtDesc(
        UUID documentoId
    );

    Optional<
        DocumentoAnaliseModel
    > findTopByDocumentoIdAndOrigemAnaliseOrderByVersaoAnaliseDescCreatedAtDesc(
        UUID documentoId,
        OrigemAnaliseIAEnum origemAnalise
    );

    boolean existsByDocumentoIdAndOrigemAnaliseAndPendenteConfirmacaoTrue(
        UUID documentoId,
        OrigemAnaliseIAEnum origemAnalise
    );
}
