package com.spring.flix.avaliacoes;

import java.time.LocalDateTime;

public record AvaliacaoResponse(
        Long id,
        Integer nota,
        String comentario,
        LocalDateTime dataAvaliacao,
        Long perfilId,
        String perfilNome,
        Long filmeId,
        String filmeTitulo) {

    public static AvaliacaoResponse from(Avaliacao avaliacao) {
        return new AvaliacaoResponse(
                avaliacao.getId(),
                avaliacao.getNota(),
                avaliacao.getComentario(),
                avaliacao.getDataAvaliacao(),
                avaliacao.getPerfil().getId(),
                avaliacao.getPerfil().getNome(),
                avaliacao.getFilme().getId(),
                avaliacao.getFilme().getTitulo());
    }
}
