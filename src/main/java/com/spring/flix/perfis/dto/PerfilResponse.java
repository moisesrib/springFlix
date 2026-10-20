package com.spring.flix.perfis.dto;

import com.spring.flix.perfis.entity.Perfil;

public record PerfilResponse(Long id, String nome, boolean infantil) {

    public static PerfilResponse from(Perfil perfil) {
        return new PerfilResponse(perfil.getId(), perfil.getNome(), perfil.isInfantil());
    }
}
