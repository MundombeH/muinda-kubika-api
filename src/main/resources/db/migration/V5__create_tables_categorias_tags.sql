CREATE TABLE IF NOT EXISTS public.categorias(
  id uuid PRIMARY KEY,
  descricao varchar(255) NOT NULL,
  is_active boolean,
  created_at timestamp,
  updated_at timestamp
);

CREATE TABLE IF NOT EXISTS public.tags(
    id uuid PRIMARY KEY,
    descricao varchar(255) NOT NULL,
    is_active boolean,
    created_at timestamp,
    updated_at timestamp

    )