INSERT INTO roles_permissions(role_id, permission_id)
SELECT r.id, p.id
FROM roles r
         JOIN permissions p ON p.descricao IN (
         'DOCUMENTO_CRIAR',
         'DOCUMENTO_EDITAR'
    )
WHERE r.descricao = 'ROLE_ESTUDANTE';


INSERT INTO roles_permissions(role_id, permission_id)
SELECT r.id, p.id
FROM roles r
         JOIN permissions p ON p.descricao IN (
         'DOCUMENTO_CRIAR',
         'DOCUMENTO_EDITAR'
    )
WHERE r.descricao = 'ROLE_DOCENTE';



INSERT INTO roles_permissions(role_id, permission_id)
SELECT r.id, p.id
FROM roles r
         JOIN permissions p ON p.descricao IN (

'DOCUMENTO_REJEITAR',
'DOCUMENTO_PUBLICAR',
'INSTITUICAO_GERIR',
'PERFIL_APROVAR',
'PERFIL_REJEITAR',
'PERFIL_REMOVER'

    )
WHERE r.descricao = 'ROLE_ADMIN_INSTITUICAO';


INSERT INTO roles_permissions(role_id, permission_id)
SELECT r.id, p.id
FROM roles r
         JOIN permissions p ON p.descricao IN (

         'DOCUMENTO_REJEITAR',
         'DOCUMENTO_PUBLICAR',
         'INSTITUICAO_GERIR',
         'USUARIO_GERIR',
         'INSTITUICAO_GERIR',
         'PERFIL_APROVAR',
         'PERFIL_REJEITAR',
         'PERFIL_REMOVER'

    )
WHERE r.descricao = 'ROLE_ADMIN';
