package com.api.muinda_kubika.Repository.Roles_Permissions;

import com.api.muinda_kubika.model.Roles_permissions.PermissionsModel;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionsRepository
    extends JpaRepository<PermissionsModel, UUID>
{
    List<PermissionsModel> findByIsActiveTrue();

    boolean existsByDescricao(String descricao);

    boolean existsByDescricaoAndIsActiveTrue(String descricao);

    Set<PermissionsModel> findAllByIdInAndIsActiveTrue(Set<UUID> ids);

    Optional<PermissionsModel> findByIdAndIsActiveTrue(UUID id);
}
