package com.spring.flix.filmes;

import com.spring.flix.categorias.CategoriaResponse;

import java.util.Comparator;
import java.util.List;

public record FilmeResponse(
        Long id,
        String titulo,
        String sinopse,
        String diretor,
        Integer anoLancamento,
        Integer duracaoMinutos,
        String classificacaoIndicativa,
        List<CategoriaResponse> categorias) {

    public static FilmeResponse from(Filme filme) {
        return new FilmeResponse(
                filme.getId(),
                filme.getTitulo(),
                filme.getSinopse(),
                filme.getDiretor(),
                filme.getAnoLancamento(),
                filme.getDuracaoMinutos(),
                filme.getClassificacaoIndicativa(),
                filme.getCategorias().stream()
                        .map(CategoriaResponse::from)
                        .sorted(Comparator.comparing(CategoriaResponse::nome))
                        .toList());
    }
}
