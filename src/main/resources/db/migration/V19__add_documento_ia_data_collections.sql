CREATE TABLE IF NOT EXISTS public.documento_tecnologias_sugeridas (
    documento_id UUID NOT NULL REFERENCES public.documentos(id) ON DELETE CASCADE,
    tecnologia VARCHAR(255) NOT NULL,
    PRIMARY KEY (documento_id, tecnologia)
);

CREATE TABLE IF NOT EXISTS public.documento_frameworks_sugeridos (
    documento_id UUID NOT NULL REFERENCES public.documentos(id) ON DELETE CASCADE,
    framework VARCHAR(255) NOT NULL,
    PRIMARY KEY (documento_id, framework)
);

CREATE TABLE IF NOT EXISTS public.documento_palavras_chave_ia (
    documento_id UUID NOT NULL REFERENCES public.documentos(id) ON DELETE CASCADE,
    palavra_chave VARCHAR(255) NOT NULL,
    PRIMARY KEY (documento_id, palavra_chave)
);
