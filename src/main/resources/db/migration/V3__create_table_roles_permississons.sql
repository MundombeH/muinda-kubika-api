CREATE TABLE IF NOT EXISTS public.roles(
     id uuid PRIMARY KEY,
     descricao varchar(255) NOT NULL UNIQUE,
    is_active boolean,
    created_at timestamp,
    updated_at timestamp
);

CREATE TABLE IF NOT EXISTS public.permissions(
    id uuid PRIMARY KEY,
    descricao varchar(255) NOT NULL UNIQUE,
    is_active boolean,
    created_at timestamp,
    updated_at timestamp
    );


CREATE TABLE IF NOT EXISTS public.roles_permissions (
    permission_id uuid NOT NULL REFERENCES public.permissions(id),
    role_id uuid NOT NULL REFERENCES public.roles(id),
    PRIMARY KEY (permission_id, role_id)
    );
