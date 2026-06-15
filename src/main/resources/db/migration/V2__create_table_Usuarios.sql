CREATE TABLE IF NOT EXISTS public.usuarios (
id UUID PRIMARY KEY,
    is_active BOOLEAN,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    nome VARCHAR(255) NOT NULL,
    data_de_nascimento DATE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    numero_de_telefone VARCHAR(255) NOT NULL UNIQUE
    );
