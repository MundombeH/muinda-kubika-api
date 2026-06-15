ALTER TABLE IF EXISTS public.documento_analize
    ADD COLUMN IF NOT EXISTS titulo_sugerido VARCHAR(255),
    ADD COLUMN IF NOT EXISTS titulo_confianca INT,
    ADD COLUMN IF NOT EXISTS categoria_sugerida VARCHAR(255),
    ADD COLUMN IF NOT EXISTS categoria_confianca INT,
    ADD COLUMN IF NOT EXISTS subcategoria_sugerida VARCHAR(255),
    ADD COLUMN IF NOT EXISTS subcategoria_confianca INT;

ALTER TABLE IF EXISTS public.documento_analise_palavras_chave_ia
    ADD COLUMN IF NOT EXISTS confianca INT;

CREATE TABLE IF NOT EXISTS public.documento_analise_tags_sugeridas (
    documento_analise_id UUID NOT NULL,
    tag VARCHAR(255) NOT NULL,
    confianca INT,
    PRIMARY KEY (documento_analise_id, tag),
    CONSTRAINT fk_documento_analise_tags_sugeridas
        FOREIGN KEY (documento_analise_id) REFERENCES public.documento_analize(id)
);

CREATE TABLE IF NOT EXISTS public.documento_analise_tecnologias_sugeridas (
    documento_analise_id UUID NOT NULL,
    tecnologia VARCHAR(255) NOT NULL,
    confianca INT,
    PRIMARY KEY (documento_analise_id, tecnologia),
    CONSTRAINT fk_documento_analise_tecnologias_sugeridas
        FOREIGN KEY (documento_analise_id) REFERENCES public.documento_analize(id)
);

CREATE TABLE IF NOT EXISTS public.documento_analise_frameworks_sugeridos (
    documento_analise_id UUID NOT NULL,
    framework VARCHAR(255) NOT NULL,
    confianca INT,
    PRIMARY KEY (documento_analise_id, framework),
    CONSTRAINT fk_documento_analise_frameworks_sugeridos
        FOREIGN KEY (documento_analise_id) REFERENCES public.documento_analize(id)
);

CREATE TABLE IF NOT EXISTS public.documento_analise_conflitos (
    documento_analise_id UUID NOT NULL,
    conflito TEXT NOT NULL,
    PRIMARY KEY (documento_analise_id, conflito),
    CONSTRAINT fk_documento_analise_conflitos
        FOREIGN KEY (documento_analise_id) REFERENCES public.documento_analize(id)
);
