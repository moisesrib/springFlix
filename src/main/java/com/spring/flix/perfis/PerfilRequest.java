package com.spring.flix.perfis;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PerfilRequest(
        @NotBlank @Size(max = 50) String nome,
        @NotNull Boolean infantil) {
}
