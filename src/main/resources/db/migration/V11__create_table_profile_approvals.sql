CREATE TABLE IF NOT EXISTS public.profile_approvals (
    id UUID PRIMARY KEY,
    profile_type VARCHAR(30) NOT NULL
        CHECK (profile_type IN ('ADMIN', 'ADMIN_INSTITUICAO', 'DOCENTE', 'ESTUDANTE')),
    profile_id UUID NOT NULL,
    usuario_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING'
        CHECK (status IN ('PENDING', 'ACTIVE', 'REJECTED')),
    aprovado_por UUID,
    data_aprovacao TIMESTAMP,
    rejeitado_por UUID,
    data_rejeicao TIMESTAMP,
    motivo_rejeicao TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_profile_approval_usuario
        FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id) ON DELETE CASCADE,
    CONSTRAINT fk_profile_approval_aprovado_por
        FOREIGN KEY (aprovado_por) REFERENCES public.usuarios(id),
    CONSTRAINT fk_profile_approval_rejeitado_por
        FOREIGN KEY (rejeitado_por) REFERENCES public.usuarios(id),
    CONSTRAINT uq_profile_approval_profile
        UNIQUE (profile_type, profile_id)
);

CREATE INDEX IF NOT EXISTS idx_profile_approvals_status
    ON public.profile_approvals(status);

CREATE INDEX IF NOT EXISTS idx_profile_approvals_usuario
    ON public.profile_approvals(usuario_id);
