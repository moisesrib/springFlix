package com.spring.flix.categorias.dto;

import com.spring.flix.categorias.entity.Categoria;

public record CategoriaResponse(Long id, String nome) {

    public static CategoriaResponse from(Categoria categoria) {
        return new CategoriaResponse(categoria.getId(), categoria.getNome());
    }
}
