INSERT INTO roles (
    id,
    descricao,
    is_active,
    created_at,
    updated_at
)
VALUES
    (
        gen_random_uuid(),
        'ROLE_ADMIN',
        TRUE,
        NOW(),
        NOW()
    ),
    (
        gen_random_uuid(),
        'ROLE_ADMIN_INSTITUICAO',
        TRUE,
        NOW(),
        NOW()
    ),
    (
        gen_random_uuid(),
        'ROLE_DOCENTE',
        TRUE,
        NOW(),
        NOW()
    ),
    (
        gen_random_uuid(),
        'ROLE_USUARIO',
        TRUE,
        NOW(),
        NOW()
    ),
    (
        gen_random_uuid(),
        'ROLE_ESTUDANTE',
        TRUE,
        NOW(),
        NOW()
    );


INSERT INTO permissions (
    id,
    descricao,
    is_active,
    created_at,
    updated_at

)
VALUES

    (
        gen_random_uuid(),
        'DOCUMENTO_CRIAR',
        TRUE,
        NOW(),
        NOW()
    ),

    (
        gen_random_uuid(),
        'DOCUMENTO_EDITAR',
        TRUE,
        NOW(),
        NOW()
    ),

    (
        gen_random_uuid(),
        'DOCUMENTO_REMOVER',
        TRUE,
        NOW(),
        NOW()
    ),

    (
        gen_random_uuid(),
        'DOCUMENTO_PUBLICAR',
        TRUE,
        NOW(),
        NOW()
    ),

    (
        gen_random_uuid(),
        'DOCUMENTO_APROVAR',
        TRUE,
        NOW(),
        NOW()
    ),

    (
        gen_random_uuid(),
        'DOCUMENTO_REJEITAR',
        TRUE,
        NOW(),
        NOW()
    ),

    (
        gen_random_uuid(),
        'USUARIO_GERIR',
        TRUE,
        NOW(),
        NOW()
    ),

    (
        gen_random_uuid(),
        'INSTITUICAO_GERIR',
        TRUE,
        NOW(),
        NOW()
    ),
    (
        gen_random_uuid(),
        'PERFIL_APROVAR',
        TRUE,
        NOW(),
        NOW()
    ),
    (
        gen_random_uuid(),
        'PERFIL_REJEITAR',
        TRUE,
        NOW(),
        NOW()

    ),
    (
        gen_random_uuid(),
        'PERFIL_REMOVER',
        TRUE,
        NOW(),
        NOW()
    );


