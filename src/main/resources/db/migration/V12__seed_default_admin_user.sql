-- Garante que ROLE_ADMIN tenha todas as permissões ativas
INSERT INTO roles_permissions(permission_id, role_id)
SELECT p.id, r.id
FROM roles r
         JOIN permissions p ON p.is_active = TRUE
WHERE r.descricao = 'ROLE_ADMIN'
ON CONFLICT (permission_id, role_id) DO NOTHING;

-- Cria um utilizador admin padrão, caso ainda não exista
INSERT INTO usuarios (
    id,
    is_active,
    created_at,
    updated_at,
    nome,
    data_de_nascimento,
    email,
    password,
    numero_de_telefone
)
SELECT
    gen_random_uuid(),
    TRUE,
    NOW(),
    NOW(),
    'Administrador do Sistema',
    '1990-01-01'::date,
    'admin@muindakubika.com',
    '$2b$12$lPARvc60dmCSo9V3cQ39auUFr0O.3zmENl8LGswLtoKGRsaBcMTm2',
    '999999999'
WHERE NOT EXISTS (
    SELECT 1
    FROM usuarios u
    WHERE u.email = 'admin@muindakubika.com'
       OR u.numero_de_telefone = '999999999'
);

-- Associa o utilizador admin ao ROLE_ADMIN
INSERT INTO users_roles (roles_id, user_id)
SELECT r.id, u.id
FROM roles r
         JOIN usuarios u ON u.email = 'admin@muindakubika.com'
WHERE r.descricao = 'ROLE_ADMIN'
ON CONFLICT (roles_id, user_id) DO NOTHING;

-- Cria o perfil de admin global para o utilizador padrão
INSERT INTO usuario_admin (
    id,
    is_active,
    created_at,
    updated_at,
    usuario_id
)
SELECT
    gen_random_uuid(),
    TRUE,
    NOW(),
    NOW(),
    u.id
FROM usuarios u
WHERE u.email = 'admin@muindakubika.com'
  AND NOT EXISTS (
    SELECT 1
    FROM usuario_admin ua
    WHERE ua.usuario_id = u.id
);
