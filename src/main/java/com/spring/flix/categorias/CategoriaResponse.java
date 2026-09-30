package com.spring.flix.categorias;

public record CategoriaResponse(Long id, String nome) {

    public static CategoriaResponse from(Categoria categoria) {
        return new CategoriaResponse(categoria.getId(), categoria.getNome());
    }
}
