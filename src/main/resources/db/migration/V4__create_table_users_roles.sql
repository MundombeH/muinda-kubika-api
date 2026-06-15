CREATE TABLE IF NOT EXISTS users_roles(
    roles_id uuid NOT NULL REFERENCES public.roles(id),
    user_id uuid NOT NULL REFERENCES public.usuarios(id),
    PRIMARY KEY (roles_id, user_id)
)