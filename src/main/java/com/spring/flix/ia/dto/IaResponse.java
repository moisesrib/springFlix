package com.spring.flix.ia.dto;

import com.spring.flix.filmes.dto.FilmeResponse;

import java.util.List;

public record IaResponse(String modo, String explicacao, List<FilmeResponse> filmes) {
}
