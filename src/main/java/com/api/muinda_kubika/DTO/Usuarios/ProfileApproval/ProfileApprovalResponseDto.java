package com.api.muinda_kubika.DTO.Usuarios.ProfileApproval;

import com.api.muinda_kubika.Defaults.DefaultDto;
import com.api.muinda_kubika.DTO.Usuarios.DefaultUser.DefaultUserResumoDto;
import com.api.muinda_kubika.Enums.ProfileApprovalStatusEnum;
import com.api.muinda_kubika.Enums.ProfileTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ProfileApprovalResponseDto extends DefaultDto {

    private ProfileTypeEnum profileType;
    private UUID profileId;
    private ProfileApprovalStatusEnum status;
    private DefaultUserResumoDto usuario;
    private DefaultUserResumoDto aprovadoPor;
    private LocalDateTime dataAprovacao;
    private DefaultUserResumoDto rejeitadoPor;
    private LocalDateTime dataRejeicao;
    private String motivoRejeicao;
}
