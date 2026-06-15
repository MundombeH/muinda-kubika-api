package com.api.muinda_kubika.Repository.Roles_Permissions;

import com.api.muinda_kubika.model.Roles_permissions.RolesModel;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<RolesModel, UUID> {
    boolean existsByDescricao(String descricao);

    Optional<RolesModel> findByIdAndIsActiveTrue(UUID id);

    Set<RolesModel> findAllByIdInAndIsActiveTrue(Set<UUID> roleIds);

    Optional<RolesModel> findByDescricao(String descricao);

    boolean existsByDescricaoAndIsActiveTrue(String descricao);

    List<RolesModel> findByIsActiveTrue();
}
