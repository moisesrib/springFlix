package com.spring.flix.ia.dto;

import com.spring.flix.filmes.dto.FilmeResponse;

import java.util.List;

/**
 * Resposta das rotas de IA.
 *
 * @param modo       "ia" quando o modelo respondeu ou "simulado" quando foi usado o algoritmo local
 * @param explicacao texto curto explicando o resultado
 * @param filmes     filmes sugeridos ou encontrados, em ordem de relevância
 */
public record IaResponse(String modo, String explicacao, List<FilmeResponse> filmes) {
}
