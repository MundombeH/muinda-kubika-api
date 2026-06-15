CREATE TABLE IF NOT EXISTS public.instituicoes (
    id UUID PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL,
    ano_de_fundacao INT NOT NULL,
    numero_de_telefone VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    horaio_de_funcionamento TIME,
    bairro_id UUID,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    versao Integer,
    is_active boolean,
    created_at timestamp,
    updated_at timestamp,
    CONSTRAINT fk_instituicao_bairro
    FOREIGN KEY (bairro_id) REFERENCES public.bairros(id)


);

CREATE TABLE IF NOT EXISTS instituicao_tipo (
   instituicao_id UUID NOT NULL,
   tipo VARCHAR(50) NOT NULL,
    PRIMARY KEY (instituicao_id, tipo),
   CONSTRAINT fk_instituicao_tipo
    FOREIGN KEY (instituicao_id) REFERENCES public.instituicoes(id)

);

-- Documento
CREATE TABLE IF NOT EXISTS public.documentos (
    id UUID PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    resumo TEXT,
    tipo_de_documento VARCHAR(50) NOT NULL,
    status VARCHAR(50),
    usuario_id UUID,
    instituicao_id UUID,
    aprovado_por UUID,
    data_aprovacao TIMESTAMP,
    is_active boolean,
    created_at timestamp,
    updated_at timestamp,
    CONSTRAINT fk_documento_usuario
    FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id),
    CONSTRAINT fk_documento_instituicao
    FOREIGN KEY (instituicao_id) REFERENCES public.instituicoes(id),
    CONSTRAINT fk_documento_aprovado_por
    FOREIGN KEY (aprovado_por) REFERENCES public.usuarios(id)
);

-- Autores do documento
CREATE TABLE IF NOT EXISTS public.documento_autores (
    documento_id UUID NOT NULL,
    autor VARCHAR(255) NOT NULL,
    PRIMARY KEY (documento_id, autor),
    CONSTRAINT fk_autores_documento
    FOREIGN KEY (documento_id) REFERENCES public.documentos(id)
    );


CREATE TABLE IF NOT EXISTS ficheiro (
    id UUID PRIMARY KEY,
    nome VARCHAR(255),
    url TEXT,
    tamanho BIGINT,
    formato VARCHAR(50),
    mime_type VARCHAR(100),
    checksum VARCHAR(255),
    documento_id UUID NOT NULL,
    is_active boolean,
    created_at timestamp,
    updated_at timestamp,
    CONSTRAINT fk_ficheiro_documento
    FOREIGN KEY (documento_id) REFERENCES public.documentos(id)
);

-- documento_categorias
CREATE TABLE IF NOT EXISTS documentos_categorias (
    documento_id UUID NOT NULL,
    categoria_id UUID NOT NULL,
    PRIMARY KEY (documento_id, categoria_id),
    CONSTRAINT fk_doc_categoria_doc
    FOREIGN KEY (documento_id) REFERENCES public.documentos(id),
    CONSTRAINT fk_doc_categoria_cat
    FOREIGN KEY (categoria_id) REFERENCES public.categorias(id)


);

CREATE TABLE IF NOT EXISTS documentos_tags (
    documento_id UUID NOT NULL,
    tag_id UUID NOT NULL,
    PRIMARY KEY (documento_id, tag_id),
    CONSTRAINT fk_doc_tags_doc
    FOREIGN KEY (documento_id) REFERENCES public.documentos(id),

    CONSTRAINT fk_doc_tags_tag
    FOREIGN KEY (tag_id) REFERENCES public.tags(id)
    );

-- Analize feita pela ia
CREATE TABLE IF NOT EXISTS documento_analize (
  id UUID PRIMARY KEY,
  documento_id UUID,
  resumo_gerado_ia TEXT,
  motivo_rejeicao TEXT,
  observacao_admin TEXT,
  data_processamento TIMESTAMP,
  versao INT,
  is_active boolean,
  created_at timestamp,
  updated_at timestamp,
  CONSTRAINT fk_analise_documento
  FOREIGN KEY (documento_id) REFERENCES public.documentos(id)
);

CREATE TABLE IF NOT EXISTS documento_analise_palavras_chave_ia (
    documento_analise_id UUID NOT NULL,
    palavra VARCHAR(255) NOT NULL,
    PRIMARY KEY (documento_analise_id, palavra),
    CONSTRAINT fk_analize_palavras
    FOREIGN KEY (documento_analise_id) REFERENCES public.documento_analize(id)
);


-- Repositorio

CREATE TABLE IF NOT EXISTS repositorio (
   id UUID PRIMARY KEY,
   documento_id UUID NOT NULL UNIQUE,
   url_github TEXT,
   is_active boolean,
   created_at timestamp,
   updated_at timestamp,
   CONSTRAINT fk_repositorio_documento
   FOREIGN KEY (documento_id) REFERENCES public.documentos(id)

);


CREATE TABLE IF NOT EXISTS repositorio_tecnologias_usadas (
   repositorio_id UUID NOT NULL,
   tecnologia VARCHAR(255),
   PRIMARY KEY (repositorio_id, tecnologia),
   CONSTRAINT fk_repositorio_tecnologias_usadas
   FOREIGN KEY (repositorio_id) REFERENCES public.repositorio(id)


);

