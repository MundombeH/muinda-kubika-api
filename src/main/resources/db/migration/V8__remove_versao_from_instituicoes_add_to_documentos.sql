ALTER TABLE IF EXISTS public.instituicoes
DROP COLUMN versao;

ALTER TABLE IF EXISTS public.documentos
    ADD COLUMN versao INTEGER;