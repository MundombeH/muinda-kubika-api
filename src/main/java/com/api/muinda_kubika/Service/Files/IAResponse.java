package com.api.muinda_kubika.Service.Files;

import com.api.muinda_kubika.model.Files.SugestaoConfiancaModel;
import java.util.List;
import java.util.Set;

public record IAResponse(
        String titulo,
        String resumo,
        List<String> autores,
        String categoriaSugerida,
        Integer categoriaConfianca,
        String subcategoriaSugerida,
        Integer subcategoriaConfianca,
        Set<SugestaoConfiancaModel> palavrasChaveIA,
        Set<SugestaoConfiancaModel> tagsSugeridas,
        Set<SugestaoConfiancaModel> tecnologiasSugeridas,
        Set<SugestaoConfiancaModel> frameworksSugeridos,
        Set<String> conflitosDetectados
) {}
