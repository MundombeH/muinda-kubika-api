package com.api.muinda_kubika.model.Usuarios;

import com.api.muinda_kubika.Defaults.DefaultModel;
import com.api.muinda_kubika.Enums.ProfileApprovalStatusEnum;
import com.api.muinda_kubika.Enums.ProfileTypeEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "PROFILE_APPROVALS")
public class ProfileApprovalModel extends DefaultModel {

    @Enumerated(EnumType.STRING)
    @Column(name = "profile_type", nullable = false)
    private ProfileTypeEnum profileType;

    @Column(name = "profile_id", nullable = false)
    private java.util.UUID profileId;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private DefaultUserModel usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProfileApprovalStatusEnum status = ProfileApprovalStatusEnum.PENDING;

    @ManyToOne
    @JoinColumn(name = "aprovado_por")
    private DefaultUserModel aprovadoPor;

    @Column(name = "data_aprovacao")
    private LocalDateTime dataAprovacao;

    @ManyToOne
    @JoinColumn(name = "rejeitado_por")
    private DefaultUserModel rejeitadoPor;

    @Column(name = "data_rejeicao")
    private LocalDateTime dataRejeicao;

    @Column(name = "motivo_rejeicao")
    private String motivoRejeicao;
}
