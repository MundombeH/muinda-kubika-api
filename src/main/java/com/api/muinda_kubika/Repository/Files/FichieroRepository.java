package com.api.muinda_kubika.Repository.Files;

import com.api.muinda_kubika.model.Files.FicheiroModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FichieroRepository extends JpaRepository<FicheiroModel, UUID> {
    Optional<FicheiroModel> findByIdAndIsActiveTrue(UUID id);

    List<FicheiroModel> findByIsActiveTrue();
}
