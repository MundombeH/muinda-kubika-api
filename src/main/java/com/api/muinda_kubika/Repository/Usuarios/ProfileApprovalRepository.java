package com.api.muinda_kubika.Repository.Usuarios;

import com.api.muinda_kubika.Enums.ProfileApprovalStatusEnum;
import com.api.muinda_kubika.Enums.ProfileTypeEnum;
import com.api.muinda_kubika.model.Usuarios.ProfileApprovalModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProfileApprovalRepository extends JpaRepository<ProfileApprovalModel, UUID> {

    Optional<ProfileApprovalModel> findByProfileTypeAndProfileId(
            ProfileTypeEnum profileType, UUID profileId);

    Optional<ProfileApprovalModel> findByProfileTypeAndProfileIdAndStatus(
            ProfileTypeEnum profileType, UUID profileId, ProfileApprovalStatusEnum status);

    List<ProfileApprovalModel> findByStatus(ProfileApprovalStatusEnum status);

    List<ProfileApprovalModel> findByStatusAndProfileType(
            ProfileApprovalStatusEnum status, ProfileTypeEnum profileType);

    boolean existsByProfileTypeAndProfileIdAndStatusIn(
            ProfileTypeEnum profileType, UUID profileId, List<ProfileApprovalStatusEnum> statuses);
}
