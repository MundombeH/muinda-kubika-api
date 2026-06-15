package com.api.muinda_kubika.Repository.Usuarios;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.api.muinda_kubika.model.Usuarios.DefaultUserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultUserRepository extends JpaRepository<DefaultUserModel, UUID> {

    List<DefaultUserModel> findByIsActiveTrue();

    Optional<DefaultUserModel>findByEmail(String email);

    Optional<DefaultUserModel>findByNumeroDeTelefone(String telefone);

    Optional<DefaultUserModel> findByNumeroDeTelefoneOrEmail(String telefone, String email);

    boolean existsByEmailAndIsActiveTrue(String email);

    boolean existsByNumeroDeTelefoneAndIsActiveTrue(String telefone);

    Optional<DefaultUserModel>findByIdAndIsActiveTrue(UUID id);

}