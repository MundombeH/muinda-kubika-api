ALTER TABLE IF EXISTS public.documento_analize
    ADD COLUMN IF NOT EXISTS origem_analise VARCHAR(30),
    ADD COLUMN IF NOT EXISTS pendente_confirmacao BOOLEAN,
    ADD COLUMN IF NOT EXISTS versao_analise INT;

UPDATE public.documento_analize
SET origem_analise = 'FICHEIRO'
WHERE origem_analise IS NULL;

UPDATE public.documento_analize
SET pendente_confirmacao = TRUE
WHERE pendente_confirmacao IS NULL;

UPDATE public.documento_analize
SET versao_analise = 1
WHERE versao_analise IS NULL;

ALTER TABLE IF EXISTS public.documento_analize
    ALTER COLUMN origem_analise SET NOT NULL,
    ALTER COLUMN pendente_confirmacao SET NOT NULL,
    ALTER COLUMN versao_analise SET NOT NULL;

CREATE INDEX IF NOT EXISTS idx_documento_analize_documento_origem
    ON public.documento_analize (documento_id, origem_analise, created_at DESC);

CREATE UNIQUE INDEX IF NOT EXISTS ux_documento_analise_pendente_por_origem
    ON public.documento_analize (documento_id, origem_analise)
    WHERE pendente_confirmacao = TRUE;
