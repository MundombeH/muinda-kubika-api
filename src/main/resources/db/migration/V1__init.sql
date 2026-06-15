-- tableas paises

CREATE TABLE IF NOT EXISTS public.pais (
    id uuid PRIMARY KEY,
    descricao varchar(255),
    is_active boolean,
    created_at timestamp,
    updated_at timestamp
    );

-- 2. Tabela PROVINCIAS
CREATE TABLE IF NOT EXISTS public.provincias (
    id uuid PRIMARY KEY,
    descricao varchar(255),
    pais_id uuid REFERENCES public.pais(id),
    is_active boolean,
    created_at timestamp,
    updated_at timestamp
    );

-- 3. Tabela MUNICIPIOS
CREATE TABLE IF NOT EXISTS public.municipios (
    id uuid PRIMARY KEY,
    descricao varchar(255),
    provincia_id uuid REFERENCES public.provincias(id),
    is_active boolean,
    created_at timestamp,
    updated_at timestamp
    );

-- 4. Tabela BAIRROS
CREATE TABLE IF NOT EXISTS public.bairros (
    id uuid PRIMARY KEY,
    descricao varchar(255),
    municipio_id uuid REFERENCES public.municipios(id),
    is_active boolean,
    created_at timestamp,
    updated_at timestamp
    );

