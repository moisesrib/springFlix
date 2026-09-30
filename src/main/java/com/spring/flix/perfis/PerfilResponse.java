package com.spring.flix.perfis;

public record PerfilResponse(Long id, String nome, boolean infantil) {

    public static PerfilResponse from(Perfil perfil) {
        return new PerfilResponse(perfil.getId(), perfil.getNome(), perfil.isInfantil());
    }
}
