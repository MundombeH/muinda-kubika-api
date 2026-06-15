-- Estudantes
CREATE TABLE IF NOT EXISTS public.usuarios_estudantes (
    id UUID PRIMARY KEY,
    is_active BOOLEAN,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    curso VARCHAR(255) NOT NULL,
    ano INTEGER NOT NULL,
    genero VARCHAR(20) CHECK (genero IN ('MASCULINO', 'FEMININO', 'NAO_BINARIO')),
    identificacao VARCHAR(255) UNIQUE,
    bairro_id UUID,
    instituicao_id UUID NOT NULL,
    usuario_id UUID NOT NULL UNIQUE,
    CONSTRAINT fk_estudante_usuario
    FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id) ON DELETE CASCADE,
    CONSTRAINT fk_estudante_bairro
    FOREIGN KEY (bairro_id) REFERENCES public.bairros(id),
    CONSTRAINT fk_estudante_instituicao
    FOREIGN KEY (instituicao_id) REFERENCES public.instituicoes(id)
    );

-- Docentes
CREATE TABLE IF NOT EXISTS public.usuarios_docentes (
id UUID PRIMARY KEY,
    is_active BOOLEAN,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    departamento VARCHAR(255),
    genero VARCHAR(20) CHECK (genero IN ('MASCULINO', 'FEMININO', 'NAO_BINARIO')),
    identificacao VARCHAR(255) NOT NULL UNIQUE,
    bairro_id UUID,
    usuario_id UUID NOT NULL UNIQUE,
    CONSTRAINT fk_docente_usuario
    FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id) ON DELETE CASCADE,
    CONSTRAINT fk_docente_bairro
    FOREIGN KEY (bairro_id) REFERENCES public.bairros(id)
    );

-- Admin global
CREATE TABLE IF NOT EXISTS public.usuario_admin (
    id UUID PRIMARY KEY,
    is_active BOOLEAN,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    usuario_id UUID NOT NULL UNIQUE,
    CONSTRAINT fk_admin_usuario
    FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id) ON DELETE CASCADE
    );

-- Admin de instituição  ← estava em falta
CREATE TABLE IF NOT EXISTS public.usuario_admin_instituicao (
   id UUID PRIMARY KEY,
   is_active BOOLEAN,
   created_at TIMESTAMP,
   updated_at TIMESTAMP,
   usuario_id UUID NOT NULL UNIQUE,
   CONSTRAINT fk_admin_inst_usuario
   FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id) ON DELETE CASCADE
    );

-- Tabela de junção: admin_instituicao <-> instituicoes
CREATE TABLE IF NOT EXISTS public.admin_instituicao (
    admin_instituicao_id UUID NOT NULL,
    instituicao_id UUID NOT NULL,
    PRIMARY KEY (admin_instituicao_id, instituicao_id),
    CONSTRAINT fk_admin_inst_admin
    FOREIGN KEY (admin_instituicao_id) REFERENCES public.usuario_admin_instituicao(id),
    CONSTRAINT fk_admin_inst_instituicao
    FOREIGN KEY (instituicao_id) REFERENCES public.instituicoes(id)
    );

-- Tabela  docentes instituicoes
CREATE TABLE IF NOT EXISTS public.instituicoes_docentes (
    docente_id UUID NOT NULL,
    instituicao_id UUID NOT NULL,
    PRIMARY KEY (docente_id, instituicao_id),
    CONSTRAINT fk_inst_docente_docente
    FOREIGN KEY (docente_id) REFERENCES public.usuarios_docentes(id),
    CONSTRAINT fk_inst_docente_instituicao
    FOREIGN KEY (instituicao_id) REFERENCES public.instituicoes(id)
    );