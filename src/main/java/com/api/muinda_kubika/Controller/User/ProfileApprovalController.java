package com.api.muinda_kubika.Controller.User;

import com.api.muinda_kubika.DTO.Usuarios.ProfileApproval.ProfileApprovalResponseDto;
import com.api.muinda_kubika.DTO.Usuarios.ProfileApproval.ProfileRejectionRequestDto;
import com.api.muinda_kubika.Enums.ProfileTypeEnum;
import com.api.muinda_kubika.Service.Usuarios.ProfileApprovalService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("usuario/aprovacoes")
public class ProfileApprovalController {

    private final ProfileApprovalService profileApprovalService;

    public ProfileApprovalController(
        ProfileApprovalService profileApprovalService
    ) {
        this.profileApprovalService = profileApprovalService;
    }

    @PreAuthorize(
        "@roleChecker.hasAnyActiveRole(authentication, 'ROLE_ADMIN', 'ROLE_ADMIN_INSTITUICAO') and @roleChecker.hasAnyAuthority(authentication, 'PERFIL_APROVAR', 'PERFIL_REJEITAR')"
    )
    @GetMapping
    public ResponseEntity<List<ProfileApprovalResponseDto>> listPending(
        @RequestParam(required = false) ProfileTypeEnum profileType
    ) {
        return ResponseEntity.ok(
            profileApprovalService.listPending(profileType)
        );
    }

    @PreAuthorize(
        "@roleChecker.hasAnyActiveRole(authentication, 'ROLE_ADMIN', 'ROLE_ADMIN_INSTITUICAO') and @roleChecker.hasAnyAuthority(authentication, 'PERFIL_APROVAR', 'PERFIL_REJEITAR')"
    )
    @GetMapping("/{approvalId}")
    public ResponseEntity<ProfileApprovalResponseDto> getById(
        @PathVariable UUID approvalId
    ) {
        return ResponseEntity.ok(profileApprovalService.getById(approvalId));
    }

    @PreAuthorize(
        "@roleChecker.hasAnyActiveRole(authentication, 'ROLE_ADMIN', 'ROLE_ADMIN_INSTITUICAO') and @roleChecker.hasAuthority(authentication, 'PERFIL_APROVAR')"
    )
    @PatchMapping("/{approvalId}/aprovar")
    public ResponseEntity<String> approve(
        @PathVariable UUID approvalId,
        Authentication auth
    ) {
        UUID approverId = UUID.fromString(auth.getName());
        return ResponseEntity.ok(
            profileApprovalService.approve(approverId, approvalId)
        );
    }

    @PreAuthorize(
        "@roleChecker.hasAnyActiveRole(authentication, 'ROLE_ADMIN', 'ROLE_ADMIN_INSTITUICAO') and @roleChecker.hasAuthority(authentication, 'PERFIL_REJEITAR')"
    )
    @PatchMapping("/{approvalId}/rejeitar")
    public ResponseEntity<String> reject(
        @PathVariable UUID approvalId,
        @RequestBody(required = false) ProfileRejectionRequestDto dto,
        Authentication auth
    ) {
        UUID rejectorId = UUID.fromString(auth.getName());
        String motivo = dto != null ? dto.getMotivoRejeicao() : null;
        return ResponseEntity.ok(
            profileApprovalService.reject(rejectorId, approvalId, motivo)
        );
    }
}
