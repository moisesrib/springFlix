package com.spring.flix.usuarios.dto;

import com.spring.flix.usuarios.entity.Role;
import com.spring.flix.usuarios.entity.Usuario;

public record UsuarioResponse(Long id, String nome, String email, Role role) {

    public static UsuarioResponse from(Usuario usuario) {
        return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getRole());
    }
}
