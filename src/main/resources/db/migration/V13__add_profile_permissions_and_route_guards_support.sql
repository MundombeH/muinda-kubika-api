-- Permissões adicionais para fluxo de aprovação/rejeição de perfis
INSERT INTO permissions (id, descricao, is_active, created_at)
SELECT gen_random_uuid(), 'PERFIL_APROVAR', TRUE, NOW()
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE descricao = 'PERFIL_APROVAR');

INSERT INTO permissions (id, descricao, is_active, created_at)
SELECT gen_random_uuid(), 'PERFIL_REJEITAR', TRUE, NOW()
WHERE NOT EXISTS (SELECT 1 FROM permissions WHERE descricao = 'PERFIL_REJEITAR');

-- ROLE_ADMIN: garantir permissões necessárias
INSERT INTO roles_permissions(permission_id, role_id)
SELECT p.id, r.id
FROM roles r
         JOIN permissions p ON p.descricao IN (
             'USUARIO_GERIR',
             'INSTITUICAO_GERIR',
             'PERFIL_APROVAR',
             'PERFIL_REJEITAR'
         )
WHERE r.descricao = 'ROLE_ADMIN'
ON CONFLICT (permission_id, role_id) DO NOTHING;

-- ROLE_ADMIN_INSTITUICAO: permissões de gestão institucional + aprovação de perfis
INSERT INTO roles_permissions(permission_id, role_id)
SELECT p.id, r.id
FROM roles r
         JOIN permissions p ON p.descricao IN (
             'INSTITUICAO_GERIR',
             'PERFIL_APROVAR',
             'PERFIL_REJEITAR'
         )
WHERE r.descricao = 'ROLE_ADMIN_INSTITUICAO'
ON CONFLICT (permission_id, role_id) DO NOTHING;
