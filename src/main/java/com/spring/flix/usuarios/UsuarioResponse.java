package com.spring.flix.usuarios;

public record UsuarioResponse(Long id, String nome, String email, Role role) {

    public static UsuarioResponse from(Usuario usuario) {
        return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getRole());
    }
}
